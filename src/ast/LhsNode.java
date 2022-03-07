package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.TypeErrorException;
import util.Environment;
import util.STEntry;
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
		
		
		for(int i=0; i < this.getDereferenceNum()-1; i++)
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public int getDereferenceNum() {
        if( lhs != null )
        	return lhs.getDereferenceNum() + 1;
        else
        	return 1;
    }
	
	public void setLhsDerNum(int n) {
		id.setIdDerNum(n);
	}

}
