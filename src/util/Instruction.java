package util;

public class Instruction {
	
	private int instr;
	private String arg1;
	private Integer offset;
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
		this.offset = null;
	}
	
	public Instruction(int instr) {
		this.instr = instr;		
		this.arg1 = null;		
		this.arg2 = null;
		this.arg3 = null;
		this.offset = null;
	}

	public Instruction(int instr, String arg1, String arg2, String arg3) {
		this.instr = instr;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.arg3 = arg3;
		this.offset = null;
	}

	public Instruction (int instr, String arg1, String arg2) {
		this.instr = instr;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.arg3 = null;
		this.offset = null;
	} 
	
	public Instruction(int instr, String arg1, int offset) {
		this.instr = instr;
		this.arg1 = arg1;
		this.arg2 = null;
		this.arg3 = null;
		this.offset = offset;
	}
	
	public Instruction(int instr, String arg1, int offset, String arg2) {
		this.instr = instr;
		this.arg1 = arg1;
		this.arg2 = arg2;
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
		String str = "" + getInstructionNameFromNumber(instr) +" ";
		if( arg1 != null )
			str = str + arg1 + " ";
		
		if( offset != null )
			str = str + Integer.toString(offset) + " ";
		
		if( arg2 != null )
			str = str + arg2 + " ";
		
		if( arg3 != null )
			str = str + arg3 + " ";
		
		return str;
	}
	
	public String getInstructionNameFromNumber(int x) {
		switch(x) {
			case 3:
				return "PUSH";
			case 4:
				return "POP";
			case 5:
				return "ADD";
			case 6:
				return "SUB";
			case 7:
				return "MULT";
			case 8:
				return "DIV";
			case 9:
				return "ADDI";
			case 10:
				return "SUBI";
			case 11:
				return "MULTI";
			case 12:
				return "DIVI";
			case 13:
				return "LI";
			case 14:
				return "LB";
			case 15:
				return "STOREW";
			case 16:
				return "LOADW";
				
			case 17:
				return "BRANCH";
			case 18:
				return "BRANCHEQ";
			case 19:
				return "BRANCHLESSEQ";
			case 20:
				return "JR";
			case 21:
				return "JAL";
			case 22:
				return "PRINT";
			case 23:
				return "DELETION";
			case 24:
				return "MOVE";
			case 25:
				return "AND";
			case 26:
				return "OR";
			case 27:
				return "NOT";
			case 28:
				return "HALT";
			default: 
				return "no name instruction";
		}
	}
}
