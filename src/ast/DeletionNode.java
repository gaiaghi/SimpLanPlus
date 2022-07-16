package ast;

import java.util.ArrayList;
import java.util.List;

import exception.MissingDecException;
import exception.TypeErrorException;
import util.Effect;
import util.Environment;
import util.STEntry;
import util.SemanticError;

public class DeletionNode implements Node{

	// grammar rule:
	// deletion    : 'delete' ID;
	
	private IdNode id;

	public DeletionNode(IdNode id) {
		this.id = id;
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "Delete: \n" + this.id.toPrint(indent + "  ");
	}

	@Override
	public String toString() {
		return this.toPrint("");
	}
	
	@Override
	public Node typeCheck() throws TypeErrorException{
		if (!(id.typeCheck() instanceof PointerTypeNode))
			throw new TypeErrorException("cannot delete the non pointer variable " + id.getId());
		
		return null;
	}

	@Override
	public String codeGeneration() {
		id.setDeletionNode(true);
		String code = id.codeGeneration();
		code = code + "del $a0\n";
		return code;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return id.checkSemantics(env);
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		
		ArrayList<SemanticError> res = new ArrayList<>();
		
		res.addAll(id.checkEffects(env));
		
		/*int derNumDec = id.getDerNumDec();
		Effect seqEffect = Effect.seq(id.getSTEntry().getVarEffect(derNumDec), Effect.DELETED);
		id.getSTEntry().getVarEffect(derNumDec).setEffect(seqEffect);
		
		
		if (id.getSTEntry().getVarEffect(derNumDec).equals(Effect.ERROR) )
			res.add(new SemanticError("Variable "+ id.getId() +" was already deleted.  Deletion"));
		*/
		
		try {
			STEntry idEntry = env.lookup(id.getId()); 
			int derNumDec = id.getDerNumDec();
			
			
			System.out.println("\n\n\nPRIMA\nDELETE "+id.getId() + hashEffect(idEntry.getVarEffectList()) );
			System.out.println("x " + hashEffect(env.lookup("x").getVarEffectList()));
			
			
			
			Effect seqEffect = Effect.seq(idEntry.getVarEffect(derNumDec), Effect.DELETED);
			idEntry.getVarEffect(derNumDec).setEffect(seqEffect);
			
			if ( idEntry.getVarEffect(derNumDec).equals(Effect.ERROR) )
				res.add(new SemanticError("Variable " + id.getId() + " was already deleted."));
			
			System.out.println("DOPO\nDELETE "+id.getId() + hashEffect(idEntry.getVarEffectList()) );
			System.out.println("x " + hashEffect(env.lookup("x").getVarEffectList()));
		
			id.setSTEntry(new STEntry( env.lookup(id.getId())) );
			
			
			
			System.out.println("FINE\nDELETE "+id.getId() + hashEffect(idEntry.getVarEffectList()) );
			System.out.println("x " + hashEffect(env.lookup("x").getVarEffectList())+"\n\n");
			
		} catch (MissingDecException e1) {
			res.add(new SemanticError("MissingDecException: " + id.getId()));
		}
	
		return res;
	}

	private String hashEffect(List<Effect> list) {
		String str="[";
		for(Effect e : list)
			str = str + e + ",";
		str=str+"]        [";
		for(Effect e : list)
			str = str + e.hashCode() + ",";
		return str+"]";
	}
	
	
	
}
