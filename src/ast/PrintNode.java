package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class PrintNode implements Node {
	//	 print	    : 'print' exp;
	
	private Node exp;

	public PrintNode(Node exp) {
		this.exp = exp;
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "Print: " + this.exp;
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
