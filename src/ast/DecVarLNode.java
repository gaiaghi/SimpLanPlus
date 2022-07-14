package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class DecVarLNode implements Node{

	//	grammar rule:
	//	declaration:	decVar 			
	
	private Node decVar;
	
	public DecVarLNode(Node decVar) {
		this.decVar = decVar;
	}

	@Override
	public String toPrint(String indent) {
		return this.decVar.toPrint(indent);
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		return decVar.typeCheck();
	}

	@Override
	public String codeGeneration() {
		return decVar.codeGeneration();
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return decVar.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return decVar.checkEffects(env);
	}
	
	

}
