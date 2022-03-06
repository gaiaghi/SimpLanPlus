package util;

import ast.*;
import exception.TypeErrorException;

public class SimpLanPlusLib {
	
  
	private static int labCount=0; 
  
	private static int funLabCount=0; 

	private static String funCode=""; 

	
	public static boolean isEquals (Node a, Node b) {
		
		if( a == null && b == null )
			return true;
		
		if( (a == null && b instanceof VoidTypeNode) || (a instanceof VoidTypeNode && b == null) )
			return true;
		
		if( a == null || b == null )
			return false;
		
		return a.getClass().equals(b.getClass()) ; 
	} 
  
	
	public static String freshLabel() { 
		return "label"+(labCount++);
	} 

	
	public static String freshFunLabel() { 
		return "function"+(funLabCount++);
	} 
  
  
	public static void putCode(String c) { 
		funCode+="\n"+c; //aggiunge una linea vuota di separazione prima di funzione
	} 
  
  
	public static String getCode() { 
		return funCode;
	} 

	
	public static int counterPointerNumber(Node node) throws TypeErrorException {
		int countPointer = 0;
		boolean stop = false;
		while( !stop ) {
			if( node instanceof PointerTypeNode )
			{
				countPointer ++;
				node = node.typeCheck();
			}
			else
				stop = true;
		}
		return countPointer;
	}
	
	public static int dereferenceNumFromExp(Node exp) {
		while( true ) {
			if( exp instanceof DerExpNode ) {
				return ((DerExpNode) exp).getLhs().getDereferenceNum();
			}
			else if( exp instanceof BaseExpNode ){
				((BaseExpNode) exp).getExp();
			}			
		}
	}

}