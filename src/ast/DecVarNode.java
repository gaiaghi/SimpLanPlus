package ast;

import java.util.ArrayList;
import java.util.HashMap;

import exception.MultipleDecException;
import exception.TypeErrorException;
import util.Environment;
import util.STEntry;
import util.SemanticError;
import util.SimpLanPlusLib;


//decVar      : type ID ('=' exp)? ';' ;
public class DecVarNode implements Node{
	
	private Node type;
	private IdNode id;
	private Node exp;
	
	
	public DecVarNode(Node type, IdNode id, Node exp) {
		this.id = id;
		this.exp = exp;
		this.type = type; 
	}

	@Override
	public String toPrint(String indent) {
		String expString;
		if (exp != null)
			expString = "\n" +exp.toPrint(indent +"  ");
		else
			expString = "";
		
		return indent +"Var: " +id.getId()
		+"\n" +type.toPrint(indent +"  ") 
		+ expString;
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException {
		
		if (exp == null)
			return null; 
		
		Node expType = exp.typeCheck();
		
		Node decType = type;
		if( decType instanceof PointerTypeNode && expType instanceof PointerTypeNode ) {
			int derNumRightDec = ((PointerTypeNode) decType).getDereferenceNum();
			int derNumExpDec = ((PointerTypeNode) expType).getDerNumDec();
			
			if( derNumRightDec != derNumExpDec )
				 throw new TypeErrorException("expression "+exp+" cannot be assigned "
				 		+ "to variable "+id.getId()+" of type "+type.toPrint(""));
			
			decType = ((PointerTypeNode) decType).getPointedType();
			expType = ((PointerTypeNode) expType).getPointedType();
		}
		else if( expType instanceof PointerTypeNode ) {
			int derNumExpDec =  ((PointerTypeNode) expType).getDerNumDec();
			int derNumExp = ((PointerTypeNode) expType).getDerNumStm();
			if( derNumExpDec == derNumExp )
				expType = ((PointerTypeNode) expType).getPointedType();
		}
		
		if (! SimpLanPlusLib.isEquals(expType, decType))
			 throw new TypeErrorException("expression "
		+exp.toString().substring(exp.toString().indexOf(".")+1, exp.toString().indexOf("@"))+" cannot be assigned "
			 		+ "to variable "+id.getId()+" of type "+type.toPrint(""));
		
		return null; 
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
  		
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
  	  
  		//TODO env.offset = -2;
  		//PROF: STentry entry = new STentry(env.nestingLevel,type, env.offset--);
		//dovo decrementare l'offset dopo aver creato una nuova entry?
  		//controlla offset passato come parametro
        STEntry entry = new STEntry(env.getNestingLevel(), type, env.getOffset()); 
        env.updateOffset(); //decremento offset
        
        try {
        	env.addEntry(id.getId(), entry);
        	id.setSTEntry(entry);
        }catch(MultipleDecException e) {
        	res.add(new SemanticError("Var id "+id.getId() +" already declared"));
        }
        
        if (exp != null)
        	res.addAll(exp.checkSemantics(env));
        
        return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
