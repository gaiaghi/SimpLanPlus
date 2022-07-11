grammar SVM;

@header {
import java.util.List;
import java.util.ArrayList;
}

@lexer::members {
private List<String> errors = new ArrayList<>();
	
	public int errorCount() {
		return errors.size();
	}
	
	public ArrayList getErrors(){
		return new ArrayList(errors);
	}
}

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/
  
assembly: (instruction)* ;

instruction:
    ( 	PUSH 	reg=REGISTER 		     
	  | POP		
	     
	  | ADD		res=REGISTER	term1=REGISTER		term2=REGISTER	    
	  | SUB		res=REGISTER 	term1=REGISTER 		term2=REGISTER	    
	  | MULT 	res=REGISTER 	term1=REGISTER 		term2=REGISTER	    
	  | DIV		res=REGISTER 	term1=REGISTER 		term2=REGISTER
	  | ADDI	res=REGISTER 	term1=REGISTER 		term2=NUMBER	    
	  | SUBI	res=REGISTER 	term1=REGISTER 		term2=NUMBER	    
	  | MULTI 	res=REGISTER 	term1=REGISTER 		term2=NUMBER	    
	  | DIVI	res=REGISTER 	term1=REGISTER 		term2=NUMBER
	  	 
	  | LI 		res=REGISTER 	term=NUMBER   
	  | LB 		res=REGISTER 	term=BOOL  
	  | STOREW 	value=REGISTER 	offset=NUMBER 	'(' address=REGISTER ')' 
	  | LOADW 	value=REGISTER 	offset=NUMBER 	'(' address=REGISTER ')' 
	  | l=LABEL COL     
	  | BRANCH 			l=LABEL  
	  | BRANCHEQ 		term1=REGISTER		term2=REGISTER 		l=LABEL 
	  | BRANCHLESSEQ 	term1=REGISTER 		term2=REGISTER 		l=LABEL 
	  | JR 			reg=REGISTER     
	  | JAL 		l=LABEL 
	  | MOVE 		to=REGISTER 	from=REGISTER  
	  | PRINT 		reg=REGISTER  	term1=NUMBER
	  | DELETION 	reg=REGISTER 
	  
	  | AND		res=REGISTER 	term1=REGISTER 		term2=REGISTER
	  | OR		res=REGISTER 	term1=REGISTER 		term2=REGISTER
	  | NOT		res=REGISTER 	term1=REGISTER 
	  | ANDB	res=REGISTER 	term1=REGISTER 		term2=BOOL
	  | ORB		res=REGISTER 	term1=REGISTER 		term2=BOOL
	  | NOTB 	res=REGISTER 	term1=BOOL
	   
	  | HALT
	  ) ;
 	 
/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/
 
PUSH	: 'push' ; 	
POP	 	: 'pop' ; 	
ADD	 	: 'add' ;  	
SUB	 	: 'sub' ;	
MULT	: 'mult' ;  	
DIV	 	: 'div' ;	
ADDI	: 'addi' ;  	
SUBI	: 'subi' ;	//non utilizzato
MULTI	: 'multi' ;  	
DIVI	: 'divi' ;	//non utilizzato
LI 		: 'li';
LB 		: 'lb';		//non utilizzato
STOREW	: 'sw' ; 			
LOADW	: 'lw' ;			
BRANCH	: 'b' ;				
BRANCHEQ 		: 'beq' ;	
BRANCHLESSEQ	: 'bleq' ;
JR	 	: 'jr' ;	
JAL	 	: 'jal' ;
PRINT	: 'print' ;
DELETION 	: 'del';	
MOVE 	: 'mv';
AND 	: 'and';
OR		: 'or';
NOT 	: 'not';
ANDB 	: 'andb';	//non utilizzato
ORB 	: 'orb';	//non utilizzato
NOTB 	: 'notb';	//non utilizzato
HALT	: 'halt' ;	


REGISTER:
	'$a0'	// registro accumulatore utilizzato per memorizzare i risultati 
	| 
	'$t1'	// registro utilizzato per memorizzare valori temporanei
	|
	'$sp'	// Stack Pointer, punta alla cima dello stack
	| 
	'$fp'	// Frame Pointer, punta alla cella Access Link del frame corrente
	| 
	'$al'	// Access Link, usato per implementare la catena statica
	| 
	'$ra'	// Return address, contiene il return address
	| 
	'$hp' 	// Heap pointer, punta alla prima cella libera nello heap
	| 
	'$ret'; // registro utilizzato per la gestione dei return di funzione
	
	

COL	 	 : ':' ;
LABEL	 : ('a'..'z'|'A'..'Z')('a'..'z' | 'A'..'Z')*'_'('0'..'9')('0'..'9')* ;
NUMBER	 : '0' | ('-')?(('1'..'9')('0'..'9')*) ;
BOOL 	 : 'false' | 'true';

WHITESP  : ( '\t' | ' ' | '\r' | '\n' )+   -> channel(HIDDEN);

ERR   	 : . { errors.add("Invalid character: "+ getText());  } -> channel(HIDDEN); 

