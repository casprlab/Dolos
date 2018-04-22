package com.mizan.dsg.dataclass;

import java.util.HashMap;

public class Userv2 {
	public String id; 
	public HashMap<String, Integer> edges;
	public int adjecencyUserCount;
	public int componentID;
	public String decedent;
	public int DSGId;
	public int nextDSGId;
	
	private int ccID = -1;
	
	public Userv2(String id) {
		this.id = id;
		this.edges = new HashMap<String, Integer>();
		this.decedent = null;
	}
	
	public void addEdge(String appid, int weight) {
		edges.put(appid,weight); 
	}
	
	public Userv2 clone(){
		Userv2 userv2 = new Userv2(this.id);
		userv2.adjecencyUserCount = adjecencyUserCount;
		
		HashMap<String, Integer> newedges = new HashMap<String, Integer>();
		
		for (String userid : edges.keySet()) {
			newedges.put(userid, edges.get(userid));
		}
		
		userv2.edges = newedges;
		
		return userv2;
	}
	
	public void deleteFromAdjecencyList(Userv2 v){
		edges.remove(v.id);
	}
	
	@Override
	public String toString() {
		String s = id + " " + edges.size();   
		return s;
	}

	public int getCcID() {
		return ccID;
	}

	public void setCcID(int ccID) {
		this.ccID = ccID;
	}
}
