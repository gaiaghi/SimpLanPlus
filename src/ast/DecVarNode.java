package ast;

import java.util.ArrayList;
import java.util.HashMap;

import exception.MissingDecException;
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
			return null; //valore di ritorno non utilizzato
		
		Node expType = exp.typeCheck();
		
		//controllo puntatori
		if( type instanceof PointerTypeNode && expType instanceof PointerTypeNode ) {
			int derNumDec = SimpLanPlusLib.counterPointerNumber(type);
			if( !(exp instanceof NewExpNode) ) {
				long derNumExpDec = ((DerExpNode) exp).getLhs().getId().getDereferenceNum();
				long derNumExp = ((DerExpNode) exp).getLhs().getDereferenceNum();
				
				if( derNumDec != (derNumExpDec - derNumExp) )
					throw new TypeErrorException("not valid initialization of pointer "+id.getId());
			}
			else {
				//exp instanceof NewExpNode
				int countPointer = SimpLanPlusLib.counterPointerNumber(((NewExpNode) exp).getNode());
				
				if( (derNumDec - countPointer) != 1 )
					throw new TypeErrorException("incorrect new expression "+id.getId());	
			}
		}
		
		
		if (! SimpLanPlusLib.isEquals(expType, type))
			 throw new TypeErrorException("expression "+exp+" cannot be assigned to variable "+id.getId()+" of type "+type.toPrint(""));
		
		
		
		return null; //valore di ritorno non utilizzato
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws MissingDecException, MultipleDecException {
  		
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
  	  
  		//TODO env.offset = -2;
  		//PROF: STentry entry = new STentry(env.nestingLevel,type, env.offset--);
		//dovo decrementare l'offset dopo aver creato una nuova entry?
  		//controlla offset passato come parametro
        STEntry entry = new STEntry(env.getNestingLevel(), type, env.getOffset(), id.getDereferenceNum()); 
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
