package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import ast.ArrowTypeNode;
import ast.LhsNode;
import exception.MissingDecException;
import exception.MultipleDecException;


public class Environment {
	
	private List<HashMap<String, STEntry>> symbolTable;
	private int nestingLvl; 	// livello scope corrente, -1 all'inizio (main block ha livello 0)
	private int offset;
	
	
	public Environment (List<HashMap<String, STEntry>> symbolTable, int nestingLvl, int offset) {
		this.symbolTable = symbolTable; 
		this.nestingLvl = nestingLvl;
		this.offset = offset;
	}
	
	// costruttore per environment vuoto
	public Environment() {
		this( new ArrayList<>(), -1, -2);
	}
	
	// per clonare un ambiente
	public Environment(Environment env) {
		this(new ArrayList<>(), env.nestingLvl, env.offset);
		
		for (HashMap<String, STEntry> scope: env.symbolTable) {
			HashMap<String, STEntry> scopeCopy = new HashMap<>();
			for (String id: scope.keySet())
				scopeCopy.put(id, new STEntry(scope.get(id)));
			
			// qui di seguito non si deve usare addScope perchè nestingLvl non deve essere incrementato.
			this.symbolTable.add(scopeCopy);   
		}
	}
	
	/* Restituisce una copia dell'ambiente env. Crea nuove entry ma senza usare una
	 * nuova copia degli effetti nelle entry.
	 * */
	public static Environment cloneEnvWithoutEffects(Environment env) {
		
		Environment newEnv = new Environment(new ArrayList<>(), env.nestingLvl, env.offset);
		
		for (HashMap<String, STEntry> scope: env.symbolTable) {
			HashMap<String, STEntry> scopeCopy = new HashMap<>();
			for (String id: scope.keySet())
				scopeCopy.put(id, STEntry.cloneSTEntryWithoutEffects(scope.get(id)));
			
			// qui di seguito non si deve usare addScope perchè nestingLvl non deve essere incrementato.
			newEnv.symbolTable.add(scopeCopy);   
		}
		
		return newEnv;
	}

	
	public int getNestingLevel() {
		return this.nestingLvl;
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public int getAndUpdateOffset() {
		return offset--;
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
	

	public void safeAddEntry(String id, STEntry entry) {
		getCurrentScope().put(id, entry);
	}
	
	public STEntry lookup (String id) throws MissingDecException {
		for (int i = nestingLvl; i>=0; i--) {
			HashMap<String, STEntry> scope = symbolTable.get(i);
			if (scope.containsKey(id))
				return scope.get(id);
		}
		throw new MissingDecException("Missing declaration: "+id);
	}
	
	
	public void addScope(HashMap<String,STEntry> scope) {
		this.symbolTable.add(scope);
		this.nestingLvl += 1;
		offset = -2;
	}
	
	public void addScope() {
		this.symbolTable.add(new HashMap<>());
		this.nestingLvl +=1;
		offset = -2;
	}
	
	public void removeScope() {
		this.symbolTable.remove(nestingLvl);
		this.nestingLvl--;
	}


	public void removeFirstEntry (String id) {
		for (int i = this.symbolTable.size()-1; i>=0; i--)
			if (this.symbolTable.get(i).containsKey(id)) {
				this.symbolTable.get(i).remove(id);
				return;
			}
	}
	
	
//	---Per l'analisi degli effetti:
	
	// seq(env1, env2)
	public static Environment seqEnv (Environment env1, Environment env2 ) {
		
		Environment envSeq = new Environment(new ArrayList<>(), env1.nestingLvl, env1.offset);
		int envLength = env1.symbolTable.size(); 
		
		// per ogni scope dell'ambiente
		for (int i = 0; i < envLength; i++) {
			HashMap<String, STEntry> scopeSeq = new HashMap<>();
			
			var scope1 = env1.symbolTable.get(i);
			var scope2 = env2.symbolTable.get(i);
			
			// per ogni variabile nello scope
			for (String varId : scope1.keySet() ) {
				STEntry entry1 = scope1.get(varId);
				STEntry entry2 = scope2.get(varId);
				
				if (entry2 == null) 
					// se nel secondo ambiente non e' presente la variabile
					scopeSeq.put(varId, entry1);
				else { 
					// se la variabile e' presente sia nel primo che nel secondo ambiente
					STEntry entrySeq = new STEntry(entry1.getNestingLevel(), entry1.getType(), entry1.getOffset());
					
					// per ogni effetto della variabile aggiungo l'effetto seq tra i due
					for (int j=0; j<entry1.getVarEffectList().size(); j++) 
						entrySeq.setVarEffect(j, Effect.seq(entry1.getVarEffect(j), entry2.getVarEffect(j) )); 
					
					scopeSeq.put(varId, entrySeq);
				}
			}
			envSeq.addScope(scopeSeq);
		}
		
		return envSeq;
	}
	
	
	// max(env1, env2)
	public static Environment maxEnv (Environment env1, Environment env2 ) {
		
		Environment envMax = new Environment(new ArrayList<>(), env1.nestingLvl, env1.offset);
		int envLength = env1.symbolTable.size(); 
		
		// per ogni scope dell'ambiente
		for (int i = 0; i < envLength; i++) {
			HashMap<String, STEntry> scopeMax = new HashMap<>();
			
			var scope1 = env1.symbolTable.get(i);
			var scope2 = env2.symbolTable.get(i);
		
			// per ogni variabile nello scope
			for (String varId : scope1.keySet() ) {
				STEntry entry1 = scope1.get(varId);
				STEntry entry2 = scope2.get(varId);
				
				if (entry2 == null) 
					// se nel secondo ambiente non e' presente la variabile
					scopeMax.put(varId, entry1);
				else { 
					// se la variabile e' presente sia nel primo che nel secondo ambiente
					STEntry entryMax = STEntry.cloneSTEntryWithoutEffects(entry1);
					
					// per ogni effetto della variabile aggiungo l'effetto massimo tra i due
					for (int j = 0; j < entry1.getVarEffectList().size(); j ++) {
						entryMax.getVarEffect(j).setEffect(Effect.max(entry1.getVarEffect(j), entry2.getVarEffect(j)));
					}
					
					scopeMax.put(varId, entryMax);
				}
			}
			// non si deve chiamare addScope perchè nestingLvl non deve essere incrementato (è una copia di env1)
			envMax.symbolTable.add(scopeMax); 
		}
		
		return envMax;
	}
	
	
	// par(env1, env2)
	public static Environment parEnv (Environment env1, Environment env2 ) {
		Environment envPar = new Environment();
		envPar.addScope();
		
		HashMap<String, STEntry> scope1 = env1.symbolTable.get(env1.symbolTable.size() -1);
		HashMap<String, STEntry> scope2 = env2.symbolTable.get(env2.symbolTable.size() -1);	
		
		// env1(x) se x non e' in env2
		for (var varEntry1: scope1.entrySet()) {
			if(! scope2.containsKey(varEntry1.getKey())) {
				STEntry entry = STEntry.cloneSTEntryWithoutEffects(varEntry1.getValue());
				envPar.safeAddEntry(varEntry1.getKey(), entry);
			}
		}

		
		//env2(x) se x non e' in env1
		for (var varEntry2: scope2.entrySet()) {
			if(! scope1.containsKey(varEntry2.getKey())) {
				STEntry entry = STEntry.cloneSTEntryWithoutEffects(varEntry2.getValue());
				envPar.safeAddEntry(varEntry2.getKey(), entry);
			}
		}
		
		
		for (var varEntry1: scope1.entrySet()) {
			for (var varEntry2: scope2.entrySet()) {
				if ( varEntry1.getKey().compareTo(varEntry2.getKey()) == 0 ) {
					STEntry entry = new STEntry(varEntry1.getValue());
					
					for (int i=0; i < varEntry2.getValue().getVarEffectList().size(); i++) 
						entry.setVarEffect(i, Effect.par(varEntry1.getValue().getVarEffect(i), varEntry1.getValue().getVarEffect(i)));
					
					envPar.safeAddEntry(varEntry2.getKey(), entry);
				}
			}
		} 

		
		
		/*System.out.println("\nFINE   ENV PAR  ");
		try { System.out.println("x     envPar "+envPar.lookup("x") +"     "+hashEffect(envPar.lookup("x").getVarEffectList()));
		} catch (MissingDecException e) {System.err.println("ENV PAR   x  non trovata in envPar  ");		}
		*/
		
		return envPar;
	}
	
	
	private static String hashEffect(List<Effect> list) {
		String str="[";
		for(Effect e : list)
			str = str + e + ",";
		str=str+"]        [";
		for(Effect e : list)
			str = str + e.hashCode() + ",";
		return str+"]";
	}
	
	/* env1 = env1a-> env (multi-scope)
	 * env2 (single scope contenente gli effetti dei parametri attuali passati per puntatore)
	 * 
	 * update(env1a->env1b, env2)=
	 * a--- update(env1b->env1b[u->a], env'')					se env2 = env''[u->a] && u \in env1b
	 * b--- update(update(env1a, [u->a])->env1b, env'')			se env2 = env''[u->a] && u \notin env1b
	 * c--- env1a->env1b										se env2 = 0
	 * */
	public static Environment updateEnv (Environment env1, Environment env2 ) {
		
		if (env1.symbolTable.size() == 0 || env2.symbolTable.size() == 0)
			return new Environment(env1);
		
		Environment updatedEnv;
		
		HashMap<String, STEntry> topScope = env1.symbolTable.get(env1.symbolTable.size()-1);
		HashMap<String, STEntry> env2Scope = env2.symbolTable.get(env2.symbolTable.size()-1);
		
		// c
		if (env2Scope.keySet().isEmpty())
			return Environment.cloneEnvWithoutEffects(env1);
		
		Entry<String, STEntry> u = env2Scope.entrySet().stream().findFirst().get();
		env2.removeFirstEntry(u.getKey()); 	// env''
				
		// a
		if (topScope.containsKey(u.getKey())) {
			topScope.put(u.getKey(), u.getValue());
			updatedEnv = updateEnv(env1, env2);
		}
		
		// b
		else {
			// creazione Environment contenente u
			Environment envU = new Environment();
			envU.addScope();
			envU.safeAddEntry(u.getKey(), u.getValue());
			STEntry entryU = envU.symbolTable.get(0).get(u.getKey());
			
			// copia degli effetti
			for (int i = 0; i< u.getValue().getVarEffectList().size(); i++)
				entryU.getVarEffect(i).setEffect(u.getValue().getVarEffect(i));
			
			env1.removeScope(); // env1a
			
			Environment envTemp = updateEnv(env1, envU);
			envTemp.addScope(topScope); 
			updatedEnv = updateEnv(envTemp, env2);	
		}
		
		return updatedEnv;
	}
	
	
	/*
	 * Input: lista di identificatori presenti in un nodo espressione.
	 * Output: lista di errori.
	 * 
	 * Per ogni identificatore: controlla Seq(env, id->RW)
	 * */
	public static ArrayList<SemanticError> checkExpressionEffects(List<LhsNode> vars, Environment env){
		ArrayList<SemanticError> errors = new ArrayList<>();
		
		for(LhsNode id : vars) {
			
			try {
				STEntry entry = env.lookup(id.getId().getId());
				int derNum = id.getDereferenceNum();
				Effect effect = entry.getVarEffect(derNum);
				Effect result = Effect.seq(effect, Effect.READ_WRITE);
				entry.getVarEffect(derNum).setEffect(result);
				
				if( result.equals(Effect.ERROR) ) {
					errors.add(new SemanticError("Cannot read '" + id.getId().getId() + "' after its deletion."));
					
					for( int i = 0; i < entry.getSizeVarEffects(); i ++ )
						entry.setVarEffect(i, result);
				}
			}
			catch(MissingDecException e) {}
		}
		
		return errors;
	}
	
	
	
	public void copyFrom(Environment env) {
        symbolTable.clear();
        nestingLvl = env.nestingLvl;
        offset = env.offset;

        for (var scope : env.symbolTable) {
        	HashMap<String, STEntry> copiedScope = new HashMap<String, STEntry>();
            for (var id : scope.keySet()) {
            	copiedScope.put(id, STEntry.cloneSTEntryWithoutEffects(scope.get(id)));
            }
            symbolTable.add(copiedScope);
        }
    }
	
	
	/* copia gli effetti delle variabili/puntatori in envFrom nelle variabili/puntatori
	 * corrispondenti in envTo
	 * */
	public static Environment cloneMaxEnv(Environment envFrom, Environment envTo) {
        
		Environment newEnv = new Environment();
		newEnv.nestingLvl = envFrom.nestingLvl;
		newEnv.offset = envFrom.offset;
		
		
		for(int i = 0; i < envFrom.symbolTable.size(); i ++ ) {
			HashMap<String, STEntry> copiedScope = new HashMap<String, STEntry>();
			HashMap<String, STEntry> scopeFrom = envFrom.symbolTable.get(i);
			boolean searchInEnvTo = false;
			HashMap<String, STEntry> scopeTo = null;
			if( i < envTo.symbolTable.size() ) {
				searchInEnvTo = true;
				scopeTo = envTo.symbolTable.get(i);
			}
				
			
			for (var id : scopeFrom.keySet() ) {
				STEntry newEntry = null;
				if( searchInEnvTo && scopeTo.containsKey(id) ) {
					
					newEntry = scopeTo.get(id);
					for(int j = 0; j < newEntry.getVarEffectList().size(); j ++) {
						Effect newEffect = Effect.max(newEntry.getVarEffect(j), scopeFrom.get(id).getVarEffect(j));
						newEntry.getVarEffect(j).setEffect(newEffect);
					}
					
				}
				else {
					newEntry = scopeFrom.get(id);
				}
				copiedScope.put(id, newEntry);
				
            }
			
			newEnv.symbolTable.add(copiedScope);
		}
		
		
       return newEnv;
    }
	
	
	public ArrayList<SemanticError> checkErrors(){
		
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
	
		for( HashMap<String, STEntry> scope : symbolTable ) {
			for( Entry<String,STEntry> entryVar : scope.entrySet() ) {
				errors.addAll(entryVar.getValue().checkEffectError(entryVar.getKey()));
			}
		}
		
		return errors;
	}
	
	
	
	public Environment envFunc() throws MultipleDecException {
		Environment newEnv = new Environment();
		for( HashMap<String, STEntry> scope : symbolTable ) {
			newEnv.addScope();
			for( Entry<String,STEntry> entryVar : scope.entrySet() ) {
				if( entryVar.getValue().getType() instanceof ArrowTypeNode ) {
					newEnv.addEntry(entryVar.getKey(), new STEntry(entryVar.getValue()) );
				}
			}
		}
		return newEnv;
	}
		
		
		
	public void printEnv(String str) {
		System.out.println("\n\n\n----------PRINT ENV--------------------\n"+str);
		int i = 0;
		for( HashMap<String, STEntry> scope : symbolTable ) {
			System.out.println("----------scope "+i+"--------------------");
			i ++;
			for( Entry<String,STEntry> entryVar : scope.entrySet() ) {
				System.out.println(entryVar.getKey());
			}
		}
		System.out.println("----------FINE PRINT ENV--------------------\n");
	}
		
	
	
	
}

