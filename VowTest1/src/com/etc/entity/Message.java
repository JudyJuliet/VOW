package com.etc.entity;

import java.io.Serializable;

public class Message implements Serializable{

	int msgid;
	String message;
	User user1,user2;
	String sendtime;
	public Message(int msgid, User user1, User user2, String sendtime,String message) {
		super();
		this.msgid = msgid;
		this.message = message;
		this.user1 = user1;
		this.user2 = user2;
		this.sendtime = sendtime;
	}
	public int getMsgid() {
		return msgid;
	}
	public void setMsgid(int msgid) {
		this.msgid = msgid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public User getUser1() {
		return user1;
	}
	public void setUser1(User user1) {
		this.user1 = user1;
	}
	public User getUser2() {
		return user2;
	}
	public void setUser2(User user2) {
		this.user2 = user2;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	
}
