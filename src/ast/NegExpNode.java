package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class NegExpNode implements Node {
	//grammar rule:
	//exp	    : '-' exp
	
	private Node exp;
	
	public NegExpNode(Node exp) {
		this.exp = exp;
	}

	@Override
	public String toPrint(String indent) {
		return indent +"Neg:\n" +exp.toPrint(indent +"  ");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		if( !(exp.typeCheck() instanceof IntTypeNode) )
			throw new TypeErrorException("this expression " +exp +" is not int type.");
		
		return new IntTypeNode();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws MissingDecException, MultipleDecException {
		return exp.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
