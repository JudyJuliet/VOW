package com.etc.entity;

import java.io.Serializable;

public class Repost_content implements Serializable{

	private User user;
	private Upload_content uc;
	private String addtion;
	private int if_deleted;
	
	public Repost_content(User user, Upload_content uc, String addtion, int if_deleted) {
		super();
		this.user = user;
		this.uc = uc;
		this.addtion = addtion;
		this.if_deleted = if_deleted;
	}
	
	public Repost_content(){
	
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Upload_content getUc() {
		return uc;
	}

	public void setUc(Upload_content uc) {
		this.uc = uc;
	}

	public String getAddtion() {
		return addtion;
	}

	public void setAddtion(String addtion) {
		this.addtion = addtion;
	}

	public int getIf_deleted() {
		return if_deleted;
	}

	public void setIf_deleted(int if_deleted) {
		this.if_deleted = if_deleted;
	}
	
	
	
}
