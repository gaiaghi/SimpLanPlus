package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class BlockNode implements Node {
	//grammar rule:
	//block	    : '{' declaration* statement* '}';

	private ArrayList<Node> declarations;
	private ArrayList<Node> statements;
	
	public BlockNode(ArrayList<Node> declarations, ArrayList<Node> statements) {
		this.declarations = declarations;
		this.statements = statements;
	}

	@Override
	public String toPrint(String indent) {
		String str = "Block:";
		int i;
		for(i=0; i<declarations.size(); i++)
			str = str +"\n" + declarations.get(i).toPrint(indent +"  ");
		
		for(i=0; i<statements.size(); i++)
			str = str +"\n" + statements.get(i).toPrint(indent +"  ");
		
		return indent + str;
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		for (Node dec: declarations)
			dec.typeCheck();
		
		//DA FINIRE
		ArrayList<Node> types = new ArrayList<>();
		for (Node stm: statements){
			
		}
		
		return new VoidTypeNode();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
		
		env.addScope();
		
		//check semantics in declaration
		if(declarations.size() > 0){
			//TODO env.offset = -2;
		for(Node n : declarations)
			res.addAll(n.checkSemantics(env));
		}
  
		//check semantics in statement
		if(statements.size() > 0){
			for(Node n : statements)
				res.addAll(n.checkSemantics(env));
		}     
  
		env.removeScope();
  
		return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ArrayList<Node> getDeclarations(){
		return declarations;
	}
	
	public int getDeclarationsSize(){
		return declarations.size();
	}
	
	public ArrayList<Node> getStatements(){
		return statements;
	}
	
	public int getStatementsSize(){
		return statements.size();
	}
	
}
