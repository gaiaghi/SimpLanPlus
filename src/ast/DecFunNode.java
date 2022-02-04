package ast;

import java.util.ArrayList;
import java.util.HashMap;

import exception.MultipleDecException;
import util.Environment;
import util.STEntry;
import util.SemanticError;

public class DecFunNode implements Node {
//   decFun	    : (type | 'void') ID '(' (arg (',' arg)*)? ')' block ;

	private Node type;
	private IdNode id;
	private ArrayList<Node> args;
	private BlockNode block;
	
	public DecFunNode(Node type, IdNode id, ArrayList<Node> args, BlockNode block) {
		this.type = type;
		this.id = id;
		this.args = args;
		this.block = block;		
	}
	
	@Override
	public String toPrint(String indent) {
		String argList = "";
		if( args.size() > 0 )
		{
			//argList = "\n";
			for (int i = 0; i<  this.args.size(); i++)
				argList = argList +"\n" +this.args.get(i).toPrint(indent +"  ");
		}
		
		String dec = "";
		if( block.getDeclarationsSize() > 0 )
		{
			//dec = "\n";
			ArrayList<Node> decs = block.getDeclarations();
			for (int i = 0; i<  block.getDeclarationsSize(); i++)
				dec = dec + "\n" + decs.get(i).toPrint(indent +"  ");
		}
		
		String body = "";
		if( block.getStatementsSize() > 0 )
		{
			//body = "\n";
			ArrayList<Node> stms = block.getStatements();
			for (int i = 0; i<  block.getStatementsSize(); i++)
				body = body + "\n" + stms.get(i).toPrint(indent +"  ");
		}
		
		return indent +"Fun: " +id.getId() +"\n" 
			+type.toPrint(indent +"  ") +argList +dec +body;
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
  
		//TODO env.offset = -2;
  		//PROF: STentry entry = new STentry(env.nestingLevel, env.offset--);
		//dovo decrementare l'offset dopo aver creato una nuova entry?
  		//controlla offset passato come parametro
		STEntry entry = new STEntry(env.getNestingLevel(), env.getOffset()); 
        env.updateOffset(); //decremento offset
  
        try {
        	env.addEntry(id.getId(), entry);
        	id.setSTEntry(entry);
        	
        	env.addScope();
        	
        	//aggiungo la dichiarazione della fun anche nel nuovo
        	//scope per evitare che vengano dichiarate altre funzioni
        	//o variabili con lo stesso nome
        	env.addEntry(id.getId(), entry);
        	
        	ArrayList<Node> parTypes = new ArrayList<Node>();
			int paroffset=1;
			//check args
			for(Node a : args){
				ArgNode arg = (ArgNode) a;
				parTypes.add(arg.getType());
				try {
					STEntry parEntry = new STEntry(env.getNestingLevel(), arg.getType(), paroffset++);
					env.addEntry(arg.getId().getId(), parEntry);
					arg.getId().setSTEntry(parEntry);
				}catch(MultipleDecException e) {
					res.add(new SemanticError("Parameter id "+arg.getId().getId()+" already declared"));
				}		
			}
        	
			//set func type
			entry.setType( new ArrowTypeNode(parTypes, type) );
			
			//check semantics in the dec list
			if(block.getDeclarationsSize() > 0){
				//TODO env.offset = -2;
				for(Node n : block.getDeclarations())
					res.addAll(n.checkSemantics(env));
			}
	 
			//check body
			//res.addAll(((Node) block.getStatements()).checkSemantics(env));
			for(Node n : block.getStatements())
				res.addAll(n.checkSemantics(env));
	  
			//close scope
			env.removeScope();
			
        }catch(MultipleDecException e) {
        	res.add(new SemanticError("Fun id "+id.getId() +" already declared"));
        }
        
        return res;		
	}

		
	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
