package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.TypeErrorException;
import util.Effect;
import util.Environment;
import util.STEntry;
import util.SemanticError;

public class IdNode implements Node {
	
	private String id;
	private STEntry entry;
	private int nestingLvl; //nesting level corrente
	private int derNum;
	private boolean isInDeletionNode;
	
	public IdNode(String id) {
		this.id = id;
		isInDeletionNode = false;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setSTEntry(STEntry entry) {
		this.entry = entry;
	}
	
	public STEntry getSTEntry() {
		return this.entry;
	}
	
	
	public int getNestingLevel() {
		return this.nestingLvl;
	}
	
	
	
	
	@Override
	public String toPrint(String indent) {
		return indent + "Id: " +id +"\n" +entry.toPrint(indent +"  ");
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		Node idType = entry.getType();
		if (idType instanceof ArrowTypeNode) {
			throw new TypeErrorException("wrong usage of function identifier "+ id);
		}
		
		if( idType instanceof PointerTypeNode ) {
			PointerTypeNode pointer = (PointerTypeNode) idType;
			pointer.setDerNum(getDerNumDec(), getDerNumLhs(), id);
			
			if( pointer.getDerNumStm() > pointer.getDerNumDec() )
				throw new TypeErrorException("too many dereference operations at pointer " +id);
		}
			  
		return idType;
	}

	@Override
	public String codeGeneration() {
		String code = "";
		
		//code = code + "lw $al 0($fp)\n";
		code = code + "mv $al $fp\n";
		
		for(int i = 0; i < nestingLvl - entry.getNestingLevel(); i ++ ) {
			code = code + "lw $al 0($al)\n";
		}
		
		int offset = entry.getOffset();
		code = code + "lw $a0 " + offset + "($al)\n";
		
		if( isInDeletionNode ) {
			for(int i = 0; i < getDerNumDec()-1; i ++ ) {
				code = code + "lw $a0 0($a0)\n";
			}
		}
		
		return code;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		ArrayList<SemanticError> res = new ArrayList<>();
		
		try { //declared id
			this.entry = env.lookup(this.id);
			this.nestingLvl = env.getNestingLevel();
		} catch (MissingDecException e) { 
			// id not declared
			res.add(new SemanticError(e.getMessage()));
		}
		
		return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		
		try {
			entry = env.lookup(id);
		} catch (MissingDecException e) {
			errors.add(new SemanticError("IdNode 1: Missing declaration: "+id));
			return errors;
		}
		
	    return errors;
	}
	
	
	public int getDerNumDec() {
		if( entry.getType() instanceof PointerTypeNode )
			return ((PointerTypeNode) entry.getType()).getDereferenceNum();
		else 
			return 0;
	}
	
	
	public int getDerNumLhs() {
		return derNum;
	}
	
	
	public void setIdDerNum(int n) {
		derNum = n;
	}
	
	public int getDereferenceNum () {
		return this.getDerNumDec();
	}
	
	public Effect getEffect(int derNum) {
		return entry.getVarEffect(derNum);
	}
	
	public void setDeletionNode(boolean value) {
		isInDeletionNode = value;
	}

}
