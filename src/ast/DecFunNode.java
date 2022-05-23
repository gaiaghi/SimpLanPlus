package ast;

import java.util.ArrayList;
import java.util.HashMap;
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
//   decFun	    : (type | 'void') ID '(' (arg (',' arg)*)? ')' block ;

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
			//argList = "\n";
			for (int i = 0; i<  this.args.size(); i++)
				argList = argList +"\n" +this.args.get(i).toPrint(indent +"  ");
			status = "\n  " + indent +"STEntry: Arg Status = " + this.id.getSTEntry().getParEffectList();
		}
		
		String dec = "";
		if( block.getDeclarationsSize() > 0 )
		{
			//dec = "\n";
			ArrayList<Node> decs = block.getDeclarations();
			for (int i = 0; i<  block.getDeclarationsSize(); i++)
				dec = dec + "\n" + decs.get(i).toPrint(indent +"  ");
		}
		
		String body = "";
		if( block.getStatementsSize() > 0 )
		{
			//body = "\n";
			ArrayList<Node> stms = block.getStatements();
			for (int i = 0; i<  block.getStatementsSize(); i++)
				body = body + "\n" + stms.get(i).toPrint(indent +"  ");
		}
		
		return indent +"Fun: " +id.getId() +"\n" 
			+type.toPrint(indent +"  ") +argList +status +dec +body;
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		Node bodyType = block.typeCheck();
		if (type instanceof PointerTypeNode)
			throw new TypeErrorException("function "+id.getId()+" cannot be of type pointer.");
		
		if( ! SimpLanPlusLib.isEquals(bodyType, type) )
			throw new TypeErrorException("wrong return type for function "+ id.getId());
		
		return null; //valore di ritorno non usato
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
  
		//TODO env.offset = -2;
  		//PROF: STentry entry = new STentry(env.nestingLevel, env.offset--);
		//dovo decrementare l'offset dopo aver creato una nuova entry?
  		//controlla offset passato come parametro
		STEntry entry = new STEntry(env.getNestingLevel(), env.getOffset()); 
        env.updateOffset(); //decremento offset
  
        try {
        	env.addEntry(id.getId(), entry);
        	id.setSTEntry(entry);
        	
        	env.addScope();
        	
        	//aggiungo la dichiarazione della fun anche nel nuovo
        	//scope per evitare che vengano dichiarate altre funzioni
        	//o variabili con lo stesso nome
        	env.addEntry(id.getId(), entry);
        	
        	ArrayList<Node> parTypes = new ArrayList<Node>();
			int paroffset=1;
			//check args
			for(Node a : args){
				ArgNode arg = (ArgNode) a;
				parTypes.add(arg.getType());
				try {
					STEntry parEntry = new STEntry(env.getNestingLevel(), arg.getType(), paroffset++);
					env.addEntry(arg.getId().getId(), parEntry);
					arg.getId().setSTEntry(parEntry);
				}catch(MultipleDecException e) {
					res.add(new SemanticError("Parameter id "+arg.getId().getId()+" already declared"));
				}		
			}
        	
			//set func type
			entry.setType( new ArrowTypeNode(parTypes, type) );
			
			block.setIsFunBody(true); 
			res.addAll(block.checkSemantics(env));
			
			env.removeScope();
			
        }catch(MultipleDecException e) {
        	res.add(new SemanticError("Fun id "+id.getId() +" already declared"));
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
		
		/* Aggiungo all'ambiente la entry relativa alla funzione, 
		 * ho bisogno di questa entry perchè sono ammesse le chiamate 
		 * ricorsive 
		 **/
		try {
			env.addEntry(id.getId(), id.getSTEntry());
		} catch (MultipleDecException e) {
			errors.add(new SemanticError("Fun id "+id.getId() +" already declared"));
			return errors;
		}
		
		
		/* creo l'ambiente {Sigma_FUN, Sigma_0[f -> Sigma_0 ->Sigma_1]}.
		 * - faccio una copia dell'ambiente corrente, e
		 * - creo un nuovo scope per i parametri della funzione.
		 * 
		 * creo la lista degli effetti dei parametri sigma_0
		 **/
		Environment env_0 = new Environment(env); //copio il vecchio ambiente per avere accesso alle variabili globali
		env_0.addScope();
		//Environment env_0 = new Environment();
		//env_0.addEntry(id.getId(), id.getSTEntry());
		/*QUI sopra! ci vuole un copia dell'ambiente o un ambiente nuovo (con solo la entry della funzione)? 
		 * nella regola c'è scritto che non dovrei avere accesso alle variabili globali
		 * */
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
			for(int i=0; i<newArgEntry.getSizeVarEffects(); i++) {
				argEffects.add(new Effect(Effect.READ_WRITE));		//Effect.INITIALIZED oppure Effect.RW??
			}
			newArgEntry.setVarEffectList(argEffects);
			sigma_0.add(argEffects);
		}
		
		/*
		 * Aggiungo la entry della funzione nel nuovo scope
		 * */
		STEntry newFunEntry = new STEntry(id.getSTEntry());
		try {
			env_0.addEntry(id.getId(), newFunEntry);
		} catch (MultipleDecException e) {
			errors.add(new SemanticError("Fun id "+id.getId() +" already declared"));
			return errors;
		}
		
		
		/*
		 * Calcolo del punto fisso
		 * */
		List<List<Effect>> sigma_1 = new ArrayList<List<Effect>>(args.size());
		boolean stop = false;
		while( !stop ) {
			// setto gli effetti dei parametri della funzione
			newFunEntry.setParEffectList(sigma_0);
			// valuto gli effetti nel corpo della funzione
			errors.addAll(block.checkEffects(env_0));
			
			// ricavo gli effetti ottenuti dopo la valutazione del corpo della funzione
			//sigma_1 = newFunEntry.getParEffectList();
			String argId = null;
			try {
				sigma_1.clear();
				for( Node a : args ) {
					// nuova copia della entry del paramentro
					argId = ((ArgNode) a).getId().getId(); 
					sigma_1.add( env_0.lookup(argId).getVarEffectList() );
				}
				
			} catch (MissingDecException e) {
				errors.add(new SemanticError("DecFunNode: Missing declaration: "+argId));
				return errors;
			}
			
			// controllo terminazione punto fisso (sigma_0 == sigma_1)
			if( sigma_0.equals(sigma_1) )
				stop = true;
			else
				sigma_0 = sigma_1;
		}
		
		
		// chiudi lo scope dopo il calcolo del punto fisso
		env_0.removeScope();
		
		
		// setto gli effetti della funzione nell'ambiente originale
		try {
			env.lookup(id.getId()).setParEffectList(sigma_1);
		} catch (MissingDecException e) {
			errors.add(new SemanticError("DecFunNode: Missing declaration: "+id.getId()));
			return errors;
		}
		
		return errors;
	}
	

}
