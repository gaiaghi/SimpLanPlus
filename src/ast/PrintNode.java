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

public class PrintNode implements Node {
	
	// grammar rule:
	// print	    : 'print' exp;
	
	private Node exp;

	public PrintNode(Node exp) {
		this.exp = exp;
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "Print:\n" + this.exp.toPrint(indent + "  ");
	}
	
	@Override
	public String toString() {
		return this.toPrint(""); 
	}

	@Override
	public Node typeCheck() throws TypeErrorException {
		Node expType = exp.typeCheck();
		
		if( SimpLanPlusLib.isEquals(expType, new VoidTypeNode() ))
			throw new TypeErrorException("print argument is void type.");
		
		return null;
	}

	@Override
	public String codeGeneration() {
		String code = exp.codeGeneration();
		Node expType = null;
		try {
			expType = exp.typeCheck();
		} catch (TypeErrorException e) {
			return code + "print $a0 1\n";
		}
		
		if( expType instanceof PointerTypeNode )
			expType = ((PointerTypeNode) expType).getPointedType();
		
		if( expType instanceof BoolTypeNode )
			return code + "print $a0 0\n";
		
		if( expType instanceof IntTypeNode )
			return code + "print $a0 1\n";
		
		return code + "print $a0 1\n";
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return exp.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		ArrayList<SemanticError> errors = new ArrayList<>();
		
		/*
		if( exp instanceof DerExpNode )
			((DerExpNode) exp).setInAssign(true);
		if( exp instanceof BaseExpNode )
			((BaseExpNode) exp).setInAssign(true);
		*/
		exp.setInAssign(true);
		
		errors = exp.checkEffects(env);
		
		/*if( exp instanceof DerExpNode )
			System.err.println("PRINT " +((DerExpNode) exp).getLhs().getId().getId() +"  "
				+hashEffect(((DerExpNode) exp).getLhs().getId().getSTEntry().getVarEffectList()));
		else
			System.err.println("PRINT --------------------------------------------------------");
		*/
		
		exp.updateEffectsOfId(env);
		
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

}
