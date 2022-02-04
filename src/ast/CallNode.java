package ast;

import java.util.ArrayList;

import parser.SimpLanPlusParser.ExpContext;
import util.Environment;
import util.SemanticError;
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
	public Node typeCheck() {
		// TODO Auto-generated method stub
		//controllo numero e tipo parametri qui
		return null;
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
		}
		
		return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
