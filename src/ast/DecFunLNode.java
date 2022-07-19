package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class DecFunLNode implements Node {
	
	// grammar rule:
	// declaration : decFun
	
	private Node decFun;
	
	public DecFunLNode(Node decFun) {
		this.decFun = decFun;
	}

	@Override
	public String toPrint(String indent) {
		return this.decFun.toPrint(indent);
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}
	
	@Override
	public Node typeCheck() throws TypeErrorException{
		return decFun.typeCheck();
	}

	@Override
	public String codeGeneration() {
		return decFun.codeGeneration();
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return decFun.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return decFun.checkEffects(env);
	}
	
	

}
