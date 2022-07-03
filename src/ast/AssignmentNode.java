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
		
		lhsType =  util.SimpLanPlusLib.getNodeIfPointer(lhsType);		
		expType =  util.SimpLanPlusLib.getNodeIfPointer(expType);

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
		String code = "";
		code = code + exp.codeGeneration();
		code = code + "push $a0\n";
		//lhs.setLeftSide(true);
		code = code + lhs.codeGeneration(); 
		code = code + "lw $t1 0($sp)\n"; //risultato di exp
		code = code +"pop\n";
		code = code +"sw $t1 0($a0)\n"; //lasciamo il calcolo di al al LhsNode?
		
		return code;
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
		
		lhs.setLeftSide(true);
		
		res.addAll(exp.checkEffects(env));
		
		STEntry lhsEntry = null;
		try {
			lhsEntry = env.lookup(lhs.getId().getId());
		} catch (MissingDecException e1) {
			res.add(new SemanticError("AssignmentNode: MissingDecException "+lhs.getId().getId()));
			return res;
		}
		
		//env seq[lhs = RW]
		if( lhs.isPointer() 
				&& lhs.getId().getDerNumLhs() == lhs.getId().getDerNumDec() 
				&& lhsEntry.getVarEffect(lhs.getDereferenceNum()).equals(Effect.DELETED)) {
			lhsEntry.setVarEffect(lhs.getDereferenceNum(), Effect.READ_WRITE);
		}
		
		// controllo che la catena del puntatore non sia a INIT
		if(lhs.isPointer()) {
			for(int i = 0; i < lhs.getDereferenceNum(); i ++)
				if ( ! lhsEntry.getVarEffect(i).equals(Effect.READ_WRITE) ) {
		            res.add(new SemanticError(lhs.getId().getId() + " has not status RW. AssignmentNode2"));
		            return res;
				}
		}
		
		// aggiorno effetto variabile left side
		Effect newEffect = Effect.seq(lhsEntry.getVarEffect(lhs.getDereferenceNum()), Effect.READ_WRITE);
		lhsEntry.setVarEffect(lhs.getDereferenceNum(), newEffect);
		
		res.addAll(lhs.checkEffects(env));
		
		
		//assegnamento di puntatori: copio gli effetti di exp in lhs
		if (exp instanceof DerExpNode) {
			
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
