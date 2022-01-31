package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class PointerTypeNode implements Node {

	//grammar rule:
	//type	    : '^' type
	
	private Node type;
	
	public PointerTypeNode(Node type) {
		this.type = type; 
	}

	@Override
	public String toPrint(String indent) {
		// TODO Auto-generated method stub
		return null;
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
