package com.CaSPR.mcdense;
import java.util.ArrayList;
import java.util.List;

public class Edgev2 {

	public final List<Vertexv2> ends = new ArrayList<Vertexv2>();

	public Edgev2(Vertexv2 fst, Vertexv2 snd) {
		if (fst == null || snd == null) {
			throw new IllegalArgumentException("Both vertices are required");
		}
		ends.add(fst);
		ends.add(snd);
	}

	public boolean contains(Vertexv2 v1, Vertexv2 v2) {
		return ends.contains(v1) && ends.contains(v2);
	}

	public Vertexv2 getOppositeVertex(Vertexv2 v) {
		if (!ends.contains(v)) {
			throw new IllegalArgumentException("Vertex " + v.lbl);
		}
		return ends.get(1 - ends.indexOf(v));
	}

	public void replaceVertex(Vertexv2 oldV, Vertexv2 newV) {
		if (!ends.contains(oldV)) {
			throw new IllegalArgumentException("Vertex " + oldV.lbl);
		}
		ends.remove(oldV);
		ends.add(newV);
	}
}
