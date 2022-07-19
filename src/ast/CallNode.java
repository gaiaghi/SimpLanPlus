package ast;

import java.util.ArrayList;
import java.util.List;

import exception.MissingDecException;
import exception.TypeErrorException;
import util.Effect;
import util.Environment;
import util.SemanticError;
import util.SimpLanPlusLib;
import util.STEntry;

public class CallNode implements Node {
	
	// grammar rule:
	// call        : ID '(' (exp(',' exp)*)? ')';
	
	private IdNode id;
	private STEntry entry; 
	private ArrayList<Node> parlist; 
	private int nestingLvl;
	
	public CallNode(IdNode id, ArrayList<Node> args) {
		this.id = id;
	    parlist = args;
	}

	@Override
	public String toPrint(String indent) {
		String str = "Call: " + id.getId() 
					+ "\n" +id.getSTEntry().toPrint(indent + "  ");
		
		for(int i = 0; i < parlist.size(); i ++)
			str = str + "\n" + parlist.get(i).toPrint(indent + "  ");
		
		return indent + str;
	}

	@Override
	public Node typeCheck() throws TypeErrorException {
		// controllo che l'id corrisponda ad una funzione
		ArrowTypeNode funType = (ArrowTypeNode) id.getSTEntry().getType();
		if( !(funType instanceof ArrowTypeNode) )
			throw new TypeErrorException("invocation of a non-function " +id);
		
		// controllo che la chiamata abbia il numero corretto di parametri
	    ArrayList<Node> par_formali = funType.getParList();
	     
		if ( !(par_formali.size() == parlist.size()) ) 
			throw new TypeErrorException("wrong number of parameters in the invocation of " +id.getId());
	     
	    // controllo che il tipo dei parametri sia corretto
	    for (int i = 0; i < parlist.size(); i ++) {	
	    	Node formalParType = par_formali.get(i);
	 		Node actualParType = util.SimpLanPlusLib.getNodeIfPointer(parlist.get(i).typeCheck());
	 		 
	 		if( formalParType instanceof PointerTypeNode && actualParType instanceof PointerTypeNode ) {
	 			 
	 			PointerTypeNode pointerFormal = (PointerTypeNode) formalParType;
	 			int derNumFormalDec = pointerFormal.getDerNumDec();
				
	 			PointerTypeNode pointerActual = (PointerTypeNode) actualParType;
	 			int derNumActualDec = pointerActual.getDerNumDec();
	 			int derNumActual = pointerActual.getDerNumStm();
				 
	 			if( derNumFormalDec != (derNumActualDec - derNumActual) ) 
	 				throw new TypeErrorException("not valid pointer parameter " 
							+ pointerFormal.getErrorMsg() + " and " + pointerActual.getErrorMsg());
		 		
	 			formalParType = pointerFormal.getPointedType();
	 			actualParType = pointerActual.getPointedType();
	 		}
	 		 
		    if ( !(SimpLanPlusLib.isEquals( actualParType, formalParType) ) ) 
	    		throw new TypeErrorException("wrong type for " + (i+1) + "-th parameter in the invocation of " + id.getId() );
	    }
	     
	    return funType.getRet();
	}

	/*
	 * ...
	 * DECS
	 * RA
	 * AL
	 * 
	 * ARG 1
	 * ...
	 * ARG n-1
	 * ARG n
	 * 
	 * OLD FP
	 * */
	/* the stack discipline guarantees that on function exit $sp is the
	same as it was on function entry
	there is no need to store $sp in the AR */
	
	@Override
	public String codeGeneration() {
		
		String code = "";
		
		code = code + "push $fp\n";
		
		// caricamento dei parametri dall'ultimo al primo
		for (int i = parlist.size()-1; i >= 0; i --) {
			code = code + parlist.get(i).codeGeneration();
			code = code + "push $a0\n";
		}
		
		// Access link
		code = code + "mv $al $fp\n";
		for (int i = 0; i < nestingLvl - id.getNestingLevel(); i++ ) {
			code = code + "lw $al 0($al)\n";
		}
		code = code + "push $al\n";
		
		code = code + "jal " + entry.getFunLabel() + "\n";
		
		return code;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		ArrayList<SemanticError> res = new ArrayList<>();
	
		// controllo dichiarazione id
		res.addAll(id.checkSemantics(env));
		
		if (res.size()==0) {
			this.nestingLvl = env.getNestingLevel(); 
			
			// controllo parametri
			for(Node par : parlist)
				res.addAll(par.checkSemantics(env));
			
			entry = id.getSTEntry();
		}
		
		return res;
	}

	
	
