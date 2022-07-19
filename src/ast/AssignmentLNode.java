package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class AssignmentLNode implements Node {
	
	// grammar rule:
	// statement   : assignment ';'
	
	private Node assignNode;
	
	public AssignmentLNode(Node visitAssignment) {
		this.assignNode = visitAssignment; 
	}

	
	@Override
	public String toPrint(String indent) {
		return assignNode.toPrint(indent);
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		return assignNode.typeCheck();
	}

	@Override
	public String codeGeneration() {
		return assignNode.codeGeneration();
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return assignNode.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return assignNode.checkEffects(env);
	}

	
}
