package ast;

import java.util.ArrayList;
import java.util.List;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class CallExpNode implements Node {
	
	//gramma rule:
	//exp	    : call
	
	private CallNode call;
	
	public CallExpNode(CallNode call) {
		this.call = call;
	}

	@Override
	public String toPrint(String indent) {
		return call.toPrint(indent);
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		return call.typeCheck();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return call.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		ArrayList<SemanticError> errors = new ArrayList<>();

        errors.addAll(call.checkEffects(env));

        errors.addAll(Environment.checkExpressionEffects(getIDsOfVariables()));

        return errors;
	}
	
	public List<LhsNode> getIDsOfVariables() {
        return call.getIDsOfVariables();
    }

}
