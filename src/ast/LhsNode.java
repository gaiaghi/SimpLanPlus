package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
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
		for(int i=0; i<getDereferenceNum(); i++)
			str = str + "^";
		str = str + "\n" +this.id.getSTEntry().toPrint(indent +"  ");
		
		return str;
	}			
	
	
	public int getDereferenceNum() {
		if (this.lhs != null)
			return this.lhs.getDereferenceNum() + 1;
		return 0;
	}
	
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		if (lhs == null)
			return id.typeCheck();	
		
		int derefNum = getDereferenceNum();
		if (derefNum >1+ id.getDereferenceNum())
			throw new TypeErrorException("too many dereference operations at pointer " + id.getId());
		//da controllare se funziona!
		return ((PointerTypeNode) lhs.typeCheck()).getPointedType();
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

}
