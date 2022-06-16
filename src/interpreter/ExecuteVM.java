package interpreter;

import java.util.ArrayList;

import exception.AccessToFreeMemoryCellException;
import exception.MemoryException;
import exception.MissingValueCellException;
import svm.SVMParser;
import util.Cell;
import util.Instruction;
import util.Registers;

public class ExecuteVM {
    
	// dimensione code area
    public int CODESIZE = 10000;
    
    // dimensione other space (stack + heap)
    public int MEMSIZE = 10000;
 
    private ArrayList<Instruction> code;
    private ArrayList<Cell> memory;
    
    private Registers regs;
    /*private int ip = 0;
    private int sp = MEMSIZE;
    private int hp = 0;       
    private int fp = MEMSIZE; 
    private int ra;           
    private int ret;
    private int a0;
    private int al;*/
    
    public ExecuteVM(ArrayList<Instruction> c, int sizeCodeArea, int sizeOtherSpace) {
      code = c;
      CODESIZE = sizeCodeArea;
      MEMSIZE = sizeOtherSpace;
      memory = new ArrayList<Cell>(CODESIZE + MEMSIZE);
      
      //TODO setta valori iniziali registri, devi settare hp
      //a0, t1, sp, fp, al, ra, hp, ret, ip
      regs = new Registers(0,0,MEMSIZE,MEMSIZE,0,0,0,0,0);
    }
    
    
    
    
    public void cpu(){
      
    	try {
	    	while ( true ) {
	    	
	    		if( regs.getHP() + 1 >= regs.getSP() ) {
	    			System.out.println("\nError: Out of memory");
	    			return;
	    		}
	    		else {
		    		Instruction bytecode = code.get( regs.getIP() );  // fetch
		    		regs.addOneToIP();
		            int v1,v2;
		            int address;
		            int arg1;
		            int arg2;
		            int arg3;
		            int offset;
		            
		            switch ( bytecode.getInstr() ) {
		            	
		            	case SVMParser.PUSH:
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		/*
		            		push( Integer.parseInt(code.get( regs.getIP() ).getArg1() ) );
		            		regs.addOneToIP();
		            		*/
		            		break;
		            	
		            	case SVMParser.POP:
		            		
		            		/*
		            		pop();
		            		*/
		            		break;
		              
		            	case SVMParser.ADD :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		/*
			            	v1=pop();
			                v2=pop();
			                push(v2 + v1);
			                */
			                break;
		            	
		            	case SVMParser.SUB :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		/*
			            	v1=pop();
		            		v2=pop();
		            		push(v2 - v1);
			                */
		            		break;
		              
		            	case SVMParser.MULT :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		/*
			            	v1=pop();
			                v2=pop();
			                push(v2 * v1);
			                */
			                break;
		              
		            	case SVMParser.DIV :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		/*
			            	v1=pop();
			                v2=pop();
			                push(v2 / v1);
			                */
			                break;  
			                
		            	case SVMParser.ADDI :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		
			                break;
		            	
		            	case SVMParser.SUBI :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		
		            		break;
		              
		            	case SVMParser.MULTI :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		
			                break;
		              
		            	case SVMParser.DIVI :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		
			                break;  
			            
		            	case SVMParser.LI :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		
			                break; 
		                
		            	case SVMParser.LB :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		
			                break; 
		              
		            	case SVMParser.STOREW : 
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		offset = bytecode.getOffset();
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		/*
			            	address = pop();
			            	writeOnMemory(address, pop() );
			                */
			                break;
		              
		            	case SVMParser.LOADW : //
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		offset = bytecode.getOffset();
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		/*
			            	// check if object address where we take the method label
			            	// is null value (-10000)
			                if ( readFromMemory(regs.getSP() ) == -10000 ) {
			                	System.out.println("\nError: Null pointer exception");
			                	return;
			                }  
			                push( readFromMemory(pop()) );
			                */
			                break;
		              
		            	case SVMParser.BRANCH : 
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		/*
			            	address = Integer.parseInt( code.get(regs.getIP()).getArg1() );
			                regs.setIP(address); 
			                */
			                break;
		              
		            	case SVMParser.BRANCHEQ : //
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		
		            		/*
			            	address = Integer.parseInt(code.get(regs.getIP()).getArg3());
			                regs.addOneToIP();
			                v1 = pop();
			                v2 = pop();
			                if (v2 == v1) 
			                	regs.setIP(address);
			                */
			                break;
		              
		            	case SVMParser.BRANCHLESSEQ :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		
		            		/*
			            	address = Integer.parseInt(code.get(regs.getIP()).getArg3());
			                regs.addOneToIP();
			                v1=pop();
			                v2=pop();
			                if (v2 <= v1) 
			                	regs.setIP(address);
			                */
			                break;
		            	
		            	case SVMParser.JR :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		
			                break;
			                
		            	case SVMParser.JAL :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		
			                break;
			                
		            	case SVMParser.MOVE :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		
			                break;
			                
		            	case SVMParser.PRINT :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		
		            		/*
			            	if( regs.getSP() < MEMSIZE )
		            			System.out.println( readFromMemory(regs.getSP()) );
		            		else
		            			System.out.println("Empty stack!");
			                */
			                break;
			                
		            	case SVMParser.DELETION :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		
			                break;
			                
		            	case SVMParser.AND :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		
			                break;
			                
		            	case SVMParser.OR :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		arg3 = Integer.parseInt( bytecode.getArg3() );
		            		
			                break;
			                
		            	case SVMParser.NOT :
		            		arg1 = Integer.parseInt( bytecode.getArg1() );
		            		arg2 = Integer.parseInt( bytecode.getArg2() );
		            		
			                break;
		             
		            	case SVMParser.HALT :
		            		
		            		/*
			            	//to print the result 
			             	System.out.println("\nResult: " + readFromMemory(regs.getSP()) + "\n");
			             	*/
			             	return;
			             	
		            	default:
		            		System.out.println("Error: invalid instruction "+bytecode.getInstr());
		    	            return;
		            }
	    		} 
	    	}	
    	}catch(MemoryException | AccessToFreeMemoryCellException | MissingValueCellException e) {
    		System.out.println("Error during execution:");
    		System.out.println(e.getMessage());
    	}
    } 
    
    
    private int pop() throws AccessToFreeMemoryCellException, MemoryException, MissingValueCellException {
    	int value = readFromMemory(regs.getSP());
    	regs.setSP( regs.getSP() + 1);
    	return value;
    }
    
    
    private void push(int value) throws MemoryException {
    	try {
	    	regs.setSP( regs.getSP() - 1);
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
    
}