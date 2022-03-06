package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class NotExpNode implements Node {
	//grammar rule:
	//exp	    : '!' exp
	
	private Node exp;
	
	public NotExpNode(Node exp) {
		this.exp = exp;
	}

	@Override
	public String toPrint(String indent) {
		return indent +"Not:\n" +exp.toPrint(indent +"  ");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		if( !(exp.typeCheck() instanceof BoolTypeNode) )
			throw new TypeErrorException("the argument of 'Not' is not bool type.");
		
		return new BoolTypeNode();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return exp.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Node getExp() {
		return exp;
	}

}
