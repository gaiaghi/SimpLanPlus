package ast;

import java.util.ArrayList;
import java.util.Map.Entry;

import exception.TypeErrorException;
import util.Environment;
import util.STEntry;
import util.SemanticError;
import util.SimpLanPlusLib;

public class BlockNode implements Node {
	//grammar rule:
	//block	    : '{' declaration* statement* '}';

	private ArrayList<Node> declarations;
	private ArrayList<Node> statements;
	private boolean inFunction; //true: è dentro il corpo di una funzione
	private boolean isFunBody; //true: è il blocco principale del corpo di una funzione
	
	private boolean returns;
	private boolean main;
	private String funEndLabel;
	private String funEndBlock;
	
	public BlockNode(ArrayList<Node> declarations, ArrayList<Node> statements) {
		this.declarations = declarations;
		this.statements = statements;
		this.inFunction = false;
		this.isFunBody = false;
		main = false;
		this.returns = findReturns();
	}

	@Override
	public String toPrint(String indent) {
		String str = "Block:";
		int i;
		for(i=0; i<declarations.size(); i++)
			str = str +"\n" + declarations.get(i).toPrint(indent +"  ");
		
		for(i=0; i<statements.size(); i++)
			str = str +"\n" + statements.get(i).toPrint(indent +"  ");
		
		return indent + str;
	}
	
	@Override
	public String toString() {
		return this.toPrint("");
	}

	@Override
	public Node typeCheck() throws TypeErrorException{
		for (Node dec: declarations)
			dec.typeCheck(); 
			
		//lista dei nodi che hanno un return
		ArrayList<Node> types = new ArrayList<>();
		
		for (Node stm: statements){
			Node stmType = stm.typeCheck();		//controllo su tutti gli statement 
			
			if( stmType != null && !(stm instanceof CallLNode)) {
				types.add(stmType);
			}
			
					
		} 

		if( inFunction ) {
			if( statements.size() > 0 ) {
				if (types.size() > 0){
					for(int i=0; i<types.size(); i++) {
						if( ! SimpLanPlusLib.isEquals(types.get(i), types.get(0)) )
							throw new TypeErrorException("block with multiple return statements having mismatching types.");
					}
				}		
				
				return statements.get( statements.size()-1 ).typeCheck();
			}
			else
				return null; //return implicito, NullType
		}
		else {
			return null; //non è il corpo di una funzione, non ho tipo di ritorno
		}
			
	}

