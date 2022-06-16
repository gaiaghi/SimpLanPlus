package util;

import exception.AccessToFreeMemoryCellException;
import exception.MemoryException;
import exception.MissingValueCellException;

public class Cell {

	/* isInCodeArea =
	 * 		- true 		se la cella di memoria si trova nella code area
	 * 		- false 	se la cella di memoria si trova in other space (stack/heap)
	 *  */
	private boolean isInCodeArea;
	private Instruction code;
	private Integer data;
	private boolean isFree;
	
	public Cell(boolean b) {
		isInCodeArea = b;
		code = null;
		data = null;
		isFree = true;
	}
	
	public Cell(Instruction c) {
		isInCodeArea = true;
		code = c;
		data = null;
		isFree = false;
	}
	
	public Cell(Integer d) {
		isInCodeArea = false;
		code = null;
		data = d;
		isFree = false;
	}
	
	
	public boolean isFree() {
		return isFree;
	}
	
	
	public void free() {
		isFree = true;
		if( isInCodeArea )
			code = null;
		else
			data = null;
	}
	
	
	public Integer getData() throws AccessToFreeMemoryCellException, MemoryException, MissingValueCellException {
		
		if( isInCodeArea )
			throw new MemoryException("You cannot find data in code area.");
		
		if( isFree )
			throw new AccessToFreeMemoryCellException("You cannot access to a free memory cell.");
		
		if( data == null )
			throw new MissingValueCellException("You cannot access to a memory cell without data.");
		
		return data;
	}
	
	
	public void setData(Integer d) throws MemoryException {
		if( isInCodeArea )
			throw new MemoryException("You can write only instruction in code area.");
		
		data = d;
		isFree = false;
	}
	
	
	public Instruction getInstruction() throws AccessToFreeMemoryCellException, MemoryException, MissingValueCellException {
		
		if( !isInCodeArea )
			throw new MemoryException("You cannot find instruction in stack/heap.");
		
		if( isFree )
			throw new AccessToFreeMemoryCellException("You cannot access to a free memory cell.");
		
		if( code == null )
			throw new MissingValueCellException("You cannot access to a memory cell without instruction.");
		
		return code;
	}

	
	public void setInstruction(Instruction c) throws MemoryException {
		if( !isInCodeArea )
			throw new MemoryException("You can write only data in stack/heap.");
		
		code = c;
		isFree = false;
	}
	
	
	
	public String toString() {
		String str = "--- empty cell ---";
		
		if( isInCodeArea && code != null )
			str = code.toString();
		
		if( !isInCodeArea && data != null )
			str = Integer.toString(data);
		
		return str;
	}
	
}
