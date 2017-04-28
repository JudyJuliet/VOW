package com.etc.entity;

import java.io.Serializable;

public class Content_file implements Serializable{

	private int fileid;
	private String filename;
	private Upload_content uc;
	
	public Content_file(int fileid, String filename, Upload_content uc) {
		super();
		this.fileid = fileid;
		this.filename = filename;
		this.uc = uc;
	}
	
	public Content_file(){
		
	}

	public int getFileid() {
		return fileid;
	}

	public void setFileid(int fileid) {
		this.fileid = fileid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Upload_content getUc() {
		return uc;
	}

	public void setUc(Upload_content uc) {
		this.uc = uc;
	}
	
	
}
