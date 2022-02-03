package ast;

import java.util.ArrayList;
import java.util.HashMap;

import exception.MultipleDecException;
import util.Environment;
import util.STEntry;
import util.SemanticError;


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
		return indent +"Var: " +id.getId() 
		+"\n" +type.toPrint(indent +"  ") 
		+"\n" +exp.toPrint(indent +"  ");
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() {
		// TODO Auto-generated method stub
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
  		// controlla offset passato come parametro
        STEntry entry = new STEntry(env.getNestingLevel(), type, env.getOffset()); 
        
        try {
        	env.addEntry(id.getId(), entry);
        }catch(MultipleDecException e) {
        	res.add(new SemanticError("Var id "+id+" already declared"));
        }
        
        res.addAll(exp.checkSemantics(env));
        
        return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
