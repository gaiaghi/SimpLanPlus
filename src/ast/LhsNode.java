package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Effect;
import util.Environment;
import util.SemanticError;

public class LhsNode implements Node {

	// grammar rule:
	//	lhs         : ID | lhs '^' ;
	
	private IdNode id;
	private LhsNode lhs;
	private boolean leftSide;
	
	public LhsNode(IdNode id, LhsNode lhs) {
		this.id = id;
		this.lhs = lhs;
		this.leftSide = false;
	}
	
	
	public IdNode getId() {
		return this.id;
	}
	
	
	@Override
	public String toPrint(String indent) {
		String str = indent + "Id: " + this.id.getId();
		
		for(int i=0; i < this.getDereferenceNum(); i++)
			str = str + "^";
		str = str + "\n" +this.id.getSTEntry().toPrint(indent + "  ");
		
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
		
		if( leftSide ) {
			code = code + "mv $al $fp\n";
			
			for(int i = 0; i < id.getNestingLevel() - id.getSTEntry().getNestingLevel(); i ++ ) {
				code = code + "lw $al 0($al)\n";
			}
			
			int offset = id.getSTEntry().getOffset();
			code = code + "addi $a0 $al " + offset + "\n";
		}
		else
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
		
		if( lhs == null ) {
			errors.addAll(id.checkEffects(env));
		}
		else {
			
			//lhs.setLeftSide(leftSide);
			
			errors.addAll(lhs.checkEffects(env));
			
			// serve per evitare tanti errori tutti uguali relativi allo stesso puntatore
			/*if( getDereferenceNum() == id.getDerNumLhs()) {
				
			
				if( leftSide ) {
					if ( ! id.getEffect(getDereferenceNum()).equals(Effect.READ_WRITE) ) {
			            errors.add(new SemanticError("'"+lhs.getId().getId() + "' has not status RW. lhs leftSide"));
					}
				}
				//questo ramo else era commentato
				else {
					if ( id.getEffect(getDereferenceNum()).equals(Effect.INITIALIZED) ) {
			            errors.add(new SemanticError("'"+lhs.getId().getId() + "' has not status RW. lhs rightSide"));
					}
				}
			
			}*/
		}
		
		
		
		return errors;
	}
	
	
	public int getDereferenceNum() { 
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
	
	public void setLeftSide(boolean value) {
		leftSide = value;
	}
	
}
