package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;
import util.SimpLanPlusLib;

public class AssignmentNode implements Node {

//	assignment  : lhs '=' exp ;
	
	private Node exp;
	private LhsNode lhs;
	
	public AssignmentNode(LhsNode lhs, Node exp) {
		this.exp = exp;
		this.lhs = lhs;
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "Assignment:\n" + this.lhs.toPrint(indent +"  ") +"\n" + this.exp.toPrint(indent +"  ");
	}

	@Override
	public String toString() {
		return this.toPrint("");
	}
	
	@Override
	public Node typeCheck() throws TypeErrorException {
		Node expType = exp.typeCheck();
		Node lhsType = lhs.typeCheck();
		if (! SimpLanPlusLib.isSubtype(expType, lhsType))
			throw new TypeErrorException("cannot assign "+expType.toPrint("") +
					" value for variable " + lhs.getId().getId() + " of type " + lhsType.toPrint(""));
		return null;
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		ArrayList<SemanticError> res = new ArrayList<>();
		res.addAll(lhs.checkSemantics(env));
		res.addAll(exp.checkSemantics(env));
		
		return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
