package com.mizan.dsg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mizan.dsg.dataclass.Edgev2;
import com.mizan.dsg.dataclass.Graphv2;
import com.mizan.dsg.dataclass.Userv2;

public class DSG {
	private static final double delta_filter_algo = 0.5;
	//coreview_file_path = "/Users/mdmizanurrahman/Google Drive/onair/244/co-review/"+ currentAppId +".csv";
	//private static final String EDGESFILE = "/Users/mdmizanurrahman/Google Drive/WorkerProfile/0.alldata/wokercoreview/wokercoreview23.csv";
	private static String currentAppId = "com.GreatAppsUniverse.FreeBloodSugarTestPrank.BloodSugars";
	private static String worker_json_file = "/Users/mdmizanurrahman/Google Drive/onair/244_400/app.worker.info_json/json/"+ currentAppId  +".json";
	private static String EDGESFILE = "/Users/mdmizanurrahman/Google Drive/onair/244_400/co-review/"+ currentAppId +".csv";
	
	private HashMap<String, Userv2> allUserHashMap;
	private HashMap<String, Userv2> graphUserHashMap;
	private List<String> alreadyaddedusersinDSG;
	private List<Edgev2> allEdges;
	private List<String> otherSetUsers;
	
	private int totalEdges = 0;
	private static final double delta_filter = 0.5;
	private static int edge_filter = 5;
	
	static List<List<String>> DSGComponents;
	private static int num_reviewers;
	private static List<String> allCompUserIds;
	
	public static boolean is_message_print = false; 
	
	
	public DSG(String appid) {
		allUserHashMap = new HashMap<String, Userv2>();
		graphUserHashMap = new HashMap<String, Userv2>();
		allEdges = new ArrayList<Edgev2>();
		otherSetUsers = new ArrayList<String>();
		alreadyaddedusersinDSG = new ArrayList<String>();
		DSGComponents = new ArrayList<List<String>>();
		allCompUserIds = new ArrayList<String>();
		
		currentAppId = appid;
		worker_json_file = "/Users/mdmizanurrahman/Google Drive/onair/244_400/app.worker.info_json/json/"+ currentAppId  +".json";
		EDGESFILE = "/Users/mdmizanurrahman/Google Drive/onair/244_400/co-review/"+ currentAppId +".csv";
		
		//currentAppId = "com.GreatAppsUniverse.FreeBloodSugarTestPrank.BloodSugars";
		//private static String worker_json_file = "/Users/mdmizanurrahman/Google Drive/onair/244/app.worker.info_json/json/"+ currentAppId  +".json";
		//private static final String EDGESFILE = "/Users/mdmizanurrahman/Google Drive/onair/244/co-review/"+ currentAppId +".csv";
		

		ProcessData();
		startDSG();
		
		compareResult();
	}
	
	private void startDSG() {

		//System.out.println("step 1");
		
		while(true){
			List<Userv2> users = new ArrayList<Userv2>();
			List<String> userids = new ArrayList<String>();
			List<Edgev2> edges = new ArrayList<Edgev2>();
			
			//System.out.println("Total users " + graphUserHashMap.keySet().size()); 
			//System.out.println("users already in dsg " + alreadyaddedusersinDSG.size()); 
			
			for (String userid : graphUserHashMap.keySet()) {
				if (!alreadyaddedusersinDSG.contains(userid)) {
					users.add(graphUserHashMap.get(userid));
					userids.add(userid);
				}
			}
			
			//System.out.println("total users in graph " + users.size()); 
			
			
			if (userids.size() < 5) {
				break;
			}
			
			for (Edgev2 edge : allEdges) {
				if (userids.contains(edge.userid1) && userids.contains(edge.userid2)) {
					edges.add(edge);
					
					Userv2 user1 = allUserHashMap.get(edge.userid1);
					Userv2 user2 = allUserHashMap.get(edge.userid2);
					user1.addEdge(edge.userid2, edge.weight);
					user2.addEdge(edge.userid1, edge.weight);
					
				}
			}

			Graphv2 graphv2 = new Graphv2(users, edges); 
			
			Graphv2 densestSubGraph = findDensestSubGraph(graphv2);
			
			if(is_message_print) System.out.println("***density " + densestSubGraph.getEdgeDensity() + " size " + densestSubGraph.users.size());
			
			List<String> DSGUsers = new ArrayList<String>();
			for (Userv2 user : densestSubGraph.users) {
				//***
				if(is_message_print) System.out.println(user.id);
				alreadyaddedusersinDSG.add(user.id);
				DSGUsers.add(user.id);
			}
			
			if(DSGUsers.size() >= 5 && densestSubGraph.getEdgeDensity() > delta_filter) {
				DSGComponents.add(DSGUsers);
				allCompUserIds.addAll(DSGUsers);
			}
			//System.out.println("\n");
		}
	}

