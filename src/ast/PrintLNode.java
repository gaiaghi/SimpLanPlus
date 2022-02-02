package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class PrintLNode implements Node {
//	statement   : 	print ';'				#printL
	
	private Node print;
	
	public PrintLNode(Node print) {
		this.print = print;
	}

	
	@Override
	public String toPrint(String indent) {
		return print.toPrint(indent);
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
