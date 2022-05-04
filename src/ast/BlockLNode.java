package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class BlockLNode implements Node {
//	statement   :	block					#blockL;
	
	private Node block;
	
	public BlockLNode(Node block) {
		this.block = block; 
	}
	

	@Override
	public String toPrint(String indent) {
		return block.toPrint(indent);
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		return block.typeCheck();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return block.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return block.checkEffects(env);
	}

	
	public void setInFunction(boolean b) {
		((BlockNode) block).setInFunction(b);
	}
	
	
	public boolean getReturns() {
		return ((BlockNode) block).getReturns();
	}
	

}
