package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import util.Environment;
import util.SemanticError;

public class BoolNode implements Node {
	//grammar rule:
	//exp	    : BOOL
	
	private Boolean val;
	
	public BoolNode(Boolean val) {
		this.val = val;
	}

	@Override
	public String toPrint(String indent) {
		return indent +"Bool: " +val;
	}

	@Override
	public Node typeCheck() {
		return new BoolTypeNode();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws MissingDecException, MultipleDecException {
		return new ArrayList<>();
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
