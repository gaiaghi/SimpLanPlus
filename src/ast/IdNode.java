package ast;

import java.util.ArrayList;

import util.Environment;
import util.STEntry;
import util.SemanticError;

public class IdNode implements Node {
	
	private String id;
	private STEntry entry;
	private int nestingLvl; //da aggiungere durante l'analisi semantica
	
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
