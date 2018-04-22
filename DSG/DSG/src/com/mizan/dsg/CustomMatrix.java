/******************************************************************************
 *  Compilation:  javac Matrix.java
 *  Execution:    java Matrix
 *
 *  A bare-bones collection of  methods for manipulating
 *  matrices.
 *
 ******************************************************************************/
package com.mizan.dsg;

import Jama.Matrix;

public class CustomMatrix {

    // return a random m-by-n matrix with values between 0 and 1
    public static int[][] random(int m, int n) {
        int[][] C = new int[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = (int) Math.random();
        return C;
    }

    // return n-by-n identity matrix I
    public static int[][] identity(int n) {
        int[][] I = new int[n][n];
        for (int i = 0; i < n; i++)
            I[i][i] = 1;
        return I;
    }

    // return x^T y
    public static int dot(int[] x, int[] y) {
        if (x.length != y.length) throw new RuntimeException("Illegal vector dimensions.");
        int sum = 0;
        for (int i = 0; i < x.length; i++)
            sum += x[i] * y[i];
        return sum;
    }

    // return C = A^T
    public static int[][] transpose(int[][] A) {
        int m = A.length;
        int n = A[0].length;
        int[][] C = new int[n][m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[j][i] = A[i][j];
        return C;
    }

    // return C = A + B
    public static int[][] add(int[][] A, int[][] B) {
        int m = A.length;
        int n = A[0].length;
        int[][] C = new int[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }

    // return C = A - B
    public static int[][] subtract(int[][] A, int[][] B) {
        int m = A.length;
        int n = A[0].length;
        int[][] C = new int[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }

    // return C = A * B
    public static int[][] multiply(int[][] A, int[][] B) {
        int mA = A.length;
        int nA = A[0].length;
        int mB = B.length;
        int nB = B[0].length;
        if (nA != mB) throw new RuntimeException("Illegal matrix dimensions.");
        int[][] C = new int[mA][nB];
        for (int i = 0; i < mA; i++)
            for (int j = 0; j < nB; j++)
                for (int k = 0; k < nA; k++)
                    C[i][j] += A[i][k] * B[k][j];
        return C;
    }

    // matrix-vector multiplication (y = A * x)
    public static int[] multiply(int[][] A, int[] x) {
        int m = A.length;
        int n = A[0].length;
        if (x.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        int[] y = new int[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                y[i] += A[i][j] * x[j];
        return y;
    }


    // vector-matrix multiplication (y = x^T A)
    public static int[] multiply(int[] x, int[][] A) {
        int m = A.length;
        int n = A[0].length;
        if (x.length != m) throw new RuntimeException("Illegal matrix dimensions.");
        int[] y = new int[n];
        for (int j = 0; j < n; j++)
            for (int i = 0; i < m; i++)
                y[j] += A[i][j] * x[i];
        return y;
    }
    
	public static int traceMatric(int a[][]) {
		int sum = 0;
		for (int i = 0; i < a.length; ++i)
	    {
	        sum = sum + a[i][i];
	    }
		return sum;
	}
	
	public static void displayAdjacencyMatrix(int a[][]) {
		System.out.println("\nThe Matrix is :");
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				System.out.print(String.format( "%.0f",a[i][j])  + " ");
			}
			System.out.println();
		}
	}
	
	public static double calculateTriangleCount(Matrix matrix){ 
		Matrix a3 = matrix.times(matrix).times(matrix);
		//a3.print(n, n); 
		double trace = a3.trace();
		//System.out.println(trace);
		double triangleCount = trace/6;
		return triangleCount; 
	}
	
	public static double calculateTriangleCount(int a[][]){ 
		int b[][] = multiply(a,a);
		int c[][] = multiply(b, a);
		int trace = traceMatric(a); 
		double triangleCount = trace/(double)6;
		return triangleCount; 
	}
	
	public static double calculateDensity(int a[][]) {
		double calculateTriangleCount = calculateTriangleCount(a);
		int n = a.length;
		double triangleDensity = calculateTriangleCount/(double)n;		
		return triangleDensity;
	}
	
//	public double calculateTriangleCount(int a[][]) {
//		int b[][] = multiply(a,a);
//		int c[][] = multiply(b, a);
//		
//		Matrix adjmatrix = new Matrix(adjMat);
//		
//		double calculateTriangleCount = calculateTriangleCount(adjmatrix);
//		n = adjMat.length;
//		triangleDensity = calculateTriangleCount/n;		
//		return triangleDensity;
//	}
}