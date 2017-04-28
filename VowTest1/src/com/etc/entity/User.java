package com.etc.entity;

import java.io.Serializable;

public class User implements Serializable {

	private int userid;
	private int themeid;
	private String username;
	private String password;
	private String regtime;//timestamp
	private int score;
	private String email;
	private String location;
	private String signature;
	private String photo;
	private String gender;	
	private String phone;	
	private String birthday;//date
	
	public User(int userid, int themeid, String username, String password, String regtime, int score, String email,
			String location, String signature, String photo, String gender, String phone, String birthday) {
		super();
		this.userid = userid;
		this.themeid = themeid;
		this.username = username;
		this.password = password;
		this.regtime = regtime;
		this.score = score;
		this.email = email;
		this.location = location;
		this.signature = signature;
		this.photo = photo;
		this.gender = gender;
		this.phone = phone;
		this.birthday = birthday;
	}
	
	public User(){
		
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getThemeid() {
		return themeid;
	}

	public void setThemeid(int themeid) {
		this.themeid = themeid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegtime() {
		return regtime;
	}

	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	
	
	
}
