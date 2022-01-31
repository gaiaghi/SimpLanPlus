package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class CallLNode implements Node {
//	statement   :	call ';'				#callL
	private Node call;
	
	public CallLNode (Node call) {
		this.call = call;
	}
	
	@Override
	public String toPrint(String s) {
		return s + call;
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
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
