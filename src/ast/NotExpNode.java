package ast;

import java.util.ArrayList;
import java.util.List;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class NotExpNode implements Node {
	
	// grammar rule:
	// exp	    : '!' exp
	
	private Node exp;
	
	public NotExpNode(Node exp) {
		this.exp = exp;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Not:\n" + exp.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		
		Node typeExp = exp.typeCheck();
		
		typeExp = util.SimpLanPlusLib.getNodeIfPointer(typeExp);
		
		if( !(typeExp instanceof BoolTypeNode) )
			throw new TypeErrorException("the argument of 'Not' is not bool type.");
		
		return new BoolTypeNode();
	}

	@Override
	public String codeGeneration() {
		String code = exp.codeGeneration();
		code = code + "not $a0 $a0\n";
		return code;
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
	
	public Node getExp() {
		return exp;
	}

}
