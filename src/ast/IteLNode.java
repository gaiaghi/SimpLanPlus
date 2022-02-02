package ast;

import java.util.ArrayList;

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
//	statement   :	ite					#iteL
	
}
