package ast;

import java.util.ArrayList;
import java.util.List;

import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
import util.Effect;
import util.Environment;
import util.STEntry;
import util.SemanticError;
import util.SimpLanPlusLib;

public class DecFunNode implements Node {
	
	// grammar rule:
	// decFun	    : (type | 'void') ID '(' (arg (',' arg)*)? ')' block ;

	private Node type;
	private IdNode id;
	private ArrayList<Node> args;
	private BlockNode block;
	
	public DecFunNode(Node type, IdNode id, ArrayList<Node> args, BlockNode block) {
		this.type = type;
		this.id = id;
		this.args = args;
		this.block = block;	
		this.block.setInFunction(true);
	}
	
	@Override
	public String toPrint(String indent) {
		String argList = "";
		String status = "";
		
		if( args.size() > 0 )
		{
			for (int i = 0; i < this.args.size(); i ++)
				argList = argList + "\n" + this.args.get(i).toPrint(indent + "  ");
			
			status = "\n  " + indent + "STEntry: Arg Status = " + this.id.getSTEntry().getParEffectList();
		}
		
		String dec = "";
		if( block.getDeclarationsSize() > 0 )
		{
			ArrayList<Node> decs = block.getDeclarations();
			for (int i = 0; i < block.getDeclarationsSize(); i ++)
				dec = dec + "\n" + decs.get(i).toPrint(indent + "  ");
		}
		
		String body = "";
		if( block.getStatementsSize() > 0 )
		{
			ArrayList<Node> stms = block.getStatements();
			for (int i = 0; i < block.getStatementsSize(); i ++)
				body = body + "\n" + stms.get(i).toPrint(indent + "  ");
		}
		
		return indent + "Fun: " + id.getId() + "\n" 
			+ type.toPrint(indent + "  ") + argList + status + dec + body;
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		Node bodyType = block.typeCheck();
		
		if ( type instanceof PointerTypeNode )
			throw new TypeErrorException("function " + id.getId() + " cannot be of type pointer.");
		
		if( ! SimpLanPlusLib.isEquals(bodyType, type) )
			throw new TypeErrorException("wrong return type for function " + id.getId());
		
		return null; //valore di ritorno non usato
	}

	/*
	 * ...
	 * 	 -->DECS
	 *	 -->RA
	 * AL
	 * 
	 * ARG 1
	 * ...
	 * ARG n-1
	 * ARG n
	 * 
	 * OLD FP
	 * */
	@Override
	public String codeGeneration() {
		String code = "";
		int n = args.size();
		
		String label = SimpLanPlusLib.freshLabel();
		code = code + "b " + label + "\n";
				
		code = code + id.getSTEntry().getFunLabel() + ":\n";
		code = code + "mv $fp $sp\n";
		code = code + "push $ra\n";
		block.setFunEndLabel(id.getSTEntry().getFunEndLabel());
		
		code = code + block.codeGeneration(); 
		
		code = code + id.getSTEntry().getFunEndLabel() + ":\n";
		code = code + "lw $ra 0($sp)\n"; 			// $ra <- top
		code = code + "addi $sp $sp "+(n+2)+"\n"; 	// pop di parametri formali + ra + al
		code = code + "lw $fp 0($sp)\n";
		code = code + "pop\n"; 						// pop di OldFP
		code = code + "li $ret 0\n";				// ripristino il valore di $ret
		code = code + "jr $ra\n";
		
		code = code + label + ":\n";
		
		return code;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
		
		STEntry entry = new STEntry(env.getNestingLevel(), env.getAndUpdateOffset() ); 
  
        try {
        	env.addEntry(id.getId(), entry);
        	entry.setFunLabels();
        	id.setSTEntry(entry);
        	
        	env.addScope();
        	/* aggiungo la dichiarazione della fun anche nel nuovo
        	   scope per evitare che vengano dichiarate altre funzioni
        	   o variabili con lo stesso nome */
        	env.addEntry(id.getId(), entry);
        	
        	ArrayList<Node> parTypes = new ArrayList<Node>();
			int paroffset = 1;
			// check args
			for(Node a : args){
				ArgNode arg = (ArgNode) a;
				Node argType = arg.getType();
				if( argType  instanceof PointerTypeNode )
					 ((PointerTypeNode) argType).setDerNum(argType.getDereferenceNum(), 0, arg.getId().getId()); 
				parTypes.add(argType);
				try {
					STEntry parEntry = new STEntry(env.getNestingLevel(), arg.getType(), paroffset++); 
					env.addEntry(arg.getId().getId(), parEntry);
					arg.getId().setSTEntry(parEntry);
				}catch(MultipleDecException e) {
					res.add(new SemanticError("Parameter id " + arg.getId().getId() + " already declared"));
					return res;
				}		
			}
        	
			// set func type
			entry.setType( new ArrowTypeNode(parTypes, type) );
			
			block.setIsFunBody(true); 
			
			/* creo un nuovo ambiente in cui valutare il corpo della funzione.
			   il nuovo ambiente contiene solo le dichiarazioni di funzione precedenti.
			   nel corpo della funzione non si può accedere alle variabili globali. */
			Environment env_0 = null;
			try {
				env_0 = env.envFunc();
				for( Node a : args ) {
					// nuova copia della entry del paramentro
					IdNode arg = ((ArgNode) a).getId(); 
					STEntry newArgEntry = new STEntry(arg.getSTEntry());
					
					// inserisco la entry del parametro nell'ambiente
					try {
						env_0.addEntry(arg.getId(), newArgEntry);
					} catch (MultipleDecException e) {
						res.add(new SemanticError("Arg id " + arg.getId() + " already declared"));
						return res;
					}
				}
			} catch (MultipleDecException e1) {
				res.add(new SemanticError("There is a function already declared"));
				return res;
			} 
			res.addAll(block.checkSemantics(env_0));
			
			env.removeScope();
			
        }catch(MultipleDecException e) {
        	res.add(new SemanticError("Fun id " + id.getId() + " already declared"));
        }
        
        return res;		
	}

	
	/*
	 * Sigma_0 = [x1 -> init, y1 -> init]
	 * {Sigma_FUN, Sigma_0[f -> Sigma_0 ->Sigma_1]} |- s {Sigma_FUN, Sigma_1[f -> Sigma_0 ->Sigma_1]}
	 * ------------------------------------------------------------------------------------------------
	 * Sigma |- f( var T1 x1, T1' y1) s : Sigma[f -> Sigma_0 ->Sigma_1]
	 * 
	 * */
	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		ArrayList<SemanticError> errorsPuntoFisso = new ArrayList<SemanticError>();
		
