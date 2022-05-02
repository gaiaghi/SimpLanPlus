package ast;

import java.util.ArrayList;
import java.util.List;

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
		ArrayList<SemanticError> errors = new ArrayList<>();

        errors.addAll(exp.checkEffects(env));

        errors.addAll(Environment.checkExpressionEffects(getIDsOfVariables()));

        return errors;
	}
	
	public List<LhsNode> getIDsOfVariables() {
        return exp.getIDsOfVariables();
    }
	
	public Node getExp() {
		return exp;
	}

}
