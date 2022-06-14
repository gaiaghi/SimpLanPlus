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
	  | PRINT 		reg=REGISTER  
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
SUBI	: 'subi' ;	
MULTI	: 'multi' ;  	
DIVI	: 'divi' ;
LI 		: 'li';
LB 		: 'lb';
STOREW	: 'sw' ; 			// store in the memory cell pointed by top the value next
LOADW	: 'lw' ;			// load a value from the memory cell pointed by top
BRANCH	: 'b' ;				// jump to label
BRANCHEQ 		: 'beq' ;	// jump to label if top == next
BRANCHLESSEQ	: 'bleq' ;	// jump to label if top <= next
JR	 	: 'jr' ;	
JAL	 	: 'jal' ;
PRINT	: 'print' ;
DELETION 	: 'del';	
MOVE 	: 'mv';
AND 	: 'and';
OR		: 'or';
NOT 	: 'not';
ANDB 	: 'andb';
ORB 	: 'orb';
NOTB 	: 'notb';
HALT	: 'halt' ;	// stop execution


REGISTER:
	'$a0'
	| // Accumulator is used to store the computed value of expressions
	'$t1'
	| // General purpose register is used to store temporary values
	'$sp'
	| // Stack Pointer points to the top of the stack
	'$fp'
	| // Frame Pointer points to the current Access Link relative to the active frame
	'$al'
	| // Access Link is used to go through the static chain (i.e. scopes)
	'$ra'
	| // Return address stores the return address
	'$hp' // Heap pointer points to the top of the heap
	| // 
	'$ret'; // 
	
	

COL	 : ':' ;
LABEL	 : ('a'..'z'|'A'..'Z')('a'..'z' | 'A'..'Z')*'_'('0'..'9')('0'..'9')* ;
NUMBER	 : '0' | ('-')?(('1'..'9')('0'..'9')*) ;
BOOL 	 : 'false' | 'true';

WHITESP  : ( '\t' | ' ' | '\r' | '\n' )+   -> channel(HIDDEN);

ERR   	 : . { errors.add("Invalid character: "+ getText());  } -> channel(HIDDEN); 

