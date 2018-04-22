package com.mizan.dsg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DSGMain {

	private static final String AppList_FilePath = "/Users/mdmizanurrahman/Google Drive/onair/244_400/140apps_sort.csv";
	
	public static void main(String[] args) {
		//DFS400v3 dfs400v3 = new DFS400v3("white.simplicity.babyabcbengalibengali"); 
		runonAll(); 
	}

	private static void runonAll() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(AppList_FilePath));
			String appid = null;

			while ((appid = br.readLine()) != null) {
				DSG dfs400v3 = new DSG(appid);
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
