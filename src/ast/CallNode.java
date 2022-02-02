package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;
import util.STEntry;

public class CallNode implements Node {
	
	//grammar rule:
	//call        : ID '(' (exp(',' exp)*)? ')';
	
	private IdNode id;
	private STEntry entry; 
	private ArrayList<Node> parlist; 
	private int nestinglevel;
	
	public CallNode(IdNode id, ArrayList<Node> args) {
		this.id = id;
	    parlist = args;
	}

	@Override
	public String toPrint(String indent) {
		String str = "Call:\n" +id.toPrint(indent +"  ");
		if(parlist.size() > 0)
			str = str + "\n";
		
		for(int i=0; i<parlist.size(); i++)
			str = str + parlist.get(i).toPrint(indent +"  ");
		
		return str;
	}

	@Override
	public Node typeCheck() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
