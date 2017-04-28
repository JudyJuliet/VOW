package com.etc.entity;

import java.io.Serializable;

public class Content_tag implements Serializable{

	private Upload_content uc;
	private Tag tag;
	
	public Content_tag(Upload_content uc, Tag tag) {
		super();
		this.uc = uc;
		this.tag = tag;
	}
	
	public Content_tag(){
		
	}

	public Upload_content getUc() {
		return uc;
	}

	public void setUc(Upload_content uc) {
		this.uc = uc;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	
}
