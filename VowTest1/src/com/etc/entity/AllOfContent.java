package com.etc.entity;

import java.io.Serializable;
import java.util.List;

public class AllOfContent implements Serializable{

	private Upload_content content;
	private List<Tag> taglist;
	private List<String> filelist;
	private int comments_number;
	private boolean like,favor;
	public AllOfContent() {
		// TODO Auto-generated constructor stub
	}
	public Upload_content getContent() {
		return content;
	}
	public void setContent(Upload_content content) {
		this.content = content;
	}
	public List<Tag> getTaglist() {
		return taglist;
	}
	public void setTaglist(List<Tag> taglist) {
		this.taglist = taglist;
	}
	public List<String> getFilelist() {
		return filelist;
	}
	public void setFilelist(List<String> filelist) {
		this.filelist = filelist;
	}
	public int getComments_number() {
		return comments_number;
	}
	public void setComments_number(int comments_number) {
		this.comments_number = comments_number;
	}
	public boolean isLike() {
		return like;
	}
	public void setLike(boolean like) {
		this.like = like;
	}
	public boolean isFavor() {
		return favor;
	}
	public void setFavor(boolean favor) {
		this.favor = favor;
	}
	

}
//第一步:内容--赞的数量已存在,内容原用户,
//标签列表
//文件列表
//评论数量
//是否点赞,是否收藏.
//第二步:
//通过uc id 取出这些对应的所有值.