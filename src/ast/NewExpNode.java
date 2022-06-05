package ast;

import java.util.ArrayList;

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
		PointerTypeNode node = new PointerTypeNode(type);
		node.setDerNum(node.getDereferenceNum(), 0, "new expression");
		return node;
	}

	@Override
	public String codeGeneration() {
		// TODO da rivedere
		String code = "li $t1 -1\n";
		code = code + "sw $t1 0($hp)\n";
		return code;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return new ArrayList<>();
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return new ArrayList<SemanticError>();
	}
	
	
	public Node getNode() {
		return type;
	}
	


}
