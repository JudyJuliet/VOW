package com.etc.entity;

import java.io.Serializable;

public class Like_favorite_download implements Serializable{

	private User user;
	private Upload_content uc;
	private int uclike;//点赞  0否1是
	private int favorite;//收藏  0否1是
	private int download;//下载  0否1是
	
	public Like_favorite_download(User user, Upload_content uc, int uclike, int favorite, int download) {
		super();
		this.user = user;
		this.uc = uc;
		this.uclike = uclike;
		this.favorite = favorite;
		this.download = download;
	}
	
	public Like_favorite_download(){
	
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

	public int getUclike() {
		return uclike;
	}

	public void setUclike(int like) {
		this.uclike = like;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}

	public int getDownload() {
		return download;
	}

	public void setDownload(int download) {
		this.download = download;
	}
	
	
}
