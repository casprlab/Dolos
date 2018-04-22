package com.mizan.dsg.dataclass;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Jama.Matrix;

public class Graphv2 {
	public List<Userv2> users;
	public List<Edgev2> edges;
	public List<String> alluserID;
	
	public int n;
	public double triangleDensity;
	public double[][] adjMat;
	public int ID;
	
	public Graphv2() {
		users = new ArrayList<Userv2>();
		edges = new ArrayList<Edgev2>();
		this.adjMat = new double[n][n];
	}
	
	public Graphv2(List<Userv2> users, List<Edgev2> edges) { 
		this.users = new ArrayList<Userv2>();
		this.edges = new ArrayList<Edgev2>(); 
		
		this.users.addAll(users);
		this.edges.addAll(edges);
		
		this.n = this.users.size();
		
		alluserID = new ArrayList<String>();
		
		for (Userv2 user : users) alluserID.add(user.id);
		
		generateAdjacencyMatrix();
	}

	public void addUser(Userv2 u) {
		if (!this.users.contains(u)) {
			this.users.add(u);
		}
	}
	
	public void addEdge(Edgev2 e){
		if (!this.edges.contains(e)) {
			this.edges.add(e);
		}
	}
	
	
	@Override
	public String toString() {
		String s =  "user size " + users.size() + " edge size " + edges.size() + " density " + triangleDensity; 
		return s;
	}

	public void generateAdjacencyMatrix() {
		//System.out.println("generating adj matrix for n ::" + users.size()); 
		n = users.size();
		this.adjMat = new double[n][n];
		
		for (Edgev2 edge : edges) {
			int n1 = alluserID.indexOf(edge.userid1);
			int n2 = alluserID.indexOf(edge.userid2);
			
			this.adjMat[n1][n2] = 1;
			this.adjMat[n2][n1] = 1;
		}
	}
	
//	public void displayAdjacencyMatrix() {
//		CustomMatrix.displayAdjacencyMatrix(adjMat);
//	}

	public Graphv2 clone(){
		List<Userv2> duplicateusers = new ArrayList<Userv2>();
		
		for (Userv2 user : users) {
			duplicateusers.add(user);
		}
		
		List<Edgev2> duplicateedges = new ArrayList<Edgev2>();
		
		for (Edgev2 edge : edges) {
			duplicateedges.add(edge);
		}
		
		Graphv2 newgraph = new Graphv2(duplicateusers,duplicateedges);
		newgraph.triangleDensity = triangleDensity;
		return newgraph;
	}

	public Userv2 getLeastConnectedNode() {
		Userv2 user = null;
		int leastcount = 1000000;
		
		//displayAdjacencyMatrix();
		for (Userv2 u : users) {
			ArrayList < String > adjecencyUsers = new ArrayList < String > ();
			adjecencyUsers.addAll(u.edges.keySet());
			
			int count = 0;

			for (int i = 0; i < adjecencyUsers.size(); i++) {
				String user1 = adjecencyUsers.get(i);  
				for (int j = i; j < adjecencyUsers.size(); j++) {
					String user2 = adjecencyUsers.get(j);
					
					int indexOf1 = alluserID.indexOf(user1);
					int indexOf2 = alluserID.indexOf(user2);
					
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

	public void deleteNode(Userv2 v) {
		users.remove(v);
		alluserID.remove(v.id);
		
		for (Userv2 user : users) {
			user.deleteFromAdjecencyList(v); 
		}
		
		List<Edgev2> tempedges = new ArrayList<Edgev2>(); 
		
		for (Edgev2 edge : edges) {
			if (edge.userid1.equalsIgnoreCase(v.id)) {
				//edges.remove(edge);
				tempedges.add(edge);
				continue;
			}
			
			if (edge.userid2.equalsIgnoreCase(v.id)) {
				//edges.remove(edge); 
				tempedges.add(edge);
				continue;
			}
		}
		
		for (Edgev2 edge : tempedges) {
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
		float newNchoose3 = n * (n - 1) * (n - 2) / 6;
		//triangleDensity = calculateTriangleCount/n;
		triangleDensity = calculateTriangleCount/newNchoose3;		
		return triangleDensity;
	}

	
//	public DSGInfo convertDSGInfo() {
//		DSGInfo dsgInfo = new DSGInfo();
//		dsgInfo.setAllUserIDs(getUsersIDs());
//		dsgInfo.setMinimumDegree(getMinimumEdgeDegree()); 
//		dsgInfo.setEdgeDensity(getEdgeDensity());
//		
//		return dsgInfo;
//	}

	public float getEdgeDensity() { 
		float density = 0;
		float newNchoose2 = users.size() * (users.size() + 1) / 2;
		density = edges.size()/newNchoose2;
		
		return density;
	}
	
}
