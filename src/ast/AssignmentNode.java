package ast;

import java.util.ArrayList;

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
			int dereferenceNumDec = ((PointerTypeNode) lhsType).getDerNumDec();
			int dereferenceNumLhs = ((PointerTypeNode) lhsType).getDerNumStm();
			if( dereferenceNumLhs == dereferenceNumDec ) 
				lhsType = ((PointerTypeNode) lhsType).getPointedType();
		}
		else if( !(lhsType instanceof PointerTypeNode) && expType instanceof PointerTypeNode ) {
			int dereferenceNumDec = ((PointerTypeNode) expType).getDerNumDec();
			int dereferenceNumLhs = ((PointerTypeNode) expType).getDerNumStm();
			if( dereferenceNumLhs == dereferenceNumDec ) 
				expType = ((PointerTypeNode) expType).getPointedType();
		}
		else if( lhsType instanceof PointerTypeNode && expType instanceof PointerTypeNode ) {
			//if( !(exp instanceof NewExpNode) ) {
				int derNumLhsDec = ((PointerTypeNode) lhsType).getDerNumDec();
				int derNumLhs = ((PointerTypeNode) lhsType).getDerNumStm();
				
				int derNumExpDec = ((PointerTypeNode) expType).getDerNumDec();
				int derNumExp = ((PointerTypeNode) expType).getDerNumStm();
				
				if( (derNumLhsDec - derNumLhs) != (derNumExpDec - derNumExp) )
					throw new TypeErrorException("not valid assignment between pointer "/*+
							lhs.getId().getId() +" and " +((DerExpNode) exp).getLhs().getId().getId()*/);
			//}
			/*else {
				//exp instanceof NewExpNode
				//int countP = SimpLanPlusLib.counterPointerNumber(((NewExpNode) exp).getNode());
				
				Node n = expType;
				while( n instanceof PointerTypeNode ) {
					System.out.print("Pointer --> ");
					n=((PointerTypeNode) n).getType();
				}
				System.out.print(n.getClass()+"\n\n");
				
				int countPointer = ((PointerTypeNode) expType).getDereferenceNum();
				int derNumLhsDec2 = ((PointerTypeNode) lhsType).getDerNumDec();
				
				n = lhsType;
				while( n instanceof PointerTypeNode ) {
					System.out.print("Pointer --> ");
					n=((PointerTypeNode) n).getType();
				}
				System.out.print(n.getClass()+"\n\n");
				
				System.out.println("lhs "+derNumLhsDec2 +"    exp "+countPointer );
				
				if( (derNumLhsDec2 - countPointer) != 1 )
					throw new TypeErrorException("incorrect new expression "+lhs.getId().getId());
			}*/
			
			lhsType = ((PointerTypeNode) lhsType).getPointedType();
			expType = ((PointerTypeNode) expType).getPointedType();
		}
		
		if (! SimpLanPlusLib.isEquals(lhsType, expType))
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
	public ArrayList<SemanticError> checkSemantics(Environment env) {
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
