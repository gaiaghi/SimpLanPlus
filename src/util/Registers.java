package util;

import exception.InvalidInstructionException;

public class Registers {

	private int a0;
	private int t1;
	private int sp;
	private int fp;
	private int al;
	private int ra;
	private int hp;
	private int ret;  
	private int ip;
	
	
	public Registers(int a0, int t1, int sp, int fp, int al, int ra, int hp, int ret, int ip) {
		this.a0 = a0;
		this.t1 = t1;
		this.sp = sp;
		this.fp = fp;
		this.al = al;
		this.ra = ra;
		this.hp = hp;
		this.ret = ret;
		this.ip = ip;
	}


	public int getA0() {
		return a0;
	}


	public void setA0(int a0) {
		this.a0 = a0;
	}


	public int getT1() {
		return t1;
	}


	public void setT1(int t1) {
		this.t1 = t1;
	}


	public int getSP() {
		return sp;
	}


	public void setSP(int sp) {
		this.sp = sp;
	}


	public int getFP() {
		return fp;
	}


	public void setFP(int fp) {
		this.fp = fp;
	}


	public int getAL() {
		return al;
	}


	public void setAL(int al) {
		this.al = al;
	}


	public int getRA() {
		return ra;
	}


	public void setRA(int ra) {
		this.ra = ra;
	}
	
	
	public int getHP() {
		return hp;
	}


	public void setHP(int hp) {
		this.hp = hp;
	}


	public int getRET() {
		return ret;
	}


	public void setRET(int ret) {
		this.ret = ret;
	}


	public int getIP() {
		return ip;
	}


	public void setIP(int ip) {
		this.ip = ip;
	}
	
	public void addOneToIP() {
		ip = ip+1;
	}
	
	public void moveUpSP() {
		sp = sp - 1;
	}
	
	public void moveDownSP() {
		sp = sp + 1;
	}
	
	public int getRegisterValue(String reg) throws InvalidInstructionException {
		switch( reg ) {
			case "$a0":
				return getA0();
			
			case "$t1":
				return getT1();
				
			case "$sp":
				return getSP();
				
			case "$fp":
				return getFP();
				
			case "$al":
				return getAL();
				
			case "$ra":
				return getRA();
				
			case "$hp":
				return getHP();
				
			case "$ret":
				return getRET();
				
			case "$ip":
				return getIP();
			
			default:
				throw new InvalidInstructionException("Unknown register.");	
		}
		
	}
	
	
	public void setRegisterValue(String reg, int value) throws InvalidInstructionException {
		switch( reg ) {
			case "$a0":
				setA0(value);
			
			case "$t1":
				setT1(value);
				
			case "$sp":
				setSP(value);
				
			case "$fp":
				setFP(value);
				
			case "$al":
				setAL(value);
				
			case "$ra":
				setRA(value);
				
			case "$hp":
				setHP(value);
				
			case "$ret":
				setRET(value);
				
			case "$ip":
				setIP(value);
			
			default:
				throw new InvalidInstructionException("Unknown register.");	
		}
		
	}
	
	
	public String toString() {
		String str = "Registers:\n";
		
		str = str + "$ip = " + ip +"\n";
		
		str = str + "$sp = " + sp +"\t";
		str = str + "$fp = " + fp +"\t";
		str = str + "$al = " + al +"\t";
		str = str + "$hp = " + hp +"\n";
		
		str = str + "$a0 = " + a0 +"\t";
		str = str + "$t1 = " + t1 +"\t";
		str = str + "$ra = " + ra +"\t";
		str = str + "$ret = " + ret;
		
		return str;
	}
	

}
