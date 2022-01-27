package ast;

import java.util.ArrayList;

import parser.SimpLanPlusBaseVisitor;
import parser.SimpLanPlusParser;
import parser.SimpLanPlusParser.DeclarationContext;
import parser.SimpLanPlusParser.StatementContext;

public class SimpLanPlusVisitorImpl extends SimpLanPlusBaseVisitor<Node> {


	//il visitor attraversa il parse tree chiamando il metodo visit() 
	//(di AbstractParseTreeVisitor) sui nodi figli.
	
	
	//context ctx: contesto che contiene tutte le informazioni di una frase riconosciuta dal parser.
	//conosce il token di inizio e di fine della frase, e permette l'accesso ad ogni elemento.
	@Override
	public Node visitBlock(SimpLanPlusParser.BlockContext ctx) {
//		grammar rule:
//		block	    : '{' declaration* statement* '}';

		ArrayList<Node> declarations = new ArrayList<>();
		ArrayList<Node> statements = new ArrayList<>();
		
		//visita e aggiunta delle dichiarazioni
		for (DeclarationContext dec : ctx.declaration()) {
			declarations.add( visit(dec) );
			
			//dove ctx.declaration() è un metodo del contesto della regola block.
			//Ogni contesto contiene un metodo per ogni nodo/elemento della grammatica presente
			//nella regola in esame. in questo caso: declaration() e statement()
		}
		
		//visita e aggiunta degli statements
		for (StatementContext stm : ctx.statement()) {
			statements.add( visit(stm) );
		}
				
		return new BlockNode(declarations, statements);
	}
	
	@Override 
	public Node visitStatement(SimpLanPlusParser.StatementContext ctx) {
		//TODO
		return null; 
	}
	
	@Override
	public Node visitDeclaration(SimpLanPlusParser.DeclarationContext ctx) {
		//TODO
		return null;
	}
	
	@Override 
	public Node visitDecFun(SimpLanPlusParser.DecFunContext ctx) {
		//TODO
		return null;
	}
	
	public Node visitDecVar (SimpLanPlusParser.DecVarContext ctx) {
//		decVar      : type ID ('=' exp)? ';' ;
		
		Node type = visit(ctx.type());
		Node exp = visit(ctx.exp());
		Node id = new IdNode(ctx.ID().getText());
		
		return new DecVarNode(type, id, exp);
		
	}
	 
}
