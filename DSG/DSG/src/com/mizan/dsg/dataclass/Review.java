package com.mizan.dsg.dataclass;

import java.util.Date;

public class Review {
	private User user;
	private int day;
	private Date date;
	private String appid;
	private String dataText;
	private String title;
	private String text;
	

	public Review(String appid, User user, Date date) {
		this.setUser(user);
		this.setAppid(appid);
		this.setDate(date); 
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	@Override
	public String toString() {
		String returnstring = "Review Info :" + getUser().getName() + " user id " + getUser().getId() + " appid " + appid + " " + date + " daye: " + day;
		return returnstring;
	}

	public String getDataText() {
		return dataText;
	}

	public void setDataText(String dataText) {
		this.dataText = dataText;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
