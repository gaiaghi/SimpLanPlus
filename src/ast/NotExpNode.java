package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class NotExpNode implements Node {
	//grammar rule:
	//exp	    : '!' exp
	
	private Node exp;
	
	public NotExpNode(Node exp) {
		this.exp = exp;
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
