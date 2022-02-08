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
	public BlockNode visitBlock(SimpLanPlusParser.BlockContext ctx) {
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
	public Node visitAssignmentL(SimpLanPlusParser.AssignmentLContext ctx) {
//		grammar rule:
//		statement   : assignment ';'	#assignmentL

		return new AssignmentLNode(visitAssignment(ctx.assignment()));
	}
	
	
	@Override
	public Node visitDeletionL(SimpLanPlusParser.DeletionLContext ctx) {
//		grammar rule:
//		statement   : deletion ';'			#deletionL
		
		return new DeletionLNode(visitDeletion(ctx.deletion()));
		
	} 
	
	@Override
	public Node visitPrintL(SimpLanPlusParser.PrintLContext ctx) {
//		grammar rule:
//		statement   : 	print ';'				#printL

		return new PrintLNode(visitPrint(ctx.print()));
	}
	
	@Override
	public Node visitRetL(SimpLanPlusParser.RetLContext ctx) {
//		grammar rule:
//		statement   : ret ';'				#retL
		return new RetLNode(visitRet(ctx.ret()));
	}
	
	@Override
	public Node visitIteL(SimpLanPlusParser.IteLContext ctx) {
//		grammar rule:
//		statement   :	ite					#iteL
		return new IteLNode(visitIte(ctx.ite()));
	}
	
	@Override
	public Node visitCallL(SimpLanPlusParser.CallLContext ctx) {
//		grammar rule:
//		statement   :	call ';'				#callL
		return new CallLNode(visitCall(ctx.call()));
	}
	
	@Override
	public Node visitBlockL(SimpLanPlusParser.BlockLContext ctx) {
//		grammar rule:
//		statement   :	block					#blockL;
		return new BlockLNode(visitBlock(ctx.block()));
	}
	
	@Override 
	public Node visitDecFunL(SimpLanPlusParser.DecFunLContext ctx) {
//		grammar rule:
//		declaration : decFun			#decFunL
		return new DecFunLNode(visitDecFun(ctx.decFun()));
	}
	
	@Override 
	public Node visitDecVarL(SimpLanPlusParser.DecVarLContext ctx) {
//		grammar rule:
//		declaration:	decVar 			#decVarL;
		return new DecVarLNode(visitDecVar(ctx.decVar()));
	}
	
	
	@Override
	public Node visitDecFun(SimpLanPlusParser.DecFunContext ctx) {
//		grammar rule:
//		decFun	    : (type | 'void') ID '(' (arg (',' arg)*)? ')' block ;
		
		Node type;
		if( ctx.type() != null )
			type = visit(ctx.type());
		else
			type = new VoidTypeNode();
		IdNode id = new IdNode(ctx.ID().getText());
		
		ArrayList<Node> args = new ArrayList<>();
		for (SimpLanPlusParser.ArgContext arg : ctx.arg()) {
			args.add( visit(arg) );
		}
		
		BlockNode block = visitBlock(ctx.block());
		
		return new DecFunNode(type, id, args, block);
	}
	
	@Override
	public Node visitDecVar (SimpLanPlusParser.DecVarContext ctx) {
//		grammar rule:
//		decVar      : type ID ('=' exp)? ';' ;
		
		Node type = visit(ctx.type());
		Node exp = visit(ctx.exp());
		IdNode id = new IdNode(ctx.ID().getText());
		
		return new DecVarNode(type, id, exp);
		
	}

	 @Override
	 public Node visitArg(SimpLanPlusParser.ArgContext ctx) {
//		 grammar rule:
//		 arg         : type ID;
		 Node type = visit(ctx.type());
		 IdNode id = new IdNode(ctx.ID().getText());
		 
		 return new ArgNode(type, id);
			 
	 }
	 
	 @Override 
	 public Node visitAssignment(SimpLanPlusParser.AssignmentContext ctx){
//		 grammar rule:
//		 assignment  : lhs '=' exp ;
		 
		 Node exp = visit(ctx.exp());
		 Node lhs = visit(ctx.lhs());
		 
		 return new AssignmentNode(lhs,exp);
	 }
	 
	 @Override 
	 public LhsNode visitLhs(SimpLanPlusParser.LhsContext ctx) {
//		grammar rule:
//		lhs         : ID | lhs '^' ;
		 
		 //puntatore
		 if (ctx.lhs() != null) {
			 LhsNode lhs = visitLhs(ctx.lhs());
			 return new LhsNode(lhs.getId(), lhs);
		 }
		 //id
		 else {
			 IdNode id = new IdNode(ctx.ID().getText());
			 return new LhsNode(id, null);
		 }
		 
	 }
	 
	 @Override
	 public Node visitDeletion(SimpLanPlusParser.DeletionContext ctx) {
//		 grammar rule:
//		 deletion    : 'delete' ID;
		 IdNode id = new IdNode(ctx.ID().getText());
		 return new DeletionNode(id);
	 }
	 
	 @Override 
	 public Node visitPrint(SimpLanPlusParser.PrintContext ctx) {
//		 grammar rule:
//		 print	    : 'print' exp;
		 return new PrintNode(visit(ctx.exp()));
	 }
	 
	 @Override
	 public Node visitRet(SimpLanPlusParser.RetContext ctx) {
//		 grammar rule:
//		 ret	    : 'return' (exp)?;
		 return new RetNode(visit(ctx.exp()));
	 }
	
	
	
	
	@Override 
	public Node visitType(SimpLanPlusParser.TypeContext ctx) { 
		//grammar rule:
		//grammar rule:
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
		//grammar rule:
		//ite         : 'if' '(' exp ')' statement ('else' statement)?;
		Node cond = visit(ctx.exp());
		Node thenStm = visit(ctx.statement(0));
		Node elseStm = visit(ctx.statement(1));
		
		return new IteNode(cond, thenStm, elseStm);
	}
	
	
	@Override 
	public CallNode visitCall(SimpLanPlusParser.CallContext ctx) { 
		//grammar rule:
		//call        : ID '(' (exp(',' exp)*)? ')';
		
		//this corresponds to a function invocation
				
		//get the invocation arguments
		ArrayList<Node> args = new ArrayList<Node>();
		
		for(ExpContext exp : ctx.exp())
			args.add(visit(exp));
		
		//instantiate the invocation
		CallNode res = new CallNode(new IdNode(ctx.ID().getText()), args);
		
		return res;
	}
	
	
	@Override 
	public Node visitBaseExp(SimpLanPlusParser.BaseExpContext ctx) { 
		//grammar rule:
		//exp	    : '(' exp ')'
		Node baseExp = visit(ctx.exp());
		return new BaseExpNode(baseExp); 
	}
	
	
	@Override 
	public Node visitNegExp(SimpLanPlusParser.NegExpContext ctx) { 
		//grammar rule:
		//exp	    : '-' exp
		Node negExp = visit(ctx.exp());
		return new NegExpNode(negExp); 
	}
	
	
	@Override 
	public Node visitNotExp(SimpLanPlusParser.NotExpContext ctx) { 
		//grammar rule:
		//exp	    : '!' exp
		Node notExp = visit(ctx.exp());
		return new NotExpNode(notExp);  
	}
	
	
	@Override 
	public Node visitDerExp(SimpLanPlusParser.DerExpContext ctx) { 
		//grammar rule:
		//exp	    : lhs
		LhsNode lhs = visitLhs(ctx.lhs());
		return new DerExpNode(lhs); 
	}
	
	
	@Override 
	public Node visitNewExp(SimpLanPlusParser.NewExpContext ctx) { 
		//grammar rule:
		//exp	    : new type
		Node type = visitType(ctx.type());
		return new NewExpNode(type);
	}
	
	
	@Override 
	public Node visitBinExp(SimpLanPlusParser.BinExpContext ctx) { 
		/*grammar rule:
		  exp	    :
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
		//grammar rule:
		//exp	    : call	
		CallNode call = visitCall(ctx.call());
		return new CallExpNode(call);
	}
	
	
	@Override 
	public Node visitBoolExp(SimpLanPlusParser.BoolExpContext ctx) { 
		//grammar rule:
		//exp	    : BOOL
		return new BoolNode(Boolean.parseBoolean(ctx.getText())); 
	}
	
	
	@Override 
	public Node visitValExp(SimpLanPlusParser.ValExpContext ctx) { 
		//grammar rule:
		//exp	    : NUMBER
		return new NumberNode(Integer.parseInt(ctx.getText())); 
	}
	
	
	
}
