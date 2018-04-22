package com.mizan.dsg.dataclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
	private String name;
	private String id;
	//private List<Edge> edges;
	private int day;
	//private boolean processed;
	private List<String> appids; //list of all ids 
	private List<String> appidsof400;
	
	private List<User> adjecencyUsers;
	
	private boolean isCombined; 
	private List<User> conbinedUsers;
	private List<Integer> DSGIDs;
	private int dsgID;
	private HashMap<String, Integer> edges;
	
	
	
	private int ccID = -1;


	public User(String name, String id) {
		this.name = name;
		this.id = id;
		appids = new ArrayList<String>();
		setAppidsof400(new ArrayList<String>());
		//edges = new ArrayList<Edge>();
		setAdjecencyUsers(new ArrayList<User>());
		DSGIDs = new ArrayList<Integer>();
		setEdges(new HashMap<String, Integer>());
	}

	public String getName() {
		return name;
	}
	
	public void addDSGID(int dsgID){
		DSGIDs.add(dsgID);
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

//	public List<Edge> getEdges() {
//		return edges;
//	}
//
//	public void setEsges(List<Edge> esges) {
//		this.edges = esges;
//	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

//	public boolean isProcessed() {
//		return processed;
//	}
//
//	public void setProcessed(boolean processed) {
//		this.processed = processed;
//	}

	public List<String> getAppids() {
		return appids;
	}

	public void setAppids(List<String> appids) {
		this.appids = appids; 
	}

	public boolean addNewAppid(String appid) {
		if (!appids.contains(appid)) { 
			appids.add(appid);
			return true;
		}
		return false;
	}
	
	public void addEdge(String appid, int weight) {
		edges.put(appid,weight); 
	}
	
	
	public boolean addNewAppid400(String appid) {
		if (!getAppidsof400().contains(appid)) { 
			getAppidsof400().add(appid);
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		String returnString = name + " count " + appids.size() + " id: " + id + " days :" + day + " adjecency users :: " + adjecencyUsers.size() + " DSG ID " + dsgID;
		// + " appids: " + appids.toString()
		return returnString;
	}

//	public void addNewEdge(Edge edge) {
//		edges.add(edge);
//	}
 
	public void addAdjecencyUsers(User user) { 
		if (!getAdjecencyUsers().contains(user)) {
			getAdjecencyUsers().add(user);
		}
	}

	public List<User> getAdjecencyUsers() {
		return adjecencyUsers;
	}

	public void setAdjecencyUsers(List<User> adjecencyUsers) {
		this.adjecencyUsers = adjecencyUsers;
	}

	public void deleteFromAdjecencyList(User v) {
		adjecencyUsers.remove(v);
	}

	
	public User clone() {
		// TODO Auto-generated method stub
		User user = new User(this.name,this.id);

		user.setDay(day);
		user.getAppids().addAll(appids);
		user.getAdjecencyUsers().addAll(adjecencyUsers);

		return user;
	}

	public boolean isCombined() {
		return isCombined;
	}

	public void setCombined(boolean isCombined) {
		this.isCombined = isCombined;
	}

	public List<User> getConbinedUsers() {
		return conbinedUsers;
	}

	public void setConbinedUsers(List<User> conbinedUsers) {
		this.conbinedUsers = new ArrayList<User>();
		
		for (User user : conbinedUsers) {
			this.conbinedUsers.add(user);
		}
	}
	
	public void clearAdjecency() {
		this.adjecencyUsers = new ArrayList<User>();
	}


	public List<Integer> getDSGIDs() {
		return DSGIDs;
	}


	public void setDSGIDs(List<Integer> dSGIDs) {
		DSGIDs = dSGIDs;
	}

	public int getDsgID() {
		return dsgID;
	}

	public void setDsgID(int dsgID) {
		this.dsgID = dsgID;
	}

	public List<String> getAppidsof400() {
		return appidsof400;
	}

	public void setAppidsof400(List<String> appidsof400) {
		this.appidsof400 = appidsof400;
	}

	public HashMap<String, Integer> getEdges() {
		return edges;
	}

	public void setEdges(HashMap<String, Integer> edges) {
		this.edges = edges;
	}
}
