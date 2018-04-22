package com.mizan.dsg.dataclass;

import java.util.ArrayList;
import java.util.List;

public class DSGInfo {
	private List<String> allUserIDs;
	private int minimumDegree;
	private float edgeDensity;
	
	
	public DSGInfo() {
		allUserIDs = new ArrayList<String>(); 
	}

	public List<String> getAllUserIDs() {
		return allUserIDs;
	}

	public void setAllUserIDs(List<String> allUserIDs) {
		this.allUserIDs = allUserIDs;
	}

	public int getMinimumDegree() {
		return minimumDegree;
	}

	public void setMinimumDegree(int minimumDegree) {
		this.minimumDegree = minimumDegree;
	}

	public float getEdgeDensity() {
		return edgeDensity;
	}

	public void setEdgeDensity(float edgeDensity) {
		this.edgeDensity = edgeDensity;
	}
}
