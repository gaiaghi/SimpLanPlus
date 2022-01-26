package ast;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public interface Node {
	
  //Stampa il nodo come stringa, serve per la stampa dell'AST. 
  //Indent indica la stringa che descrive l'ast che viene prima del nodo in esame  
  String toPrint(String indent);
  
  
  //fa il type checking e ritorna il tipo dell'espressione/identificatore
  //throws exception ?
  Node typeCheck();
  
  
  //genera codice assembly
  String codeGeneration();
  
  
  //analisi semantica??
  ArrayList<SemanticError> checkSemantics(Environment env);
  
  
  //analisi degli effetti
  ArrayList<SemanticError> checkEffects(Environment env);
}  