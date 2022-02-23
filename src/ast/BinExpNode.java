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
		
		Node leftType = leftExp.typeCheck();
		Node rightType = rightExp.typeCheck();
		
		if( leftType instanceof PointerTypeNode ) {
			if( leftExp instanceof DerExpNode ) {
				long derNumLeftDec =  ((DerExpNode) leftExp).getLhs().getId().getDereferenceNum();
				long derNumLeft = ((DerExpNode) leftExp).getLhs().getDereferenceNum();
				if( derNumLeftDec == derNumLeft )
					leftType = ((PointerTypeNode) leftType).getPointedType();
			}
			//da cancellare
			else 
				System.out.println("left exp is not lhs "+op +"    "+leftExp.getClass());
			
			//ci potrebbe essere if(leftExp instanceof CallNode )
		}
		
		if( rightType instanceof PointerTypeNode ) {
			if( rightExp instanceof DerExpNode ) {
				long derNumRightDec =  ((DerExpNode) rightExp).getLhs().getId().getDereferenceNum();
				long derNumRight = ((DerExpNode) rightExp).getLhs().getDereferenceNum();
				if( derNumRightDec == derNumRight )
					rightType = ((PointerTypeNode) rightType).getPointedType();
			}
		}
		
		
		switch( op ) {
			case "*":
			case "/":
			case "+":
			case "-":
				if( !(leftType instanceof IntTypeNode && rightType instanceof IntTypeNode) )
					throw new TypeErrorException("the " +op +" operator require 2 int type expressions.");
				return new IntTypeNode();
			
			case "&&":
			case "||":
				if( !(leftType instanceof BoolTypeNode && rightType instanceof BoolTypeNode) )
					throw new TypeErrorException("the " +op +" operator require 2 bool type expressions.");
				return new BoolTypeNode();
				
			case "<":
			case "<=":
			case ">":
			case ">=":
				if( !(leftType instanceof IntTypeNode && rightType instanceof IntTypeNode) )
					throw new TypeErrorException("the " +op +" operator require 2 int type expressions.");
				return new BoolTypeNode();
				
			case "==":
			case "!=":
				if( !SimpLanPlusLib.isEquals(leftType, rightType) )
					throw new TypeErrorException("the " +op +" operator require 2 int type expressions or 2 bool type expressions.");
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
		
		if( leftExp instanceof NewExpNode || rightExp instanceof NewExpNode )
			res.add(new SemanticError("You cannot use new expression in binary operation."));
		  
	 	return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
