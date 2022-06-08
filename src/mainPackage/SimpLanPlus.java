package mainPackage;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import ast.Node;
import ast.SVMVisitorImpl;
import ast.SimpLanPlusVisitorImpl;
import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
import parser.SimpLanPlusLexer;
import parser.SimpLanPlusParser;
import util.Environment;
import util.STEntry;
import util.SemanticError;
import parser.SPPErrorListener;
import svm.SVMLexer;
import svm.SVMParser;


public class SimpLanPlus{
	public static void main(String[] args) {

		//String fileName = args[0];
		String fileName = "prova.simplanplus";
		
		CharStream inputCode = null;
		
		//open code file
		try {
			inputCode = CharStreams.fromFileName(fileName);
		} 
		catch (IOException e) {
			System.err.println("The file " + fileName + " was not found");
			System.exit(1);
		}
		
//		//SimpLanPlus lexer
		SimpLanPlusLexer lexer = new SimpLanPlusLexer(inputCode);
		
		//SimpLanPlus parser
		SimpLanPlusParser parser = new SimpLanPlusParser(new CommonTokenStream(lexer));
		parser.removeErrorListeners();
		SPPErrorListener listener = new SPPErrorListener();
		try {
			parser.addErrorListener(listener);
		}
		catch (NullPointerException e) {
			System.err.println("Error in addErrorListener: " +e.getMessage());
			System.exit(1);
		}
		
		
		//Tree visitor
		SimpLanPlusVisitorImpl visitor = new SimpLanPlusVisitorImpl();
		Node ast = null;
		try {
			ast = visitor.visit(parser.block()); 
		}catch(Exception e) {
			System.err.println("Codice esempio in input non trovato");
			System.err.println("Error in visitor.visit(...): " +e.getMessage());
			System.exit(1);
		}
		
		//checking lexical errors
		if (lexer.errorCount() > 0) {
			ArrayList lxErrors = lexer.getErrors();
			for (int i=0; i<lexer.errorCount(); i++)
				System.err.println(lxErrors.get(i));
		
            System.err.println("There are (" +lexer.errorCount()+") lexical errors in the file. Impossible to compile");
            
            System.exit(1);
        } 

		
		//checking syntactical errors
		if(parser.getNumberOfSyntaxErrors()>0) {
			ArrayList syErrors = listener.getErrors();
			for (int i=0; i<syErrors.size(); i++)
				System.err.println(syErrors.get(i));
		
			System.err.println("There are (" + parser.getNumberOfSyntaxErrors() +") syntax errors in the file. Impossible to compile.");
            System.exit(1); 
		}
		
		
			
		//checking semantic errors
		Environment env = new Environment();	
		ArrayList<SemanticError> err = new ArrayList();
		err = ast.checkSemantics(env);
		
		if(err.size()>0){
			System.err.println("You had: " +err.size()+" semantic errors:");
			for(SemanticError e : err)
				System.err.println("\t" + e);
			System.exit(1);
		}
		
		
		//type-checking bottom-up 
		try {
			Node type = ast.typeCheck(); 
			if( type == null )
				System.out.println("Type checking ok! Type of the program is: NULL");
			else
				System.out.println(type.toPrint("Type checking ok! Type of the program is: "));
		}catch(TypeErrorException e){
			System.err.println("Type error: " +e.getMessage());
			System.exit(1);
		}
	
		
		
		//checking effect errors
		ArrayList<SemanticError> effectsErrors = ast.checkEffects(env);
		if(effectsErrors.size()>0){
            System.err.println("There are " +effectsErrors.size()+ " errors from the effects analysis:");
            
           for(SemanticError e : effectsErrors)
			System.err.println("\t" + e);
			System.exit(1);
        }

		
		System.out.println("Visualizing AST...");
		System.out.println(ast.toPrint(""));
		
		
		// CODE GENERATION  prova.SimpLan.asm
		String code=ast.codeGeneration(); 
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(fileName+".asm"));
			out.write(code);
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		 
		System.out.println("Code generated! Assembling and running generated code.");
		/*
		//FileInputStream isASM = new FileInputStream(fileName+".asm");
		CharStream isASM = null;
		try {
			isASM = CharStreams.fromFileName(fileName+".asm");
		} 
		catch (IOException e) {
			System.err.println("The file " + fileName + " was not found");
			System.exit(1);
		}
		SVMLexer lexerASM = new SVMLexer(isASM);
		CommonTokenStream tokensASM = new CommonTokenStream(lexerASM);
		SVMParser parserASM = new SVMParser(tokensASM);
		
		SVMVisitorImpl visitorSVM = new SVMVisitorImpl();
		visitorSVM.visit(parserASM.assembly()); 

		System.out.println("You had: "+lexerASM.errorCount()+" lexical errors and "+parserASM.getNumberOfSyntaxErrors()+" syntax errors.");
		if (lexerASM.errorCount()>0 || parserASM.getNumberOfSyntaxErrors()>0) System.exit(1);
		*/
		
	}
	
}