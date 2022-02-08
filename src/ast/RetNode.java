package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class RetNode implements Node {

//	ret	    : 'return' (exp)?;

	private Node exp;
	
	public RetNode(Node exp) {
		this.exp = exp;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Return:\n" 
				+(this.exp == null ? indent +"  voidType" : this.exp.toPrint(indent +"  "));
	}

	@Override
	public String toString() {
		return this.toPrint("");
	}
	
	@Override
	public Node typeCheck() throws TypeErrorException {
		if( exp == null )
			return new VoidTypeNode();
		else 
			return exp.typeCheck();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		if(exp != null)
			return exp.checkSemantics(env);
		
		return new ArrayList<>();
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