	/*
	 * Regola [Invk-e]: 
	 * 
	 * (1)	Lambda |- f : &T1, .., &T_m, T1', .., T_n' -> return_type(f)				
	 * (2)	Sigma(f) = Sigma_0 -> Sigma_1 
	 * (3)	( Sigma_1(y_i) <= d ) per ogni 1<=i<=n	
	 * (4)  Sigma_2 = Sigma[ (z_i -> SEQ(Sigma(z_i), RW) ) per z_i in Var(e1, .., e_n) ]
	 * (5)  Sigma_3 = PAR_(1<=i<=m) [ u_i -> SEQ(Sigma(u_i), Sigma_1(x_i)) ]
	 * ---------------------------------------------------------------------------------------
	 * (6) 	Sigma |- f( u_1, .., u_m, e1, .., e_n) s : update(Sigma_2, Sigma_3)
	 * 
	 * dove:
	 * 	(1) recupero il tipo della funzione f dalla entry
	 * 	(2) recupero gli effetti della funzione f dalla entry
	 * 	(3) i parametri formali y non devono avere effetto <= d
	 * 	(4) sulle variabili che compaiono in e_i si fa la SEQ con RW
	 *  (5) faccio la PAR delle SEQ tra l'effetto del parametro attuale u_i 
	 *  	e del parametro formale x_i corrispondente
	 *  (6) update(Sigma_2, Sigma_3)
	 * 
	 * e_1 parametro attuale <==> y_1 parametro formale 	Non sono puntatori.
	 * u_1 parametro attuale <==> x_1 parametro formale 	Sono puntatori.
	 * 
	 **/
	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		
		// controllo che i parametri siano inizializzati
		for(Node par : parlist)
			errors.addAll(par.checkEffects(env));
		
		
		//testEffetti
		List<List<Effect>> pl = new ArrayList();
		
		for(Node par : parlist) {
			try {
				if( par instanceof DerExpNode ) {
					pl.add( env.lookup(((DerExpNode) par).getLhs().getId().getId()).getVarEffectList()  );
					//System.err.println(id.getId()+"  CALL par "+hashEffect(env.lookup(((DerExpNode) par).getLhs().getId().getId()).getVarEffectList()));
				}
				else {
					ArrayList<Effect> arr = new ArrayList<Effect>();
					arr.add(new Effect(Effect.READ_WRITE));
					pl.add(arr);
				}
			} catch (MissingDecException e) {}
		}
		