	public Graphv2 findDensestSubGraph(Graphv2 g){
		//System.out.println("Graphv2 stat user count :: " + g.users.size() + " edge count:: " + g.edges.size());
		Graphv2 SG = new Graphv2();
		Graphv2 H = (Graphv2) g.clone();
		double d = H.calculateDensity();
		
		//System.out.println(d); 
		//System.out.println("density " + d);
		
		if(H.getEdgeDensity() >= delta_filter_algo){
			return H;
		}
		
		if (d == 0) {
			return g;
		}
		
		SG = g.clone();
		
		int totalNodes = g.users.size();
		for (int i = 2; i < totalNodes; i++) {
			Userv2 v = H.getLeastConnectedNode();
			H.deleteNode(v);
			double calculateDensity = H.calculateDensity();
			double calculateedgedensity = H.getEdgeDensity();

			if (calculateDensity > d || calculateedgedensity < delta_filter_algo) {
			//if (calculateedgedensity < delta_filter_algo) {
			//if (calculateDensity > d) {
				SG = H.clone(); 
				d = calculateDensity;
			}else{
				//if(calculateedgedensity > delta_filter_algo){
					return SG;
				//}
			}
		}
		
		return SG;
	}
	
	
	private static void compareResult() {
		try {
			String jsonData = Utility.readFile(worker_json_file); 
			JSONObject jobj = new JSONObject(jsonData);
			
			compareComponentsProfile(jobj);
			compareworkersprofile(jobj);
			
		} catch (JSONException e) {
			System.out.println("worker json file not found"); 
			e.printStackTrace();
		}
	}
	
	private static void compareComponentsProfile(JSONObject jobj) throws JSONException {
		int comcount = 0;
		
		int workercount_50p = 0;
		int workercount_70p = 0;
		int workercount_80p = 0;
		int workercount_90p = 0;
		
		for (List<String> userids : DSGComponents) {
			comcount++;
			if(is_message_print)  System.out.println("***comp no : " + comcount + " node size : " + userids.size());
			if(is_message_print)  System.out.println("***===========================");
			
			int maxMatchCompSize = 0;
			
			
			
			boolean isfound = false;
			
			for (int i = 0; i < 24; i++) {
				if (jobj.has(Integer.toString(i))) {
					List<String> jsonUserids = getallJsonUsers(jobj, i);
					
					ArrayList<String> compareComponent = Utility.compareComponent(userids, jsonUserids);
					
					int matchsize = compareComponent.size();
					
					if(matchsize > maxMatchCompSize){
						maxMatchCompSize = matchsize;
						
						
						isfound = true;
					}
					
					
					if (matchsize > 0) {
						float ratio = (float)matchsize/(float)userids.size();
						if (ratio > 0.1) {
							//***
							if(is_message_print) System.out.println(i + " : "+ "from json " + jsonUserids.size() + " actual match " + matchsize + " ratio " + ratio);
							if(is_message_print) System.out.println(compareComponent); 
						
							for(String userid:compareComponent){
								//if(is_message_print) System.out.println(userid); 
							}
						}
					}
				}
			}
			
			//System.out.println(maxMatchCompSize); 
			
			if(isfound == true){
				float ratio = (float)maxMatchCompSize/(float)userids.size()*100;
				
				//System.out.println(ratio); 
				
				if(ratio >= 50 ) workercount_50p++;
				if(ratio >= 70 ) workercount_70p++;
				if(ratio >= 80 ) workercount_80p++;
				if(ratio >= 90 ) workercount_90p++;
			}
		}
		
		//System.out.println(workercount_50p); 
		
		float ratio_50 = (float)workercount_50p/(float)DSGComponents.size();
		float ratio_70 = (float)workercount_70p/(float)DSGComponents.size();
		float ratio_80 = (float)workercount_80p/(float)DSGComponents.size();
		float ratio_90 = (float)workercount_90p/(float)DSGComponents.size();
		
		//ArrayList<String> allCommonUserids = Utility.compareComponent(allCompUserIds, allworkeruserids);
		
		
		
		
		
		//System.out.println(currentAppId + "," + DSGComponents.size() + "," + allCompUserIds.size() + "," + ratio_50 + "," + ratio_70 + "," + ratio_80 + "," + ratio_90 );
	}
	
	private static List<String> getallJsonUsers(JSONObject jobj, int i)
			throws JSONException {
		JSONArray jsonArray = jobj
				.getJSONArray(Integer.toString(i));
		List<String> jsonUserids = new ArrayList<String>();

		JSONArray jarr1 = new JSONArray(jsonArray.toString());

		for (int j = 0; j < jarr1.length(); j++) {
			String userid = jarr1.getString(j);
			jsonUserids.add(userid);
			//System.out.println(userid); 
		}
		return jsonUserids;
	}
	
