package ast;

import java.util.ArrayList;

import exception.TypeErrorException;

import util.Environment;
import util.SemanticError;
import util.SimpLanPlusLib;

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
	public Node typeCheck() throws TypeErrorException{
		
		Node left = leftExp.typeCheck();
		Node right = rightExp.typeCheck();
		
		switch( op ) {
			case "*":
			case "/":
			case "+":
			case "-":
				if( !(left instanceof IntTypeNode && right instanceof IntTypeNode) )
					throw new TypeErrorException("the " +op +"operator require 2 int type expressions.");
				return new IntTypeNode();
			
			case "&&":
			case "||":
				if( !(left instanceof BoolTypeNode && right instanceof BoolTypeNode) )
					throw new TypeErrorException("the " +op +"operator require 2 bool type expressions.");
				return new BoolTypeNode();
				
			case "<":
			case "<=":
			case ">":
			case ">=":
				if( !(left instanceof IntTypeNode && right instanceof IntTypeNode) )
					throw new TypeErrorException("the " +op +"operator require 2 int type expressions.");
				return new BoolTypeNode();
				
			case "==":
			case "!=":
				if( !SimpLanPlusLib.isSubtype(left, right) )
					throw new TypeErrorException("the " +op +"operator require 2 int type expressions or 2 bool type expressions.");
				return new BoolTypeNode();
		}
		
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
