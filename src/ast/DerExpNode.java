package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class DerExpNode implements Node {

	//grammar rule:
	//exp	    : lhs
	
	private LhsNode lhs;
	
	public DerExpNode(LhsNode lhs) {
		this.lhs = lhs;
	}

	@Override
	public String toPrint(String indent) {
		return lhs.toPrint(indent);
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		return lhs.typeCheck();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws MissingDecException, MultipleDecException {
		return lhs.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public LhsNode getLhs() {
		return lhs;
	}

}
