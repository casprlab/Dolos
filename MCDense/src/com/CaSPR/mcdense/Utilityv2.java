package com.CaSPR.mcdense;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utilityv2 {
	
	static String letters[] = "0123456789ABCDEF".split("");
	static int min=letters.length-(letters.length/3);
	int max=letters.length;
	static Random rnd=new Random(1000);
	static String colorEx[]= new String[]{"00","00","00"};
	static int colorChange=0;
	static int addColorChange=1;

	public static String getRandomColor() {
	    StringBuilder color = new StringBuilder("#");
	    int highColor=rnd.nextInt(2)+1;
	    for (int i = 0; i<3; i++) {
	        //int addColor=0;
	        if (i==highColor)
	            highColor=min;

	        color.append(colorEx[i]);

	        if (colorChange==i) {
	            if (colorEx[i].equals("00"))
	                colorEx[i]="55";
	            else if (colorEx[i].equals("55"))
	                colorEx[i]="AA";
	            else if (colorEx[i].equals("AA"))
	                colorEx[i]="FF";
	            else {
	                if (i>0 && !"00".equals(colorEx[i-1]))
	                    colorEx[i-1]="00";
	                else if (i<2)
	                    colorEx[i+1]="00";
	                colorChange+=addColorChange;
	                //colorChange++;
	                if (colorChange>2 || colorChange<0) {
	                    //colorChange=0;
	                    addColorChange=-addColorChange;
	                    colorChange+=addColorChange;
	                    colorChange+=addColorChange;
	                }
	            }
	        }
	    }
	    return color.toString();
	}
	
	
	public static <T> ArrayList<T> intersection(Collection<T> a, Collection<T> b) {
	    if (b.size() > a.size()) {
	        return intersection(b, a);
	    } else {
	        if (b.size() > 20 && !(a instanceof HashSet)) {
	            a = new HashSet<T>(a); 
	        }
	        ArrayList<T> result = new ArrayList<T>();
	        for (T objb : b) {
	            if (a.contains(objb)) {
	                result.add(objb);
	            }
	        }
	        return result;
	    }
	}

	static ArrayList<String> compareComponent(List<String> userids, List<String> jsonUserids) {
		ArrayList<String> intersection = intersection(userids, jsonUserids);
		return intersection;
	}
	
	public static String readFile(String filename) {
	    String result = "";
	    try {
	        BufferedReader br = new BufferedReader(new FileReader(filename));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            line = br.readLine();
	        }
	        result = sb.toString();
	        
	        br.close();
	        
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	
	static List<String> getalluserlist(String FILE_PATH) { 
		List<String> userids = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
			String line = null;

			while ((line = br.readLine()) != null) {
				String[] split = line.split(",");
				
				String userid1 = split[0];
				String userid2 = split[1];
				
				if (!userids.contains(userid1)) userids.add(userid1);
				if (!userids.contains(userid2)) userids.add(userid2);
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return userids;
	}
	
	static Graphv2 createGraphFromFile(List<String> userids, String FILE_PATH, int edge_filter) {
    	Graphv2 gr = new Graphv2();
    	
    	try {
			BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
			String line = null;

			while ((line = br.readLine()) != null) {
				String[] split = line.split(",");
				String userid1 = split[0];
				String userid2 = split[1];
				
				if (userids.contains(userid1) && userids.contains(userid2)) { 
					int weight = Integer.parseInt(split[2]);
					
					Vertexv2 v1 = gr.getVertex(userid1);
					Vertexv2 v2 = gr.getVertex(userid2);
					
					if (weight >= edge_filter ) {
						
						Edgev2 e;
						if ((e = v2.getEdgeTo(v1)) == null) {
							//System.out.println(userid1 + "," + userid2 + "," + weight); 
							e = new Edgev2(v1, v2);
							gr.edgev2s.add(e);
							v1.addEdge(e);
							v2.addEdge(e);
						}
					}
				}
				
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("coreview file not found"); 
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return gr;
    }
	
	public static Graphv2 createGraphFromGraph(List<String> userids, Graphv2 originalGraph) {
		Graphv2 gr = new Graphv2();
		
		for (Edgev2 edge : originalGraph.edgev2s) {
			String userid1 = edge.ends.get(0).lbl; 
			String userid2 = edge.ends.get(1).lbl;;
			
			if (userids.contains(userid1) && userids.contains(userid2)) { 
				Vertexv2 v1 = gr.getVertex(userid1);
				Vertexv2 v2 = gr.getVertex(userid2);
				
				Edgev2 e;
				if ((e = v2.getEdgeTo(v1)) == null) {
					//System.out.println(userid1 + "," + userid2 + "," + weight); 
					e = new Edgev2(v1, v2);
					gr.edgev2s.add(e);
					v1.addEdge(e);
					v2.addEdge(e);
				}
			}
		}
		
		return gr;
	}
	
	public static String generateColorCode(){
		 // create random object - reuse this as often as possible
        Random random = new Random();

        // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
        int nextInt = random.nextInt(256*256*256);

        // format it as hexadecimal string (with hashtag and leading zeros)
        String colorCode = String.format("#%06x", nextInt);

        // print it
        System.out.println(colorCode);
        
        return colorCode;
	}
	

	private static List<String> getallJsonUsers(JSONObject jobj, int i)
			throws JSONException {
		JSONArray jsonArray = jobj
				.getJSONArray(Integer.toString(i));
		List<String> jsonUserids = new ArrayList<String>();

		JSONArray jarr1 = new JSONArray(jsonArray.toString());

		for (int j = 0; j < jarr1.length(); j++) {
			jsonUserids.add(jarr1.getString(j));
		}
		return jsonUserids;
	}
	
	
	public static void compareworkersprofile(JSONObject jobj, List<List<String>> kargerComponents, String currentAppId, int num_reviewers, List<String> allCompUserIds) throws JSONException {
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
				allworkeruserids.addAll(jsonUserids);
				allFakeUsersCount += jsonUserids.size();
				//float workersDelta = getDelta(jsonUserids);
				
				//System.out.println("\nworker profile : " + i + " total node: " + jsonUserids.size() + " delta: " + workersDelta);  
				//System.out.println("=============================================================="); 
				
				
				int maxmatchFound = 0;
				//int compid = 0;
				List<String> allmatchedComponents = new ArrayList<String>();
				
				for (List<String> userids : kargerComponents) { 
					//compid++;
					ArrayList<String> compareComponent = Utilityv2.compareComponent(userids, jsonUserids);
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
		
		ArrayList<String> allCommonUserids = Utilityv2.compareComponent(allCompUserIds, allworkeruserids);
		
		System.out.println(currentAppId + "," + num_reviewers + "," + workercount + "," + workerids + "," + kargerComponents.size() + "," + allFakeUsersCount + "," + allCompUserIds.size() + "," + allCommonUserids.size() + "," + ratio_50 + "," + ratio_70 + "," + ratio_80 + "," + ratio_90 );
	}
	
	@SuppressWarnings("unused")
	private static void compareComponentsProfile(JSONObject jobj, List<List<String>> kargerComponents) throws JSONException {
		int comcount = 0;
		
		for (List<String> userids : kargerComponents) {
			comcount++;
			//System.out.println("comp no : " + comcount + " node size : " + userids.size() + " delta " + getDelta(userids));
			//System.out.println("===========================");
			
			
			for (int i = 0; i < 24; i++) {
				if (jobj.has(Integer.toString(i))) {
					List<String> jsonUserids = getallJsonUsers(jobj, i);
					
					ArrayList<String> compareComponent = Utilityv2.compareComponent(userids, jsonUserids);
					
					int matchsize = compareComponent.size(); 
					if (matchsize > 0) {
						float ratio = (float)matchsize/(float)userids.size();
						if (ratio > 0.4) {
							//System.out.println(i + " : "+ "from json " + jsonUserids.size() + " actual match " + matchsize + " ratio " + ratio);
						}
					}
				}
			}
		}
	}
}
