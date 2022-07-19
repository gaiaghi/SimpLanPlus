package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class PrintLNode implements Node {

	// grammar rule:
	// statement   : 	print ';'			
	
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
	public Node typeCheck() throws TypeErrorException{
		return print.typeCheck();
	}

	@Override
	public String codeGeneration() {
		return print.codeGeneration();
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return print.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return print.checkEffects(env);
	}
	
	

}
