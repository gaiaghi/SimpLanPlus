package util;

import java.util.ArrayList;
import java.util.List;

import ast.ArrowTypeNode;
import ast.DecFunNode;
import ast.Node;

// entry of the Symbol Table

public class STEntry {
	private final int nestingLvl; 
	private final int offset; 
	private Node type;
	private List<Effect> varEffects; 			// lista effetti per le variabili
	private List< List<Effect> > parEffects; 	// lista effetti per le funzioni
	private String funLabel; 					// label delle funzioni per codGen
	private String funEndLabel; 				// label fine funzione per il return
	
	
	
	//testEffetti
	private DecFunNode decFun;
	private Environment envFunc;
	
	public Environment getEnvFunc() {
		return envFunc;
	}

	public void setEnvFunc(Environment envFunc) {
		this.envFunc = envFunc;
	}

	public DecFunNode getDecFun() {
		return decFun;
	}

	public void setDecFun(DecFunNode decFun) {
		this.decFun = decFun;
	}

	
	
	
	public STEntry (int nestingLvl, int offset) {
		this.nestingLvl = nestingLvl;
		this.offset = offset; 
		this.varEffects = new ArrayList<>();
		this.parEffects = new ArrayList<>();
		
		//testEffetti
		decFun = null;
		envFunc = null;
	}
	
	public STEntry(int nestingLvl, Node type, int offset) {
		this(nestingLvl, offset);
		this.type = type;
		
		if (type instanceof ArrowTypeNode) {
			// per ogni parametro creo la lista dei suoi effetti
			for (Node par: ((ArrowTypeNode)type).getParList()) { 
				List<Effect> effects = new ArrayList<>();    	
				for (int i=0; i<= par.getDereferenceNum(); i ++)  
					effects.add(new Effect(Effect.INITIALIZED));
				
				this.parEffects.add(effects);
			}
		}
		else {
			// creo la lista degli effetti della variabile/puntatore
			for (int i = 0; i <= type.getDereferenceNum(); i ++)
				this.varEffects.add(new Effect(Effect.INITIALIZED));
		}
		
		//testEffetti
		decFun = null;
		envFunc = null;
		
	}


	// per clonare una STEntry
	public STEntry(STEntry entry) {
		this(entry.nestingLvl, entry.offset);
		this.type = entry.type;
		this.funLabel = entry.funLabel;
		
		for (Effect e: entry.varEffects)
			this.varEffects.add(new Effect(e));
		
		for (List<Effect> parEffList: entry.parEffects) {
			List<Effect> copyEffects = new ArrayList<>();
			for (Effect e: parEffList)
				copyEffects.add(e);
			
			this.parEffects.add(copyEffects);
		}
		
		//testEffetti
		decFun = entry.decFun;
		envFunc = entry.envFunc;
		
	}
	
	// Restituisce una nuova STEntry composta da una nuona copia di tutti 
	// gli attributi, tranne che degli effetti.
	public static STEntry cloneSTEntryWithoutEffects(STEntry entry) {
		
		STEntry newEntry = new STEntry(entry.nestingLvl, entry.offset);
		
		newEntry.type = entry.type;
		newEntry.funLabel = entry.funLabel;
		
		newEntry.varEffects = entry.varEffects;
		newEntry.parEffects = entry.parEffects;
		
		
		//testEffetti
		newEntry.decFun = entry.decFun;
		newEntry.envFunc = entry.envFunc;
		
		return newEntry;
	}
	
	
	public void setType(Node type) {
		this.type = type;
		
		if (type instanceof ArrowTypeNode ) 
			// inizializzazione effetti dei parametri
			for (Node par: ((ArrowTypeNode)type).getParList()) {
				List<Effect> effects = new ArrayList<>();    	
				for (int i=0; i<= par.getDereferenceNum(); i++)  
					effects.add(new Effect(Effect.INITIALIZED));
				
				this.parEffects.add(effects);
			}
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
		return new ArrayList<>(varEffects);
	}
	public List<List<Effect>> getParEffectList(){
		return new ArrayList<>(parEffects);
	}
	
	public Effect getVarEffect(int index) {
		return varEffects.get(index);
	}
	
	public void setVarEffect(int level, Effect e) {
		this.varEffects.set(level, e);
	}
	
	public void setParEffect(int index, int level, Effect e) {
		this.parEffects.get(index).set(level, e);
	}
	
	public void setVarEffectList(List<Effect> l) {
		this.varEffects = new ArrayList<>(l);
	}
	
	public void setParEffectList(List<List<Effect>> l) {
		this.parEffects = new ArrayList<>(l);
	}
	
	public void setFunLabels() {
		this.funLabel = SimpLanPlusLib.freshFunLabel();
		this.funEndLabel = SimpLanPlusLib.freshFunLabel();
	}
	
	public String getFunLabel() {
		return this.funLabel;
	}
	
	public String getFunEndLabel() {
		return this.funEndLabel;
	}
	
	
	
	public String toPrint(String indent) {
		
		String status = "";
		String str = indent + "STEntry: Nesting Level = " + this.nestingLvl + "\n";
		if(type instanceof ArrowTypeNode)
			str = str + indent + "STEntry: Type = \n" + this.type.toPrint(indent + "  ");
		else {
			str = str + indent + "STEntry: Type = " + this.type.toPrint("");
			status = "\n" + indent + "STEntry: Status = " + this.varEffects; 
		}
			
		str = str + "\n" + indent + "STEntry: Offset = " + this.offset + status;
				  
		return str;
	}
	
	
	public String toString(){
		return toPrint("");
	}
	
	
	public int getSizeVarEffects(){
		return varEffects.size();
	}
	
	
	public ArrayList<SemanticError> checkEffectError(String id){
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		
		if( varEffects.size() > 0 ) {
			boolean error = false;
			for( int i = 0; i < varEffects.size() && !error; i ++ ) {
				if( varEffects.get(i).equals(Effect.ERROR) ) {
					error = true;
				}
			}
			if( error )
				errors.add(new SemanticError("'"+id + "' has error effect. Status: " + varEffects));
		}
		
		return errors;
	}

	
}
