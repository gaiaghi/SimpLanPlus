package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class DecFunNode implements Node {
//   decFun	    : (type | 'void') ID '(' (arg (',' arg)*)? ')' block ;

	private Node type;
	private Node id;
	private ArrayList<Node> args;
	private Node block;
	
	public DecFunNode(Node type, Node id, ArrayList<Node> args, Node block) {
		this.type = type;
		this.id = id;
		this.args = args;
		this.block = block;
		
	}
	
	@Override
	public String toPrint(String indent) {
		String argList = this.args.get(0).toPrint(indent);
		for (int i = 1; i<  this.args.size(); i++)
			argList = argList + ", " + this.args.get(i).toPrint(indent);
		
		String dec = indent + "Fun. declaration: ( "+ id + ": " + argList 
				+ " -> " + this.type.toPrint(indent) + "; body: " 
				+ this.block.toPrint(indent) + " )"; 
		return dec;
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
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
