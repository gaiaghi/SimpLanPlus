package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class DeletionLNode implements Node {
	
	// grammar rule:
	// statement   : deletion ';'			
	
	private Node deletion;
	
	public DeletionLNode(Node deletion) {
		this.deletion = deletion; 
	}
	

	@Override
	public String toPrint(String indent) {
		return deletion.toPrint(indent);
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
		return deletion.codeGeneration();
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return deletion.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return deletion.checkEffects(env);
	}

}
