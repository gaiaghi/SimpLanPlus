package ast;

import java.util.ArrayList;
import java.util.List;

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
		
		leftType = util.SimpLanPlusLib.getNodeIfPointer(leftType);
		rightType = util.SimpLanPlusLib.getNodeIfPointer(rightType);
		
		
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
		String code = "";
		
		code = code + leftExp.codeGeneration();
		code = code + "push $a0\n";
		code = code + rightExp.codeGeneration();			// $a0 = rightExp
		code = code + "lw $t1 0($sp)\n";  // $t1 <- top		$t1 = leftExp
		
		String trueLabel_1;
		String endIfLabel_1;
		String trueLabel_2;
		String endIfLabel_2;
		
		switch( op ) {
			case "*":
				code = code + "mult $a0 $t1 $a0\n";
				break;
				
			case "/":
				code = code + "div $a0 $t1 $a0\n";
				break;
				
			case "+":
				code = code + "add $a0 $t1 $a0\n";
				break;
				
			case "-":
				code = code + "sub $a0 $t1 $a0\n";
				break;
				
			case "&&":
				code = code + "and $a0 $t1 $a0\n";
				break;
				
			case "||":
				code = code + "or $a0 $t1 $a0\n";
				break;
				
			case "<":
				/* if_1 ( $t1 <= $a0 )
				 * 		if_2 ( $t1 == $a0 )
				 * 			$a0 = 0
				 * 		else_2
				 * 			$a0 = 1
				 * else_1 
				 * 		$a0 = 0
				 * */
				trueLabel_1 = SimpLanPlusLib.freshLabel();
				endIfLabel_1 = SimpLanPlusLib.freshLabel();
				trueLabel_2 = SimpLanPlusLib.freshLabel();
				endIfLabel_2 = SimpLanPlusLib.freshLabel();
				
				// verifico la condizione leftExp <= rightExp
				code = code + "bleq $t1 $a0 " + trueLabel_1 + "\n";
				
				// se vale leftExp > rightExp
				code = code + "li $a0 0\n";
				code = code + "b " + endIfLabel_1 + "\n";
				
				// se vale leftExp <= rightExp
				code = code + trueLabel_1 + ":\n";
				
				// verifico la condizione leftExp == rightExp
				code = code + "beq $t1 $a0 " + trueLabel_2 + "\n";
				
				// se vale leftExp <= rightExp
				code = code + "li $a0 1\n";
				code = code + "b " + endIfLabel_2 + "\n";
				
				// se vale leftExp == rightExp
				code = code + trueLabel_2 + ":\n";
				code = code + "li $a0 0\n";
				code = code + endIfLabel_2 + ":\n";
				
				
				// fine if 
				code = code + endIfLabel_1 + ":\n";
				
				break;
				
			case "<=":
				/* if_1 ( $t1 <= $a0 )
				 * 		$a0 = 1
				 * else_1 
				 * 		$a0 = 0
				 * */
				trueLabel_1 = SimpLanPlusLib.freshLabel();
				endIfLabel_1 = SimpLanPlusLib.freshLabel();
				
				// verifico la condizione leftExp <= rightExp
				code = code + "bleq $t1 $a0 " + trueLabel_1 + "\n";
				
				// se vale leftExp > rightExp
				code = code + "li $a0 0\n";
				code = code + "b " + endIfLabel_1 + "\n";
				
				// se vale leftExp <= rightExp
				code = code + trueLabel_1 + ":\n";
				code = code + "li $a0 1\n";
				
				// fine if 
				code = code + endIfLabel_1 + ":\n";
				
				break;
				
			case ">":
				/* if_1 ( $t1 <= $a0 )
				 * 		$a0 = 0
				 * else_1 
				 * 		$a0 = 1
				 * */
				trueLabel_1 = SimpLanPlusLib.freshLabel();
				endIfLabel_1 = SimpLanPlusLib.freshLabel();
				
				// verifico la condizione leftExp <= rightExp
				code = code + "bleq $t1 $a0 " + trueLabel_1 + "\n";
				
				// se vale leftExp > rightExp
				code = code + "li $a0 0\n";
				code = code + "b " + endIfLabel_1 + "\n";
				
				// se vale leftExp <= rightExp
				code = code + trueLabel_1 + ":\n";
				code = code + "li $a0 1\n";
				
				// fine if 
				code = code + endIfLabel_1 + ":\n";
				
				break;
				
			case ">=":
				/* if_1 ( $t1 <= $a0 )
				 * 		if_2 ( $t1 == $a0 )
				 * 			$a0 = 1
				 * 		else_2
				 * 			$a0 = 0
				 * else_1 
				 * 		$a0 = 1
				 * */
				trueLabel_1 = SimpLanPlusLib.freshLabel();
				endIfLabel_1 = SimpLanPlusLib.freshLabel();
				trueLabel_2 = SimpLanPlusLib.freshLabel();
				endIfLabel_2 = SimpLanPlusLib.freshLabel();
				
				// verifico la condizione leftExp <= rightExp
				code = code + "bleq $t1 $a0 " + trueLabel_1 + "\n";
				
				// se vale leftExp > rightExp
				code = code + "li $a0 1\n";
				code = code + "b " + endIfLabel_1 + "\n";
				
				// se vale leftExp <= rightExp
				code = code + trueLabel_1 + ":\n";
				
				// verifico la condizione leftExp == rightExp
				code = code + "beq $t1 $a0 " + trueLabel_2 + "\n";
				
				// se vale leftExp <= rightExp
				code = code + "li $a0 0\n";
				code = code + "b " + endIfLabel_2 + "\n";
				
				// se vale leftExp == rightExp
				code = code + trueLabel_2 + ":\n";
				code = code + "li $a0 1\n";
				code = code + endIfLabel_2 + ":\n";
				
				
				// fine if 
				code = code + endIfLabel_1 + ":\n";
				
				break;
				
			case "==":
				/* if_1 ( $t1 == $a0 )
				 * 		$a0 = 1
				 * else_1 
				 * 		$a0 = 0
				 * */
				trueLabel_1 = SimpLanPlusLib.freshLabel();
				endIfLabel_1 = SimpLanPlusLib.freshLabel();
				
				// verifico la condizione leftExp == rightExp
				code = code + "beq $t1 $a0 " + trueLabel_1 + "\n";
				
				// se vale leftExp != rightExp
				code = code + "li $a0 0\n";
				code = code + "b " + endIfLabel_1 + "\n";
				
				// se vale leftExp == rightExp
				code = code + trueLabel_1 + ":\n";
				code = code + "li $a0 1\n";
				
				// fine if 
				code = code + endIfLabel_1 + ":\n";
				
				break;
				
			case "!=":
				/* if_1 ( $t1 == $a0 ) 
				 * 		$a0 = 0
				 * else_1 
				 * 		$a0 = 1
				 * */
				trueLabel_1 = SimpLanPlusLib.freshLabel();
				endIfLabel_1 = SimpLanPlusLib.freshLabel();
				
				// verifico la condizione leftExp != rightExp
				code = code + "beq $t1 $a0 " + trueLabel_1 + "\n";
				
				// se vale leftExp != rightExp
				code = code + "li $a0 1\n";
				code = code + "b " + endIfLabel_1 + "\n";
				
				// se vale leftExp == rightExp
				code = code + trueLabel_1 + ":\n";
				code = code + "li $a0 0\n";
				
				// fine if 
				code = code + endIfLabel_1 + ":\n";
				
				break;
		
		}
		code = code + "pop\n";
		
		return code;
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
		ArrayList<SemanticError> errors = new ArrayList<>();
		
        errors.addAll(leftExp.checkEffects(env));
        errors.addAll(rightExp.checkEffects(env));

        //errors.addAll(Environment.checkExpressionEffects(getIDsOfVariables(), env));

        return errors;
	}
	
	public List<LhsNode> getIDsOfVariables() {
		ArrayList<LhsNode> vars = new ArrayList<>();
		
		vars.addAll(leftExp.getIDsOfVariables());
		vars.addAll(rightExp.getIDsOfVariables());
		
		return vars;
    }

}
