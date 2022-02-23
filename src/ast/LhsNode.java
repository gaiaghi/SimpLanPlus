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
	private long dereferenceNum;
	
	public LhsNode(IdNode id, LhsNode lhs) {
		
		this.id = id;
		this.lhs = lhs;
		this.dereferenceNum = 0;
	}
	
	public LhsNode(IdNode id, LhsNode lhs, long dereferenceNum) {
		
		this.id = id;
		this.lhs = lhs;
		this.dereferenceNum = dereferenceNum;
	}
	
	public IdNode getId() {
		return this.id;
	}
	
	@Override
	public String toPrint(String indent) {
		String str = indent +"Id: " +this.id.getId();
		for(int i=0; i<this.dereferenceNum; i++)
			str = str + "^";
		str = str + "\n" +this.id.getSTEntry().toPrint(indent +"  ");
		
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
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
		
		long dereferenceNumDec = 0;
		Node idType = null;
		try {
			STEntry entry = env.lookup(id.getId());
			dereferenceNumDec = entry.getDereferenceNum();
			idType = entry.getType();
		}catch(MissingDecException e) {
			res.add(new SemanticError(e.getMessage()));
			return res;		
		}
		
		
		
		if (lhs == null) {
			id.setDereferenceNum(dereferenceNumDec);
			return id.checkSemantics(env);
		}
		else {
			if( ! (idType instanceof PointerTypeNode) )
				return id.checkSemantics(env);
			else {
				if( this.dereferenceNum > dereferenceNumDec ) {
					res.add(new SemanticError("too many dereference operations at pointer " +id.getId()));
					return res;
				}
				else
					return lhs.checkSemantics(env);
			}
		}

	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public long getDereferenceNum() {
		return dereferenceNum;
	}

}
