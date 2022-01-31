grammar SimpLanPlus;

@lexer::header {
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


// THIS IS THE PARSER INPUT

block	    : '{' declaration* statement* '}';

statement   : assignment ';'	#assignmentL
	    | deletion ';'			#deletionL
	    | print ';'				#printL
	    | ret ';'				#retL
	    | ite					#iteL
	    | call ';'				#callL
	    | block					#blockL;
	    
	    

declaration : decFun			#decFunL
            | decVar 			#decVarL;

decFun	    : (type | 'void') ID '(' (arg (',' arg)*)? ')' block ;

decVar      : type ID ('=' exp)? ';' ;

type        : 'int'
            | 'bool'
	    	| '^' type ;

arg         : type ID;

assignment  : lhs '=' exp ;

lhs         : ID | lhs '^' ;

deletion    : 'delete' ID;

print	    : 'print' exp;

ret	    : 'return' (exp)?;

ite         : 'if' '(' exp ')' statement ('else' statement)?;

call        : ID '(' (exp(',' exp)*)? ')';

exp	    : '(' exp ')'				        				#baseExp
	    | '-' exp					        				#negExp
	    | '!' exp                                           #notExp
	    | lhs												#derExp
	    | 'new' type										#newExp
	    | left=exp op=('*' | '/')               right=exp   #binExp
	    | left=exp op=('+' | '-')               right=exp   #binExp
	    | left=exp op=('<' | '<=' | '>' | '>=') right=exp   #binExp
	    | left=exp op=('=='| '!=')              right=exp   #binExp
	    | left=exp op='&&'                      right=exp   #binExp
	    | left=exp op='||'                      right=exp   #binExp
	    | call                                              #callExp
	    | BOOL                                              #boolExp
	    | NUMBER					        				#valExp;


// THIS IS THE LEXER INPUT

//Booleans
BOOL        : 'true'|'false';

//IDs
fragment CHAR 	    : 'a'..'z' |'A'..'Z' ;
ID          : CHAR (CHAR | DIGIT)* ;

//Numbers
fragment DIGIT	    : '0'..'9';
NUMBER      : DIGIT+;

//ESCAPE SEQUENCES
WS              : (' '|'\t'|'\n'|'\r')-> skip;
LINECOMMENTS 	: '//' (~('\n'|'\r'))* -> skip;
BLOCKCOMMENTS   : '/*'( ~('/'|'*')|'/'~'*'|'*'~'/'|BLOCKCOMMENTS)* '*/' -> skip;

// azione da eseguire in caso di errore
ERR      : . { errors.add("Invalid character: "+ getText());  } -> channel(HIDDEN);