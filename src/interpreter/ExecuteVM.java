package interpreter;

import java.util.ArrayList;

import exception.AccessToFreeMemoryCellException;
import exception.InvalidInstructionException;
import exception.MemoryException;
import exception.MissingValueCellException;
import exception.SmallCodeAreaCException;
import svm.SVMParser;
import util.Cell;
import util.Instruction;
import util.Registers;

public class ExecuteVM {
    
	// dimensione code area
    public int CODESIZE;
    
    // dimensione other space (stack + heap)
    public int MEMSIZE;
 
    private ArrayList<Instruction> code;
    private ArrayList<Cell> memory;
    
    private Registers regs;
    
    public ExecuteVM(ArrayList<Instruction> c, int sizeCodeArea, int sizeOtherSpace) throws SmallCodeAreaCException, MemoryException {
      code = c;
      CODESIZE = sizeCodeArea;
      MEMSIZE = sizeOtherSpace;
      memory = new ArrayList<Cell>(CODESIZE + MEMSIZE);
      
      //TODO setta valori iniziali registri, 	 hp=CODESIZE ??? è corretto?
      //ordine parametri: 	a0, t1, sp, fp, al, ra, hp, ret, ip
      regs = new Registers(0,0,MEMSIZE-1,MEMSIZE-1,0,0,CODESIZE,0,0);
      
      // Inizializzo la memoria con celle vuote.
      // Inizializzo le celle della code area. true indice una cella della code area
      for(int i = 0; i < CODESIZE; i ++) 
		  memory.add(new Cell( true ));
      
      // Inizializzo le celle di Other Space (stack + heap). false indice una cella dello stack/heap
      for(int i = CODESIZE; i < CODESIZE + MEMSIZE; i ++) 
		  memory.add(new Cell( false ));
      
      /* controllo che la dimensione della code area sia sufficiente a 
       	 contenere il codice ricevuto in input.
  		 	- Se è sufficiente, copio ogni istruzione del codice in input
  		 	 	in una cella di memoria della code area
	 	 	- altrimenti eccezione
       */
      if( code.size() > CODESIZE )
    	  throw new SmallCodeAreaCException("Code area too small!");
      else
    	  for(int i = 0; i < code.size(); i ++) 
    		  memory.get(i).setInstruction(code.get(i));
    	
      //TODO debug
      System.out.println("Dimensione Code Area = "+CODESIZE +
    		  "Dimensione stack/heap = "+MEMSIZE +
    		  "Dimensione totale memoria = "+(CODESIZE+MEMSIZE));
    }
    
    
    
    
    public void cpu(){
    	Instruction bytecode = null;
    	try {
    		
	    	while ( true ) {
	    	
	    		if( regs.getHP() + 1 >= regs.getSP() ) {
	    			System.out.println("\nError: Out of memory");
	    			return;
	    		}
	    		else {
	    			// fetch
		    		bytecode = code.get( regs.getIP() );  
		    		regs.addOneToIP();
		            // TODO controlla se tutte queste variabili vengono usate
		    		int value;
		            int address;
		            int arg1;
		            int arg2;
		            int arg3;
		            int offset;
		            
		            switch ( bytecode.getInstr() ) {
		            	
		            	case SVMParser.PUSH:
		            		value = regs.getRegisterValue( bytecode.getArg1() );
		            		push(value);
		            		break;
		            	
		            	case SVMParser.POP:
		            		pop();
		            		break;
		              
		            	case SVMParser.ADD :
		            		arg2 = regs.getRegisterValue(bytecode.getArg2());
		            		arg3 = regs.getRegisterValue(bytecode.getArg3());
		            		regs.setRegisterValue(bytecode.getArg1(), arg2 + arg3);
			                break;
		            	
		            	case SVMParser.SUB :
		            		arg2 = regs.getRegisterValue(bytecode.getArg2());
		            		arg3 = regs.getRegisterValue(bytecode.getArg3());
		            		regs.setRegisterValue(bytecode.getArg1(), arg2 - arg3);
		            		break;
		              
		            	case SVMParser.MULT :
		            		arg2 = regs.getRegisterValue(bytecode.getArg2());
		            		arg3 = regs.getRegisterValue(bytecode.getArg3());
		            		regs.setRegisterValue(bytecode.getArg1(), arg2 * arg3);
			                break;
		              
		            	case SVMParser.DIV :
		            		arg2 = regs.getRegisterValue(bytecode.getArg2());
		            		arg3 = regs.getRegisterValue(bytecode.getArg3());
		            		regs.setRegisterValue(bytecode.getArg1(), arg2 / arg3);
			                break;  
			                
		            	case SVMParser.ADDI :
		            		arg2 = regs.getRegisterValue(bytecode.getArg2());
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		regs.setRegisterValue(bytecode.getArg1(), arg2 + arg3);
			                break;
		            	
		            	case SVMParser.SUBI :
		            		arg2 = regs.getRegisterValue(bytecode.getArg2());
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		regs.setRegisterValue(bytecode.getArg1(), arg2 - arg3);
			                break;
		              
		            	case SVMParser.MULTI :
		            		arg2 = regs.getRegisterValue(bytecode.getArg2());
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		regs.setRegisterValue(bytecode.getArg1(), arg2 * arg3);
			                break;
		              
		            	case SVMParser.DIVI :
		            		arg2 = regs.getRegisterValue(bytecode.getArg2());
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		regs.setRegisterValue(bytecode.getArg1(), arg2 / arg3);
			                break;  
			            
		            	case SVMParser.LI :
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		regs.setRegisterValue(bytecode.getArg1(), arg2);
			                break; 
		                
		            	case SVMParser.LB :
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		regs.setRegisterValue(bytecode.getArg1(), arg2);
			                break; 
		              
		            	case SVMParser.STOREW :   //TODO da rivedere, nel caso heap?
		            		offset = bytecode.getOffset();
		            		address = regs.getRegisterValue(bytecode.getArg2()) + offset;
		            		value = regs.getRegisterValue(bytecode.getArg1());
		            		writeOnMemory(address, value);
			                break;
		              
		            	case SVMParser.LOADW : 
		            		printCPU();
		            		offset = bytecode.getOffset();
		            		address = regs.getRegisterValue(bytecode.getArg2()) + offset;
		            		
		            		// TODO debug
		            		System.err.println("LOADW\targ1=" +bytecode.getArg1() +
		            				"\toffset="+offset + "\targ2=" +bytecode.getArg2());
		            		System.err.println("address = " + address +"\tvalueArg2="+ regs.getRegisterValue(bytecode.getArg2())
		            				); 
		            		// fine debug 
		            		
		            		value = readFromMemory(address);
		            		
		            		// TODO debug
		            		System.err.println("LOADW\tvalue=" +value );
		            		// fine debug 
		            		
		            		regs.setRegisterValue(bytecode.getArg1(), value);
		            		
		            		// TODO debug
		            		System.err.println("LOADW\tnuovo valore registro = " +regs.getRegisterValue(bytecode.getArg1()) +"\n");
		            		// fine debug 
		            		
		            		printCPU();
			                break;
		              
		            	case SVMParser.BRANCH : 
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		regs.setIP(arg1);
			                break;
		              
		            	case SVMParser.BRANCHEQ : 
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		value = regs.getRegisterValue(bytecode.getArg1());
		            		if( value == regs.getRegisterValue(bytecode.getArg2()) )
		            			regs.setIP(arg3);
			                break;
		              
		            	case SVMParser.BRANCHLESSEQ :
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		value = regs.getRegisterValue(bytecode.getArg1());
		            		if( value <= regs.getRegisterValue(bytecode.getArg2()) )
		            			regs.setIP(arg3);
			                break;
		            	
		            	case SVMParser.JR :
		            		address = regs.getRegisterValue( bytecode.getArg1() );
		            		regs.setIP(address);
			                break;
			                
		            	case SVMParser.JAL :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		regs.setRegisterValue("$ra", regs.getIP() );
		            		regs.setIP(arg1);
			                break;
			                
		            	case SVMParser.MOVE :
		            		value = regs.getRegisterValue( bytecode.getArg2() );
		            		regs.setRegisterValue(bytecode.getArg1(), value);
			                break;
			                
		            	case SVMParser.PRINT :		//TODO da rivedere
		            		if( regs.getSP() <= MEMSIZE + CODESIZE )
		            			System.out.println( "--print--> " + regs.getRegisterValue(bytecode.getArg1()) );
		            		else
		            			System.out.println("Empty stack!");
			                
			                break;
			                
		            	case SVMParser.DELETION :
		            		arg1 = regs.getRegisterValue( bytecode.getArg1() );
		            		free(arg1);
			                break;
			                
		            	case SVMParser.AND :
		            		arg2 = regs.getRegisterValue(bytecode.getArg2());
		            		arg3 = regs.getRegisterValue(bytecode.getArg3());
		            		regs.setRegisterValue(bytecode.getArg1(), and(arg2, arg3) );
			                break;
			                
		            	case SVMParser.OR :
		            		arg2 = regs.getRegisterValue(bytecode.getArg2());
		            		arg3 = regs.getRegisterValue(bytecode.getArg3());
		            		regs.setRegisterValue(bytecode.getArg1(), or(arg2, arg3) );
			                break;
			                
		            	case SVMParser.NOT :
		            		arg2 = regs.getRegisterValue(bytecode.getArg2());
		            		regs.setRegisterValue(bytecode.getArg1(), not(arg2) );
			                break;
		             
		            	case SVMParser.HALT :
		            		//TODO non posso fare niente perchè a questo punto 
		            		// lo stack è vuoto
		            		
		            		/* versione prof
			            	//to print the result 
			             	System.out.println("\nResult: " + readFromMemory(regs.getSP()) + "\n");
			             	*/
			             	return;
			             	
		            	default:
		            		System.err.println("Error: invalid instruction "+bytecode.getInstr());
		    	            return;
		            }
	    		} 
	    	}	
    	}catch(MemoryException | AccessToFreeMemoryCellException | 
    			MissingValueCellException | InvalidInstructionException e) {
    		System.err.println("Error during execution:");
    		System.out.println(e.getMessage());
    		System.err.println("Instruction: " + bytecode.getInstr());
    		/*
    		 * T__0=1, T__1=2, PUSH=3, POP=4, ADD=5, SUB=6, MULT=7, DIV=8, ADDI=9, SUBI=10, 
				MULTI=11, DIVI=12, LI=13, LB=14, STOREW=15, LOADW=16, BRANCH=17, BRANCHEQ=18, 
				BRANCHLESSEQ=19, JR=20, JAL=21, PRINT=22, DELETION=23, MOVE=24, AND=25, 
				OR=26, NOT=27, ANDB=28, ORB=29, NOTB=30, HALT=31, REGISTER=32, COL=33, 
				LABEL=34, NUMBER=35, BOOL=36, WHITESP=37, ERR=38;
    		 * */
    	}
    	
    	printCPU();
    } 
    
    
    private void pop() throws AccessToFreeMemoryCellException, MemoryException, MissingValueCellException {
    	// TODO la free è necessaria? ci serve impostare la cella a isFree=false?
    	free(regs.getSP());
    	// addi $sp $sp 1
    	regs.setSP( regs.getSP() + 1);
    }
    
    
    private void push(int value) throws MemoryException {
    	try {
    		// addi $sp $sp -1
    		regs.moveUpSP();
    		// sw $t 0($sp)
	    	writeOnMemory(regs.getSP(), value );
	    	
    	}catch(IndexOutOfBoundsException e) {
    		throw new MemoryException("Access out of memory.");
    	}
    }
    
