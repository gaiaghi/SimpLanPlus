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
    
	//TODO 
	private boolean debug;
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
      
      //TODO setta valori iniziali registri, 	 hp=CODESIZE ??? è corretto?
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
    	
      //TODO cancellare??
      /*System.out.println("Dimensione Code Area = "+CODESIZE +
    		  "\nDimensione stack/heap = "+MEMSIZE +
    		  "\nDimensione totale memoria = "+(CODESIZE+MEMSIZE) +"\n");
      */
      
      
      stack = new ArrayList();
      for(int i = 0; i < CODESIZE+MEMSIZE; i ++) 
    	  stack.add(false);
      
    //TODO da cancellare
      //writeCode();
      
    }
    
    
    
    
    public void cpu(){
    	Instruction bytecode = null;
    	try {
    		
    		//TODO cancellare
    		printCPU("INIT");
    		
	    	while ( true ) {
	    	
	    		if( regs.getHP() + 1 >= regs.getSP() ) {
	    			System.out.println("\nError: Out of memory");
	    			return;
	    		}
	    		else {
	    			// fetch
	    			
	    			// TODO cancellare
	    			/*if( debug ) 
	    				System.out.println("Size "+code.size() +"     ip "+regs.getIP());
	    			*/
	    			
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
		            		
		            		// quando c'è una "push $fp" vuol dire che sta iniziando un nuovo AR
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
		            				//TODO quando facciamo "addi $sp $sp n" equivale a fare "n" pop
				            		// quindi devo settare le celle a free
				            		// "n" deve essere positivo
		            				int tmp = regs.getRegisterValue("$sp");
			            			for(int i = tmp - 1; i > tmp-1-arg3; i -- )
			            				free(i);
		            			}
		            			else {
		            				if( arg3 < 0 ) {
		            					//TODO quando facciamo "addi $sp $sp -n" equivale a fare "n" dec senze valore
					            		// per settare la cella a isFree=false per visualizzarla nella printCPU
		            					// scrivo nella cella il valore -1, non dovrebbe essere acceduta senza prima un
		            					// assegnamento
					            		// "n" deve essere negativo
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
		              
		            	case SVMParser.STOREW :   //TODO da rivedere, nel caso heap?
		            
		            		offset = bytecode.getOffset();
		            		address = regs.getRegisterValue(bytecode.getArg2()) + offset;
		            		value = regs.getRegisterValue(bytecode.getArg1());
		            		
		            		/*System.err.println("STOREW\targ1=" +bytecode.getArg1() + "=" +regs.getRegisterValue(bytecode.getArg1()) +
		            				"\toffset="+offset + "\targ2=" +bytecode.getArg2()+"="+regs.getRegisterValue(bytecode.getArg2()) );
		            		System.err.println("STOREW\taddress = " + address );
		            		*/
		            		writeOnMemory(address, value);
		            		
		            		
		            		
			                break;
		              
		            	case SVMParser.LOADW : 
		            		
		            		offset = bytecode.getOffset();
		            		address = regs.getRegisterValue(bytecode.getArg2()) + offset;
		            		
		            		// TODO debug
		            		/*System.err.println("LOADW\targ1=" +bytecode.getArg1() + "=" +regs.getRegisterValue(bytecode.getArg1()) +
		            				"\toffset="+offset + "\targ2=" +bytecode.getArg2()+"="+regs.getRegisterValue(bytecode.getArg2()) );
		            		System.err.println("LOADW\taddress = " + address ); 
		            		// fine debug 
		            		*/
		            		value = readFromMemory(address);
		            		
		            		// TODO debug
		            		//System.err.println("LOADW\tvalore letto=" +value );
		            		// fine debug 
		            		
		            		regs.setRegisterValue(bytecode.getArg1(), value);
		            		
		            		// TODO debug
		            		//System.err.println("LOADW\tnuovo valore registro = " +regs.getRegisterValue(bytecode.getArg1()) +"\n");
		            		// fine debug 
		            		
			                break;
		              
		            	case SVMParser.BRANCH : 
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		//System.err.println("BRANCH "+arg1); //TODO
		            		regs.setIP(arg1);
			                break;
		              
		            	case SVMParser.BRANCHEQ : 
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		value = regs.getRegisterValue(bytecode.getArg1());
		            		if( value == regs.getRegisterValue(bytecode.getArg2()) )
		            			regs.setIP(arg3);
		            		//System.err.println("BRANCHEQ "+value +"  ==   "+regs.getRegisterValue(bytecode.getArg2())); //TODO
			                break;
		              
		            	case SVMParser.BRANCHLESSEQ :
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		value = regs.getRegisterValue(bytecode.getArg1());
		            		if( value <= regs.getRegisterValue(bytecode.getArg2()) )
		            			regs.setIP(arg3);
		            		//System.err.println("BRANCHLESSEQ "+value +"  <=   "+regs.getRegisterValue(bytecode.getArg2())); //TODO
			                break;
		            	
		            	case SVMParser.JR :
		            		address = regs.getRegisterValue( bytecode.getArg1() );
		            		//System.err.println("JR "+address +"    reg "+bytecode.getArg1()); //TODO
		            		regs.setIP(address);
			                break;
			                
		            	case SVMParser.JAL :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		regs.setRegisterValue("$ra", regs.getIP() );
		            		//System.err.println("JAL "+arg1); //TODO
		            		regs.setIP(arg1);
			                break;
			                
		            	case SVMParser.MOVE :
		            		value = regs.getRegisterValue( bytecode.getArg2() );
		            		regs.setRegisterValue(bytecode.getArg1(), value);
			                break;
			                
		            	case SVMParser.PRINT :		//TODO da rivedere
		            		if( regs.getSP() <= MEMSIZE + CODESIZE ) {
		            			arg1 = regs.getRegisterValue(bytecode.getArg1());
		            			arg2 = Integer.parseInt( bytecode.getArg2() );
		            			if( arg2 == 1 )
		            				System.out.println( "-> " + arg1 );
		            			else {
		            				if( arg1 == 1 )
		            					System.out.println( "-> true");
		            				else
		            					System.out.println( "-> false");
		            			}
		            		}
		            		else		//TODO questo ramo else forse non serve perchè non prendo il valore da stampare dallo stack
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
		            		clearHeap();
		            		printCPU(bytecode.toString());
		            		System.out.println("\nEnd of execution!");
			             	return;
			             	
		            	default:
		            		System.err.println("Error: invalid instruction "+bytecode.getInstr());
		    	            return;
		            }
		            printCPU(bytecode.toString());
	    		} 
	    	}	
	    	
    	}catch(MemoryException | AccessToFreeMemoryCellException | 
    			MissingValueCellException | InvalidInstructionException e) {
    		System.err.println("Error during execution:");
    		System.err.println(e.getMessage());
    		System.err.println("Instruction: " + bytecode.getInstr());
    		printCPU("Exception");
    	}
    	
    	
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
    
    private void printCPU(String s) {
    	if( debug ) {
	    	System.out.println("\n\n\n\n------------------------------------------");
	    	System.out.println("|           CPU status	                 |");
	    	System.out.println("------------------------------------------");
	    	
	    	if( s.compareTo("") != 0 ) {
	        	System.out.print("|           " +s);
	        	for(int i = 0; i < 28-s.length(); i ++)
	        		System.out.print(" ");
	        	System.out.print(" |\n");
	        	System.out.println("------------------------------------------");
	    	}
	    	
	    	System.out.println(regs.toString());
	    	
	    	System.out.println("------------------------------------------");
	    	
	    	System.out.println("  Heap:");
	    	for(int i = CODESIZE; i <= regs.getHP(); i ++) {
	    		if( !memory.get(i).isFree() ) {
		    		String str = "[" + i + "]\t" + memory.get(i);
		    		
		    		// "<--  $hp" non verrà stampata mai perchè $hp dovrebbe puntare sempre ad una cella libera
		    		if( regs.getHP() == i ) str = str + "\t<--  $hp";
		    		
		    		System.out.println("  " + str);
	    		}	
	    	}
	    	
	    	System.out.println("\n  Stack:");
	    	for(int i = regs.getHP()+1; i <= CODESIZE+MEMSIZE-1; i ++) {
	    		
	    		if( !memory.get(i).isFree() ) {
	    			
		    		String str = "[" + i + "]\t" + memory.get(i);
		    		
		    		if( regs.getAL() == i ) str = str + "\t<--  $al";
		    		if( regs.getFP() == i ) str = str + "\t<--  $fp";
		    		if( regs.getSP() == i ) str = str + "\t<--  $sp";
		    		
		    		System.out.println("  "+str);
		    		
		    		if( stack.get(i) )
		    			System.out.println("");
	    		}	
	    	}
	    	System.out.println("------------------------------------------");
    	}
    }
    
    
    private void writeCode() {
    	BufferedWriter out;
    	String str="";
    	for(Instruction i : code)
    		str = str + i.toString() +"\n";
    	
    	
		try {
			out = new BufferedWriter(new FileWriter("bytecode"));
			out.write(str);
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
    }
    
    void clearHeap() {
    	for(int i = CODESIZE; i <= CODESIZE+MEMSIZE-1; i ++) 
    		if( !memory.get(i).isFree() ) 
    			memory.get(i).free();
    }
    
}