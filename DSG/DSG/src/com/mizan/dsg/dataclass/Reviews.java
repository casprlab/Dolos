package com.mizan.dsg.dataclass;

import java.util.ArrayList;
import java.util.List;

public class Reviews {
	private List<Review> reviews;
	private int day;
	
	public Reviews() {
		reviews = new ArrayList<Review>();
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
	
	public Reviews clone() {
		Reviews reviews = new Reviews();
		for (Review review : getReviews()) {
			Review clonereview = new Review(review.getAppid(), review.getUser(), review.getDate());
			reviews.add(clonereview);
		}
		
		return reviews;
	}

	private void add(Review review) {
		reviews.add(review);
	}
	
	@Override
	public String toString() {
		String s = "day :: " + day + " count " +  reviews.size() + " ";
		
		//for (Review review : reviews) {
			//s = s + review.toString() + "\n";
		//}
		
		// TODO Auto-generated method stub
		return s;
	}
}
