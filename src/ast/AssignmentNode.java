package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
import util.Effect;
import util.Environment;
import util.STEntry;
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
		
//		if( lhsType instanceof PointerTypeNode && !(expType instanceof PointerTypeNode) ) {
//			int dereferenceNumDec = ((PointerTypeNode) lhsType).getDerNumDec();
//			int dereferenceNumLhs = ((PointerTypeNode) lhsType).getDerNumStm();
//			if( dereferenceNumLhs == dereferenceNumDec ) 
//				lhsType = ((PointerTypeNode) lhsType).getPointedType();
//		}
//		else if( !(lhsType instanceof PointerTypeNode) && expType instanceof PointerTypeNode ) {
//			int dereferenceNumDec = ((PointerTypeNode) expType).getDerNumDec();
//			int dereferenceNumLhs = ((PointerTypeNode) expType).getDerNumStm();
//			if( dereferenceNumLhs == dereferenceNumDec ) 
//				expType = ((PointerTypeNode) expType).getPointedType();
//		}
		
		lhsType =  util.SimpLanPlusLib.getNodeIfPointer(lhsType);		
		expType =  util.SimpLanPlusLib.getNodeIfPointer(expType);
		
//		else 
		if( lhsType instanceof PointerTypeNode && expType instanceof PointerTypeNode ) {
			PointerTypeNode pointerLhs = (PointerTypeNode) lhsType;
			int derNumLhsDec = pointerLhs.getDerNumDec();
			int derNumLhs = pointerLhs.getDerNumStm();
			
			PointerTypeNode pointerExp = (PointerTypeNode) expType;
			int derNumExpDec = pointerExp.getDerNumDec();
			int derNumExp = pointerExp.getDerNumStm();
			
			if( (derNumLhsDec - derNumLhs) != (derNumExpDec - derNumExp) ) 
				throw new TypeErrorException("not valid assignment between pointer "+
						pointerLhs.getErrorMsg() +" and " +pointerExp.getErrorMsg());
			
			lhsType = pointerLhs.getPointedType();
			expType = pointerExp.getPointedType();
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
		ArrayList<SemanticError> res = new ArrayList<>();
		
		res.addAll(lhs.checkEffects(env));
		res.addAll(exp.checkEffects(env));
		
		//STEntry lhsEntry = lhs.getId().getSTEntry();
		
		STEntry lhsEntry = null;
		try {
			lhsEntry = env.lookup(lhs.getId().getId());
		} catch (MissingDecException e1) {
			System.out.println("AssignmentNode: MissingDecException "+lhs.getId().getId());
		}
		
		
		//env seq[lhs = RW]
		Effect newEffect = Effect.seq(lhsEntry.getVarEffect(lhs.getDereferenceNum()), Effect.READ_WRITE);
		lhsEntry.setVarEffect(lhs.getDereferenceNum(), newEffect);
		
		// if variable statuts = ERROR
		if ( lhsEntry.getVarEffect(lhs.getDereferenceNum()).equals(Effect.ERROR)) {
			res.add(new SemanticError("Variable "+lhs.getId().getId()+" cannot be used after deletion."));
		}
		//assegnamento di puntatori: copio gli effetti di exp in lhs
		else if (exp instanceof DerExpNode) {
			int lhsDer = lhs.getDereferenceNum();
			int maxDer = lhs.getId().getSTEntry().getVarEffectList().size();

			DerExpNode derNode = ((DerExpNode) exp);
			int expDerNum = derNode.getLhs().getDereferenceNum();
			
			for (int i = lhsDer, j = expDerNum; i< maxDer; i++, j++) {
				//recupero l'effetto da exp e lo copio in lhs
				Effect expEffect = derNode.getLhs().getId().getSTEntry().getVarEffect(j); 
				lhs.getId().getSTEntry().setVarEffect(i, expEffect);
			}
		}
		
		
		return res;
	}
	
	
}
