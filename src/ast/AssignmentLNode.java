package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class AssignmentLNode implements Node {
	
	private Node assignNode;
	
	public AssignmentLNode(Node visitAssignment) {
		this.assignNode = visitAssignment;
	}

	
	@Override
	public String toPrint(String s) {
		return s + assignNode;
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
