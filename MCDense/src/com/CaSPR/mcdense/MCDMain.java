package com.CaSPR.mcdense;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Md Mizanur Rahman
 * PhD CS, FIU
 */


public class MCDMain {
    
	//private static final double delta_filter = 0.4;
	private static final int edge_filter = 10;
		
    /**
     * @param args
     */
    public static void main( String[] args ) {
    	//runOnSingleApp();
    	runOnAllApp();
    }

	@SuppressWarnings("unused")
	private static void runOnSingleApp() { 
		String currentAppId = "com.hacklondon.notify";
		String worker_json_file = "/Users/mdmizanurrahman/Google Drive/Face2Fraud/TestApps/244_400/app.worker.info_json/json/"+ currentAppId  +".json";
		String coreview_file_path = "/Users/mdmizanurrahman/Google Drive/Face2Fraud/TestApps/244_400/co-review640/"+ currentAppId +".csv";
		
		RandomString rndStr = new RandomString(10);
		List<String> userids = Utilityv2.getalluserlist(coreview_file_path);
    	Graphv2 graph = Utilityv2.createGraphFromFile(userids, coreview_file_path, edge_filter); 
    	int num_reviewers = userids.size();
    	MCD mcd = new MCD(graph);
    	mcd.startMCD(rndStr.nextString(),userids);
    	List<List<String>> mcdComponents = mcd.getMCDComponents();
    	List<String> allCompUserIds = mcd.getAllCompUserIds(); 
    	
		try {
			String jsonData = Utilityv2.readFile(worker_json_file); 
			JSONObject jobj = new JSONObject(jsonData);
			Utilityv2.compareworkersprofile(jobj, mcdComponents, currentAppId, num_reviewers, allCompUserIds);
		} catch (JSONException e) {
			System.out.println("worker json file not found"); 
			e.printStackTrace();
		}
		
	}
	
	private static void runOnAllApp() {
		try {
			String AppList_FilePath = "/Users/mdmizanurrahman/Google Drive/Face2Fraud/TestApps/244_400/244apps.csv";
			BufferedReader br = new BufferedReader(new FileReader(AppList_FilePath));
			String appid = null;

			while ((appid = br.readLine()) != null) {
				RandomString rndStr = new RandomString(10);
				String currentAppId = appid;
				//System.out.println(appid);
				String worker_json_file = "/Users/mdmizanurrahman/Google Drive/Face2Fraud/TestApps/244_400/app.worker.info_json/json/"+ currentAppId  +".json";
				String coreview_file_path = "/Users/mdmizanurrahman/Google Drive/Face2Fraud/TestApps/244_400/co-review/"+ currentAppId +".csv";
			
		    	List<String> userids = Utilityv2.getalluserlist(coreview_file_path);
		    	int num_reviewers = userids.size();
		    	
		    	Graphv2 graph = Utilityv2.createGraphFromFile(userids, coreview_file_path, edge_filter); 
		    	MCD mcd = new MCD(graph);
		    	mcd.startMCD(rndStr.nextString(), userids);
		    	List<List<String>> mcdComponents = mcd.getMCDComponents();
		    	List<String> allCompUserIds = mcd.getAllCompUserIds(); 
		    	
				try {
					String jsonData = Utilityv2.readFile(worker_json_file); 
					JSONObject jobj = new JSONObject(jsonData);
					Utilityv2.compareworkersprofile(jobj, mcdComponents, appid, num_reviewers, allCompUserIds);
				} catch (JSONException e) {
					System.out.println("worker json file not found"); 
					e.printStackTrace();
				}
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
