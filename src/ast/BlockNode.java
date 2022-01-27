package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class BlockNode implements Node {
	//grammar rule:
	//block	    : '{' declaration* statement* '}';

	private ArrayList<Node> declarations;
	private ArrayList<Node> statements;
	
	public BlockNode(ArrayList<Node> declarations, ArrayList<Node> statements) {
		this.declarations = declarations;
		this.statements = statements;
		
	}

	@Override
	public String toPrint(String indent) {
		// TODO Auto-generated method stub
		return null;
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
