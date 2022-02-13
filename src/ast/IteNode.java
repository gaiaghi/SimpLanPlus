package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;
import util.SimpLanPlusLib;

public class IteNode implements Node {
	
	//grammar rule
	//ite         : 'if' '(' exp ')' statement ('else' statement)?;
	
	private Node cond;
	private Node thenStm;
	private Node elseStm;
	private boolean inFunction;
	
	private boolean thenRet;
	private boolean elseRet;
  
	public IteNode(Node cond, Node thenStm, Node elseStm) {
		this.cond = cond;
		this.thenStm = thenStm;
		this.elseStm = elseStm;
		this.inFunction = false;
		
		this.thenRet = findReturns(thenStm);
		this.elseRet = findReturns(elseStm);
	}

	@Override
	public String toPrint(String indent) {
		String str = indent +"If:\n"
				+cond.toPrint(indent +"  ")
				+"\n" +indent +"Then:\n" +thenStm.toPrint(indent +"  ");
		
		if( elseStm != null )
				str = str +"\n" +indent +"Else:\n" +elseStm.toPrint(indent +"  ");
		
		return str;
	}

	@Override
	public Node typeCheck() throws TypeErrorException {
		Node condType = cond.typeCheck();
		if ( !(SimpLanPlusLib.isSubtype(condType, new BoolTypeNode() )) ) 
			throw new TypeErrorException("if condition is not bool type.");
		
	    Node thenType = thenStm.typeCheck();
	    Node elseType = null;
	    if( elseStm != null )
	    	elseType = elseStm.typeCheck();
	    else
	    	return thenType;
	    
	    if ( !(SimpLanPlusLib.isSubtype(thenType, elseType)) ) 
			throw new TypeErrorException("incompatible types in then else branches.");
	    
	    return thenType;
		
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		ArrayList<SemanticError> res = new ArrayList<>();
		res.addAll(cond.checkSemantics(env));
		res.addAll(thenStm.checkSemantics(env));
		if (elseStm != null)
			res.addAll(elseStm.checkSemantics(env));
		
		return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setInFunction(boolean b){
		inFunction = b;
		
		if( thenStm instanceof BlockLNode )
			((BlockLNode) thenStm).setInFunction(b);
		
		else if( thenStm instanceof IteLNode )
			((IteLNode) thenStm).setInFunction(b);
		
		else if( thenStm instanceof RetLNode )
			((RetLNode) thenStm).setInFunction(b);
		
		if( elseStm != null ) {
			if( elseStm instanceof BlockLNode )
				((BlockLNode) elseStm).setInFunction(b);
			
			else if( elseStm instanceof IteLNode )
				((IteLNode) elseStm).setInFunction(b);
			
			else if( elseStm instanceof RetLNode )
				((RetLNode) elseStm).setInFunction(b);
		}
	}
	
	
	public boolean isIfThenElse() {
		if( elseStm != null )
			return true;
		return false;
	}
	
	
	private boolean findReturns(Node node) {
		if( node != null ) {
			if( node instanceof RetLNode ) {
				return true;
			}
			else if( node instanceof BlockLNode )
				return ((BlockLNode) node).getReturns();
			
			else if( node instanceof IteLNode )
				return ((IteLNode) node).getReturns();
		}
		
		return false;
	}
	
	
	public boolean getReturns() {
		return thenRet && elseRet;
	}
	

}
