package com.vow.entity;

import java.io.Serializable;
import java.util.List;

public class Upload_content implements Serializable{

	private int ucid;
	private String uploadtime;//Datetime
	private String description;
	private User originalUser;//原创用户
	private int loadtimes;//下载次数
	private int liketimes;//点赞次数
	private int favoritetimes;//收藏次数
	private int if_deleted;//是否被删除
	
	
	//private List<Tag> taglist;//标签列表		

	public Upload_content(int ucid, String uploadtime, String description, User originalUser, int loadtimes,
			int liketimes, int favoritetimes,int if_deleted) {
		super();
		this.ucid = ucid;
		this.uploadtime = uploadtime;
		this.description = description;
		this.originalUser = originalUser;
		this.loadtimes = loadtimes;
		this.liketimes = liketimes;
		this.favoritetimes = favoritetimes;
		this.if_deleted=if_deleted;
	}

	public Upload_content(){
		
	}

	public int getUcid() {
		return ucid;
	}

	public void setUcid(int ucid) {
		this.ucid = ucid;
	}

	public String getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLoadtimes() {
		return loadtimes;
	}

	public void setLoadtimes(int loadtimes) {
		this.loadtimes = loadtimes;
	}

	public int getLiketimes() {
		return liketimes;
	}

	public void setLiketimes(int liketimes) {
		this.liketimes = liketimes;
	}

	public int getFavoritetimes() {
		return favoritetimes;
	}

	public void setFavoritetimes(int favoritetimes) {
		this.favoritetimes = favoritetimes;
	}

	public User getOriginalUser() {
		return originalUser;
	}

	public void setOriginalUser(User originalUser) {
		this.originalUser = originalUser;
	}

	public int getIf_deleted() {
		return if_deleted;
	}

	public void setIf_deleted(int if_deleted) {
		this.if_deleted = if_deleted;
	}

	
	
}
