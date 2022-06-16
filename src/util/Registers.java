package util;

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
		this.ip = ip+1;
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
