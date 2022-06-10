package ast;

import java.util.ArrayList;

import exception.MissingDecException;
import exception.TypeErrorException;
import util.Effect;
import util.Environment;
import util.STEntry;
import util.SemanticError;

public class DeletionNode implements Node{

	private IdNode id;

	public DeletionNode(IdNode id) {
		this.id = id;
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "Delete: \n" + this.id.toPrint(indent +"  ");
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
			
		// si mettono a delete tutti gli oggetti della catena di puntatori
		for (int i = 0; i <= id.getDerNumDec(); i++) {
//			id.getSTEntry().setVarEffect(i, Effect.seq(id.getSTEntry().getVarEffect(i), Effect.DELETED));

			Effect seqEffect = Effect.seq(id.getSTEntry().getVarEffect(i), Effect.DELETED);
			id.getSTEntry().getVarEffect(i).setEffect(seqEffect);
		}
		
		if (id.getSTEntry().getVarEffect(0).equals(Effect.ERROR) )
			res.add(new SemanticError("Variable "+ id.getId() +" was already deleted.  Deletion"));
		
		
		return res;
	}

}
