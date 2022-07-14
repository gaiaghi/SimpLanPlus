package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class CallLNode implements Node {
	
	// grammar rule:
	// statement   :	call ';'		
	
	private Node call;
	
	public CallLNode (Node call) {
		this.call = call;
	}
	
	@Override
	public String toPrint(String indent) {
		return call.toPrint(indent);
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		return call.typeCheck();
	}

	@Override
	public String codeGeneration() {
		return call.codeGeneration();
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return call.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return call.checkEffects(env);
	}

}
