package com.mizan.dsg;

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

import com.mizan.dsg.dataclass.Graphv2;

public class Utility {
	
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
	
	public static void compareworkersprofile(JSONObject jobj, List<List<String>> kargerComponents) 
			throws JSONException {
		for (int i = 1; i < 24; i++) {
			
			if (jobj.has(Integer.toString(i))) {
				System.out.println("\nworker profile : " + i); 
				System.out.println("========================"); 
				
				
				JSONArray jsonArray = jobj
						.getJSONArray(Integer.toString(i));
				List<String> jsonUserids = new ArrayList<String>();

				JSONArray jarr1 = new JSONArray(jsonArray.toString());

				for (int j = 0; j < jarr1.length(); j++) {
					jsonUserids.add(jarr1.getString(j));
				}
				
				int maxmatchFound = 0;
				int compid = 0;
				List<String> allmatchedComponents = new ArrayList<String>();
				
				
				for (List<String> userids : kargerComponents) { 
					compid++;
					ArrayList<String> compareComponent = Utility.compareComponent(userids, jsonUserids);
					allmatchedComponents.addAll(compareComponent);
					
					if (compareComponent.size() > 0) {
						System.out.println("Component " + compid + ": matched count " + compareComponent.size());
					}
					
					if (compareComponent.size() > maxmatchFound) {
						maxmatchFound = compareComponent.size();
					}
				}
				
				System.out.println("number of nodes not found : " + (jsonUserids.size() - allmatchedComponents.size())); 

				//if (maxmatchFound > 0) {
				//	System.out.println(i + " : "+ "from json " + jsonUserids.size() + " actual match " + maxmatchFound + " ratio " + (float)maxmatchFound/(float)jsonUserids.size());
				//}
			} 
		}
	}
}
