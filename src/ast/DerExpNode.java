package ast;

import java.util.ArrayList;
import java.util.List;

import exception.MissingDecException;
import exception.TypeErrorException;
import util.Environment;
import util.STEntry;
import util.SemanticError;
import util.Effect;

public class DerExpNode implements Node {

	// grammar rule:
	// exp	    : lhs
	
	private LhsNode lhs;
	
	public DerExpNode(LhsNode lhs) {
		this.lhs = lhs;
	}

	@Override
	public String toPrint(String indent) {
		return lhs.toPrint(indent);
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		return lhs.typeCheck();
	}

	@Override
	public String codeGeneration() {
		return lhs.codeGeneration();
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return lhs.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		ArrayList<SemanticError> errors = new ArrayList<>();

		
		try {
			System.out.println("------------ der "+env.lookup("x").getVarEffectList()
					+"\n "+lhs.getId().getEffect(lhs.getDereferenceNum()));
		} catch (MissingDecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        errors.addAll(lhs.checkEffects(env));
        	
        try {
			System.out.println("------------ der "+env.lookup("x").getVarEffectList()
					+"\n "+lhs.getId().getEffect(lhs.getDereferenceNum()));
		} catch (MissingDecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if (lhs.getId().getEffect(lhs.getDereferenceNum()).equals(Effect.INITIALIZED)) {
            errors.add(new SemanticError(lhs.getId().getId() + " not initialized."));
            return errors;
        }
        
        errors.addAll(Environment.checkExpressionEffects(getIDsOfVariables(), env));     

        try {
			System.out.println("------------ der "+env.lookup("x").getVarEffectList()
					+"\n "+lhs.getId().getEffect(lhs.getDereferenceNum()));
		} catch (MissingDecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return errors;
	}
	
	public List<LhsNode> getIDsOfVariables() {
		ArrayList<LhsNode> vars = new ArrayList<>();

        vars.add(lhs);

        return vars;
    }
	
	public LhsNode getLhs() {
		return lhs;
	}

}
