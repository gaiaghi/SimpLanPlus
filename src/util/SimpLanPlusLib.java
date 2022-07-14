package util;

import ast.*;
import exception.TypeErrorException;

public class SimpLanPlusLib {
	
  
	private static int labCount = 0; 
  
	private static int funLabCount = 0; 

	private static String funCode = ""; 

	
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
		return "label_" + (labCount ++);
	} 

	
	public static String freshFunLabel() { 
		return "function_" + (funLabCount ++);
	} 
  
  
	public static void putCode(String c) {
		// aggiunge una linea vuota di separazione prima di funzione
		funCode += "\n" + c; 
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
	
	

	public static Node getNodeIfPointer(Node node) {
		
		if( node instanceof PointerTypeNode ) {	
			PointerTypeNode pointer = (PointerTypeNode) node;
			if( pointer.getDerNumDec() == pointer.getDerNumStm() ) {
				node = pointer.getPointedType();
			}
		}
		
		return node;
	}
	

}