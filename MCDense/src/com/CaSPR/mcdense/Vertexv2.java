package com.CaSPR.mcdense;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Vertexv2 {
	public final String lbl;
	public final Set<Edgev2> edgev2s = new HashSet<Edgev2>();
	public List<String> userids;
	public boolean isCom;

	public Vertexv2(String lbl) {
		this.lbl = lbl;
		isCom = false;
	}

	public Vertexv2(Vertexv2 v1, Vertexv2 v2) {
		this.lbl = v1.lbl;
		isCom = true;
		userids = new ArrayList<String>();

		addUsersofVertex(v1);
		addUsersofVertex(v2);
	}

	private void addUsersofVertex(Vertexv2 v) {
		if (v.isCom) {
			for (String userid : v.userids) {
				userids.add(userid);
			}
		}else{
			userids.add(v.lbl);
		}
	}

	public void addEdge(Edgev2 edgev2) {
		edgev2s.add(edgev2);
	}

	public Edgev2 getEdgeTo(Vertexv2 v2) {
		for (Edgev2 edgev2 : edgev2s) {
			if (edgev2.contains(this, v2))
				return edgev2;
		}
		return null;
	}

}