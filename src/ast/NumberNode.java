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
		return new IntTypeNode();
	}

	@Override
	public String codeGeneration() {
		return "li $a0 " +val +"\n";
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return new ArrayList<>();
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return new ArrayList<SemanticError>();
	}

}
