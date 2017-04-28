package com.vow.entity;
import java.io.Serializable;

public class Comments implements Serializable{

	private int commentid;
	private User user;
	private Upload_content uc;
	private String comdetail;
	
	public Comments(int commentid, User user, Upload_content uc, String comdetail) {
		super();
		this.commentid = commentid;
		this.user = user;
		this.uc = uc;
		this.comdetail = comdetail;
	}
	
	public Comments(){
		
	}

	public int getCommentid() {
		return commentid;
	}

	public void setCommentid(int commentid) {
		this.commentid = commentid;
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

	public String getComdetail() {
		return comdetail;
	}

	public void setComdetail(String comdetail) {
		this.comdetail = comdetail;
	}
	
	
}
