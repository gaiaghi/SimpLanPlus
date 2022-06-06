package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class IteLNode implements Node{
//	statement   :	ite					#iteL
	
	private Node ite;
	
	
	public IteLNode(Node ite) {
		this.ite = ite;
	}

	
	@Override
	public String toPrint(String indent) {
		return ite.toPrint(indent);
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException {
		return ite.typeCheck();
	}

	@Override
	public String codeGeneration() {
		return ite.codeGeneration();
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return ite.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return ite.checkEffects(env);
	}

	
	public void setInFunction(boolean b) {
		((IteNode) ite).setInFunction(b);
	}
	
	
	public boolean getReturns() {
		return ((IteNode) ite).getReturns();
	}
}
