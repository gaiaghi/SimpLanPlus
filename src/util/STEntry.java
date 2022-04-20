package util;

import java.util.ArrayList;
import java.util.List;

import ast.ArrowTypeNode;
import ast.Node;

//entry of the Symbol Table
public class STEntry {
	private final int nestingLvl; 
	private final int offset; // code generation
	private Node type;
	private List<Effect> varEffects; //lista effetti per le variabili
	private List< List<Effect> > parEffects; //lista effetti per le funzioni

	
	
	public STEntry (int nestingLvl, int offset) {
		this.nestingLvl = nestingLvl;
		this.offset = offset; 
		
		this.varEffects = new ArrayList<>();
		this.parEffects = new ArrayList<>();
	}
	
	public STEntry(int nestingLvl, Node type, int offset) {
		this(nestingLvl, offset);
		this.type = type;
		
		if (type instanceof ArrowTypeNode) {
			for (Node par: ((ArrowTypeNode)type).getParList()) { //per ogni parametro
				List<Effect> effects = new ArrayList<>();    	//creo la lista dei suoi effetti
				for (int i=0; i<= par.getDereferenceNum(); i++)  
					effects.add(new Effect(Effect.INITIALIZED));
				
				this.parEffects.add(effects);
			}
		}
		else {
			for (int i = 0; i<=type.getDereferenceNum(); i++)
				this.varEffects.add(new Effect(Effect.INITIALIZED));
		}
		
	}


	//to clone. Quando serve?
//	public STEntry(STEntry s) {
//		
//	}
	
	
	public void setType(Node type) {
		this.type = type;
	}
	
	public Node getType() {
		return this.type;
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public int getNestingLevel() {
		return this.nestingLvl;
	}
	
	public List<Effect> getVarEffectList(){
		return varEffects;
	}
	public List<List<Effect>> getParEffectList(){
		return parEffects;
	}
	
	public Effect getVarEffect(int index) {
		return varEffects.get(index);
	}
	
	public void setVarEffect(int level, Effect e) {
		this.varEffects.set(level, e);
	}
	
	//sempre per la stampa dell'ast (anche in simplan)
	public String toPrint(String indent) {
		//da aggiungere stato degli effetti
		
		String str = indent + "STEntry: Nesting Level = " +this.nestingLvl +"\n";
		if(type instanceof ArrowTypeNode) 
			str = str +indent +"STEntry: Type = \n" +this.type.toPrint(indent +"  ");
		else
			str = str +indent +"STEntry: Type = " +this.type.toPrint("");
		
		str = str +"\n" +indent +"STEntry: Offset = " +this.offset
				  +"\n" +indent +"STEntry: Status = "+ this.varEffects;
		
		return str;
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
