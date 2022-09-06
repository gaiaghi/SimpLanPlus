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
	private boolean inAssign;
	
	public DerExpNode(LhsNode lhs) {
		this.lhs = lhs;
		this.inAssign = false;
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
		
		STEntry lhsEntry = null;
		try {
			lhsEntry = env.lookup(lhs.getId().getId());
		} catch (MissingDecException e1) {}
		
        errors.addAll(lhs.checkEffects(env));
       
        if ( !lhs.isPointer() && lhs.getId().getEffect(lhs.getDereferenceNum()).equals(Effect.INITIALIZED)) {
    		errors.add(new SemanticError("'"+lhs.getId().getId() + "' not initialized. derExp 1"));
            return errors;
        } 
        
        if( inAssign ) {

        	if ( lhs.getId().getEffect(lhs.getDereferenceNum()).equals(Effect.DELETED)) {
       		 	errors.add(new SemanticError("Cannot read '" + lhs.getId().getId() + "' after its deletion. derExp 3"));
                return errors;
            } 
        	
        	if ( lhs.getId().getEffect(lhs.getDereferenceNum()).equals(Effect.INITIALIZED)) {
       		 	errors.add(new SemanticError("'"+lhs.getId().getId() + "' not initialized. derExp 2"));
                return errors;
            } 
        	 
        }
       
        errors.addAll(Environment.checkExpressionEffects(getIDsOfVariables(), env)); 
        
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

	public void setInAssign(boolean value) {
		inAssign = value;
	}
	
	public void updateEffectsOfId(Environment env) {
		try {
			lhs.getId().setSTEntry(new STEntry( env.lookup(lhs.getId().getId()) ));
		} catch (MissingDecException e1) {}
	}
	

}