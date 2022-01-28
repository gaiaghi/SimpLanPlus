package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Environment {
	
	private List<HashMap<String, STEntry>> symbolTable;
	private int nestingLvl; //scope lvl corrente, -1 all'inizio (main block ha livello 0)
	private int offset;
	
	
	public Environment (List<HashMap<String, STEntry>> symbolTable, int nestingLvl, int offset) {
		this.symbolTable = symbolTable; //gli si passa una lista di hash maps, anche vuota
		this.nestingLvl = nestingLvl;
		this.offset = offset;
	}
	
	//costruttore per environment vuoto
	public Environment() {
		this( new ArrayList<>(), -1, 0);
	}
	
	//to clone. quando ci serve?
//	public Environment(Environment e) {
//		
//	}
	
	
	//ritorna il nesting level corrente
	public int getNestingLevel() {
		return this.nestingLvl;
	}
	
	public HashMap<String, STEntry> getCurrentScope(){
		return this.symbolTable.get(nestingLvl);
	}
	
	//bisogna controllare che l'id non sia già presente
	public void addEntry(String id, STEntry entry) {
		//TODO
	}
	
	//da controllare gli errori
	public STEntry lookup (String id) {
		//TODO
		return null;
	}
	
	public void addScope(HashMap<String,STEntry> scope) {
		this.symbolTable.add(scope);
		this.nestingLvl += 1;
		//per godgen:
		//offset = 0;
	}
	
	public void addScope() {
		this.symbolTable.add(new HashMap<>());
		this.nestingLvl +=1;
		//per codgen:
		//offset = 0;
	}
	
	public void removeScope() {
		this.symbolTable.remove(nestingLvl);
		this.nestingLvl--;
		//modifica offset per codgen?
	}
	
}