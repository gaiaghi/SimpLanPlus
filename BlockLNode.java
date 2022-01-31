package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class BlockLNode implements Node {
//	statement   :	block					#blockL;
	
	private Node block;
	
	public BlockLNode(Node block) {
		this.block = block;
	}
	

	@Override
	public String toPrint(String s) {
		return s + block;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}


}
