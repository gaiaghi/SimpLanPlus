package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class BinExpNode implements Node {
	/* grammar rule:
	   exp	    :
	    	| left=exp op=('*' | '/')               right=exp   
			| left=exp op=('+' | '-')               right=exp   
		    | left=exp op=('<' | '<=' | '>' | '>=') right=exp   
		    | left=exp op=('=='| '!=')              right=exp   
		    | left=exp op='&&'                      right=exp   
		    | left=exp op='||'                      right=exp
	*/
	
	private Node leftExp;
	private Node rightExp;
	private String op;
	
	public BinExpNode(Node leftExp, Node rightExp, String op) {
		this.leftExp = leftExp;
		this.rightExp = rightExp;
		this.op = op;
	}

	@Override
	public String toPrint(String indent) {
		return indent +op + "\n" +leftExp.toPrint(indent +"  ") + "\n" +rightExp.toPrint(indent +"  ");
	}

	@Override
	public Node typeCheck() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
		    
		res.addAll(leftExp.checkSemantics(env));
		res.addAll(rightExp.checkSemantics(env));
		  
	 	return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
