package ast;

import java.util.ArrayList;

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
		return deletion.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
