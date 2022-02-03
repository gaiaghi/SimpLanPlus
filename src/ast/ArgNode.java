package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class ArgNode implements Node {
//	arg         : type ID;
	
	private Node type;
	private Node id;
	
	public ArgNode(Node type, Node id) {
		this.type = type;
		this.id = id;
	}
	
	public Node getType() {
		return this.type;
	}
	
	public Node getId() {
		return this.id;
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "Arg:\n" + this.type.toPrint(indent +"  ") +"\n"+  this.id.toPrint(indent +"  ");
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
		return new ArrayList<SemanticError>();
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