	private static void compareworkersprofile(JSONObject jobj)
			throws JSONException {
		int workercount = 0;
		int workercount_50p = 0;
		int workercount_70p = 0;
		int workercount_80p = 0;
		int workercount_90p = 0;
		
		int allFakeUsersCount = 0;
		
		List<String> allworkeruserids = new ArrayList<String>();
		
		String workerids = "";
		for (int i = 1; i < 24; i++) {
			
			if (jobj.has(Integer.toString(i))) {
				
				workercount++;
				
				workerids += i + ";";
				
				List<String> jsonUserids = getallJsonUsers(jobj, i);
				
				//System.out.println("\n***worker profile : " + i + " total node: " + jsonUserids.size());  
				//System.out.println("=============================================================="); 
				
				
				
				allworkeruserids.addAll(jsonUserids);
				allFakeUsersCount += jsonUserids.size();
				//float workersDelta = getDelta(jsonUserids);
				
				
				
				int maxmatchFound = 0;
				//int compid = 0;
				List<String> allmatchedComponents = new ArrayList<String>();
				
				for (List<String> userids : DSGComponents) { 
					//compid++;
					ArrayList<String> compareComponent = Utility.compareComponent(userids, jsonUserids);
					allmatchedComponents.addAll(compareComponent);
					
					if (compareComponent.size() > 0) {
						//System.out.println("Component " + compid + ": matched count " + compareComponent.size());
					}
					
					if (compareComponent.size() > maxmatchFound) {
						maxmatchFound = compareComponent.size();
					}
				}

				//float ratio = (float)allmatchedComponents.size()/(float)jsonUserids.size()*100;
				float ratio = (float)maxmatchFound/(float)jsonUserids.size()*100;
				
				
				//System.out.println("all matched " + allmatchedComponents.size());
				//System.out.println("all json " + jsonUserids.size()); 
				//System.out.println("ratio " + ratio); 
				
				//System.out.println("percentage of node found : " + ratio + "%"); 
				
				if(ratio >= 50 ) workercount_50p++;
				if(ratio >= 70 ) workercount_70p++;
				if(ratio >= 80 ) workercount_80p++;
				if(ratio >= 90 ) workercount_90p++;
				
				if (maxmatchFound > 0) {
					//System.out.println("percentage of node found from one component " + (float)maxmatchFound/(float)jsonUserids.size()*100 +"%");
				}
			} 
		}
		
		float ratio_50 = (float)workercount_50p/(float)workercount;
		float ratio_70 = (float)workercount_70p/(float)workercount;
		float ratio_80 = (float)workercount_80p/(float)workercount;
		float ratio_90 = (float)workercount_90p/(float)workercount;
		
		ArrayList<String> allCommonUserids = Utility.compareComponent(allCompUserIds, allworkeruserids);
		
		System.out.println(currentAppId + "," + num_reviewers + "," + workercount + "," + workerids + "," + DSGComponents.size() + "," + allFakeUsersCount + "," + allCompUserIds.size() + "," + allCommonUserids.size() + "," + ratio_50 + "," + ratio_70 + "," + ratio_80 + "," + ratio_90 );
	}
	
	private void ProcessData() {
		try {
			List<String> userids = Utility.getalluserlist(EDGESFILE);
			num_reviewers = userids.size();
			
			for(String userid:userids){
				Userv2 userv2 = new Userv2(userid);
				allUserHashMap.put(userid, userv2); 
			}
			
			createEdges();
		
			for (String userid : allUserHashMap.keySet()) {
				Userv2 user = allUserHashMap.get(userid);
				if (user.edges.keySet().size() != 0) {
					graphUserHashMap.put(userid, user);
				}
			}
			
			//System.out.println("After filtering total users " + graphUserHashMap.keySet().size());

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		} 
	}

	private void createEdges() throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(EDGESFILE));
		String line = null;

		while ((line = br.readLine()) != null) {
			//System.out.println(line);
			String[] split = line.split(",");
			String userid1 = split[0].trim();
			String userid2 = split[1].trim();
			int weight = Integer.parseInt(split[2].trim()); 
			
			if (weight >= edge_filter) {
				totalEdges++;
				if (!(otherSetUsers.contains(userid1) || otherSetUsers.contains(userid2))) {
					Userv2 user1 = allUserHashMap.get(userid1);
					Userv2 user2 = allUserHashMap.get(userid2);
					user1.addEdge(userid2, weight);
					user2.addEdge(userid1, weight);
					
					Edgev2 edgev2 = new Edgev2(userid1, userid2, weight);
					allEdges.add(edgev2);
				}
			}
		}
		
		br.close();
	}
}
