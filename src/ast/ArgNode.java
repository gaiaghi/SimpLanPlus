package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import util.Environment;
import util.SemanticError;

public class ArgNode implements Node {
//	arg         : type ID;
	
	private Node type;
	private IdNode id;
	
	public ArgNode(Node type, IdNode id) {
		this.type = type;
		this.id = id;
	}
	
	public Node getType() {
		return this.type;
	}
	
	public IdNode getId() {
		return this.id;
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "Arg: " +this.id.getId() +"\n" +this.type.toPrint(indent +"  ");
	}

	@Override
	public Node typeCheck() {
		return null;
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws MissingDecException, MultipleDecException {
		return new ArrayList<SemanticError>();
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