		/* Aggiungo all'ambiente la entry relativa alla funzione, 
		 * ho bisogno di questa entry perchè sono ammesse le chiamate 
		 * ricorsive 
		 **/
		try {
			env.addEntry(id.getId(), id.getSTEntry());
		} catch (MultipleDecException e) {
			errors.add(new SemanticError("Fun id " + id.getId() + " already declared"));
			return errors;
		}
		
		
		/* creo l'ambiente {Sigma_FUN, Sigma_0[f -> Sigma_0 ->Sigma_1]}.
		 * - faccio una copia dell'ambiente corrente, e
		 * - creo un nuovo scope per i parametri della funzione.
		 * 
		 * creo la lista degli effetti dei parametri sigma_0
		 **/
		Environment env_0 = null;
		try {
			env_0 = env.envFunc();
			env_0.addScope();
		} catch (MultipleDecException e1) {
			errors.add(new SemanticError("There is a function already declared"));
			return errors;
		} 
		
		List<List<Effect>> sigma_0 = new ArrayList<List<Effect>>(args.size());
		for( Node a : args ) {
			// nuova copia della entry del paramentro
			IdNode arg = ((ArgNode) a).getId();
			STEntry newArgEntry = new STEntry(arg.getSTEntry());
			
			// inserisco la entry del parametro nell'ambiente
			try {
				env_0.addEntry(arg.getId(), newArgEntry);
			} catch (MultipleDecException e) {
				errors.add(new SemanticError("Arg id "+arg.getId() +" already declared"));
				return errors;
			}
			
			List<Effect> argEffects = new ArrayList<Effect>();
			for(int i = 0; i < newArgEntry.getSizeVarEffects(); i ++) {
				argEffects.add(new Effect(Effect.READ_WRITE));	
			}
			
			newArgEntry.setVarEffectList(argEffects);
			sigma_0.add(argEffects);
		}
		
		/*
		 * Aggiungo la entry della funzione nel nuovo scope
		 * */
		STEntry newFunEntry = new STEntry(id.getSTEntry());
		try {		
			newFunEntry.setParEffectList(sigma_0);
			env_0.addEntry(id.getId(), newFunEntry);
		} catch (MultipleDecException e) {
			errors.add(new SemanticError("Fun id " + id.getId() + " already declared"));
			return errors;
		}
		
		
		
		
		
		
		/*System.out.println("\n-----------------------------sigma_0");
		for( int j = 0; j < sigma_0.size(); j ++)
			System.err.println("EFFETTI sigma_0 "
					+"    "+hashEffect(sigma_0.get(j)));
		*/
		
		
		/*
		 * Calcolo del punto fisso
		 * */
		// Effetti dei parametri dopo una valutazione del corpo della funzione
		List<List<Effect>> sigma_1 = new ArrayList<List<Effect>>(/*sigma_0*/);
		
		
	
		
		
