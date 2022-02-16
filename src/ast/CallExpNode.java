package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class CallExpNode implements Node {
	
	//gramma rule:
	//exp	    : call
	
	private CallNode call;
	
	public CallExpNode(CallNode call) {
		this.call = call;
	}

	@Override
	public String toPrint(String indent) {
		return call.toPrint(indent);
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		return call.typeCheck();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws MissingDecException, MultipleDecException {
		return call.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
