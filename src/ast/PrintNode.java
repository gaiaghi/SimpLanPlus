package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;
import util.SimpLanPlusLib;

public class PrintNode implements Node {
	//	 print	    : 'print' exp;
	
	private Node exp;

	public PrintNode(Node exp) {
		this.exp = exp;
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "Print:\n" + this.exp.toPrint(indent +"  ");
	}
	
	@Override
	public String toString() {
		return this.toPrint(""); 
	}

	@Override
	public Node typeCheck() throws TypeErrorException {
		Node expType = exp.typeCheck();
		
		if( SimpLanPlusLib.isEquals(expType, new VoidTypeNode() ))
			throw new TypeErrorException("print argument is void type.");
		
		return null;
	}

	@Override
	public String codeGeneration() {
				String code = exp.codeGeneration();
		code = code + "print $a0\n";
		return code;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return exp.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return exp.checkEffects(env);
	}
	
	

}