		// Lista che serve a mantenere una copia di sigma_1.
		// prec_sigma_1 usata per fare il controllo di terminazione del punto fisso.
		List<List<Effect>> prec_sigma_1 = new ArrayList<List<Effect>>(/*sigma_0*/);
		
		for( int j = 0; j < sigma_0.size(); j ++) {
			sigma_1.add(new ArrayList<Effect>());
			prec_sigma_1.add(new ArrayList<Effect>());
			for( int i = 0; i < sigma_0.get(j).size(); i ++) {
				sigma_1.get(j).add(new Effect( sigma_0.get(j).get(i) ));
				prec_sigma_1.get(j).add(new Effect( sigma_0.get(j).get(i) ));
			}
				
		}
		
		
		/*System.out.println("\n-----------------------------sigma_1");
		for( int j = 0; j < sigma_1.size(); j ++)
			System.err.println("EFFETTI sigma_1 "
					+"    "+hashEffect(sigma_1.get(j))
					+"    "+hashEffect(prec_sigma_1.get(j)));
		*/
		boolean stop = false;
		while( !stop ) {
		
			/*System.out.println("\nPUNTO FISSO    -------- inizio ");
			for( int j = 0; j < sigma_1.size(); j ++)
				System.err.println("EFFETTI sigma_1 "
						+"    "+hashEffect(sigma_1.get(j)));
			*/
			
			
			// valuto gli effetti nel corpo della funzione
			errorsPuntoFisso.clear();
			errorsPuntoFisso.addAll(block.checkEffects(env_0));
			
			// ricavo gli effetti ottenuti dopo la valutazione del corpo della funzione
			String argId = null;
			try {
				sigma_1.clear();
				for( Node a : args ) {
					argId = ((ArgNode) a).getId().getId();
					sigma_1.add( env_0.lookup(argId).getVarEffectList() );
				}
				// setto i nuovi effetti dei parametri della funzione
				env_0.lookup(id.getId()).setParEffectList(sigma_1);
				
			} catch (MissingDecException e) {
				errors.add(new SemanticError("Missing declaration: " + argId));
				return errors;
			}
			
			
			/*System.out.println("\nPUNTO FISSO     -------- fine ");
			for( int j = 0; j < sigma_1.size(); j ++)
				System.err.println("EFFETTI sigma_1 "
						+"    "+hashEffect(sigma_1.get(j)));
			System.out.println("\nPUNTO FISSO     -------- fine prec ");
			for( int j = 0; j < prec_sigma_1.size(); j ++)
				System.err.println("EFFETTI sigma_1_prec "
						+"    "+hashEffect(prec_sigma_1.get(j)));
			*/
			
			
			// controllo terminazione punto fisso (prec_sigma_1 == sigma_1)
			if( prec_sigma_1.equals(sigma_1) )
				stop = true;
			else {
				prec_sigma_1 = new ArrayList<List<Effect>>(sigma_1);
				
				for( Node a : args ) {
					IdNode arg = ((ArgNode) a).getId(); 
					STEntry argEntry = null;
					try {
						argEntry = env_0.lookup(arg.getId());
					} catch (MissingDecException e) {
						errors.add(new SemanticError("Missing declaration: " + arg.getId()));
						return errors;
					}
					
					for(int i = 0; i < argEntry.getSizeVarEffects(); i ++) {
						argEntry.setVarEffect(i, new Effect(Effect.READ_WRITE));
					}
				}
			}
		}
		
		errors.addAll(errorsPuntoFisso);
		
		// chiudi lo scope dopo il calcolo del punto fisso
		env_0.removeScope();
		
		// setto gli effetti della funzione nell'ambiente originale
		try {
			env.lookup(id.getId()).setParEffectList(sigma_1);
			
			
			/*for( int j = 0; j < sigma_1.size(); j ++)
				System.err.println("EFFETTI FUN "
						+"    "+hashEffect(sigma_1.get(j)));
			*/
			
		} catch (MissingDecException e) {
			errors.add(new SemanticError("Missing declaration: "+id.getId()));
			return errors;
		}
		
		return errors;
	}
	
	
	
	private String hashEffect(List<Effect> list) {
		String str="[";
		for(Effect e : list)
			str = str + e + ",";
		str=str+"]        [";
		for(Effect e : list)
			str = str + e.hashCode() + ",";
		return str+"]";
	}

}
