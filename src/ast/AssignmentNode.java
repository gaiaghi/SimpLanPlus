package ast;

import java.util.ArrayList;
import java.util.List;

import exception.MissingDecException;
import exception.TypeErrorException;
import util.Effect;
import util.Environment;
import util.STEntry;
import util.SemanticError;
import util.SimpLanPlusLib;

public class AssignmentNode implements Node {

	// grammar rule:
	// assignment  : lhs '=' exp ;
	
	private Node exp;
	private LhsNode lhs;
	
	public AssignmentNode(LhsNode lhs, Node exp) {
		this.exp = exp;
		this.lhs = lhs;
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "Assignment:\n" + this.lhs.toPrint(indent + "  ") 
			+ "\n" + this.exp.toPrint(indent + "  ");
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
				throw new TypeErrorException("not valid assignment between pointer '" 
						+ pointerLhs.getErrorMsg() + "' and '" + pointerExp.getErrorMsg() + "'");
			
			lhsType = pointerLhs.getPointedType();
			expType = pointerExp.getPointedType();
		}
		
		
		if (! SimpLanPlusLib.isEquals(lhsType, expType))
			throw new TypeErrorException("cannot assign "+expType.toPrint("") 
				+ " value for variable '" + lhs.getId().getId() + "' of type " + lhsType.toPrint(""));
		
		return null;
	}

	@Override
	public String codeGeneration() {
		String code = "";
		code = code + exp.codeGeneration();
		code = code + "push $a0\n";
		code = code + lhs.codeGeneration(); 
		code = code + "lw $t1 0($sp)\n"; 	//risultato di exp
		code = code + "pop\n";
		code = code + "sw $t1 0($a0)\n"; 
		
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
		
		if( exp instanceof DerExpNode )
			((DerExpNode) exp).setInAssign(true);
		if( exp instanceof BaseExpNode )
			((BaseExpNode) exp).setInAssign(true);
		
		res.addAll(exp.checkEffects(env));
		
		STEntry lhsEntry = null;
		try {
			lhsEntry = env.lookup(lhs.getId().getId());
		} catch (MissingDecException e1) {}
		
		
		
		//env seq[lhs = RW]
		// controllo che la catena del puntatore non sia a INIT
		if( lhs.isPointer() ) {
			for(int i = 0; i < lhs.getDereferenceNum(); i ++)
				if ( ! lhsEntry.getVarEffect(i).equals(Effect.READ_WRITE) ) {
		            res.add(new SemanticError("'"+lhs.getId().getId() + "' has not status RW. " +lhsEntry.getVarEffectList()));
		            return res;
				}
			
			if( exp instanceof NewExpNode ) {
				
//				if( lhsEntry.getVarEffect(lhs.getDereferenceNum()+1).equals(Effect.DELETED) ) {
//					res.add(new SemanticError("Cannot use pointer '" + lhs.getId().getId() + "', it was deleted."));
//		            return res;
//				}
				
				System.err.println("prima " +hashEffect(lhsEntry.getVarEffectList()));
				
				
				for(int i = lhs.getDereferenceNum()/*+1*/; i <= lhs.getId().getDerNumDec(); i ++) {
					lhsEntry.setVarEffect(i, new Effect(Effect.INITIALIZED));
					//lhsEntry.getVarEffect(i).setEffect(new Effect(Effect.INITIALIZED));
				}
				
				System.err.println("dopo " +hashEffect(lhsEntry.getVarEffectList()));
			}
		}
		
		
		// aggiorno effetto variabile left side
		Effect newEffect = Effect.seq(lhsEntry.getVarEffect(lhs.getDereferenceNum()), Effect.READ_WRITE);
		lhsEntry.getVarEffect(lhs.getDereferenceNum()).setEffect(newEffect);
		
		
		
		res.addAll(lhs.checkEffects(env));
		
		
		if (exp instanceof DerExpNode) {
			//assegnamento di puntatori: copio gli effetti di exp in lhs
			int lhsDer = lhs.getDereferenceNum();
			int maxDer = lhs.getId().getSTEntry().getVarEffectList().size();

			DerExpNode derNode = ((DerExpNode) exp);
			int expDerNum = derNode.getLhs().getDereferenceNum();
			
			for (int i = lhsDer, j = expDerNum; i< maxDer; i++, j++) {
				//recupero l'effetto da exp e lo copio in lhs
				Effect expEffect = derNode.getLhs().getId().getSTEntry().getVarEffect(j); 
				lhsEntry.setVarEffect(i, expEffect);
			}
			
			// salvo gli effetti di exp in questo punto
			try {
				derNode.getLhs().getId().setSTEntry(new STEntry( env.lookup(derNode.getLhs().getId().getId()) ));
			} catch (MissingDecException e1) {}
			
		}
		else {
			exp.updateEffectsOfId(env);
		}

		// salvo gli effetti di lhs in questo punto
		try {
			lhs.getId().setSTEntry(new STEntry( env.lookup(lhs.getId().getId()) ));
		} catch (MissingDecException e1) {}
		
		
		return res;
	}
	
	
	private String hashEffect(List<Effect> list) {
		String str="[";
		for(Effect e : list)
			str = str + e + ",";
		str=str+"]        [";
		for(Effect e : list)
			str = str + e.hashCode() + ",";
		return str+"]";
	}
	
	
}
