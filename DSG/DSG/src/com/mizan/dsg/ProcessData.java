package com.mizan.dsg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.mizan.dsg.dataclass.Edge;
import com.mizan.dsg.dataclass.Review;
import com.mizan.dsg.dataclass.Reviews;
import com.mizan.dsg.dataclass.User;

public class ProcessData {

	public static int getDifferenceDays(Date d1, Date d2) {
		int daysdiff = 0;
		long diff = d2.getTime() - d1.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
		daysdiff = (int) diffDays;
		return daysdiff;
	}

	public static List<Review> processReview(String reviewfilepath, String appid) {
		List<Review> reviews = new ArrayList<Review>();

		DateFormat formatter;
		formatter = new SimpleDateFormat("MM-dd-yyyy");
		Date earlistdate = null;
		String line = null;

		try {
			BufferedReader br = new BufferedReader(new FileReader(
					reviewfilepath));
			

			while ((line = br.readLine()) != null) {
				line = line.replace("\"", "");
				String[] split = line.split(",");
				
				//System.out.println(split.length);
				
				//System.out.println(line);
				//line = line.replace("\"", "");
				
				if (split.length < 7) {
					//System.out.println(line);
					continue;
				}
				
				//System.out.println(split.length);

				String appidindata = split[split.length-1].trim();
				if (appidindata.equals(appid)) {
					
					//System.out.println(line);
					
					// //System.out.println(split[7]);
					String reviewername = split[1].trim();
					String datestr = split[3].trim();
					String userid = split[2].trim().replace(
							"/store/people/details?id=", "");
					User user = new User(reviewername, userid);
					
					if (userid.equalsIgnoreCase("")) {
						continue;
					}

					Date date = formatter.parse(datestr.trim());
					Review review = new Review(appid, user, date);

					if (earlistdate != null) {
						int diff = getDifferenceDays(earlistdate, date);
						// //System.out.println(diff + " " + date + " " +
						// datestr);
						if (diff <= 0) {
							earlistdate = date;
						}
					} else {
						earlistdate = date;
					}
					review.setDataText(line);
					review.setTitle(split[4].trim());
					review.setText(split[5].trim()); 
					
					
					
					reviews.add(review);
				}
			}

			for (Review review : reviews) {
				review.setDay(getDifferenceDays(earlistdate, review.getDate()));
			}

			for (Review review : reviews) {
				// //System.out.println(review.toString());
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(line);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(line);
		}
		return reviews;
	}

	public static void mapUserToReview(List<User> allUserList,
			List<Review> allreviewList) {
		int index = 0;
		for (Review review : allreviewList) {
			index++;

			for (User user : allUserList) {
				if (review.getUser().getId().trim()
						.equalsIgnoreCase(user.getId())) {
					// //System.out.println("match found for " + index);
					// //System.out.println("mapped user " + user.toString());
					user.setDay(review.getDay());
					review.setUser(user);
					////System.out.println("mapped user " + user.toString());
				}
			}

		}

		//System.out.println("Total matches : " + index);
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

	public static List<Edge> generateEdges(List<User> allUserList) { 
		List<User> users = allUserList;
		List<Edge> edges = new ArrayList<Edge>();

		//System.out.println("Total users of this app " + users.size());

		for (int i = 0; i < users.size(); i++) {
			User user1 = users.get(i);
			List<String> appidList1 = user1.getAppids();
			for (int j = i + 1; j < users.size(); j++) {
				User user2 = users.get(j);
				List<String> appidList2 = user2.getAppids();

				// //System.out.println("name " + user1.getName() + " count :" +
				// appidList1.size() + " ids " + appidList1);
				// //System.out.println("name " + user2.getName() + " count :" +
				// appidList2.size() + " ids " + appidList2);

				int count = 0;
				for (String appid : appidList2) {
					if (appidList1.contains(appid)) {
						count++;
					}
				}

				// //System.out.println("Matches : " + count + "");

				if (count > 0) {
					
					Edge edge = new Edge(user1, user2, count, Math.abs(user1
							.getDay() - user2.getDay()));
					edges.add(edge);
					
					//user1.addNewEdge(edge);
					//user2.addNewEdge(edge);
					
					user1.addAdjecencyUsers(user2);
					user2.addAdjecencyUsers(user1);
					
					user1.addEdge(user2.getId(), count);
					user2.addEdge(user1.getId(), count);
				}
			}
		}

		return edges;
	}

	private static List<User> getAllUserListOfOneApp(List<Review> allreviewList) {
		List<User> users = new ArrayList<User>();
		for (Review review : allreviewList) {
			users.add(review.getUser());
		}
		return users;
	}

	public static List<User> processReviewersInfo(List<Review> allreviewList,
			String reviewerfilepath) {
		List<User> users = new ArrayList<User>();
		List<String> alluserIDs = new ArrayList<String>();
		

		for (Review review : allreviewList) {
			boolean found = false;
			if (!alluserIDs.contains(review.getUser().getId())) {
				users.add(review.getUser());
				alluserIDs.add(review.getUser().getId());
			}
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader(
					reviewerfilepath));
			String line = null;

			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				line.replace("\"", "");
				String[] split = line.split(",");
				
				if (split.length < 8) {
					continue;
				}

				// //System.out.println(split[0]);

				String username = split[1].trim().replace("\"", "");
				
				if (split.length < 3) {
					//System.out.println(line);
				}
				
				String id = split[2].trim()
						.replace("/store/people/details?id=", "")
						.replace("\"", "");
				String appid = split[4].trim().replace("\"", "");

				for (User user : users) {
					if (user.getId().equalsIgnoreCase(id)) {
						user.addNewAppid(appid);
					}
					
					//if (user.getName().equalsIgnoreCase(username)) {
					//	user.addNewAppid(appid);
					//}

					// if (user.getName().equalsIgnoreCase(username)) {
					// user.addNewAppid(appid);
					// }
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}

		return users;

	}

	public static List<Reviews> sortReviews(List<Review> allreviewList2) {
		List<Reviews> reviewsofAllDays = new ArrayList<Reviews>();

		for (int i = 0; i < allreviewList2.size(); i++) {
			Review review = allreviewList2.get(i);
			int day = review.getDay();
			boolean found = false;
			int index = -1;
			for (int j = 0; j < reviewsofAllDays.size(); j++) {
				Reviews reviews = reviewsofAllDays.get(j);
				if (review.getDay() == reviews.getDay()) {
					found = true;
					index = j;
				}
			}

			if (found) {
				Reviews reviews = reviewsofAllDays.get(index);
				reviews.getReviews().add(review);
			} else {
				Reviews reviews = new Reviews();
				reviews.setDay(review.getDay());
				reviews.getReviews().add(review);
				reviewsofAllDays.add(reviews);
				////System.out.println("Sorting reviews of day ::" + reviews.getDay());
			}
		}

		return reviewsofAllDays;
	}

	public static void sortDaysOfReviews(List<Reviews> reviewsofAllDays) {
		for (int i = 0; i < reviewsofAllDays.size(); i++) {
			for (int j = 0; j < reviewsofAllDays.size(); j++) {
				
				if (reviewsofAllDays.get(i).getDay() < reviewsofAllDays.get(j).getDay()) {
					Reviews tempreviews = reviewsofAllDays.get(i);
					reviewsofAllDays.set(i, reviewsofAllDays.get(j));
					reviewsofAllDays.set(j, tempreviews);
				}
			}
		}
	}

	public static List<User> processReviewersInfo(List<Review> allreviewList,
			String reviewerfilepath, List<String> allAppList) {
		
		List<User> users = new ArrayList<User>();
		List<String> alluserIDs = new ArrayList<String>();
		

		for (Review review : allreviewList) {
			boolean found = false;
			if (!alluserIDs.contains(review.getUser().getId())) {
				users.add(review.getUser());
				alluserIDs.add(review.getUser().getId());
			}
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader(
					reviewerfilepath));
			String line = null;

			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				line.replace("\"", "");
				String[] split = line.split(",");
				
				if (split.length < 8) {
					continue;
				}

				// //System.out.println(split[0]);

				String username = split[1].trim().replace("\"", "");
				
				if (split.length < 3) {
					//System.out.println(line);
				}
				
				String id = split[2].trim()
						.replace("/store/people/details?id=", "")
						.replace("\"", "");
				String appid = split[4].trim().replace("\"", "");
				
				if (allAppList.contains(appid)) {
					for (User user : users) {
						if (user.getId().equalsIgnoreCase(id)) {
							user.addNewAppid(appid);
						}
						
						
						
						//if (user.getName().equalsIgnoreCase(username)) {
						//	user.addNewAppid(appid);
						//}

						// if (user.getName().equalsIgnoreCase(username)) {
						// user.addNewAppid(appid);
						// }
					}
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}

		return users;
		
	}

	public static void findReviews(String alluseridsFile, String allreviewlistFile) {
		//System.out.println("step1"); 

		try {
			BufferedReader br1 = new BufferedReader(new FileReader(alluseridsFile));
			String line1 = null;
			
			List<String> userIDs = new ArrayList<String>();

			while ((line1 = br1.readLine()) != null) {
				userIDs.add(line1.trim());
			}
			
			//System.out.println(userIDs); 
			
			BufferedReader br = new BufferedReader(new FileReader(
					allreviewlistFile));
			String line = null;

			while ((line = br.readLine()) != null) {
				line = line.replace("\"", "");
				String[] split = line.split(",");

				if (split.length < 7) {
					//System.out.println(line);
					continue;
				}
				
				String userid = split[2].trim().replace(
						"/store/people/details?id=", "");
				if (userIDs.contains(userid)) {
					System.out.println(line); 
				}
			}


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	public static void allAppsFromDSGUsers(String alluseridsFile,
			String allreviewlistFile) {
		
		try {
			BufferedReader br1 = new BufferedReader(new FileReader(alluseridsFile));
			String line1 = null;
			
			List<String> userIDs = new ArrayList<String>();

			while ((line1 = br1.readLine()) != null) {
				userIDs.add(line1.trim());
			}
			//System.out.println(userIDs); 
			
			BufferedReader br = new BufferedReader(new FileReader(
					allreviewlistFile));
			String line = null;
			
			HashMap<String, Integer> appscountmap = new HashMap<String, Integer>();
			HashMap<String, List<String>> appsUsersList = new HashMap<String, List<String>>();
			
 			

			while ((line = br.readLine()) != null) {
				line = line.replace("\"", "");
				String[] split = line.split(",");

				if (split.length < 7) {
					//System.out.println(line);
					continue;
				}
				
				String userid = split[2].trim().replace(
						"/store/people/details?id=", "");
				if (userIDs.contains(userid)) {
					String appid = split[4].trim(); 
					
					if(appscountmap.keySet().contains(appid)){
						Integer count = appscountmap.get(appid);
						count++;
						appscountmap.put(appid, count);
						
						List<String> usersList = appsUsersList.get(appid);
						usersList.add(userid);
						appsUsersList.put(appid, usersList);
					}
					else {
						appscountmap.put(appid, 1);
						List<String> usersList = new ArrayList<String>();
						usersList.add(userid);
						appsUsersList.put(appid, usersList);
					}
					
					System.out.println(line); 
				}
			}
			
			for (String appid : appscountmap.keySet()) {
				System.out.println(appid + "," + appscountmap.get(appid)); 
			}
			
			for (String appid : appsUsersList.keySet()) {
				System.out.println(appid + "," + appsUsersList.get(appid)); 
			}
			
			
			BufferedReader br2 = new BufferedReader(new FileReader("/1stdsgAppCount.csv")); 
			String line2 = null;
			
			List<String> appids = new ArrayList<String>();

			int appcount = 0;
			
			while ((line1 = br1.readLine()) != null) {
				appids.add(line2.split(",")[0].trim());
				appcount ++;
				if (appcount == 1000) {
					break;
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public static void findReviewsForDSG(List<String> userIDs,
			String allreviewlistFile, int dsgid) {
		int totalReviewCount = 0;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					allreviewlistFile));
			
			String line = null;
			
			String output = "";
			
			while ((line = br.readLine()) != null) {
				line = line.replace("\"", "");
				String[] split = line.split(",");

				if (split.length < 7) {
					//System.out.println(line);
					continue;
				}
				
				String userid = split[2].trim().replace(
						"/store/people/details?id=", "");
				
				String reviewtext = split[5].trim();
				if (reviewtext.equalsIgnoreCase("") ) {
					continue;
					
				}
				
				
				if (userIDs.contains(userid)) {
					totalReviewCount++;
					
					
					if (reviewtext.endsWith("?") || reviewtext.endsWith(".") || reviewtext.endsWith("!")) {
						output += reviewtext + " ";
						//System.out.println(reviewtext);
						
					}else {
						 
						output += reviewtext + ". ";
						//break;
					}
					//System.out.println(userid + "," + dsgid +  "," + split[4].trim() + "," + split[5].trim());
				}
			}
			
			//System.out.println(output);
			
			System.out.println(userIDs.size() + "," + totalReviewCount); 
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	public static void findReviewsForDSGWithSplit(List<String> userIDs,
			String allreviewlistFile, int dsgid) {
		int totalReviewCount = 0;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					allreviewlistFile));
			
			String line = null;
			
			String output = "";
			
			int splitfilecount = 1;
			int splitreviewcount = 0;
			
			while ((line = br.readLine()) != null) {
				line = line.replace("\"", "");
				String[] split = line.split(",");

				if (split.length < 7) {
					//System.out.println(line);
					continue;
				}
				
				String userid = split[2].trim().replace(
						"/store/people/details?id=", "");
				
				String reviewtext = split[5].trim();
				if (reviewtext.equalsIgnoreCase("") ) {
					continue;
					
				}
				
				
				if (userIDs.contains(userid)) {
					totalReviewCount++;
					splitreviewcount++;
					
					if (reviewtext.endsWith("?") || reviewtext.endsWith(".") || reviewtext.endsWith("!")) {
						output += reviewtext + " ";
						//System.out.println(reviewtext);
						
					}else {
						output += reviewtext + ". ";
						//break;
					}
					
					if (splitreviewcount == 50) {
						writereviewsinfile(output, splitfilecount, dsgid);
						splitreviewcount = 0;
						splitfilecount++;
						output = "";
						
						//break;
					}
					
					//System.out.println(userid + "," + dsgid +  "," + split[4].trim() + "," + split[5].trim());
				}
			}
			
			writereviewsinfile(output, splitfilecount, dsgid);
			
			//System.out.println(output);
			
			System.out.println(userIDs.size() + "," + totalReviewCount); 
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	private static void writereviewsinfile(String output, int splitfilecount, int dsgid) {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("dsg"+ dsgid + "/dsgreviews" + dsgid +"_" + splitfilecount + ".txt", false)));
			writer.println(output);
			System.out.println(output);
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
}
