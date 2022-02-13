package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class RetNode implements Node {

//	ret	    : 'return' (exp)?;

	private Node exp;
	private boolean inFunction;
	
	public RetNode(Node exp) {
		this.exp = exp;
		this.inFunction = false;
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
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
		
		if(exp != null)
			res.addAll(exp.checkSemantics(env));
		
		if( !inFunction )
			res.add(new SemanticError("there cannot be return in the block. It is not a function."));
		
		return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setInFunction(boolean b){
		inFunction = b;
	}
	

}
