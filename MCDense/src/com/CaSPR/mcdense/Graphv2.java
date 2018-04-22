package com.CaSPR.mcdense;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Jama.Matrix;

public class Graphv2 {

	public final Map<String, Vertexv2> vertices = new TreeMap<String, Vertexv2>(
			new Comparator<String>() {
				// for pretty printing
				@Override
				public int compare(String arg0, String arg1) {
					return arg0.compareTo(arg1);
				}
			});

	public final List<Edgev2> edgev2s = new ArrayList<Edgev2>();

	public void addVertex(Vertexv2 v) {
		vertices.put(v.lbl, v);
	}

	public Vertexv2 getVertex(String lbl) {
		Vertexv2 v;
		if ((v = vertices.get(lbl)) == null) {
			v = new Vertexv2(lbl);
			addVertex(v);
		}
		return v;
	}

	public float getDelta() {
		int n = vertices.size();
		float newNchoose2 = n * (n - 1) / 2;
		float density = edgev2s.size()/newNchoose2;
		return density;
	}
	
	public double[][] generateAdjacencyMatrix() { 
		//System.out.println("generating adj matrix for n ::" + users.size()); 
		int n = vertices.size();
		double[][] adjMat = new double[n][n];
		
		List<Vertexv2> allvertices = new ArrayList<Vertexv2>();
		
		for (String key : vertices.keySet()) {
			allvertices.add(vertices.get(key));
		}
		
		for (Edgev2 edge : edgev2s) {
			int n1 = allvertices.indexOf(edge.ends.get(0));
			int n2 = allvertices.indexOf(edge.ends.get(1));
			
			adjMat[n1][n2] = 1;
			adjMat[n2][n1] = 1;
		}
		
		return adjMat;
	}
	
	public double calculateTriangleCount(Matrix matrix){ 
		Matrix a3 = matrix.times(matrix).times(matrix);
		//a3.print(n, n); 
		double trace = a3.trace();
		//System.out.println(trace);
		double triangleCount = trace/6;
		return triangleCount; 
	}


	public double getRho() {
		int n = vertices.size();
		double[][] adjMat = generateAdjacencyMatrix();
		
		Matrix adjmatrix = new Matrix(adjMat);
		double calculateTriangleCount = calculateTriangleCount(adjmatrix);
		n = adjMat.length;
		float newNchoose3 = n * (n - 1) * (n - 2) / 6;
		//triangleDensity = calculateTriangleCount/n;
		double triangleDensity = calculateTriangleCount/newNchoose3;		
		return triangleDensity;
	}
}