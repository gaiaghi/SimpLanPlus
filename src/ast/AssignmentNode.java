package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;
import util.SimpLanPlusLib;

public class AssignmentNode implements Node {

//	assignment  : lhs '=' exp ;
	
	private Node exp;
	private LhsNode lhs;
	
	public AssignmentNode(LhsNode lhs, Node exp) {
		this.exp = exp;
		this.lhs = lhs;
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "Assignment:\n" + this.lhs.toPrint(indent +"  ") +"\n" + this.exp.toPrint(indent +"  ");
	}

	@Override
	public String toString() {
		return this.toPrint("");
	}
	
	@Override
	public Node typeCheck() throws TypeErrorException {
		Node expType = exp.typeCheck();
		Node lhsType = lhs.typeCheck();
		
		
		if( lhsType instanceof PointerTypeNode && !(expType instanceof PointerTypeNode) ) {
			long dereferenceNumDec = lhs.getId().getDereferenceNum();
			long dereferenceNumLhs = lhs.getDereferenceNum();
			if( dereferenceNumLhs == dereferenceNumDec )
				lhsType = lhsType.typeCheck();
		}
		
		if( lhsType instanceof PointerTypeNode && expType instanceof PointerTypeNode ) {
			if( !(exp instanceof NewExpNode) ) {
				long derNumLhsDec = lhs.getId().getDereferenceNum();
				long derNumLhs = lhs.getDereferenceNum();
				
				long derNumExpDec = ((DerExpNode) exp).getLhs().getId().getDereferenceNum();
				long derNumExp = ((DerExpNode) exp).getLhs().getDereferenceNum();
				
				if( (derNumLhsDec - derNumLhs) != (derNumExpDec - derNumExp) )
					throw new TypeErrorException("not valid assignment between pointer "+
							lhs.getId().getId() +" and " +((DerExpNode) exp).getLhs().getId().getId());
			}
			else {
				//exp instanceof NewExpNode
				int countPointer = SimpLanPlusLib.counterPointerNumber(((NewExpNode) exp).getNode());
				long derNumLhsDec2 = lhs.getId().getDereferenceNum();
				if( (derNumLhsDec2 - countPointer) != 1 )
					throw new TypeErrorException("incorrect new expression "+lhs.getId().getId());	
			}
		}
		
		if (! SimpLanPlusLib.isEquals(expType, lhsType))
			throw new TypeErrorException("cannot assign "+expType.toPrint("") +
					" value for variable " + lhs.getId().getId() + " of type " + lhsType.toPrint(""));
		return null;
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws MissingDecException, MultipleDecException {
		ArrayList<SemanticError> res = new ArrayList<>();
		res.addAll(lhs.checkSemantics(env));
		res.addAll(exp.checkSemantics(env));
		
		return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
