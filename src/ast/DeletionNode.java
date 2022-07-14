package ast;

import java.util.ArrayList;

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
		try {
			STEntry idEntry = env.lookup(id.getId()); 
			int derNumDec = id.getDerNumDec();
			Effect seqEffect = Effect.seq(idEntry.getVarEffect(derNumDec), Effect.DELETED);
			idEntry.getVarEffect(derNumDec).setEffect(seqEffect);
			
			
			if ( idEntry.getVarEffect(derNumDec).equals(Effect.ERROR) )
				res.add(new SemanticError("Variable " + id.getId() + " was already deleted."));
		
			id.setSTEntry(new STEntry( env.lookup(id.getId())) );
		} catch (MissingDecException e1) {
			res.add(new SemanticError("MissingDecException: " + id.getId()));
		}
		
		return res;
	}

}
