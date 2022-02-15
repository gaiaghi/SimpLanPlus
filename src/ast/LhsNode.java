package ast;

import java.util.ArrayList;

import exception.TypeErrorException;
import util.Environment;
import util.SemanticError;

public class LhsNode implements Node {
//	lhs         : ID | lhs '^' ;
	
	private IdNode id;
	private LhsNode lhs;
	
	public LhsNode(IdNode id, LhsNode lhs) {
		
		this.id = id;
		this.lhs = lhs;
		
	}
	
	public IdNode getId() {
		return this.id;
	}
	
	@Override
	public String toPrint(String indent) {
		String str = indent +"Id: " +this.id.getId();
		for(int i=0; i<getDereferenceNum(); i++)
			str = str + "^";
		str = str + "\n" +this.id.getSTEntry().toPrint(indent +"  ");
		
		return str;
	}			
	
	
	public int getDereferenceNum() {
		if (this.lhs != null)
			return this.lhs.getDereferenceNum() + 1;
		return 0;
	}
	
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		if (lhs == null)
			return id.typeCheck();	
		
		//2 righe aggiunte per test
		//System.out.println("Lhs "+id.getId() +"  "+getDereferenceNum());
		//System.out.println("id "+id.getId() +"  "+id.getDereferenceNum()+"\n");
		
		int derefNum = getDereferenceNum();

		//da controllare se funziona!
		//DA MODIFICARE: il codice sotto non funziona.
		//if (derefNum > 1 + id.getDereferenceNum() )
			//throw new TypeErrorException("too many dereference operations at pointer " + id.getId());
		
		//test
		if (derefNum > id.getDereferenceNum() )
			throw new TypeErrorException("too many dereference operations at pointer " + id.getId());
		
		return ((PointerTypeNode) lhs.typeCheck()).getPointedType();
		
		/*
		 * id.getDereferenceNum() è sempre uguale a 0 perchè:
		 * 	- quando dichiaro il puntatore in visitDecVar viene calcolato il derefenceNum 
		 * 		e viene passato al costruttore IdNode
		 * 	- quando uso il puntatore creato prima, in visitLhs creo un nuovo IdNode con derefenceNum=0
		 * 
		 * non ho ancora l'ambiente quindi è giusto creare un nuovo IdNode.
		 * in visitLhs dovrei contare con quante ^ viene usato
		 * */
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		if (lhs == null)
			return id.checkSemantics(env);
		else
			return lhs.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

}
