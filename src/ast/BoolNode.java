package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class BoolNode implements Node {
	
	// grammar rule:
	// exp	    : BOOL
	
	private Boolean val;
	
	public BoolNode(Boolean val) {
		this.val = val;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Bool: " + val;
	}

	@Override
	public Node typeCheck() {
		return new BoolTypeNode();
	}

	@Override
	public String codeGeneration() {
		int boolValue = 0;
		if( val )
			boolValue = 1; 
		
		return "li $a0 " +boolValue +"\n";
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return new ArrayList<>();
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return new ArrayList<SemanticError>();
	}

}
