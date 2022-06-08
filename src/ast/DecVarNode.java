package ast;

import java.util.ArrayList;
import java.util.HashMap;

import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
import util.Effect;
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

				throw new TypeErrorException("not valid assignment between pointer "+
						((PointerTypeNode)decType).getErrorMsg() +" and " +((PointerTypeNode)expType).getErrorMsg());
			
			
			decType = ((PointerTypeNode) decType).getPointedType();
			expType = ((PointerTypeNode) expType).getPointedType();
		}

		else 
			expType = util.SimpLanPlusLib.getNodeIfPointer(expType);


		
		if (! SimpLanPlusLib.isEquals(expType, decType))
			 throw new TypeErrorException("wrong usage of expression "
		+exp.toString().substring(exp.toString().indexOf(".")+1, exp.toString().indexOf("@"))+" with variable "+id.getId());
		
		return null; 
	}

	@Override
	public String codeGeneration() {
		String code = "";
		
		if( exp != null )
			code = code + exp.codeGeneration() + "push $a0\n";
		else
			code = code + "addi $sp $sp -1\n";
		
		return code;
	}

	
	
	
	
	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
  		
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
  	  
        STEntry entry = new STEntry(env.getNestingLevel(), type, env.getAndUpdateOffset()); 
        
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
		ArrayList<SemanticError> res = new ArrayList<>();
		
		STEntry entry = id.getSTEntry();
		
		if (exp != null) {
			res.addAll(exp.checkEffects(env));
			entry.setVarEffect(0, new Effect(Effect.READ_WRITE));
		}
		
		try {
			env.addEntry(id.getId(), entry);
		} catch (MultipleDecException e) {
			System.out.println("catch 4");
		}
		
		return res;
	}
	

}
