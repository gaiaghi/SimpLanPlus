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
		
		/*System.out.println("\n\nMAX ENV  1");
		try { System.out.println("x     env1 "+hashEffect(env1.lookup("x").getVarEffectList()));
		} catch (MissingDecException e) {System.err.println("ENV MAX   x  non trovata in env1  ");		}
		try { System.out.println("x     env2 "+hashEffect(env2.lookup("x").getVarEffectList()));
		} catch (MissingDecException e) {System.err.println("ENV MAX   x  non trovata in env2  ");		}
		*/
		
		// per ogni scope dell'ambiente
		for (int i = 0; i < envLength; i++) {
			HashMap<String, STEntry> scopeMax = new HashMap<>();
			
			var scope1 = env1.symbolTable.get(i);
			var scope2 = env2.symbolTable.get(i);
			
			/*System.out.println("\n\nMAX ENV  scope "+i);
			try { System.out.println("x     scope 1 "+hashEffect(scope1.get("x").getVarEffectList()));
			} catch (Exception e) {System.err.println("ENV MAX   x  non trovata in scope1  ");		}
			try { System.out.println("x     scope 2 "+hashEffect(scope2.get("x").getVarEffectList()));
			} catch (Exception e) {System.err.println("ENV MAX   x  non trovata in scope2  ");		}
			*/
			
			// per ogni variabile nello scope
			for (String varId : scope1.keySet() ) {
				STEntry entry1 = scope1.get(varId);
				STEntry entry2 = scope2.get(varId);
				
				if (entry2 == null) 
					// se nel secondo ambiente non e' presente la variabile
					scopeMax.put(varId, entry1);
				else { 
					// se la variabile e' presente sia nel primo che nel secondo ambiente
					//STEntry entryMax = new STEntry(entry1.getNestingLevel(), entry1.getType(), entry1.getOffset());
					STEntry entryMax = STEntry.cloneSTEntryWithoutEffects(entry1);
					
					// per ogni effetto della variabile aggiungo l'effetto massimo tra i due
					for (int j = 0; j < entry1.getVarEffectList().size(); j ++) {
						//entryMax.setVarEffect(j, Effect.max(entry1.getVarEffect(j), entry2.getVarEffect(j) ));
						
						entryMax.getVarEffect(j).setEffect(Effect.max(entry1.getVarEffect(j), entry2.getVarEffect(j)));
					}
					
					
					scopeMax.put(varId, entryMax);
				}
			}
			// non si deve chiamare addScope perchè nestingLvl non deve essere incrementato (è una copia di env1)
			envMax.symbolTable.add(scopeMax); 
		}
		
		/*try { System.out.println("FINE MAX ENV   x     envMax "+hashEffect(envMax.lookup("x").getVarEffectList()));
		} catch (MissingDecException e) {System.err.println("ENV MAX   x  non trovata in env1  ");		}
		*/
		
		return envMax;
	}
	
	
	// par(env1, env2)
	public static Environment parEnv (Environment env1, Environment env2 ) {
		Environment envPar = new Environment();
		envPar.addScope();
		
		HashMap<String, STEntry> scope1 = env1.symbolTable.get(env1.symbolTable.size() -1);
		HashMap<String, STEntry> scope2 = env2.symbolTable.get(env2.symbolTable.size() -1);	
		
		/*System.out.println("\n\nENV PAR  1");
		try { System.out.println("x     env1 "+hashEffect(env1.lookup("x").getVarEffectList()));
		} catch (MissingDecException e) {System.err.println("ENV PAR   x  non trovata in env1  ");		}
		try { System.out.println("x     env1 "+hashEffect(env1.lookup("x").getVarEffectList()));
		} catch (MissingDecException e) {System.err.println("ENV PAR   x  non trovata in env2  ");		}
		*/
		
		// env1(x) se x non e' in env2
		for (var varEntry1: scope1.entrySet()) {
			if(! scope2.containsKey(varEntry1.getKey())) {
				STEntry entry = new STEntry(varEntry1.getValue());
				envPar.safeAddEntry(varEntry1.getKey(), entry);
			}
		}

		/*System.out.println("\nENV PAR  2");
		try { System.out.println("x     envPar "+envPar.lookup("x") +"     "+hashEffect(envPar.lookup("x").getVarEffectList()));
		} catch (MissingDecException e) {System.err.println("ENV PAR   x  non trovata in envPar  ");		}
		*/
		
		//env2(x) se x non e' in env1
		for (var varEntry2: scope2.entrySet()) {
			if(! scope1.containsKey(varEntry2.getKey())) {
				STEntry entry = new STEntry(varEntry2.getValue());
				envPar.safeAddEntry(varEntry2.getKey(), entry);
			}
		}
		
		
		
		/*System.out.println("\nENV PAR  3");
		try { System.out.println("x     envPar "+envPar.lookup("x") +"     "+hashEffect(envPar.lookup("x").getVarEffectList()));
		} catch (MissingDecException e) {System.err.println("ENV PAR   x  non trovata in envPar  ");		}
		*/
		
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
		
		/*System.out.println("\n\nUPD PAR  1");
		try { System.out.println("x     env1 "+hashEffect(env1.lookup("x").getVarEffectList()));
		} catch (MissingDecException e) {System.err.println("UPD PAR   x  non trovata in env1  ");		}
		try { System.out.println("x     env2 "+hashEffect(env2.lookup("x").getVarEffectList()));
		} catch (MissingDecException e) {System.err.println("UPD PAR   x  non trovata in env2  ");		}
		*/
		
		
		if (env1.symbolTable.size() == 0 || env2.symbolTable.size() == 0)
			return new Environment(env1);
		
		Environment updatedEnv;
		
		HashMap<String, STEntry> topScope = env1.symbolTable.get(env1.symbolTable.size()-1);
		HashMap<String, STEntry> env2Scope = env2.symbolTable.get(env2.symbolTable.size()-1);
		
		// c
		if (env2Scope.keySet().isEmpty())
			//return new Environment(env1);
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
				//entryU.setVarEffect(i, u.getValue().getVarEffect(i));
				entryU.getVarEffect(i).setEffect(u.getValue().getVarEffect(i));
			
			env1.removeScope(); // env1a
			
			Environment envTemp = updateEnv(env1, envU);
			envTemp.addScope(topScope); 
			updatedEnv = updateEnv(envTemp, env2);	
		}
		
		/*System.out.println("\nFINE   ENV UPD  ");
		try { System.out.println("x     updatedEnv "+hashEffect(updatedEnv.lookup("x").getVarEffectList()));
		} catch (MissingDecException e) {System.err.println("UPD ENV   x  non trovata in envUpd  ");		}
		*/
		
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
				//System.err.println("CHECK 1 " +entry.getVarEffect(derNum) +"  "+ entry.getVarEffect(derNum).hashCode());
				//entry.setVarEffect(derNum, result);
				entry.getVarEffect(derNum).setEffect(result);
				//System.err.println("CHECK 2 " +entry.getVarEffect(derNum) +"  "+ entry.getVarEffect(derNum).hashCode());
				
				if( result.equals(Effect.ERROR) ) {
					errors.add(new SemanticError("Cannot read '" + id.getId().getId() + "' after its deletion."));
					
					for( int i = 0; i < entry.getSizeVarEffects(); i ++ )
						entry.setVarEffect(i, result);
				}
			}
			catch(MissingDecException e) {
				errors.add(new SemanticError("Missing declaration: " + id.getId().getId() ));
			}
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
                //copiedScope.put(id, new STEntry(scope.get(id)));
            	copiedScope.put(id, STEntry.cloneSTEntryWithoutEffects(scope.get(id)));
            }
            symbolTable.add(copiedScope);
        }
    }
	
	public static Environment cloneMaxEnv(Environment envFrom, Environment envTo) {
        
		Environment newEnv = new Environment();
		newEnv.nestingLvl = envFrom.nestingLvl;
		newEnv.offset = envFrom.offset;
		
		
		/*System.out.println("envFrom "+envFrom.symbolTable.size()
					+"        envTo "+envTo.symbolTable.size());
		*/
		
		for(int i = 0; i < envFrom.symbolTable.size(); i ++ ) {
			HashMap<String, STEntry> copiedScope = new HashMap<String, STEntry>();
			HashMap<String, STEntry> scopeFrom = envFrom.symbolTable.get(i);
			boolean searchInEnvTo = false;
			HashMap<String, STEntry> scopeTo = null;
			if( i < envTo.symbolTable.size() ) {
				searchInEnvTo = true;
				scopeTo = envTo.symbolTable.get(i);
			}
				
			/*System.err.println("scopeFrom "+i+"     size "+scopeFrom.size());
			System.err.println("scopeTo "+i+"     size "+scopeTo.size());*/
			for (var id : scopeFrom.keySet() ) {
				//System.err.println("for id "+id +"   i "+i);
				STEntry newEntry = null;
				if( searchInEnvTo && scopeTo.containsKey(id) ) {
					
					
					/*System.err.println("inizio for  "+id +"   TO "+hashEffect(scopeTo.get(id).getVarEffectList()) 
						+"    FROM "+hashEffect(scopeFrom.get(id).getVarEffectList()));
					*/
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
				
				//System.err.println("fine for  "+id +"    "+hashEffect(copiedScope.get(id).getVarEffectList()) );
				
				
            }
			//System.err.println("cloneEnv scope   "+i+"   "+hashEffect(copiedScope.get("x").getVarEffectList()));
			newEnv.symbolTable.add(copiedScope);
		}
		
		/*
		try {
			System.err.println("\nFINE clode  "+hashEffect(newEnv.lookup("x").getVarEffectList()) );
			System.err.println("FINE clode  "+hashEffect(newEnv.lookup("y").getVarEffectList()) );
		} catch (MissingDecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
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
	
	
}