    // legge dati da Other Space (stack + heap)
    private int readFromMemory(int index) throws AccessToFreeMemoryCellException, MemoryException, MissingValueCellException {
    	try {
    		return memory.get(index).getData();
    	}catch(IndexOutOfBoundsException e) {
    		throw new MemoryException("Access out of memory.");
    	}
    }
    
    
    // scrive dati in Other Space (stack + heap)
    private void writeOnMemory(int index, int data) throws MemoryException {
    	try {
    		memory.get(index).setData(data);
    	}catch(IndexOutOfBoundsException e) {
    		throw new MemoryException("Access out of memory.");
    	}
    }
    
    
    private void free(int index) throws MemoryException {
    	try {
    		memory.get(index).free();;
    	}catch(IndexOutOfBoundsException e) {
    		throw new MemoryException("Access out of memory.");
    	}
    }
    
    
    
    private int getFreeHeapCell() {
    	int indexCell = MEMSIZE;
    	
    	for(int i = CODESIZE; i < regs.getHP(); i ++)
    		if( memory.get(i).isFree() )
    			indexCell = i;
    	
    	// SERVE QUESTA COSA??
    	// controllo che $hp punti ad una cella occupata
    	// se punta ad una cella libera modifico il valore di $hp 
    	// facendolo puntare alla prima cella occupata
    	for(int i = regs.getHP(); i >= CODESIZE; i --) {
    		if( memory.get(i).isFree() )
    			continue;
    		else {
    			regs.setHP(i);
    			break;
    		}
    	}
    	
    	return indexCell;
    }
    
