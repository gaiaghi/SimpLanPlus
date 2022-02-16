package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class BaseExpNode implements Node {
	//grammar rule:
	//exp	    : '(' exp ')'
	
	private Node exp;
	
	public BaseExpNode(Node exp) {
		this.exp = exp;
	}
	
	@Override
	public String toPrint(String indent) {
		return exp.toPrint(indent);
	}

	@Override
	public Node typeCheck() throws TypeErrorException {
		return exp.typeCheck();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws MissingDecException, MultipleDecException {
		return exp.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
