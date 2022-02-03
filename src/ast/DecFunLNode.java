package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class DecFunLNode implements Node {

	private Node decFun;
	
	public DecFunLNode(Node decFun) {
		this.decFun = decFun;
	}

	@Override
	public String toPrint(String indent) {
		return indent + this.decFun.toPrint(indent);
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
		return decFun.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
