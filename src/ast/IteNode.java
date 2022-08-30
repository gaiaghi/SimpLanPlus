package ast;

import java.util.ArrayList;
import java.util.List;

import exception.MissingDecException;
import exception.TypeErrorException;
import util.Effect;
import util.Environment;
import util.STEntry;
import util.SemanticError;
import util.SimpLanPlusLib;

public class IteNode implements Node {
	
	//grammar rule:
	//ite         : 'if' '(' exp ')' statement ('else' statement)?;
	
	private Node cond;
	private Node thenStm;
	private Node elseStm;
	private boolean inFunction;
	
	private boolean thenRet;
	private boolean elseRet;
	
	private String funEndLabel;
  
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
		String str = indent + "If:\n"
				+ cond.toPrint(indent + "  ")
				+ "\n" + indent + "Then:\n" + thenStm.toPrint(indent + "  ");
		
		if( elseStm != null )
				str = str + "\n" + indent + "Else:\n" + elseStm.toPrint(indent + "  ");
		
		return str;
	}

	@Override
	public Node typeCheck() throws TypeErrorException {
		Node condType = cond.typeCheck();
		if ( !(SimpLanPlusLib.isEquals(condType, new BoolTypeNode() )) ) 
			throw new TypeErrorException("if condition is not bool type.");
		
	    Node thenType = thenStm.typeCheck();
	    Node elseType = null;
	    if( elseStm != null )
	    	elseType = elseStm.typeCheck();
	    else
	    	return thenType;
	    
	    if ( !(SimpLanPlusLib.isEquals(thenType, elseType)) ) 
			throw new TypeErrorException("incompatible types in then else branches.");
	    
	    return returnType(thenType, elseType);
		
	}

	@Override
	public String codeGeneration() {
		String code = "";
		
		String endLabel = SimpLanPlusLib.freshLabel();
		String thenLabel = SimpLanPlusLib.freshLabel();
		
		code = code + cond.codeGeneration(); 				// $a0 = risultato della condizione
		code = code + "li $t1 1\n"; 						// true per il confronto
		code = code + "beq $a0 $t1 " + thenLabel +"\n";
		
		if (elseStm != null)
			code = code + elseStm.codeGeneration();
		
		code = code + "b " + endLabel +"\n";
		code = code + thenLabel + ":\n";
		code = code + thenStm.codeGeneration();
		code = code + endLabel + ":\n";
		
		return code;
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
		ArrayList<SemanticError> errors = new ArrayList<>();
		
		cond.setInAssign(true);
		
        errors.addAll(cond.checkEffects(env));
        
        cond.updateEffectsOfId(env);
        
        if (elseStm != null) {
        	Environment thenEnv = new Environment(env);
        	Environment elseEnv = new Environment(env);
        	
        	/* calcolo l'ambiente relativo al branch then */
        	errors.addAll(thenStm.checkEffects(thenEnv));
        	
        	/* calcolo l'ambiente relativo al branch else*/
        	errors.addAll(elseStm.checkEffects(elseEnv));
        	        	
        	Environment maxEnv = Environment.maxEnv(thenEnv, elseEnv);
        
        	env = Environment.cloneMaxEnv(maxEnv, env);
        }
        else {
        	errors.addAll(thenStm.checkEffects(env));
        }

		return errors;
	}
	
	
	private String hashEffect(List<Effect> list) {
		String str="[";
		for(Effect e : list)
			str = str + e + ",";
		str=str+"]        [";
		for(Effect e : list)
			str = str + e.hashCode() + ",";
		return str+"]";
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
	
	
	private Node returnType(Node thenType, Node elseType) {
		
		if( thenType == null && elseType instanceof VoidTypeNode ) {
			return elseType;
		}
		else if( thenType instanceof VoidTypeNode && elseType == null ) {
			return thenType;
		}
		else 
			return thenType;
		
	}
	
	public void setFunEndLabel(String label) {
		funEndLabel = label;
		
		if( thenStm instanceof RetLNode )
			((RetLNode) thenStm).setFunEndLabel(label);
		else if ( thenStm instanceof BlockLNode ) 
			((BlockLNode) thenStm).setFunEndLabel(label);

		if( elseStm != null ) {
			if( elseStm instanceof RetLNode )
				((RetLNode) elseStm).setFunEndLabel(label);
			else if ( elseStm instanceof BlockLNode ) 
				((BlockLNode) elseStm).setFunEndLabel(label);
		}
	}
	

}
