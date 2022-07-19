package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class RetNode implements Node {

	// grammar rule:
	// ret	    : 'return' (exp)?;

	private Node exp;
	private boolean inFunction;
	private String funEndLabel;
	
	public RetNode(Node exp) {
		this.exp = exp;
		this.inFunction = false;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Return:\n" 
				+ (this.exp == null ? indent + "  voidType" : this.exp.toPrint(indent + "  "));
	}

	@Override
	public String toString() {
		return this.toPrint("");
	}
	
	@Override
	public Node typeCheck() throws TypeErrorException {
		if( exp == null )
			return new VoidTypeNode();
		else {
			Node expType = exp.typeCheck();	

			expType = util.SimpLanPlusLib.getNodeIfPointer(expType);
			
			return expType;
		}
	}

	@Override
	public String codeGeneration() {
		String code = "";
		if( exp != null )
			code = code + exp.codeGeneration();
		
		code = code + "li $ret 1\n";
		code = code + "b " + funEndLabel + "\n";
		
		return code;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
		
		if(exp != null)
			res.addAll(exp.checkSemantics(env));
		
		if( !inFunction )
			res.add(new SemanticError("there cannot be return in the block. It is not a function."));
		
		return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		
		if( exp != null )
			return exp.checkEffects(env);
		else
			return new ArrayList<SemanticError>();
	}
	
	public void setInFunction(boolean b){
		inFunction = b;
	}
	
	public void setFunEndLabel(String label) {
		funEndLabel = label;
	}
	
	
}
