package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
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
	public Node typeCheck() throws TypeErrorException{
		return print.typeCheck();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws MissingDecException, MultipleDecException {
		return print.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
