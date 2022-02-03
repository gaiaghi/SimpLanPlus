package ast;

import java.util.ArrayList;

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
		return call.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
