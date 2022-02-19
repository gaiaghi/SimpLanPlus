package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import util.Environment;
import util.SemanticError;

public class NewExpNode implements Node {

	//grammar rule:
	//exp	    : new type
	
	private Node type;
	
	public NewExpNode(Node type) {
		this.type = type;
	}

	@Override
	public String toPrint(String indent) {
		return indent +"New:\n" +type.toPrint(indent);
	}

	@Override
	public Node typeCheck() {
		return new PointerTypeNode(type);
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws MissingDecException, MultipleDecException {
		return new ArrayList<>();
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Node getNode() {
		return type;
	}
	


}
