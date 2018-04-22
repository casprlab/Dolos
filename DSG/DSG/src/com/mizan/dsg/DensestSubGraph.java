package com.mizan.dsg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.mizan.dsg.dataclass.AllDSGInfo;
import com.mizan.dsg.dataclass.Edge;
import com.mizan.dsg.dataclass.Graph;
import com.mizan.dsg.dataclass.Review;
import com.mizan.dsg.dataclass.User;

public class DensestSubGraph {
	private String reviewFilePath;
	private String reviewerFilePath;
	private String appID;
	
	List<Review> allreviewList;
	List<User> allUserListFromReview; 
	
	public List<Edge> backupEdges;
	private List<User> allUsers;
	public List<Edge> allEdges;
	
	private List<User> recentGraphUsers;
	public List<Edge> recentGraphEdges;
	
	private List<Graph> densestSubGraphs;
	
	private AllDSGInfo allDsgInfo;

	public DensestSubGraph(String appid, String reviewFilePath,
			String reviewerFilePath) {
		this.appID = appid;
		
		this.reviewFilePath = reviewFilePath;
		this.reviewerFilePath = reviewerFilePath;
		
		setDensestSubGraphs(new ArrayList<Graph>());
		
		setDsgInfo(new AllDSGInfo());
		getDsgInfo().setAppId(appid); 
		
		start();
	}

	
	private void start() {
		backupEdges = new ArrayList<Edge>();
		Graph graph = buildOriginalGraph();
		
		if (graph == null) {
			return;
		}
		
		for (Edge edge : graph.getEdges()) {
			backupEdges.add(edge);
		}
		
		recentGraphUsers = graph.getUsers();
		recentGraphEdges = graph.getEdges();

		int densestSubGraphCount = 0;
		
		
		
		while(true){
			Graph densestSubGraph = findDensestSubGraph(graph);
			
//			if (densestSubGraphs.size() == 0) {
//				getReviewsFromGraph(densestSubGraph);
//			}
						
			if (densestSubGraph == graph) { 
				//System.out.println("same graph");
				break;
			}
			
			densestSubGraphCount++;
			getDensestSubGraphs().add(densestSubGraph);
			allDsgInfo.addDSGInfo(densestSubGraph.convertDSGInfo()); 
			
			//System.out.println("densent subgraph \n\n\n\n");
			
			
			
			for (User user : densestSubGraph.getUsers()) {
				user.setDsgID(densestSubGraphCount);
				//System.out.println(user.getId() + "," + user.getDsgID() + "," + appID); 
				
				//System.out.println(user);
			}
			
			printDSGUsers(densestSubGraph);
			
			
			
			//System.out.println("original users \n\n\n\n");
			
			//for (User user : allUsers) {
			//	System.out.println(user);
			//}
			
			
			List<User> newUsers = generateNewUsers(densestSubGraph);
			List<Edge> newEdges = generateNewEdges(newUsers);
			
			//System.out.println("\n\nnew users ");
		
			graph = new Graph(newUsers, newEdges);
			
			//for (User user : newUsers) {
			//	System.out.println(user);
				
			//}
			
			recentGraphUsers = graph.getUsers();
			recentGraphEdges = graph.getEdges();
			
			if (recentGraphUsers.size() <= 3) {
				break;
			}
		}
		
		String outout = "";
		outout += appID + "," + allUserListFromReview.size() + ","+ getDensestSubGraphs().size() + ",";
		
		
		
		for (Graph densestSubGraph : getDensestSubGraphs()) {
			int totalWeight = 0;
			for (Edge edge : densestSubGraph.getEdges()) {
				totalWeight += edge.getWeight(); 
			}
			
			float meanWeight = totalWeight/densestSubGraph.getEdges().size();
			outout += "N " + densestSubGraph.getUsers().size() + " T " + String.format("%.2f", densestSubGraph.getDensity()) +" W " + String.format("%.2f", meanWeight) + " - "; 
		}
		
		System.out.println(outout);
		
		//System.out.println("Total count " + densestSubGraphCount);
	}





