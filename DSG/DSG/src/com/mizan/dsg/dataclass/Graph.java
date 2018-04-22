package com.mizan.dsg.dataclass;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class Graph {
	private List<User> users;
	private List<Edge> edges;
	private int n;
	private double triangleDensity;
	double[][] adjMat;
	Matrix adjmatrix;
	int ID;
	
	public Graph() {
		setUsers(new ArrayList<User>());
		setEdges(new ArrayList<Edge>());
		this.adjMat = new double[n][n];
	}
	
	public Graph(List<User> users, List<Edge> edges) {
		this.users = new ArrayList<User>();
		this.edges = new ArrayList<Edge>();
		
		for (Edge edge : edges) {
			this.edges.add(edge);
		}
		
		for (User user : users) {
			this.users.add(user);
		}
		
		this.n = this.users.size();
		generateAdjacencyMatrix();
	}

	public void addUser(User u) {
		if (!this.users.contains(u)) {
			this.users.add(u);
		}
	}
	
	public void addEdge(Edge e){
		if (!this.edges.contains(e)) {
			this.edges.add(e);
		}
	}
	

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public double getDensity() {
		return triangleDensity;
	}

	public void setDensity(double density) {
		this.triangleDensity = density;
	}
	
	@Override
	public String toString() {
		String s =  "user size " + users.size() + " edge size " + edges.size() + " density " + triangleDensity; 
		return s;
	}

	public void generateAdjacencyMatrix() {
		n = users.size();
		this.adjMat = new double[n][n];
		
		for (Edge edge : edges) {
			int n1 = users.indexOf(edge.getUser1());
			int n2 = users.indexOf(edge.getUser2());
			
			this.adjMat[n1][n2] = 1;
			this.adjMat[n2][n1] = 1;
			
		}
	}
	
//	public void displayAdjacencyMatrix() {
//		CustomMatrix.displayAdjacencyMatrix(adjMat);
//	}

	public Graph clone(){
		List<User> duplicateusers = new ArrayList<User>();
		
		for (User user : users) {
			duplicateusers.add(user);
		}
		
		List<Edge> duplicateedges = new ArrayList<Edge>();
		
		for (Edge edge : edges) {
			duplicateedges.add(edge);
		}
		
		Graph newgraph = new Graph(duplicateusers,duplicateedges);
		newgraph.setDensity(triangleDensity);
		return newgraph;
	}

	public User getLeastConnectedNode() {
		User user = null;
		
		int leastcount = 1000000;
		
		//displayAdjacencyMatrix();
		for (User u : getUsers()) {
			List<User> adjecencyUsers = u.getAdjecencyUsers();
			
			int count = 0;

			for (int i = 0; i < adjecencyUsers.size(); i++) {
				User user1 = adjecencyUsers.get(i);
				for (int j = i; j < adjecencyUsers.size(); j++) {
					User user2 = adjecencyUsers.get(j);
					
					int indexOf1 = users.indexOf(user1);
					int indexOf2 = users.indexOf(user2);
					
					if (indexOf1 == -1 || indexOf2 == -1) { 
						continue;
					}
					
					//System.out.println("indexes " + indexOf1 + " "+ indexOf2); 	
					
					if (this.adjMat[indexOf1][indexOf2] == 1) {
						count++;
					}
				}
			}
			
			//System.out.println("this time " + count); 
			
			if (count < leastcount) { 
				leastcount = count;
				user = u;
				//System.out.println("least cont changing " + count);
			}
		}
		
		return user;
	}

	public void deleteNode(User v) {
		users.remove(v);
		
		for (User user : users) {
			user.deleteFromAdjecencyList(v); 
		}
		
		List<Edge> tempedges = new ArrayList<Edge>();
		
		for (Edge edge : edges) {
			if (edge.getUser1() == v) {
				//edges.remove(edge);
				tempedges.add(edge);
				continue;
			}
			
			if (edge.getUser2() == v) {
				//edges.remove(edge); 
				tempedges.add(edge);
				continue;
			}
		}
		
		for (Edge edge : tempedges) {
			edges.remove(edge);
		}
		
		generateAdjacencyMatrix();
	}
	
	public static double calculateTriangleCount(Matrix matrix){ 
		Matrix a3 = matrix.times(matrix).times(matrix);
		//a3.print(n, n); 
		double trace = a3.trace();
		//System.out.println(trace);
		double triangleCount = trace/6;
		return triangleCount; 
	}

	public double calculateDensity() {
		Matrix adjmatrix = new Matrix(adjMat);  
		double calculateTriangleCount = calculateTriangleCount(adjmatrix);
		n = adjMat.length;
		triangleDensity = calculateTriangleCount/n;		
		return triangleDensity;
	}
	
	public List<String> getUsersIDs() {
		List<String> userIDs = new ArrayList<String>();
		
		for (User user : users) {
			if (!user.isCombined()) {
				userIDs.add(user.getId());
			}
		}
		
		return userIDs;
	}
	
	public DSGInfo convertDSGInfo() {
		DSGInfo dsgInfo = new DSGInfo();
		dsgInfo.setAllUserIDs(getUsersIDs());
		dsgInfo.setMinimumDegree(getMinimumEdgeDegree()); 
		dsgInfo.setEdgeDensity(getEdgeDensity());
		
		return dsgInfo;
	}

	private float getEdgeDensity() {
		float density = 0;
		float newNchoose2 = n * (n + 1) / 2;
		density = edges.size()/newNchoose2;
		
		return density;
	}

	private int getMinimumEdgeDegree() {
		int minimumDegree = 100000;
		
		for (User user : users) {
			if (user.getAdjecencyUsers().size() < minimumDegree) {
				minimumDegree = user.getAdjecencyUsers().size();
			}
		}
		
		return minimumDegree;
	}
	
	

	public void addNode(User v) {
		users.add(v);
		
		for (User user : users) {
			user.getAppids();
			
			int count = 0;
			for (String appid : user.getAppids()) {
				if (v.getAppids().contains(appid)) {
					count++;
				}
			}
			
			if (count > 0) {
				user.addAdjecencyUsers(v);
				edges.add(new Edge(user, v, count, 0)); 
			}
		}
		
	}

	public void setID(int id) {
		ID = id;
		
		for (User user : users) {
			user.addDSGID(id);	
		}
	}

	public int getID() {
		return ID;
	}
}
