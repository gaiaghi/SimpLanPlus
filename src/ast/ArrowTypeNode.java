package ast;

import java.util.ArrayList;
import java.util.List;

import util.Environment;
import util.SemanticError;

public class ArrowTypeNode implements Node {
	
	private ArrayList<Node> parlist; 
	private Node ret;
	  
	
	public ArrowTypeNode (ArrayList<Node> p, Node r) {
		parlist = p;
	    ret = r;
	}
	  
	
	public String toPrint(String indent) { 
		String parlstr = "";
	    for (Node par : parlist)
	    	parlstr += par.toPrint(indent + "  ") + "\n";
		return indent + "ArrowType\n"  + parlstr + ret.toPrint(indent + "  ->") ; 
	}
	  
	
	public Node getRet () { 
	    return ret;
	}
	  
	
	public ArrayList<Node> getParList () { 
	    return parlist;
	}

	
	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return new ArrayList<SemanticError>();
	}
	
	
	@Override
	public Node typeCheck() {
		return null;
	}

	
	@Override
	public String codeGeneration() {
		return "";
	}


	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		return new ArrayList<SemanticError>();
	}

	
	public ArrayList<Integer> getIndexesPointerPar(){
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		for( int i = 0; i < parlist.size(); i ++ ) {
			Node par = parlist.get(i);
			if( par instanceof PointerTypeNode )
				indexes.add(i);
		}
		
		return indexes;
	}
	
	
	public ArrayList<Integer> getIndexesNotPointerPar(){
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		for( int i = 0; i < parlist.size(); i ++ ) {
			Node par = parlist.get(i);
			if( ! (par instanceof PointerTypeNode) )
				indexes.add(i);
		}
		
		return indexes;
	}
	
	
	
}
