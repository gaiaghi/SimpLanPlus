package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class RetLNode implements Node{
//	statement   : ret ';'				#retL
	
	private Node ret;
	
	public RetLNode(Node ret) {
		this.ret = ret; 
	}

	
	@Override
	public String toPrint(String indent) {
		return ret.toPrint(indent);
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
		return ret.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
