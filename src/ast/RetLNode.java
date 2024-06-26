package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class RetLNode implements Node{

	// grammar rule:
	// statement   : ret ';'				
	
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
	public Node typeCheck() throws TypeErrorException {
		return ret.typeCheck();
	}

	@Override
	public String codeGeneration() {
		return ret.codeGeneration();
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return ret.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return ret.checkEffects(env);
	}
	
	public void setInFunction(boolean b) {
		((RetNode) ret).setInFunction(b);
	}
	
	public void setFunEndLabel(String label) {
		((RetNode) ret).setFunEndLabel(label);
	}
	
	
}
