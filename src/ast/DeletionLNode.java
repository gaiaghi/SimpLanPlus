package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class DeletionLNode implements Node {
//	statement   : deletion ';'			#deletionL
	
	private Node deletion;
	
	public DeletionLNode(Node deletion) {
		this.deletion = deletion; 
	}
	

	@Override
	public String toPrint(String indent) {
		return indent + deletion.toPrint(indent);
	}
	
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		return deletion.typeCheck();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws MissingDecException, MultipleDecException {
		return deletion.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
