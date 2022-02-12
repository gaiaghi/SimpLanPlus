package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;
import util.SimpLanPlusLib;

public class BlockNode implements Node {
	//grammar rule:
	//block	    : '{' declaration* statement* '}';

	private ArrayList<Node> declarations;
	private ArrayList<Node> statements;
	private boolean inFunction;
	
	public BlockNode(ArrayList<Node> declarations, ArrayList<Node> statements) {
		this.declarations = declarations;
		this.statements = statements;
		this.inFunction = false;
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
			/*if ( stm instanceof RetLNode ||
				//( (stm instanceof BlockLNode || stm instanceof IteLNode) && !(stmType instanceof NullTypeNode)
				( (stm instanceof BlockLNode || stm instanceof IteLNode) && (stmType != null))
				)
				types.add(stmType);*/
			
			if( stm instanceof RetLNode )
				types.add(stmType);
			else if( stm instanceof BlockLNode && stmType != null )
				types.add(stmType);
			/*else if( stm instanceof IteLNode && stmType != null )
				types.add(stmType);*/
					
		} 
		
		// faccio questo controllo solo quando inFunction==T
		// se non sono in una funzione non dovrei avere return
		// nella seconda IF non ci va la negazione
		/*if (types.size() > 0){
			//controllo: i tipi di ritorno devono essere uguali
			if ( ! types.stream().allMatch(types.get(0)::equals) )
				throw new TypeErrorException("block with multiple return statements having mismatching types.");
			else
				return types.get(0);
		}*/
		
		
		//se non ci sono ReturnNode restituisce NullTypeNode
		//return new NullTypeNode();
		if( inFunction ) {
			if( statements.size() > 0 ) {
				if (types.size() > 0){
					for(int i=0; i<types.size(); i++) {
						if( ! SimpLanPlusLib.isSubtype(types.get(i), types.get(0)) )
							throw new TypeErrorException("block with multiple return statements having mismatching types.");
					}
				}
				
				
				return statements.get( statements.size()-1 ).typeCheck();
			}
			else
				return null; //return implicito, NullType
		}
		else {
			return null;
		}
			
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
			/*for(Node n : statements) 
				res.addAll(n.checkSemantics(env));*/
			
			// NEW
			ArrayList<Integer> returns_index = new ArrayList<Integer>(); 
			for(int i=0; i<statements.size(); i++ ) {
				Node n = statements.get(i);
				res.addAll(n.checkSemantics(env));
				
				if( n instanceof RetLNode )
					returns_index.add(i);
			}
			
			if( inFunction ) {
				/* 	inFunc==V --> i return ci possono essere o no, 
				 * 			ma se ci sono devono essere gli ultimi	
				 */
				if( returns_index.size() > 1 )
					res.add(new SemanticError("Ci sono più return nello stesso blocco."));
				else {	
					if( returns_index.size() == 1 && 
						returns_index.get(0) != statements.size()-1 )
						res.add(new SemanticError("Il return non è l'ultimo stm del blocco. Codice irraggiungibile."));
				}
			}
			/* inFunc==F --> non ci devono essere return
			 * questo controllo viene fatto nella checkSemantics di RetNode
			 */
			
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
	
	public void setInFunction(boolean b){
		inFunction = b;
		for(Node n : statements) {
			if( n instanceof BlockLNode )
				((BlockLNode) n).setInFunction(b);
			
			if( n instanceof IteLNode )
				((IteLNode) n).setInFunction(b);
			
			if( n instanceof RetLNode )
				((RetLNode) n).setInFunction(b);
		}	
	}
	
}
