package parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

public class SPPErrorListener extends BaseErrorListener{
	private List<String> errors = new ArrayList<>();
	
	@Override
	public void syntaxError(Recognizer<?,?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e) {
		
		errors.add("Error at line " + line + ":" + charPositionInLine + " " + msg);
		underlineError(recognizer, (Token) offendingSymbol, line, charPositionInLine);
			
	}
	
	protected void underlineError(Recognizer recognizer, Token offendingToken, int line, int charPositionInLine) {
		CommonTokenStream tokens= (CommonTokenStream) recognizer.getInputStream();
		String input = tokens.getTokenSource().getInputStream().toString();
		String[] lines = input.split("\n");
		String errorLine = lines[line - 1];
		
		String tabString = "\t";
		errorLine = errorLine.replaceAll(tabString, " ");
		
		String errorMsg="";
		
		//System.err.println(errorLine);
		errorMsg = errorMsg.concat(errorLine+"\n");
		for (int i=0; i<charPositionInLine; i++)
			errorMsg = errorMsg.concat(" ");
		//	System.err.print(" ");
		int start = offendingToken.getStartIndex();
		int stop = offendingToken.getStopIndex();
		if (start>=0 && stop>=0) {
			for (int i=start; i<=stop; i++) 
			//	System.err.print("^");
				errorMsg = errorMsg.concat("^");
		}
		//System.err.println();
		errorMsg = errorMsg.concat("\n");
		errors.add(errorMsg);
	}
	
	public ArrayList<String> getErrors() {
		return new ArrayList<String>(errors);
	}
	
	
	
	
	
}