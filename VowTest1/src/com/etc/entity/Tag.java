package com.etc.entity;

import java.io.Serializable;

public class Tag implements Serializable{

	private int tagid;
	private String tagname;
	
	public Tag(int tagid, String tagname) {
		super();
		this.tagid = tagid;
		this.tagname = tagname;
	}
	
	public Tag(){
		
	}

	public int getTagid() {
		return tagid;
	}

	public void setTagid(int tagid) {
		this.tagid = tagid;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}
	
	
}
