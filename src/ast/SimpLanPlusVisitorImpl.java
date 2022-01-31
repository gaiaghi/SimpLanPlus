package ast;

import java.util.ArrayList;

import parser.SimpLanPlusBaseVisitor;
import parser.SimpLanPlusParser;
import parser.SimpLanPlusParser.DeclarationContext;
import parser.SimpLanPlusParser.ExpContext;
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
	public Node visitDecFun(SimpLanPlusParser.DecFunContext ctx) {
		//TODO
		return null;
	}
	
	
	@Override
	public Node visitDecVar (SimpLanPlusParser.DecVarContext ctx) {
//		decVar      : type ID ('=' exp)? ';' ;
		
		Node type = visit(ctx.type());
		Node exp = visit(ctx.exp());
		Node id = new IdNode(ctx.ID().getText());
		
		return new DecVarNode(type, id, exp);
		
	}
	
	
	@Override 
	public Node visitType(SimpLanPlusParser.TypeContext ctx) { 
		//type        : 'int'
		//			  | 'bool'
    	//			  | '^' type ;
		
		if( ctx.getText().compareTo("int") == 0 )
			return new IntTypeNode();
		
		if( ctx.getText().compareTo("bool") == 0 )
			return new BoolTypeNode();
		
		Node type = new PointerTypeNode(visitType(ctx.type()));
		return type; 
	}
	
	
	@Override 
	public Node visitIte(SimpLanPlusParser.IteContext ctx) { 
		//ite         : 'if' '(' exp ')' statement ('else' statement)?;
		Node cond = visit(ctx.exp());
		Node thenStm = visit(ctx.statement(0));
		Node elseStm = visit(ctx.statement(1));
		
		return new IteNode(cond, thenStm, elseStm);
	}
	
	
	@Override 
	public CallNode visitCall(SimpLanPlusParser.CallContext ctx) { 
		//call        : ID '(' (exp(',' exp)*)? ')';
		
		//this corresponds to a function invocation
				
		//get the invocation arguments
		ArrayList<Node> args = new ArrayList<Node>();
		
		for(ExpContext exp : ctx.exp())
			args.add(visit(exp));
		
		//POSSO CANCELLARE TUTTA QUESTA PARTE COMMENTATA?
		// IN SimpLanPlus C'è LA "print"
		//especial check for stdlib func
		//this is WRONG, THIS SHOULD BE DONE IN A DIFFERENT WAY
		//JUST IMAGINE THERE ARE 800 stdlib functions...
		/*if(ctx.ID().getText().equals("print"))
			res = new PrintNode(args.get(0));
		
		else*/
		//instantiate the invocation
		CallNode res = new CallNode(new IdNode(ctx.ID().getText()), args);
		
		return res;
	}
	
	
	@Override 
	public Node visitBaseExp(SimpLanPlusParser.BaseExpContext ctx) { 
		//exp	    : '(' exp ')'
		Node baseExp = visit(ctx.exp());
		return new BaseExpNode(baseExp); 
	}
	
	
	@Override 
	public Node visitNegExp(SimpLanPlusParser.NegExpContext ctx) { 
		//exp	    : '-' exp
		Node negExp = visit(ctx.exp());
		return new NegExpNode(negExp); 
	}
	
	
	@Override 
	public Node visitNotExp(SimpLanPlusParser.NotExpContext ctx) { 
		//exp	    : '!' exp
		Node notExp = visit(ctx.exp());
		return new NotExpNode(notExp);  
	}
	
	
	@Override 
	public Node visitDerExp(SimpLanPlusParser.DerExpContext ctx) { 
		//exp	    : lhs
		//bisogna prima definire visitLhs
		//TODO
		return visitChildren(ctx); 
	}
	
	
	@Override 
	public Node visitNewExp(SimpLanPlusParser.NewExpContext ctx) { 
		//exp	    : new type
		Node type = visitType(ctx.type());
		return new NewExpNode(type);
	}
	
	
	@Override 
	public Node visitBinExp(SimpLanPlusParser.BinExpContext ctx) { 
		/* exp	    :
	  	    | left=exp op=('*' | '/')               right=exp   
	    	| left=exp op=('+' | '-')               right=exp   
		    | left=exp op=('<' | '<=' | '>' | '>=') right=exp   
		    | left=exp op=('=='| '!=')              right=exp   
		    | left=exp op='&&'                      right=exp   
		    | left=exp op='||'                      right=exp
		*/
		Node leftExp = visit(ctx.left);
		Node rightExp = visit(ctx.right);
		String op = ctx.op.getText();
		
		return new BinExpNode(leftExp, rightExp, op); 
	}
	
	
	@Override 
	public Node visitCallExp(SimpLanPlusParser.CallExpContext ctx) { 
		//exp	    : call	
		CallNode call = visitCall(ctx.call());
		return new CallExpNode(call);
	}
	
	
	@Override 
	public Node visitBoolExp(SimpLanPlusParser.BoolExpContext ctx) { 
		//exp	    : BOOL
		return new BoolNode(Boolean.parseBoolean(ctx.getText())); 
	}
	
	
	@Override 
	public Node visitValExp(SimpLanPlusParser.ValExpContext ctx) { 
		//exp	    : NUMBER
		return new NumberNode(Integer.parseInt(ctx.getText())); 
	}
	
	
	
}
