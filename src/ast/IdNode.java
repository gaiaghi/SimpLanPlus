package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.TypeErrorException;
import util.Environment;
import util.STEntry;
import util.SemanticError;

public class IdNode implements Node {
	
	private String id;
	private STEntry entry;
	private int nestingLvl; //nesting level corrente
	
	public IdNode(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setSTEntry(STEntry entry) {
		this.entry = entry;
	}
	
	public STEntry getSTEntry() {
		return this.entry;
	}
	
	
	public int getNestingLevel() {
		return this.nestingLvl;
	}
	
	
	@Override
	public String toPrint(String indent) {
		return indent + "Id: " +id +"\n" +entry.toPrint(indent +"  ");
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		Node idType = entry.getType();
		if (idType instanceof ArrowTypeNode) {
			throw new TypeErrorException("wrong usage of function identifier "+ id);
		}
			  
		return idType;
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		ArrayList<SemanticError> res = new ArrayList<>();
		int nestLvl = env.getNestingLevel();
		
		try { //declared id
			this.entry = env.lookup(this.id);
			this.nestingLvl = env.getNestingLevel();
		} catch (MissingDecException e) { // id not declared
			res.add(new SemanticError(e.getMessage()));
		}
		
		return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
