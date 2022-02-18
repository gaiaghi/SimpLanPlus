package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import util.Environment;
import util.SemanticError;

public class PointerTypeNode implements Node {

	//grammar rule:
	//type	    : '^' type
	
	private Node type;
	
	public PointerTypeNode(Node type) {
		this.type = type; 
	}

	@Override
	public String toPrint(String indent) {
		return indent +"PointerType";
	}

	@Override
	public Node typeCheck() {
		return type;
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
	
	public Node getPointedType () {
		if( type instanceof PointerTypeNode )
			return ((PointerTypeNode) type).getPointedType();
		else
			return type;
	}
	
	


}
