package com.mizan.dsg;

import java.util.Collections;
import java.util.List;

public class Stat {
	public static int max(List<Integer> a) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i) > max)
				max = a.get(i);
		}
		return max;
	}

	public static int min(List<Integer> a) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i) < min)
				min = a.get(i);
		}
		return min;
	}

	public static int sum (List<Integer> a){
        if (a.size() > 0) {
            int sum = 0;
 
            for (Integer i : a) {
                sum += i;
            }
            return sum;
        }
        return 0;
    }
    public static double mean (List<Integer> a){
        int sum = sum(a);
        double mean = 0;
        mean = sum / (a.size() * 1.0);
        return mean;
    }
    public static double median (List<Integer> a){
    	Collections.sort(a);
        int middle = a.size()/2;
 
        if (a.size() % 2 == 1) {
            return a.get(middle);
        } else {
           return (a.get(middle-1) + a.get(middle)) / 2.0;
        }
    }
    public static double sd (List<Integer> a){
        int sum = 0;
        double mean = mean(a);
 
        for (Integer i : a)
            sum += Math.pow((i - mean), 2);
        return Math.sqrt( sum / ( a.size() - 1 ) ); // sample
    }
}