	private void getReviewsFromGraph(Graph graph) {
		List<String> userIDs = new ArrayList<String>();
		
		for (User user : graph.getUsers()) {
			userIDs.add(user.getId());
		}
		
		for (Review review : allreviewList) {
			if (userIDs.contains(review.getUser().getId())) { 
				System.out.println(review.getDataText()); 
				
			}
		}
	}
	
	private void printDSGUsers(Graph densestSubGraph) {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("all400appDsgUserIDs.csv", true)));
			for (User user : densestSubGraph.getUsers()) {
				writer.println(user.getId() + "," + user.getDsgID() + "," + appID + "hello");
			}
			
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
	
	private void printEdges() {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("all400appscoreviewedges.csv", true)));
			for (Edge edge : allEdges) {
				writer.println(edge.getUser1().getId() + "," + edge.getUser2().getId() + "," + edge.getWeight() + "," + appID + "hello");
			}
			
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}


	private List<Edge> generateNewEdges(List<User> newUsers) {
		List<Edge> newEdges = new ArrayList<Edge>();
		
		for (int i = 0; i < newUsers.size(); i++) {
			User user1 = newUsers.get(i);
			List<String> appidList1 = user1.getAppids();
			for (int j = i+1; j < newUsers.size(); j++) {
				User user2 = newUsers.get(j);
				List<String> appidList2 = user2.getAppids();
				
				int count = 0;
				for (String appid : appidList2) {
					if (appidList1.contains(appid)) {
						count++;
					}
				}
				
				if (count > 0) {
					Edge edge =  new Edge(user1, user2, count, 0);
					newEdges.add(edge);
					
					user1.addAdjecencyUsers(user2);
					user2.addAdjecencyUsers(user1);

				}
			}
		}
		
		return newEdges;
	}

	
	private List<User> generateNewUsers(Graph densestSubGraph) {
		User newCombinedUser = creatNewCombinedUser(densestSubGraph.getUsers());
		
		List<User> newUsers = new ArrayList<User>(); 
		
		
		newUsers.add(newCombinedUser);
		
		for (User user : recentGraphUsers) {
			if (!densestSubGraph.getUsers().contains(user)) {
				user.clearAdjecency();
				newUsers.add(user);
			}
		}
		
		return newUsers;
	}


	private ArrayList<String> removeDuplicateIDs(List<String> stringsList){
		Set<String> appIDs = new LinkedHashSet<String>();//A Linked hash set 
		//prevents the adding order of the elements
		for (String string: stringsList) {
		    appIDs.add(string);
		}
		return new ArrayList<String>(appIDs);  
	}

	private User creatNewCombinedUser(List<User> users) {
		//System.out.println("creating combined user...");
		
		User newCombinedUser = new User("*******","**********");
		newCombinedUser.setCombined(true);
		newCombinedUser.setConbinedUsers(users);
		
		List<String> appids = new ArrayList<String>();
	
		for (User user : users) {
			appids.addAll(user.getAppids());
		}
		
		//System.out.println("combined all apps " + appids.size());
		
		appids = removeDuplicateIDs(appids);
		
		//System.out.println("combined all apps no dulicate " + appids.size());
		
		newCombinedUser.setAppids(appids); 
		
		return newCombinedUser;
	}


	private Graph buildOriginalGraph() {
		//System.out.println("app name ::" + appID);
		allreviewList = ProcessData.processReview(reviewFilePath, appID);
		//System.out.println(allreviewList);
		
		allUserListFromReview = ProcessData.processReviewersInfo(allreviewList,
				reviewerFilePath);

		
		//System.out.println("all user list " + allUserListFromReview.size()); 
		//System.out.println(allUserListFromReview); 

		ProcessData.mapUserToReview(allUserListFromReview, allreviewList); 
		
		//System.out.println("all user list " + allUserList.size());
		
		allEdges = ProcessData.generateEdges(allUserListFromReview);
		
		printEdges();
//		for (Edge edge : allEdges) {
//			System.out.println(edge.getUser1().getId()+ ","+edge.getUser2().getId()+","+edge.getWeight());
//		}
		
		allUsers = new ArrayList<User>();
		
		for (Edge edge : allEdges) {
			//System.out.println(edge);
			
			if (!allUsers.contains(edge.getUser1())) {
				allUsers.add(edge.getUser1()); 	
			}

			if (!allUsers.contains(edge.getUser2())) {
				allUsers.add(edge.getUser2()); 	
			}	
		}
		
		if (allEdges.size() == 0) {
			return null;
		}
		
		Graph graph = new Graph(allUsers,allEdges);
		
//		for (User user : allUsers) {
//			System.out.println(user);
//		}
		
		return graph;
	}
	
	public Graph findDensestSubGraph(Graph g){
		Graph SG = new Graph();
		g.calculateDensity();
		Graph H = (Graph) g.clone();
		
		double d = H.calculateDensity();
		if (d == 0) {
			return g;
		}
		
		SG = g.clone();
		
		//System.out.println("graph stat user count :: " + H.getUsers().size() + " edge count:: " + H.getEdges().size());
		
		int totalNodes = g.getUsers().size();
		
		for (int i = 2; i < totalNodes; i++) { 
			User v = H.getLeastConnectedNode();
			//System.out.println("least user ::");
			//System.out.println(v);
			
//			System.out.println("\nCurrent users \n"); 
//			for (User user : H.getUsers()) {
//				System.out.println(user);				
//			}
//			
			H.deleteNode(v);
			
			double calculateDensity = H.calculateDensity();
			
			//System.out.println(calculateDensity);
			 
			if (calculateDensity >= d) {
				SG = H.clone(); 
				d = calculateDensity;
				
				//System.out.println("exchnaging");
			}else{
				H.addNode(v);
				return SG;
			}
			
			//System.out.println("graph stat user count :: " + H.getUsers().size() + " edge count:: " + H.getEdges().size() + "\n\n\n");

		}
//		
//		
//		for (User user : H.getUsers()) {
//			//System.out.println(user);			
//			System.out.println(user.getId() + "," + user.getId() + "," + "#3366FF");
//		}
//		
//		for (Edge edge : H.getEdges()) {
//			System.out.println(edge.getUser1().getId() +"," + edge.getUser2().getId() + "," + edge.getWeight()+ "," + "#222222");
//		}
//		
		return SG;
	}
	
	private void testCases() {
		double[][] testMatrix = 
			{{0,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,0,1,0,0,0,0,0,0,0,0,0,0},
			{1,1,0,0,0,0,0,0,0,0,0,0,0}, 
			{1,0,0,0,1,0,0,0,0,0,0,0,0}, 
			{1,0,0,1,0,0,0,0,0,0,0,0,0}, 
			{1,0,0,0,0,0,1,0,0,0,0,0,0}, 
			{1,0,0,0,0,1,0,0,0,0,0,0,0}, 
			{1,0,0,0,0,0,0,0,1,0,0,0,0}, 
			{1,0,0,0,0,0,0,1,0,0,0,0,0}, 
			{1,0,0,0,0,0,0,0,0,0,1,0,0}, 
			{1,0,0,0,0,0,0,0,0,1,0,0,0}, 
			{1,0,0,0,0,0,0,0,0,0,0,0,1}, 
			{1,0,0,0,0,0,0,0,0,0,0,1,0}};
		
		//CustomMatrix.displayAdjacencyMatrix(testMatrix);
		
		//Matrix testMatrix2 = new Matrix(testMatrix);
		
		//double calculateTriangleDensity = CustomMatrix.calculateTriangleCount(testMatrix2);
		
		//System.out.println("the custom graph density " +  calculateTriangleDensity);
	}


	public List<Graph> getDensestSubGraphs() {
		return densestSubGraphs;
	}


	public void setDensestSubGraphs(List<Graph> densestSubGraphs) {
		this.densestSubGraphs = densestSubGraphs;
	}


	public AllDSGInfo getDsgInfo() {
		return allDsgInfo;
	}


	public void setDsgInfo(AllDSGInfo dsgInfo) {
		this.allDsgInfo = dsgInfo;
	}
	
}
