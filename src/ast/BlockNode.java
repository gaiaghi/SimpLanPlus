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
		
		//lista dei nodi che hanno un return
		ArrayList<Node> types = new ArrayList<>();
		for (Node stm: statements){
			Node stmType = stm.typeCheck();		//controllo su tutti gli statement 
			
			//Nodi RetLNode e nodi BlockLNode e IteLNode che non siano NullType
			if ( stm instanceof RetLNode ||
					 ( (stm instanceof BlockLNode || stm instanceof IteLNode) 
							 && !(stmType instanceof NullTypeNode) )
				)
				types.add(stmType);	
		} 
		
		if (types.size() > 0){
			//controllo: i tipi di ritorno devono essere uguali
			if ( ! types.stream().allMatch(types.get(0)::equals) )
				throw new TypeErrorException("block with multiple return statements having mismatching types.");
			else
				return types.get(0);
		}
		
		//se non ci sono ReturnNode restituisce NullTypeNode
		return new NullTypeNode();
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
