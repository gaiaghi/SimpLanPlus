package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class AssignmentLNode implements Node {
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws MissingDecException, MultipleDecException {
		return assignNode.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
