package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class PointerTypeNode implements Node {

	//grammar rule:
	//type	    : '^' type
	
	private Node type;
	
	private int derNumDec;
	private int derNumStm;
	
	public PointerTypeNode(Node type) {
		this.type = type; 
		derNumDec = -1;
		derNumStm = -1;
	}

	@Override
	public String toPrint(String indent) {
		return indent +"PointerType";
	}

	@Override
	public Node typeCheck() {
		return this;
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return new ArrayList<SemanticError>();
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Node getPointedType () {
		if( type instanceof PointerTypeNode )
			return ((PointerTypeNode) type).getPointedType();
		else
			return type;
	}
	
	
	public int getDereferenceNum() {
        if( type instanceof PointerTypeNode )
        	return ((PointerTypeNode) type).getDereferenceNum() + 1;
        else
        	return 1;
    }
	
	
	public void setDerNum(int dec, int stm) {
		derNumDec = dec;
		derNumStm = stm;
	}
	
	public int getDerNumDec() {
		return derNumDec;
	}
	
	public int getDerNumStm() {
		return derNumStm;
	}
	
	
	public Node getType() {
		return type;
	}
	

}
