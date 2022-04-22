package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class BoolTypeNode implements Node {
	
	//grammar rule:
	//type	    : 'bool'
	
	public BoolTypeNode() {
		
	}

	@Override
	public String toPrint(String indent) {
		return indent +"BoolType";
	}

	@Override
	public Node typeCheck() {
		return null;
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return new ArrayList<SemanticError>();
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return new ArrayList<SemanticError>();
	}

}
