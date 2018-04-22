package com.mizan.dsg.dataclass;

public class Edge {
	private User user1;
	private User user2; 
	private int weight; 
	private int dt;  // different between days. 
	
	public Edge(User u1, User u2, int weight, int dt) {
		this.user1 = u1;
		this.user2 = u2;
		this.setWeight(weight);
		this.setDt(dt);
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getDt() {
		return dt;
	}

	public void setDt(int dt) {
		this.dt = dt;
	}
	
	@Override
	public String toString() {
		String returnString = "edge in " + user1.getName() + " and " + user2.getName() + " weight " + weight + " days " + dt;
		return returnString;
	}

}
