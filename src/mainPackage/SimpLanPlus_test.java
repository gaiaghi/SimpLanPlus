package mainPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import ast.Node;
import ast.SimpLanPlusVisitorImpl;
import exception.MissingDecException;
import exception.MultipleDecException;
import exception.TypeErrorException;
import parser.SimpLanPlusLexer;
import parser.SimpLanPlusParser;
import util.Environment;
import util.SemanticError;
import parser.SPPErrorListener;


public class SimpLanPlus{
	public static void main(String[] args) {

		//parInput = 0 -> leggi da "prova.simplanplus"
		//parInput = 1 -> leggi da "prova2.simplanplus"
		int parInput = 1;
		
		ArrayList<String> nomeEsempi = new ArrayList<String>();
		ArrayList<String> testoEsempi = new ArrayList<String>();
		ArrayList<String> soluzioneEsempi = new ArrayList<String>();
		ArrayList<String> risultatoEsempi = new ArrayList<String>();
		
		String fileName = "prova.simplanplus";
		
		
		if( parInput == 1 ) {
			try  
			{  
				File file = new File("prova2.simplanplus");      
				FileReader fr = new FileReader(file);   
				BufferedReader br = new BufferedReader(fr);   
				StringBuffer sb = new StringBuffer();     
				String line;  
				while( (line=br.readLine()) != null )  
				{  
					if( line.contains("//") ) {
						line = line.substring(2);
						
						if( line.charAt(0) == '#' ) {
							line = line.substring(1);
							String[] str = line.split("#");
							nomeEsempi.add(str[0]);
							soluzioneEsempi.add(str[1]);
							
							line=br.readLine();
							if( line.contains("//") )
								line = line.substring(2);
							
							testoEsempi.add(line);
						}
					
					}
				}  
				fr.close();    
				
				for(int i=0; i<testoEsempi.size(); i++) {
					
					if( nomeEsempi.get(i).compareTo("n2")==0 )
						continue;
					
					System.out.println("["+nomeEsempi.get(i) + "-->" 
							+soluzioneEsempi.get(i) +"]  " 
							+testoEsempi.get(i));
				}
			}  
			catch(IOException e)  
			{  
				e.printStackTrace();  
			}  
		}
		
		
		/*QUESTO é IL CODICE ORIGINALE
		CharStream inputCode = null;
		
		//open code file
		try {
			inputCode = CharStreams.fromFileName(fileName);
		} 
		catch (IOException e) {
			System.err.println("The file " + fileName + " was not found");
			System.exit(1);
		}
		*/
		CharStream inputCode = null;
		String stringa="";
		
		try {
			File file2 = new File("prova.simplanplus");      
			FileReader fr2 = new FileReader(file2);   
			BufferedReader br2 = new BufferedReader(fr2);   
			StringBuffer sb2 = new StringBuffer();     
			String line2; 
			while( (line2=br2.readLine()) != null )  
			{  
				stringa = stringa.concat(line2+"\n");
			}
			fr2.close();
		}  
		catch(IOException e)  
		{  
			e.printStackTrace();  
		} 
		if( parInput == 0 ) {
			nomeEsempi.add("nome");
			soluzioneEsempi.add("?");
			testoEsempi.add(stringa);
		}
		
		for(int esempio = 0; esempio < testoEsempi.size(); esempio++) {
		
			
		System.out.println("Esempio " +nomeEsempi.get(esempio) +" in esecuzione:\n"
				+testoEsempi.get(esempio) +"\n\n");
		inputCode = CharStreams.fromString(testoEsempi.get(esempio));
			
			
		
		
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
			//System.exit(1);
			
			risultatoEsempi.add("ERR");
			if(parInput==0)
				System.exit(1);
			else
				continue;
		}
		
		
		//Tree visitor
		SimpLanPlusVisitorImpl visitor = new SimpLanPlusVisitorImpl();
		Node ast = null;
		try {
			ast = visitor.visit(parser.block()); 
		}catch(Exception e) {
			System.err.println("Codice esempio in input non trovato");
			System.err.println("Error in visitor.visit(...): " +e.getMessage());
			//System.exit(1);
			
			risultatoEsempi.add("ERR");
			if(parInput==0)
				System.exit(1);
			else
				continue;
		}
		
		//checking lexical errors
		if (lexer.errorCount() > 0) {
			ArrayList lxErrors = lexer.getErrors();
			for (int i=0; i<lexer.errorCount(); i++)
				System.err.println(lxErrors.get(i));
		
            System.err.println("There are (" +lexer.errorCount()+") lexical errors in the file. Impossible to compile");
            //System.exit(1);
			
			risultatoEsempi.add("ERR");
			if(parInput==0)
				System.exit(1);
			else
				continue;
        } 

		
		//checking syntactical errors
		if(parser.getNumberOfSyntaxErrors()>0) {
			ArrayList syErrors = listener.getErrors();
			for (int i=0; i<syErrors.size(); i++)
				System.err.println(syErrors.get(i));
		
			System.err.println("There are (" + parser.getNumberOfSyntaxErrors() +") syntax errors in the file. Impossible to compile.");
			//System.exit(1);
			
			risultatoEsempi.add("ERR");
			if(parInput==0)
				System.exit(1);
			else
				continue;
		}
		
		
			
		//checking semantic errors
		Environment env = new Environment();	
		ArrayList<SemanticError> err = new ArrayList();
		err = ast.checkSemantics(env);
		
		if(err.size()>0){
			System.err.println("You had: " +err.size()+" semantic errors:");
			for(SemanticError e : err)
				System.err.println("\t" + e);
			//System.exit(1);
			
			risultatoEsempi.add("ERR");
			if(parInput==0)
				System.exit(1);
			else
				continue;
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
			//System.exit(1);
			
			risultatoEsempi.add("ERR");
			if(parInput==0)
				System.exit(1);
			else
				continue;
		}
		
		
		
		System.out.println("Visualizing AST...");
		System.out.println(ast.toPrint(""));
		
		risultatoEsempi.add("OK");
	}//for esempi
		
		System.out.println("\n\n\n\n\n\nRESOCONTO:");
		for(int i = 0; i < risultatoEsempi.size(); i++) {
			if( risultatoEsempi.get(i).compareTo(soluzioneEsempi.get(i)) != 0 )
				System.out.println("["+nomeEsempi.get(i) +"]  -->  "
						+"soluzione: " +soluzioneEsempi.get(i)
						+"    risultato: "+risultatoEsempi.get(i));
		}
		
	
	}
	
}
