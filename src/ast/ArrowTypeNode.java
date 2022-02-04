package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class ArrowTypeNode implements Node {
	
	private ArrayList<Node> parlist; 
	private Node ret;
	  
	
	public ArrowTypeNode (ArrayList<Node> p, Node r) {
		parlist=p;
	    ret=r;
	}
	  
	
	public String toPrint(String indent) { 
		String parlstr="";
	    for (Node par : parlist)
	    	parlstr += par.toPrint(indent +"  ") +"\n";
		return indent +"ArrowType\n"  +parlstr +ret.toPrint(indent +"  ->") ; 
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
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
