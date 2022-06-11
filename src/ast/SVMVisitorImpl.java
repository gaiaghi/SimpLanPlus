package ast;

import java.util.HashMap;

import interpreter.ExecuteVM;
import svm.SVMLexer;
import svm.SVMParser;
import svm.SVMBaseVisitor;

public class SVMVisitorImpl extends SVMBaseVisitor<Void> {

	public int[] code = new int[ExecuteVM.CODESIZE];    
    private int i = 0;
    private HashMap<String,Integer> labelAdd = new HashMap<String,Integer>(); 
    private HashMap<Integer,String> labelRef = new HashMap<Integer,String>();
    
    @Override 
    public Void visitAssembly(SVMParser.AssemblyContext ctx) { 
    	visitChildren(ctx);
    	for (Integer refAdd: labelRef.keySet()) {
            code[refAdd]=labelAdd.get(labelRef.get(refAdd));
    	}
    	return null;
    }
    
    @Override 
    public Void visitInstruction(SVMParser.InstructionContext ctx) { 
    	switch (ctx.getStart().getType()) {
			case SVMLexer.PUSH:		
				code[i++] = SVMParser.PUSH; 
	            code[i++] = Integer.parseInt(ctx.reg.getText());
				break;
			case SVMLexer.POP:
				code[i++] = SVMParser.POP;
				break;
			case SVMLexer.ADD:
				code[i++] = SVMParser.ADD;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				code[i++] = Integer.parseInt(ctx.term2.getText());
				break;
			case SVMLexer.SUB:
				code[i++] = SVMParser.SUB;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				code[i++] = Integer.parseInt(ctx.term2.getText());
				break;
			case SVMLexer.MULT:
				code[i++] = SVMParser.MULT;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				code[i++] = Integer.parseInt(ctx.term2.getText());
				break;
			case SVMLexer.DIV:
				code[i++] = SVMParser.DIV;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				code[i++] = Integer.parseInt(ctx.term2.getText());
				break;
			case SVMLexer.ADDI:
				code[i++] = SVMParser.ADDI;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				code[i++] = Integer.parseInt(ctx.term2.getText());
				break;
			case SVMLexer.SUBI:
				code[i++] = SVMParser.SUBI;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				code[i++] = Integer.parseInt(ctx.term2.getText());
				break;
			case SVMLexer.MULTI:
				code[i++] = SVMParser.MULTI;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				code[i++] = Integer.parseInt(ctx.term2.getText());
				break;
			case SVMLexer.DIVI:
				code[i++] = SVMParser.DIVI;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				code[i++] = Integer.parseInt(ctx.term2.getText());
				break;
			case SVMLexer.LI:
				code[i++] = SVMParser.LI;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term.getText());
				break;
			case SVMLexer.LB:
				code[i++] = SVMParser.LB;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term.getText());
				break;
			case SVMLexer.STOREW:
				code[i++] = SVMParser.STOREW;
				code[i++] = Integer.parseInt(ctx.value.getText());
				code[i++] = Integer.parseInt(ctx.offset.getText());
				break;
			case SVMLexer.LOADW:
				code[i++] = SVMParser.LOADW;
				code[i++] = Integer.parseInt(ctx.value.getText());
				code[i++] = Integer.parseInt(ctx.offset.getText());
				break;
			case SVMLexer.LABEL:
				labelAdd.put(ctx.l.getText(),i);
				break;
			case SVMLexer.BRANCH:
				code[i++] = SVMParser.BRANCH;
                labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
				break;
			case SVMLexer.BRANCHEQ:
				code[i++] = SVMParser.BRANCHEQ; 
                labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
                break;
			case SVMLexer.BRANCHLESSEQ:
				code[i++] = SVMParser.BRANCHLESSEQ;
				code[i++] = Integer.parseInt(ctx.term1.getText());
				code[i++] = Integer.parseInt(ctx.term2.getText());
                labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
                break;
			case SVMLexer.JR:
				code[i++] = SVMParser.JR;
				code[i++] = Integer.parseInt(ctx.reg.getText());
				break;
			case SVMLexer.JAL:
				code[i++] = SVMParser.JAL;
				labelRef.put(i++, ctx.l.getText());
				break;
			case SVMLexer.MOVE:
				code[i++] = SVMParser.MOVE;
				code[i++] = Integer.parseInt(ctx.to.getText());
				code[i++] = Integer.parseInt(ctx.from.getText());
				break;
			case SVMLexer.PRINT:
				code[i++] = SVMParser.PRINT;
				code[i++] = Integer.parseInt(ctx.reg.getText());
				break;
			case SVMLexer.DELETION:
				code[i++] = SVMParser.DELETION;
				code[i++] = Integer.parseInt(ctx.reg.getText());
				break;
			case SVMLexer.AND:
				code[i++] = SVMParser.AND;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				code[i++] = Integer.parseInt(ctx.term2.getText());
				break;
			case SVMLexer.OR:
				code[i++] = SVMParser.OR;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				code[i++] = Integer.parseInt(ctx.term2.getText());
				break;
			case SVMLexer.NOT:
				code[i++] = SVMParser.NOT;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				break;
			case SVMLexer.ANDB:
				code[i++] = SVMParser.ANDB;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				code[i++] = Integer.parseInt(ctx.term2.getText());
				break;
			case SVMLexer.ORB:
				code[i++] = SVMParser.ORB;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				code[i++] = Integer.parseInt(ctx.term2.getText());
				break;
			case SVMLexer.NOTB:
				code[i++] = SVMParser.NOTB;
				code[i++] = Integer.parseInt(ctx.res.getText());
				code[i++] = Integer.parseInt(ctx.term1.getText());
				break;
			case SVMLexer.HALT:
				code[i++] = SVMParser.HALT;
				break;             
			default:
	            break;	// Invalid instruction
    	}
    	return null;
    }

	
}
