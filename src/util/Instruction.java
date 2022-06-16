package util;

public class Instruction {
	
	private int instr;
	private String arg1;
	private int offset;
	private String arg2;
	private String arg3;

	public Instruction(int instr, String arg1, int offset, String arg2, String arg3) {
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.arg3 = arg3;
		this.instr = instr;
		this.offset = offset;
	}
	
	public Instruction(int instr, String arg1) {
		this.arg1 = arg1;
		this.instr = instr;
		this.arg2 = null;
		this.arg3 = null;
		this.offset = 0;
	}
	
	public Instruction(int instr) {
		this.instr = instr;		
		this.arg1 = null;		
		this.arg2 = null;
		this.arg3 = null;
		this.offset = 0;
	}

	public Instruction(int instr, String arg1, String arg2, String arg3) {
		this.instr = instr;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.arg3 = arg3;
		this.offset = 0;
	}

	public Instruction (int instr, String arg1, String arg2) {
		this.instr = instr;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.arg3 = null;
		this.offset = 0;
	} 
	
	public Instruction(int instr, String arg1, int offset) {
		this.instr = instr;
		this.arg1 = arg1;
		this.arg2 = null;
		this.arg3 = null;
		this.offset = offset;
	}

	public String getArg1() {
		return this.arg1;
	}

	public String getArg2() {
		return this.arg2;
	}

	public String getArg3() {
		return this.arg3;
	}

	public int getInstr() {
		return this.instr;
	}

	public int getOffset() {
		return this.offset;
	}
	
	
	public String toString() {
		String str = "" + Integer.toString(instr) +" ";
		if( arg1 != null )
			str = str + arg1 + " ";
		
		str = str + Integer.toString(offset) + " ";
		
		if( arg2 != null )
			str = str + arg2 + " ";
		if( arg3 != null )
			str = str + arg3 + " ";
		return str;
	}
}
