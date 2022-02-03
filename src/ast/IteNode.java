package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class IteNode implements Node {
	
	//grammar rule
	//ite         : 'if' '(' exp ')' statement ('else' statement)?;
	
	private Node cond;
	private Node thenStm;
	private Node elseStm;
  
	public IteNode(Node cond, Node thenStm, Node elseStm) {
		this.cond = cond;
		this.thenStm = thenStm;
		this.elseStm = elseStm;
	}

	@Override
	public String toPrint(String indent) {
		return indent +"If:\n"
				+cond.toPrint(indent +"  ")
				+thenStm.toPrint(indent +"  ")
				+elseStm.toPrint(indent +"  ");
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

}
