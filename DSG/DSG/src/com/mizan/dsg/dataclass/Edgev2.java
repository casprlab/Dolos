package com.mizan.dsg.dataclass;

public class Edgev2 {
	public String userid1;
	public String userid2; 
	public int weight; 
	
	public Edgev2(String u1, String u2, int weight) {
		this.userid1 = u1;
		this.userid2 = u2;
		this.weight = weight;
	}

	@Override
	public String toString() {
		String returnString = "edge in " + userid1 + " and " + userid2 + " weight " + weight;
		return returnString;
	}
}
