package mainPackage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import ast.Node;
import ast.SVMVisitorImpl;
import ast.SimpLanPlusVisitorImpl;
import exception.MemoryException;
import exception.SmallCodeAreaCException;
import exception.TypeErrorException;
import interpreter.ExecuteVM;
import parser.SimpLanPlusLexer;
import parser.SimpLanPlusParser;
import svm.SVMLexer;
import svm.SVMParser;
import util.Environment;
import util.SemanticError;
import parser.SLPErrorListener;


public class SimpLanPlus{
	public static void main(String[] args) {
		
		String usageString = "Usage:\n"
	            + "   java -jar exec/SimpLanPlus.jar <inputFileName> [-ast] [-codesize=n1] [-memsize=n2] [-debug]\n"
	            + "\n   where:\n"
	          	+ "      - n1 and n2 are non-zero positive numbers\n"
	          	+ "\n";
		// parametro nome del file di input
		String fileName = null;
		// parametro ast
		boolean printAST = false;
		// parametro booleano per indicare le stampe di debug
	    boolean debug = false;
		// parametro dimensione code area
	    int CODESIZE = 1000;
	    // parametro dimensione other space (stack + heap)
	    int MEMSIZE = 1000;
	       
	    try {
		    switch( args.length ) {
			    case 0:
			    	// il programma viene eseguito senza parametri
			    	System.out.println(usageString);
			    	System.exit(1);
			    	break;
			    	
			    case 1:
			    	// il programma viene eseguito con un solo parametro, 
			    	// dovrebbe essere il nome del file in input
			    	if( checkInputFileName(args[0]) )
			    		fileName = args[0];
			    	else {
			    		System.err.println("Wrong argument: " + args[0] + ".\nThe first argument must be the name of the input file.");
			    		System.out.println("\n" + usageString);
			    		System.exit(1);
			    	}
			    		
			    	break;
			    	
			    default:
			    	// il programma viene eseguito con più di un parametro
			    	if( checkInputFileName(args[0]) )
			    		fileName = args[0];
			    	else {
			    		System.err.println("Wrong argument: " + args[0] + ".\nThe first argument must be the name of the input file.");
			    		System.out.println("\n" + usageString);
			    		System.exit(1);
			    	}
			    	// conta il numero di argomenti errati
				    int counter = 0;
			    	boolean isWrongArg = true;
			    	for( int i = 1; i < args.length; i ++) {
			    		if( args[i].compareTo("-ast") == 0 ) {
			    			printAST = true;
			    			isWrongArg = false;
			    		}
			    		else if( args[i].compareTo("-debug") == 0 ) {
			    			debug = true;
			    			isWrongArg = false;
			    		}
			    		else {
			    			if( args[i].contains("-codesize=") ) {
			    				String[] parSize = args[i].split("=");
			    				if( parSize.length == 2 ) {
			    					try {
			    						CODESIZE = Integer.parseInt(parSize[1]);
			    						if( CODESIZE > 0 )
			    							isWrongArg = false;
			    					}catch(NumberFormatException e) {}
			    				}
			    			}
			    			else if( args[i].contains("-memsize=") ) {
			    				String[] parSize = args[i].split("=");
			    				if( parSize.length == 2 ) {
			    					try {
			    						MEMSIZE = Integer.parseInt(parSize[1]);
			    						if( MEMSIZE > 0 )
			    							isWrongArg = false;
			    					}catch(NumberFormatException e) {}
			    				}
			    			}
			    		}
			    		
			    		if( isWrongArg ) {
			    			counter ++;
			    			System.err.println("Wrong argument: " +args[i]);
			    		}
			    	}
			    	
			    	if( counter != 0 ) {
			    		System.out.println("\n" + usageString);
			    		System.exit(1);
			    	}
			    	break;
		    }
	    }catch(Exception e) {
	    	System.err.println("Error in parsing input arguments: " + e.toString());
	    	System.exit(1);
	    }
	    
	    	
	    //open code file
		CharStream inputCode = null;
		try {
			inputCode = CharStreams.fromFileName(fileName);
		} 
		catch (IOException e) {
			System.err.println("The file '" + fileName + "' was not found.");
			System.exit(1);
		}
		
		//SimpLanPlus lexer
		SimpLanPlusLexer lexer = new SimpLanPlusLexer(inputCode);
		//SimpLanPlus parser
		SimpLanPlusParser parser = new SimpLanPlusParser(new CommonTokenStream(lexer));
		parser.removeErrorListeners();
		SLPErrorListener listener = new SLPErrorListener();
		try {
			parser.addErrorListener(listener);
		}
		catch (NullPointerException e) {
			System.err.println("Error in ErrorListener: " + e.toString());
			System.exit(1);
		}
		
		
		// Tree visitor
		SimpLanPlusVisitorImpl visitor = new SimpLanPlusVisitorImpl();
		Node ast = null;
		try {
			ast = visitor.visit(parser.block()); 
		}catch(Exception e) {
			System.err.println("Error in the contents of the '"+ fileName +"' file.");
			//e.printStackTrace();
			System.exit(1);
		}
		
		// checking lexical errors
		if ( lexer.errorCount() > 0 ) {
			ArrayList lxErrors = lexer.getErrors();
			for (int i = 0; i < lexer.errorCount(); i ++)
				System.err.println(lxErrors.get(i));
		
            System.err.println("There are (" + lexer.errorCount() 
            	+ ") lexical errors in the file. Impossible to compile.");
            System.exit(1);
        } 

		
		// checking syntactical errors
		if( parser.getNumberOfSyntaxErrors() > 0 ) {
			ArrayList<String> syErrors = listener.getErrors();
			for (int i = 0; i < syErrors.size(); i ++)
				System.err.println(syErrors.get(i));
		
			System.err.println("There are (" + parser.getNumberOfSyntaxErrors() 
				+ ") syntax errors in the file. Impossible to compile.");
            System.exit(1); 
		}
		
		
			
		// checking semantic errors
		Environment env = new Environment();
		try {
			ArrayList<SemanticError> err = ast.checkSemantics(env);
			
			if( err.size() > 0 ){
				System.err.println("You had: " + err.size() + " semantic errors:");
				for(SemanticError e : err)
					System.err.println("\t" + e);
				System.exit(1);
			}
		}catch(Exception e){
			System.err.println("Error in semantic analysis: " + e.toString());
			System.exit(1);
		}
		
		
		
		// checking type errors
		try {
			Node type = ast.typeCheck(); 
			System.out.println("Type checking ok!");
		}catch(TypeErrorException e){
			System.err.println("Type error: " + e.getMessage());
			System.exit(1);
		}
		catch(Exception e){
			System.err.println("Error in type analysis: " + e.toString());
			System.exit(1);
		}
	
		
		
		// checking effect errors
		try {
			ArrayList<SemanticError> effectsErrors = ast.checkEffects(env);
			if( effectsErrors.size() > 0 ){
				System.err.println("There are " + effectsErrors.size() 
					+ " errors from the effects analysis:");
	            
				for(SemanticError e : effectsErrors)
					System.err.println("\t" + e);
				
				System.exit(1);
	        }
		}catch(Exception e){
			System.err.println("Error in effects analysis: " + e.toString());
			e.printStackTrace();
			System.exit(1);
		}

		
		// stampa albero di sintassi astratta
		if( printAST ) {
			System.out.println("\nVisualizing AST...");
			System.out.println(ast.toPrint(""));
		}
		
		
		// code generation
		String code = null;
		BufferedWriter out;
		try {
			code = ast.codeGeneration();
			out = new BufferedWriter(new FileWriter(fileName + ".asm"));
			out.write(code);
			out.close();
			System.out.println("\nCode generated! Assembling and running generated code.");
			
		}catch (IOException e) {
			System.err.println("Write error for '" + fileName + ".asm" + "' file: " + e.toString());
			System.exit(1);
			
		}catch(Exception e){
			System.err.println("Error in code generation: " + e.toString());
			System.exit(1);
		}
		 
		
		// leggo il file .asm
		CharStream isASM = null;
		try {
			isASM = CharStreams.fromFileName(fileName+".asm");
		} 
		catch (IOException e) {
			System.err.println("The file " + fileName + ".asm" + " was not found.");
			System.exit(1);
		}
		
		
		
		SVMVisitorImpl visitorSVM = null;
		try {
			SVMLexer lexerASM = new SVMLexer(isASM);
			CommonTokenStream tokensASM = new CommonTokenStream(lexerASM);
			SVMParser parserASM = new SVMParser(tokensASM);
			
			visitorSVM = new SVMVisitorImpl();
			visitorSVM.visit(parserASM.assembly()); 
	
			System.out.println("You had: " + lexerASM.errorCount() + " lexical errors and " 
					+ parserASM.getNumberOfSyntaxErrors() + " syntax errors.");
			
			if ( lexerASM.errorCount() > 0 || parserASM.getNumberOfSyntaxErrors() > 0 ) 
				System.exit(1);
			
		}catch(Exception e){
			System.err.println("Error in assembling generated code: " + e.toString());
			System.exit(1);
		}
			    
	    try {
			System.out.println("\nStarting Virtual Machine...\n");
			ExecuteVM vm = new ExecuteVM(visitorSVM.getCode(), CODESIZE, MEMSIZE, debug);
			vm.cpu();
			
	    }catch(SmallCodeAreaCException | MemoryException e) {
	    	System.err.println("Error during execution:");
	    	System.err.println(e.getMessage());
	    	
	    }catch(Exception e){
			System.err.println("Error during execution: " + e.toString());
			System.exit(1);
		}
		
	}
	
	
	private static boolean checkInputFileName(String name) {
		
		if( name.contains("-ast") )
			return false;
		else if( name.contains("-debug") )
			return false;
		else if( name.contains("-codesize") )
			return false;
		else if( name.contains("-memsize") )
			return false;
		else 
			return true;
		
	}
	
}