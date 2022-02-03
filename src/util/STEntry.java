package util;

import ast.Node;

//entry of the Symbol Table
public class STEntry {
	private final int nestingLvl; 
	private final int offset; // code generation
	private Node type;
	
	//Da aggiungere:  effetti, reference a DecFunNode
	//Effetti: una lista
	
	public STEntry (int nestingLvl, int offset) {
		this.nestingLvl = nestingLvl;
		this.offset = offset; 
	}
	
	public STEntry(int nestingLvl, Node type, int offset) {
		this(nestingLvl, offset);
		this.type = type;
		
		//poi dovrebbe esserci l'inizializzazione degli effetti degli id
		
	}


	//to clone. Quando serve?
//	public STEntry(STEntry s) {
//		
//	}
	
	public Node getType() {
		return this.type;
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public int getNestingLevel() {
		return this.nestingLvl;
	}
	
	//sempre per la stampa dell'ast (anche in simplan)
	public String toPrint(String indent) {
		//da aggiungere stato degli effetti
		return indent + "STEntry: Nesting Level = " +this.nestingLvl 
				+"\n" +indent +"STEntry: Type = \n" +this.type.toPrint(indent +"  ")
				+"\n" +indent +"STEntry: Offset = " +this.offset
				+"\n" +indent +"STEntry: Status = ";
	}
	
	//stampa del singolo entry
	public String toString(){
		return toPrint("");
	}

	//da aggiungere?
//	public boolean equals() {
//		return true;
//	}
	
}
