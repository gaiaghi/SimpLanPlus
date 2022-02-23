package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class VoidTypeNode implements Node {

	public VoidTypeNode() {
	
	}

	@Override
	public String toPrint(String indent) {
		return indent +"VoidType";
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
		// TODO Auto-generated method stub
		return null;
	}

}
