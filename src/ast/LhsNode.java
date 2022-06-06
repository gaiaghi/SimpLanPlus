package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.TypeErrorException;
import util.Effect;
import util.Environment;
import util.SemanticError;

public class LhsNode implements Node {
//	lhs         : ID | lhs '^' ;
	
	private IdNode id;
	private LhsNode lhs;
	
	public LhsNode(IdNode id, LhsNode lhs) {
		this.id = id;
		this.lhs = lhs;
	}
	
	
	public IdNode getId() {
		return this.id;
	}
	
	
	@Override
	public String toPrint(String indent) {
		String str = indent +"Id: " +this.id.getId();
		
		
		for(int i=0; i < this.getDereferenceNum(); i++)
			str = str + "^";
		str = str + "\n" +this.id.getSTEntry().toPrint(indent +"  ");
		
		/*
		Node type = id.getSTEntry().getType();
		if( type instanceof PointerTypeNode ) {
			PointerTypeNode pointer = (PointerTypeNode) type;
			
			System.err.println("LHS print " +id.getId() +"   "+pointer.getDerNumStm());
			
			for(int i=0; i < pointer.getDerNumStm(); i++)
				str = str + "^";
			str = str + "\n" +this.id.getSTEntry().toPrint(indent +"  ");
		}
		*/
		
		return str;
	}			
	
	
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	
	@Override
	public Node typeCheck() throws TypeErrorException{
		if (lhs == null)
			return id.typeCheck();	
		
		return lhs.typeCheck();
	}

	
	@Override
	public String codeGeneration() {
		String code = "";
		
		code = id.codeGeneration();
		
		LhsNode currentNode = lhs;
		while( currentNode != null ) {
			code = code + "lw $a0 0($a0)\n";
			currentNode = currentNode.lhs;
		}
		
		return code;
	}

	
	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		if (lhs == null) 
			return id.checkSemantics(env);
		else 
			return lhs.checkSemantics(env);
	}

	
	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		
		
		if( lhs == null )
			errors.addAll(id.checkEffects(env));
		else {
			errors.addAll(lhs.checkEffects(env));
			
			if ( ! id.getEffect(getDereferenceNum()).equals(Effect.READ_WRITE) ) {
	            errors.add(new SemanticError(lhs.getId().getId() + " has not status RW.  LhsNode"));
			}
		}
		
		return errors;
	}
	
	
	public int getDereferenceNum() { //num di utilizzo
        if( lhs != null )
        	return lhs.getDereferenceNum() + 1;
        else
        	return 0;
    }
	
	public void setLhsDerNum(int n) {
		id.setIdDerNum(n);
	}
	
	public boolean isPointer() {
		return id.getSTEntry().getType() instanceof PointerTypeNode;
	}

}