	@Override
	public String codeGeneration() {
		
		String code = "";

		//code = code + "-------------------- inizio blocco\n";
		
		if (! isFunBody) {
			//FP
			code = code + "push $fp\n";
			
			/*AL
			 * an inner block is entered or a function declared in the current
			scope is called: ACCESS_LINK = address of ACCESS_LINK in current AR*/
			code = code + "mv $al $fp\n";
			code = code + "push $al\n";
			
			code = code + "mv $fp $sp\n";
			//RA
			code = code + "li $t1 0\n";
            code = code + "push $t1\n";
		}
		//code = code + "-------------------- inizio decs\n";
		//int jj=1;
        for (Node dec : declarations) {
        	//code = code + "-------------------- dec " +jj +"\n"; jj++;
        	code = code + dec.codeGeneration();
    	}

       // code = code + "-------------------- inizio stms\n";
        //jj=1;
        for (Node stm : statements) {
        	//code = code + "-------------------- stm " +jj +"\n"; jj++;
        	code = code + stm.codeGeneration();
        }
        	
        //code = code + "-------------------- fine stms\n";
        
        if( inFunction )
        	code = code + funEndBlock + ":\n";
        
        //code = code + "-------------------- pulizia blocco\n";
		int n_var = 0;
		for (Node dec : declarations)
			if (dec instanceof DecVarLNode)
				n_var++;
		//pop var dec
		code = code + "addi $sp $sp "+n_var+"\n"; 
		
		
		if (! isFunBody) {
			code = code + "pop\n"; // pop ra 
			code = code + "pop\n"; // pop al
			code = code + "lw $fp 0($sp)\n";
			code = code + "pop\n"; // pop fp 
		}     
		
		if( inFunction ) {
			// se è stato eseguito un return allora $ret == 1
			//code = code + "-------------------- controllo se c'è stato un return\n";
			String trueLabel = SimpLanPlusLib.freshLabel();
			String endIfLabel = SimpLanPlusLib.freshLabel();
			
			// $a0 potrebbe avere un valore significativo, lo salvo sullo stack 
			// e dopo ripristino il vecchio valore
			code = code + "push $a0\n";
			code = code + "li $a0 1\n";
			code = code + "beq $ret $a0 " + trueLabel + "\n";
			
			// se vale $ret != 1
			code = code + "lw $a0 0($sp)\n";
			code = code + "pop\n";
			code = code + "b " + endIfLabel + "\n";
			
			// se vale $ret == 1
			code = code + trueLabel + ":\n";
			code = code + "lw $a0 0($sp)\n";
			code = code + "pop\n";
			//code = code + "-------------------- se $ret == 1 vai a\n";
			code = code + "b " + funEndLabel + "\n";
			
			// fine if 
			code = code + endIfLabel + ":\n";
		}
		
		if( main )
			code = code + "halt\n";
		//code = code + "-------------------- fine blocco\n";
		return code;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
		
		if( env.getNestingLevel() == -1 )
			main = true;
		
		//se è il blocco che definisce il corpo della funzione non devo creare un nuovo scope
		if(! isFunBody) 
			env.addScope();
		
		
		//check semantics in declaration
		if(declarations.size() > 0){
			//TODO env.offset = -2;
			for(Node n : declarations)
				res.addAll(n.checkSemantics(env));
		}

		
		//check semantics in statement
		if(statements.size() > 0){
			//creo una lista di indici degli stm che contengono "return"
			ArrayList<Integer> returns_index = new ArrayList<Integer>();
			
			for(int i=0; i<statements.size(); i++ ) {
				Node n = statements.get(i);
				res.addAll(n.checkSemantics(env));
				
				//controllo se lo stm ha "return" al suo interno
				if( inFunction ) {
					if( n instanceof RetLNode ) {
						returns_index.add(i);
					}
					else if ( n instanceof IteLNode ) {
						if( ((IteLNode) n).getReturns() )
							returns_index.add(i);
					}
					else if ( n instanceof BlockLNode ) {
						if( ((BlockLNode) n).getReturns() )
							returns_index.add(i);
					}
				}
			}//end for
			
			
			//Caso in cui il blocco è all'interno del corpo di una funzione
			if( inFunction ) {
				//Controllo che non ci siano "return" multipli
				if( returns_index.size() > 1 )
					res.add(new SemanticError("there cannot be multiple returns in the same block."));
				else {	
					//Controllo la presenza di codice irranggiungibile, 
					//		quindi il "return" deve essere in ultima posizione
					if( returns_index.size() == 1 && 
						returns_index.get(0) != statements.size()-1 )
						res.add(new SemanticError("the return stm is not the last statement in the block. Unreachable code."));
				}
			}
			//Caso in cui il blocco NON è all'interno del corpo di una funzione.
			//Non ci possono essere "return".
			//questo controllo viene fatto nella checkSemantics di RetNode
		}     

		if(! isFunBody)
			env.removeScope();
  
		return res;
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		ArrayList<SemanticError> errors = new ArrayList<>();
		
		if(! isFunBody)
			env.addScope();
		
		for (Node dec: declarations)
			errors.addAll(dec.checkEffects(env));
//		System.out.println("BlockNode: "+errors);
		
//		System.out.println("BlockNode env:");
//		for( Entry<String,STEntry> entryVar : env.getCurrentScope().entrySet() ) {
//			System.out.println("	"+entryVar.getKey() );
//		}
		
		for (Node stm: statements)
			errors.addAll(stm.checkEffects(env));
		
		if(! isFunBody)
			env.removeScope();
		
		//env.printEnv("fine blocco");
		
		return errors;
	}

	
	public ArrayList<Node> getDeclarations(){
		return declarations;
	}
	
	public int getDeclarationsSize(){
		return declarations.size();
	}
	
	public ArrayList<Node> getStatements(){
		return statements;
	}
	
	public int getStatementsSize(){
		return statements.size();
	}
	
	public void setInFunction(boolean b){
		inFunction = b;
		for(Node n : statements) {
			if( n instanceof BlockLNode )
				((BlockLNode) n).setInFunction(b);
			
			else if( n instanceof IteLNode )
				((IteLNode) n).setInFunction(b);
			
			else if( n instanceof RetLNode )
				((RetLNode) n).setInFunction(b);
		}	
	}
	
	public boolean getIsFunBody() {
		return this.isFunBody;
	}
	
	public void setIsFunBody(boolean isFunBody) {
		this.isFunBody = isFunBody;
	}
	
	private boolean findReturns() {
		ArrayList<Boolean> returns_list = new ArrayList<Boolean>();
		for(Node n : statements) {
			if( n instanceof BlockLNode )
				returns_list.add( ((BlockLNode) n).getReturns() );
			
			else if( n instanceof IteLNode )
				returns_list.add( ((IteLNode) n).getReturns() );
			
			else if( n instanceof RetLNode )
				returns_list.add(true);
		}	
		
		for( Boolean b : returns_list )
			if( b )
				return true;
		
		return false;
	}
	
	
	public boolean getReturns() {
		return returns;
	}
	
	public void setFunEndLabel(String labelEndFun) {
		funEndLabel = labelEndFun;
		
		funEndBlock = SimpLanPlusLib.freshLabel();
		
		for(int i=0; i<statements.size(); i++ ) {
			Node n = statements.get(i);
			
			if( n instanceof RetLNode ) {
				((RetLNode)n).setFunEndLabel(funEndBlock);
			}
			else if ( n instanceof IteLNode ) {
				((IteLNode)n).setFunEndLabel(funEndBlock);
			}
			else if ( n instanceof BlockLNode ) {
				((BlockLNode)n).setFunEndLabel(funEndBlock);
			}
		}
		
	}
	
}