    private int and(int x, int y) {
    	if( x+y == 2 )
    		return 1;
    	else
    		return 0;
    }
    
    private int or(int x, int y) {
    	if( x+y == 0 )
    		return 0;
    	else
    		return 1;
    }
    
    private int not(int x) {
    	if( x == 0 )
    		return 1;
    	else
    		return 0;
    }
    
    private void printCPU() {
    	System.out.println("\n------------------------------------------");
    	System.out.println("|           CPU status	                 |");
    	System.out.println("------------------------------------------");
    	
    	System.out.println(regs.toString());
    	
    	System.out.println("------------------------------------------");
    	
    	for(int i = CODESIZE; i < MEMSIZE+CODESIZE; i ++) {
    		if( !memory.get(i).isFree() ) {
	    		String str = "[" + i + "]\t" + memory.get(i);
	    		
	    		if( regs.getAL() == i ) str = str + "\t<--  $al";
	    		if( regs.getFP() == i ) str = str + "\t<--  $fp";
	    		if( regs.getHP() == i ) str = str + "\t<--  $hp";
	    		if( regs.getSP() == i ) str = str + "\t<--  $sp";
	    		
	    		System.out.println(str);
    		}
    		
    	}
    	
    	System.out.println("------------------------------------------");
    }
    
}