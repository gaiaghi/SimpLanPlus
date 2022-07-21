package interpreter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    
	private boolean debug;
	BufferedWriter out = null;
	
	// stack utilizzata per stampare una riga bianca tra un AR e l'altro.
	// stack[i] = true 		indica che sta iniziando un nuovo AR quindi devo lasciare una riga bianca
	private ArrayList<Boolean> stack;
	
	// dimensione code area
    public int CODESIZE;
    
    // dimensione other space (stack + heap)
    public int MEMSIZE;
 
    private ArrayList<Instruction> code;
    private ArrayList<Cell> memory;
    
    private Registers regs;
    
    public ExecuteVM(ArrayList<Instruction> c, int sizeCodeArea, int sizeOtherSpace, boolean d) throws SmallCodeAreaCException, MemoryException {
      code = c;
      debug = d;
      CODESIZE = sizeCodeArea;
      MEMSIZE = sizeOtherSpace;
      memory = new ArrayList<Cell>(CODESIZE + MEMSIZE);
      
      regs = new Registers(0,           // $a0
    		  0,						// $t1
    		  CODESIZE + MEMSIZE,		// $sp
    		  CODESIZE + MEMSIZE-1,		// $fp
    		  CODESIZE + MEMSIZE-2,		// $al
    		  0,						// $ra
    		  CODESIZE,					// $hp
    		  0,						// $ret
    		  0);						// $ip
      
      // Inizializzo la memoria con celle vuote.
      // Inizializzo le celle della code area. true indica una cella della code area
      for(int i = 0; i < CODESIZE; i ++) 
		  memory.add(new Cell( true ));
      
      // Inizializzo le celle di Other Space (stack + heap). false indica una cella dello stack/heap
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
    	
    
      stack = new ArrayList<Boolean>();
      for(int i = 0; i < CODESIZE+MEMSIZE; i ++) 
    	  stack.add(false);
      
      if( debug ) 
    		out = openDebugFile();
    }
    
    
    
    
    public void cpu(){
    	Instruction bytecode = null;
    	try {
    		
    		printCPU("INIT");
    		
	    	while ( true ) {
	    	
	    		if( regs.getHP() + 1 >= regs.getSP() ) {
	    			System.out.println("\nError: Out of memory");
	    			if( debug )
            			closeDebugFile(out);
	    			return;
	    		}
	    		else {
	    			// fetch
	    			bytecode = code.get( regs.getIP() );  
		    		regs.addOneToIP();

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
		            		
		            		// quando c'è una "push $fp" vuol dire che sta iniziando un nuovo AR
		            		// quindi lascio una una riga vuota
		            		if( bytecode.getArg1().compareTo("$fp") == 0 )
		            			stack.set(regs.getSP(), true);
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
		            		
		            		
		            		if( bytecode.getArg1().compareTo("$sp") == 0 &&	
		            				bytecode.getArg2().compareTo("$sp") == 0 ) {
		            			if( arg3 > 0 ) {
		            				/* quando facciamo "addi $sp $sp n" equivale a fare "n" pop
				            		   quindi devo settare le celle a free
				            		   "n" deve essere positivo   */
		            				int tmp = regs.getRegisterValue("$sp");
			            			for(int i = tmp - 1; i > tmp-1-arg3; i -- )
			            				free(i);
		            			}
		            			else {
		            				if( arg3 < 0 ) {
		            					/* quando facciamo "addi $sp $sp -n" equivale a fare "n" dec senze valore
					            		   per settare la cella a isFree=false per visualizzarla nella printCPU
		            					   scrivo nella cella il valore -1, non dovrebbe essere acceduta senza prima un
		            					   assegnamento
					            		   "n" deve essere negativo   */
		            					int tmp = regs.getRegisterValue("$sp");
				            			for(int i = tmp; i < tmp+(arg3*-1); i ++ ) {
				            				writeOnMemory(i,-1);
				            			}
		            				}
		            			}
		            		}
		            		
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
		              
		            	case SVMParser.STOREW :   
		            		offset = bytecode.getOffset();
		            		address = regs.getRegisterValue(bytecode.getArg2()) + offset;
		            		value = regs.getRegisterValue(bytecode.getArg1());
		            		writeOnMemory(address, value);
		            		break;
		              
		            	case SVMParser.LOADW : 
		            		offset = bytecode.getOffset();
		            		address = regs.getRegisterValue(bytecode.getArg2()) + offset;
		            		value = readFromMemory(address);
		            		regs.setRegisterValue(bytecode.getArg1(), value);
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
			                
		            	case SVMParser.PRINT :		
	            			arg1 = regs.getRegisterValue(bytecode.getArg1());
	            			arg2 = Integer.parseInt( bytecode.getArg2() );
	            			if( arg2 == 1 )
	            				System.out.println( "-> " + arg1 );
	            			else {
	            				if( arg1 == 1 )
	            					System.out.println( "-> true");
	            				else
	            					if( arg1 == 0 )
	            						System.out.println( "-> false");
	            					else 
	            						System.out.println( "-> " + arg1);
	            			}
		            		
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
		            		clearHeap();
		            		printCPU(bytecode.toString());
		            		System.out.println("\nEnd of execution!");
		            		if( debug )
		            			closeDebugFile(out);
			             	return;
			             	
		            	default:
		            		System.err.println("Error: invalid instruction "+bytecode.getInstr());
		            		if( debug )
		            			closeDebugFile(out);
		            		return;
		            }
		            printCPU(bytecode.toString());
	    		} 
	    	}	
	    	
    	}catch(MemoryException | AccessToFreeMemoryCellException | 
    			MissingValueCellException | InvalidInstructionException e) {
    		printCPU("Exception");
    		System.err.println("\n\nError during execution:");
    		System.err.println(e.getMessage());
    		System.err.println("Instruction: " + bytecode.toString());
    	
    		if( debug )
    			closeDebugFile(out);
    	}
    	
    	
    } 
    
    
    private void pop() throws AccessToFreeMemoryCellException, MemoryException, MissingValueCellException {
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
    
    private void printCPU(String s) {
    	if( debug ) {
    		
    		String stringToPrint = "";
    		
    		stringToPrint = stringToPrint + "\n\n\n\n------------------------------------------";
    		stringToPrint = stringToPrint + "\n|           CPU status	                 |";
    		stringToPrint = stringToPrint + "\n------------------------------------------\n";
	    	
	    	if( s.compareTo("") != 0 ) {
	    		stringToPrint = stringToPrint + "|           " +s;
	        	for(int i = 0; i < 28-s.length(); i ++)
	        		stringToPrint = stringToPrint + " ";
	        	stringToPrint = stringToPrint + " |\n";
	        	stringToPrint = stringToPrint + "------------------------------------------\n";
	    	}
	    	
	    	stringToPrint = stringToPrint + regs.toString();
	    	
	    	stringToPrint = stringToPrint + "\n------------------------------------------";
	    	
	    	stringToPrint = stringToPrint + "\n  Heap:";
	    	for(int i = CODESIZE; i < regs.getHP(); i ++) {
	    		if( !memory.get(i).isFree() ) {
		    		String str = "[" + i + "]\t" + memory.get(i);
		    		stringToPrint = stringToPrint + "\n  " + str;
	    		}	
	    	}
	    	stringToPrint = stringToPrint + "\n  " + "[" + regs.getHP() + "]\t" +"\t<--  $hp";
	    	
	    	stringToPrint = stringToPrint + "\n\n  Stack:";
	    	for(int i = regs.getHP()+1; i <= CODESIZE+MEMSIZE-1; i ++) {
	    		
	    		if( !memory.get(i).isFree() ) {
	    			
		    		String str = "[" + i + "]\t" + memory.get(i);
		    		
		    		if( regs.getAL() == i ) str = str + "\t<--  $al";
		    		if( regs.getFP() == i ) str = str + "\t<--  $fp";
		    		if( regs.getSP() == i ) str = str + "\t<--  $sp";
		    		
		    		stringToPrint = stringToPrint + "\n  " + str;
		    		
		    		if( stack.get(i) )
		    			stringToPrint = stringToPrint + "\n";
	    		}	
	    	}
	    	stringToPrint = stringToPrint + "\n------------------------------------------";
    	
	    	writeDebugFile(out, stringToPrint);
    	}
    }
    
    void clearHeap() {
    	for(int i = CODESIZE; i <= CODESIZE+MEMSIZE-1; i ++) 
    		if( !memory.get(i).isFree() ) 
    			memory.get(i).free();
    }
    
    
    private BufferedWriter openDebugFile() {
    	try {
			return new BufferedWriter(new FileWriter("examples/debug.txt"));
			
		} catch (IOException e) {
			System.err.println("Write error for debug file: " + e.toString());
			return null;
		} 
    }
    
    private void writeDebugFile(BufferedWriter out, String str) {
    	try {
    		out.write(str);
			
		} catch (IOException e) {
			System.err.println("Write error for debug file: " + e.toString());
			return;
		} 
    }
    
    private void closeDebugFile(BufferedWriter out) {
    	try {
    		out.close();
			
		} catch (IOException e) {
			System.err.println("Write error for debug file: " + e.toString());
			return;
		} 
    }
    
}