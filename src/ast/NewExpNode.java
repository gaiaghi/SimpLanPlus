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
		String code = "li $t1 -1\n";
		code = code + "sw $t1 0($hp)\n";
		code = code + "addi $a0 $hp 0\n"; // copio l'indirizzo puntato da $hp in $a0
		code = code + "addi $hp $hp 1\n"; // incremento $hp
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