		ArrayList<SemanticError> err = null;
		try {
			err = env.lookup(id.getId()).getDecFun().checkEffectsActualArgs(pl);
		} catch (MissingDecException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if( err.size() > 0 ) {
			//System.err.println("trovati errori ");
			errors.addAll(err);
			return errors;
		}
		
		
		
		// (1) recupero il tipo della funzione f dalla entry
		ArrowTypeNode funType = (ArrowTypeNode) entry.getType();
		
		// calcolo indici dei parametri formali y_i che non sono puntatori
		ArrayList<Integer> y_indexes = funType.getIndexesNotPointerPar();
		
		// calcolo indici dei parametri formali x_i che sono puntatori
		ArrayList<Integer> x_indexes = funType.getIndexesPointerPar();
		
		// (2) recupero gli effetti della funzione f dalla entry
		STEntry funEntry = null;
		try {
			funEntry = env.lookup(id.getId());
		} catch (MissingDecException e1) {}
		
		List< List<Effect> > parEffectList = funEntry.getParEffectList();
		
		
		// (3) i parametri formali y non devono avere effetto <= d
		for( int i : y_indexes ) {
			List<Effect> parEffect = parEffectList.get(i);
	
			for( int j = 0; j < parEffect.size(); j ++) {
				if( parEffect.get(j).getEffectValue() > Effect.DELETED.getEffectValue() ) {
					errors.add(new SemanticError("Formal parameter has error effect"));
					return errors;
				}	
			}
		}
		
		// (4) sulle variabili che compaiono in e_i si fa la SEQ con RW
		//Environment sigma_2 = new Environment(env);
		Environment sigma_2 = Environment.cloneEnvWithoutEffects(env);
		ArrayList<LhsNode> vars = new ArrayList<>();
		for( int i : y_indexes ) {
			vars.addAll(parlist.get(i).getIDsOfVariables());
		}
		
		for( LhsNode node: vars ) {
			String idvar = ((LhsNode) node).getId().getId();
			STEntry idEntry = null;
			try {
				idEntry = sigma_2.lookup(idvar);
			} catch (MissingDecException e) {}
			
			// creo una copia della entry per non modificare l'ambiente principale env,
			// modifico solo il nuovo ambiente sigma_2
			STEntry newEntry = new STEntry(idEntry);
			
			for( int j = 0; j < idEntry.getVarEffectList().size(); j ++)
				newEntry.getVarEffect(j).setEffect(idEntry.getVarEffectList().get(j));
			
			List<Effect> varEffect = newEntry.getVarEffectList();
			List<Effect> resultSeq = new ArrayList<Effect>();
			for( int i = 0; i < varEffect.size(); i ++ ) {
				resultSeq.add( Effect.seq(varEffect.get(i), Effect.READ_WRITE) );
			}
			
			for( int j = 0; j < resultSeq.size(); j ++)
				newEntry.getVarEffect(j).setEffect(resultSeq.get(j));
			
			sigma_2.safeAddEntry(idvar, newEntry);
		}
		
	
		// (5) faccio la PAR delle SEQ tra l'effetto del parametro attuale u_i 
		// 	e del parametro formale x_i corrispondente. Sono puntatori.
		//  Sigma_3 = PAR_(1<=i<=m) [ u_i -> SEQ(Sigma(u_i), Sigma_1(x_i)) ]
		ArrayList<Environment> envList = new ArrayList<Environment>();
		for( int i : x_indexes ) {
			
			// copia del collegamento tra puntatori se creato nelle funzioni
			for (int j: x_indexes) {
				if (i != j && parEffectList.get(i).size()>=parEffectList.get(j).size()) {
					List<Effect> bigPar = parEffectList.get(i);
					List<Effect> smallPar = parEffectList.get(j);
					int differ = bigPar.size() - smallPar.size();
					for(int k=0; k < bigPar.size()-differ; k++) {
						if ( bigPar.get(k+differ).hashCode() == smallPar.get(k).hashCode()) {

							STEntry newEntry = null;
							try {
								newEntry = env.lookup(((DerExpNode) parlist.get(i)).getLhs().getId().getId());
							} catch (MissingDecException e) {}
							
							List<Effect> smallParAttuale = null;
							STEntry smallEntry = null;
							try {
								smallEntry= env.lookup(((DerExpNode) parlist.get(j)).getLhs().getId().getId());
								smallParAttuale = smallEntry.getVarEffectList();
							} catch (MissingDecException e) {}

							newEntry.setVarEffect(k+differ, smallEntry.getVarEffect(k));

						}			
					}
				}
			}
			
			
			Environment tmp_env = new Environment();
			tmp_env.addScope();
			
			// recupero l'effetto Sigma_1(x_i)
			List<Effect> effettoParFormale = parEffectList.get(i);
			
			// recupero l'effetto Sigma(u_i)
			// dato che questo parametro attuale è di tipo PointerTypeNode
			List<Effect> effettoParAttuale = null;
			STEntry newEntry = null;
			try {
				newEntry = env.lookup(((DerExpNode) parlist.get(i)).getLhs().getId().getId());
				effettoParAttuale = newEntry.getVarEffectList();
			} catch (MissingDecException e) {}
			
			//i puntatori devono essere inizializzati per poterli passare come parametri
			/*for (int k = 0; k < effettoParAttuale.size(); k++) {
				if (effettoParAttuale.get(k).equals(Effect.INITIALIZED)) {
					String idName = ((DerExpNode) parlist.get(i)).getLhs().getId().getId();
					errors.add(new SemanticError("Cannot use not initialized pointer "+ idName +" as function parameter") );
					return errors;
					}
			}*/
			
			
			
			/* copio gli effetti del parametro attuale nel parametro formale.
			 * devo considerare il caso in cui il parametro attuale sia un 
			 * puntatore più lungo del parametro formale */
			List<Effect> resultSeq = new ArrayList<Effect>();
			int diff = effettoParAttuale.size() - effettoParFormale.size();
			
			for( int j = 0; j < effettoParAttuale.size() - effettoParFormale.size(); j ++ )
				resultSeq.add( effettoParAttuale.get(j) );
			
			for( int j = 0; j < effettoParFormale.size(); j ++ ) {
				resultSeq.add( Effect.seq(effettoParAttuale.get(j+diff), effettoParFormale.get(j)) );
			}
			
			
			for( int j = 0; j < resultSeq.size(); j ++)
				newEntry.getVarEffect(j).setEffect(resultSeq.get(j));

			
			tmp_env.safeAddEntry(((DerExpNode) parlist.get(i)).getLhs().getId().getId(), newEntry);	
			envList.add(tmp_env);
			
			
			((DerExpNode) parlist.get(i)).getLhs().getId().setSTEntry( new STEntry( newEntry ));
			
			
		}
		
		// faccio la PAR 
		Environment sigma_3 = new Environment();
		if( envList.size() > 0 ) {
			sigma_3 = envList.get(0);
			for( int i = 1; i < envList.size(); i ++) {
				sigma_3 = Environment.parEnv(sigma_3, envList.get(i));
			}
		}
		
		
		// (6) update(Sigma_2, Sigma_3)
		Environment updEnv = Environment.updateEnv(sigma_2, sigma_3);	

		
		// controllo se ci sono errori nell'ambiente ottenuta dalla update
		errors.addAll(updEnv.checkErrors());
		// copio l'ambiente ottenuto dalla Regola [Invk-e] nell'ambiente corrente 
		env.copyFrom(updEnv);
		
		return errors;
	}
	
	public List<LhsNode> getIDsOfVariables() {
		ArrayList<LhsNode> vars = new ArrayList<LhsNode>();
		
		for(Node par : parlist)
			vars.addAll(par.getIDsOfVariables());
		
		return vars;
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
