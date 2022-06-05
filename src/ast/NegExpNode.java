package ast;

import java.util.ArrayList;
import java.util.List;

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
			throw new TypeErrorException("the argument of 'Neg' is not int type.");
		
		return new IntTypeNode();
	}

	@Override
	public String codeGeneration() {
		String code = exp.codeGeneration();
		code = code + "multi $a0 $a0 -1\n"; 
		return  code;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return exp.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		ArrayList<SemanticError> errors = new ArrayList<>();

        errors.addAll(exp.checkEffects(env));

        //errors.addAll(Environment.checkExpressionEffects(getIDsOfVariables(), env));

        return errors;
	}
	
	public List<LhsNode> getIDsOfVariables() {
        return exp.getIDsOfVariables();
    }
	
	public Node getExp() {
		return exp;
	}

}
