package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
import parser.SimpLanPlusParser.ExpContext;
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
	public ArrayList<SemanticError> checkSemantics(Environment env) throws MissingDecException, MultipleDecException {
		ArrayList<SemanticError> res = new ArrayList<>();
	
		//controllo dichiarazione id
		res.addAll(id.checkSemantics(env));
		
		if (res.size()==0) {
			this.nestingLvl = env.getNestingLevel(); 
			
			//controllo parametri
			for(Node par : parlist)
				res.addAll(par.checkSemantics(env));
		}
		
		return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
