package util;

public class Effect {

	private static final int INIT = 0;
	private static final int RW = 1;
	private static final int DEL = 2;
	private static final int ERR = 3;

	public static final Effect INITIALIZED = new Effect(INIT);
	public static final Effect READ_WRITE = new Effect(RW);
	public static final Effect DELETED = new Effect(DEL);
	public static final Effect ERROR = new Effect(ERR);
	
	private int value;

	public Effect(int value) {
		this.value = value;
	}

	public Effect(Effect e) {
		this(e.value);
	}
	
	public Effect() {
		this.value = INIT;
	}

	
	public void setEffect (Effect status) {
		this.value = status.value;
	}
	
	public int getEffectValue() {
		return this.value;
	}
	
	public static Effect max(final Effect e1, final Effect e2) {
		return new Effect(Math.max(e1.value, e2.value));
	}
	
	public static Effect seq(final Effect e1, final Effect e2) {
		if (max(e1, e2).value <= RW)
			return max(e1, e2);
		
		else if ((e1.value <= RW && e2.value == DEL) || (e1.value == DEL && e2.value == INIT))
			return new Effect(DEL);
		
		return new Effect(ERR);
	}
	
	public static Effect par(final Effect e1, final Effect e2) {
		return max(seq(e1, e2), seq(e2, e1));
	}		
	
	public boolean equals(Effect e) {
		return this.value == e.value;
	}
	
	@Override
	public String toString() {
		switch (this.value) {
        	case INIT:
        		return "INITIALIZED";
        	case RW:
        		return "READ_WRITE";
        	case DEL:
        		return "DELETE";
        	case ERR:
        		return "ERROR";
        	default:
        		return "";
		}
	}
}
