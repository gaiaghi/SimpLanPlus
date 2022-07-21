package ast;

import java.util.ArrayList;
import java.util.List;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class BaseExpNode implements Node {
	
	// grammar rule:
	// exp	    : '(' exp ')'
	
	private Node exp;
	
	public BaseExpNode(Node exp) {
		this.exp = exp;
	}
	
	@Override
	public String toPrint(String indent) {
		return exp.toPrint(indent);
	}

	@Override
	public Node typeCheck() throws TypeErrorException {
		return exp.typeCheck();
	}

	@Override
	public String codeGeneration() {
		return exp.codeGeneration();
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return exp.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		ArrayList<SemanticError> errors = new ArrayList<>();

        errors.addAll(exp.checkEffects(env));

        return errors;
	}
	
	public List<LhsNode> getIDsOfVariables() {
        return exp.getIDsOfVariables();
    }

	
	public Node getExpNode() {
		if( exp instanceof BaseExpNode )
			return ((BaseExpNode) exp).getExpNode();
		else
			return exp;
	}
	
	public Node getExp() {
		return exp;
	}
	
	public void setInAssign(boolean value) {
		if( exp instanceof DerExpNode )
			((DerExpNode) exp).setInAssign(true);
	}
	

}
