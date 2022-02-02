package ast;

import java.util.ArrayList;

import util.Environment;
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
			argList = "\n";
			for (int i = 0; i<  this.args.size(); i++)
				argList = argList + "\n" + this.args.get(i).toPrint(indent +"  ");
		}
		
		String dec = "";
		if( block.getDeclarationsSize() > 0 )
		{
			dec = "\n";
			ArrayList<Node> decs = block.getDeclarations();
			for (int i = 0; i<  block.getDeclarationsSize(); i++)
				dec = dec + "\n" + decs.get(i).toPrint(indent +"  ");
		}
		
		String body = "";
		if( block.getStatementsSize() > 0 )
		{
			body = "\n";
			ArrayList<Node> stms = block.getStatements();
			for (int i = 0; i<  block.getStatementsSize(); i++)
				body = body + "\n" + stms.get(i).toPrint(indent +"  ");
		}
		
		return indent +"Fun: " +id.getId() +"\n" +type.toPrint(indent +"  ") +argList +dec +body;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
