package ast;

import java.util.ArrayList;
import java.util.HashMap;

import interpreter.ExecuteVM;
import svm.SVMLexer;
import svm.SVMParser;
import svm.SVMBaseVisitor;
import util.Instruction;

public class SVMVisitorImpl extends SVMBaseVisitor<Void> {
 
	public ArrayList<Instruction> code = new ArrayList<Instruction>();
    private int i = 0;
    private HashMap<String,Integer> labelAdd = new HashMap<String,Integer>(); 
    private HashMap<Integer,String> labelRef = new HashMap<Integer,String>();
    
    @Override 
    public Void visitAssembly(SVMParser.AssemblyContext ctx) { 
    	visitChildren(ctx);
    	for (Integer refAdd: labelRef.keySet()) {
    		//TODO
//            code[refAdd]=labelAdd.get(labelRef.get(refAdd));
    	}
    	return null;
    }
    
    @Override 
    public Void visitInstruction(SVMParser.InstructionContext ctx) { 
    	switch (ctx.getStart().getType()) {
			case SVMLexer.PUSH:		
				code.add(new Instruction(SVMParser.PUSH, ctx.reg.getText()));
				break;
				
			case SVMLexer.POP:
				code.add(new Instruction(SVMParser.POP));
				break;
				
			case SVMLexer.ADD:
				code.add(new Instruction(SVMParser.ADD, ctx.res.getText(), ctx.term1.getText(), ctx.term2.getText()));
				break;
				
			case SVMLexer.SUB:
				code.add(new Instruction(SVMParser.SUB, ctx.res.getText(), ctx.term1.getText(), ctx.term2.getText()));
				break;
				
			case SVMLexer.MULT:
				code.add(new Instruction(SVMParser.MULT, ctx.res.getText(), ctx.term1.getText(), ctx.term2.getText()));
				break;
				
			case SVMLexer.DIV:
				code.add(new Instruction(SVMParser.DIV, ctx.res.getText(), ctx.term1.getText(), ctx.term2.getText()));
				break;
				
			case SVMLexer.ADDI:
				code.add(new Instruction(SVMParser.ADDI, ctx.res.getText(), ctx.term1.getText(), ctx.term2.getText()));
				break;
				
			case SVMLexer.SUBI:
				code.add(new Instruction(SVMParser.SUBI, ctx.res.getText(), ctx.term1.getText(), ctx.term2.getText()));
				break;
				
			case SVMLexer.MULTI:
				code.add(new Instruction(SVMParser.MULTI, ctx.res.getText(), ctx.term1.getText(), ctx.term2.getText()));
				break;
				
			case SVMLexer.DIVI:
				code.add(new Instruction(SVMParser.DIVI, ctx.res.getText(), ctx.term1.getText(), ctx.term2.getText()));
				break;
				
			case SVMLexer.LI:
				code.add(new Instruction(SVMParser.LI, ctx.res.getText(), ctx.term.getText()));
				break;
				
			case SVMLexer.LB:
				code.add(new Instruction(SVMParser.LB, ctx.res.getText(), ctx.term.getText()));
				break;
				
			case SVMLexer.STOREW:
				code.add(new Instruction(SVMParser.STOREW, ctx.value.getText(), Integer.parseInt(ctx.offset.getText()) ) );
				break;
				
			case SVMLexer.LOADW:
				code.add(new Instruction(SVMParser.LOADW, ctx.value.getText(), Integer.parseInt(ctx.offset.getText()) ) );
				break;
				
			case SVMLexer.LABEL:
				labelAdd.put(ctx.l.getText(),i);
				break;
				
			case SVMLexer.BRANCH:
				code.add(new Instruction(SVMParser.BRANCH, ctx.l.getText()));
                labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
				break;
				
			case SVMLexer.BRANCHEQ:
				code.add(new Instruction(SVMParser.BRANCHEQ, ctx.term1.getText(), ctx.term2.getText(), ctx.l.getText()));
                labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
                break;
                
			case SVMLexer.BRANCHLESSEQ:
				code.add(new Instruction(SVMParser.BRANCHEQ, ctx.term1.getText(), ctx.term2.getText(), ctx.l.getText()));
                labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
                break;
                
			case SVMLexer.JR:
				code.add(new Instruction(SVMParser.JR, ctx.reg.getText()));
				break;
				
			case SVMLexer.JAL:
				code.add(new Instruction(SVMParser.JAL, ctx.l.getText()));
				labelRef.put(i++, ctx.l.getText());
				break;
				
			case SVMLexer.MOVE:
				code.add(new Instruction(SVMParser.MOVE, ctx.to.getText(), ctx.from.getText()));
				break;
				
			case SVMLexer.PRINT:
				code.add(new Instruction(SVMParser.PRINT, ctx.reg.getText()));
				break;
				
			case SVMLexer.DELETION:
				code.add(new Instruction(SVMParser.DELETION, ctx.reg.getText()));
				break;
				
			case SVMLexer.AND:
				code.add(new Instruction(SVMParser.AND, ctx.res.getText(), ctx.term1.getText(), ctx.term2.getText()));
				break;
				
			case SVMLexer.OR:
				code.add(new Instruction(SVMParser.OR, ctx.res.getText(), ctx.term1.getText(), ctx.term2.getText()));
				break;
				
			case SVMLexer.NOT:
				code.add(new Instruction(SVMParser.NOT, ctx.res.getText(), ctx.term1.getText()));
				break;
				
			case SVMLexer.ANDB:
				code.add(new Instruction(SVMParser.ANDB, ctx.res.getText(), ctx.term1.getText(), ctx.term2.getText()));
				break;
				
			case SVMLexer.ORB:
				code.add(new Instruction(SVMParser.ORB, ctx.res.getText(), ctx.term1.getText(), ctx.term2.getText()));
				break;
				
			case SVMLexer.NOTB:
				code.add(new Instruction(SVMParser.NOTB, ctx.res.getText(), ctx.term1.getText()));
				break;
				
			case SVMLexer.HALT:
				code.add(new Instruction(SVMParser.HALT));
				break;
				
			default:
	            break;	// Invalid instruction
    	}
    	return null;
    }

	
}
