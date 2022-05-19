package ast;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import exception.MissingDecException;
import exception.TypeErrorException;
import util.Effect;
import util.Environment;
import util.SemanticError;
import util.SimpLanPlusLib;
import util.STEntry;

public class CallNode implements Node {
	
	//grammar rule:
	//call        : ID '(' (exp(',' exp)*)? ')';
	
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
		String str = "Call: " +id.getId() 
					+"\n" +id.getSTEntry().toPrint(indent +"  ");
		if(parlist.size() > 0)
			str = str + "\n";
		
		for(int i=0; i<parlist.size(); i++)
			str = str + parlist.get(i).toPrint(indent +"  ");
		
		return indent +str;
	}

	@Override
	public Node typeCheck() throws TypeErrorException {
		//controllo che l'id corrisponda ad una funzione
//		ArrowTypeNode funType = (ArrowTypeNode) entry.getType();
		ArrowTypeNode funType = (ArrowTypeNode) id.getSTEntry().getType();
		if( !(funType instanceof ArrowTypeNode) )
			throw new TypeErrorException("invocation of a non-function " +id);
		
		//controllo che la chiamata abbia il numero corretto di parametri
	     ArrayList<Node> par_formali = funType.getParList();
	     if ( !(par_formali.size() == parlist.size()) ) 
	    	 throw new TypeErrorException("wrong number of parameters in the invocation of " +id);
	     
	     //controllo che il tipo dei parametri sia corretto
	     for (int i=0; i<parlist.size(); i++) 
	    	 if ( !(SimpLanPlusLib.isEquals( (parlist.get(i)).typeCheck(), par_formali.get(i)) ) ) 
	    		 throw new TypeErrorException("wrong type for "+(i+1)+"-th parameter in the invocation of " +id);
	        
	     return funType.getRet();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		ArrayList<SemanticError> res = new ArrayList<>();
	
		//controllo dichiarazione id
		res.addAll(id.checkSemantics(env));
		
		if (res.size()==0) {
			this.nestingLvl = env.getNestingLevel(); 
			
			//controllo parametri
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
		
		// non è detto che serva
		//controllo parametri
		for(Node par : parlist)
			errors.addAll(par.checkEffects(env));
		
		// (1) recupero il tipo della funzione f dalla entry
		ArrowTypeNode funType = (ArrowTypeNode) entry.getType();
		
		// calcolo indici dei parametri formali y_i che non sono puntatori
		ArrayList<Integer> y_indexes = funType.getIndexesNotPointerPar();
		
		// calcolo indici dei parametri formali x_i che sono puntatori
		ArrayList<Integer> x_indexes = funType.getIndexesPointerPar();
		
		// (2) recupero gli effetti della funzione f dalla entry
		List< List<Effect> > parEffectList = entry.getParEffectList();
		
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
		Environment sigma_2 = new Environment(env);
		
		ArrayList<LhsNode> vars = new ArrayList<>();
		for( int i : y_indexes ) {
			vars.addAll(parlist.get(i).getIDsOfVariables());
		}// ci possono essere delle variabili duplicate in vars? SI, potresti usare TreeSet
		
		for( LhsNode node: vars ) {
			String idvar = ((LhsNode) node).getId().getId();
			STEntry idEntry;
			try {
				idEntry = sigma_2.lookup(idvar);
			} catch (MissingDecException e) {
				errors.add(new SemanticError("CallNode 1: Missing declaration: "+idvar));
				return errors;
			}
			
			// creo una copia della entry per non modificare l'ambiente principale env,
			// modifico solo il nuovo ambiente sigma_2
			STEntry newEntry = new STEntry(idEntry);
			List<Effect> varEffect = newEntry.getVarEffectList();
			List<Effect> resultSeq = new ArrayList<Effect>();
			for( int i = 0; i < varEffect.size(); i ++ ) {
				resultSeq.add( Effect.seq(varEffect.get(i), Effect.READ_WRITE) );
				// se resultSeq.get(i)=ERROR	devo dare errore? penso di si
				//if( resultSeq.get(i).equals(Effect.ERROR) ) {
				//	errors.add(new SemanticError("Effect error on CallNode (4)"));
				//	return errors;
				//}
			}
			newEntry.setVarEffectList(resultSeq);
			sigma_2.safeAddEntry(idvar, newEntry);
		}
		
		// (5) faccio la PAR delle SEQ tra l'effetto del parametro attuale u_i 
		// 	e del parametro formale x_i corrispondente. Sono puntatori.
		//  Sigma_3 = PAR_(1<=i<=m) [ u_i -> SEQ(Sigma(u_i), Sigma_1(x_i)) ]
		
		
		ArrayList<Environment> envList = new ArrayList<Environment>();
		
		for( int i : x_indexes ) {
			
			Environment tmp_env = new Environment();
			tmp_env.addScope();
			
			// recupero l'effetto Sigma_1(x_i)
			List<Effect> effettoParFormale = parEffectList.get(i);
			
			// recupero l'effetto Sigma(u_i)
			// dato che questo parametro attuale è di tipo PointerTypeNode,
			// dovrebbe esserci solo una variabile, quindi un LhsNode, (ma non penso, somma di 2 puntatori è possibile??)
			List<LhsNode> varsInX = parlist.get(i).getIDsOfVariables();
			for( LhsNode varX : varsInX ) {
				List<Effect> effettoParAttuale;
				STEntry newEntry;
				try {
					newEntry = new STEntry(env.lookup(varX.getId().getId()));
					 effettoParAttuale = newEntry.getVarEffectList();
				} catch (MissingDecException e) {
					errors.add(new SemanticError("CallNode 1: Missing declaration: "+varX.getId().getId()));
					return errors;
				}
				
				List<Effect> resultSeq = new ArrayList<Effect>();
				for( int j = 0; j < effettoParAttuale.size(); j ++ ) {
					resultSeq.add( Effect.seq(effettoParAttuale.get(j), effettoParFormale.get(j)) );
					// se resultSeq.get(i)=ERROR	devo dare errore? penso di si
					//if( resultSeq.get(i).equals(Effect.ERROR) ) {
					//	errors.add(new SemanticError("Effect error on CallNode (5)"));
					//	return errors;
					//}
				}
				newEntry.setVarEffectList(resultSeq);
				tmp_env.safeAddEntry(varX.getId().getId(), newEntry);	// ci potrebbero essere dublicati di varX.getId().getId()?
			}
			
			envList.add(tmp_env);
		}
		
		// faccio la PAR 
		Environment sigma_3 = new Environment();
		if( envList.size() > 0 ) {
			sigma_3 = envList.get(0);
			for( int i = 0; i < envList.size(); i ++) {
				sigma_3 = Environment.parEnv(sigma_3, envList.get(i));
				//manca il controllo degli errori Effect.ERROR in sigma_3
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

}
