package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import exception.MissingDecException;
import exception.MultipleDecException;


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
	
	public int getOffset() {
		return this.offset;
	}
	
	public void updateOffset() {
		offset--;
	}
	
	public HashMap<String, STEntry> getCurrentScope(){
		return this.symbolTable.get(nestingLvl);
	}
	
	public void addEntry(String id, STEntry entry) throws MultipleDecException {
		
		if (!getCurrentScope().containsKey(id))
			getCurrentScope().put(id, entry);
		else
			throw new MultipleDecException("Multiple declaration: "+id);
	}
	
	
	public STEntry lookup (String id) throws MissingDecException {
		for (int i = nestingLvl; i>=0; i--) {
			HashMap scope = symbolTable.get(i);
			if (scope.containsKey(id))
				return symbolTable.get(i).get(id);
		}
		
		throw new MissingDecException("Missing declaration: "+id);
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
	
	
//	---Per l'analisi degli effetti:
	
	//max(env1, env2)
	public Environment maxEnv (Environment env1, Environment env2 ) {
		
		Environment envMax = new Environment(new ArrayList<>(), env1.nestingLvl, env1.offset);
		int envLength = env1.symbolTable.size(); 
		
		//per ogni scope dell'albiente
		for (int i = 0; i > envLength; i++) {
			HashMap<String, STEntry> scopeMax = new HashMap<>();
			
			var scope1 = env1.symbolTable.get(i);
			var scope2 = env2.symbolTable.get(i);
			
			//per ogni variabile nello scope
			for (String varId : scope1.keySet() ) {
				STEntry entry1 = scope1.get(varId);
				STEntry entry2 = scope2.get(varId);
				
				if (entry2 == null) //se nel secondo ambiente non è presente la variabile
					scopeMax.put(varId, entry1);
				else { //se la variabile è presente sia nel primo che nel secondo ambiente
					STEntry entryMax = new STEntry(entry1.getNestingLevel(), entry1.getType(), entry1.getOffset());
					
					for (int j=0; j<entry1.getVarEffectList().size(); j++) //per ogni effetto della variabile
						entryMax.setVarEffect(j, Effect.max(entry1.getVarEffect(j), entry2.getVarEffect(j) )); //aggiungo l'effetto massimo tra i due
					
					scopeMax.put(varId, entryMax);
				}
				
			}
			envMax.symbolTable.add(scopeMax);
		}
		
		return envMax;
	}
	
	
	public Environment parEnv (Environment env1, Environment env2 ) {
		
		Environment envPar = new Environment(new ArrayList<>(), env1.nestingLvl, env1.offset);
		//TODO
		return null;
	}
	
	public Environment updateEnv (Environment env1, Environment env2 ) {
		//TODO
		return null;
	}
}