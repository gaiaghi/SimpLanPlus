package ast;

import java.util.ArrayList;
import java.util.List;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class BaseExpNode implements Node {
	//grammar rule:
	//exp	    : '(' exp ')'
	
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

        //qui bisogna fare la SEQ tra env e tutte le variabili che 
        //compaiono nell'espressione con effetto RW

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
	

}
