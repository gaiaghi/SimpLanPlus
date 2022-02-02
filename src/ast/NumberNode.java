package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class NumberNode implements Node {
	//grammar rule:
	//exp	    : NUMBER
	
	private int val;
	
	public NumberNode(int val) {
		this.val = val;
	}

	@Override
	public String toPrint(String indent) {
		return indent +"Number: " +val;
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
