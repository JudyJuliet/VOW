package com.etc.service;

import java.util.List;

import com.etc.entity.Content_file;
import com.etc.entity.Tag;
import com.etc.entity.Upload_content;
import com.etc.entity.User;

public interface UploadContentService {

	int insertUploadContent(Upload_content uc);
	boolean deleteUploadContent(Upload_content uc);//删除操作，original_upload,content_file,content_tag,comments，
	                                               //like_favorite_download  这5张表相关条目删除，使repost_content的if_deleted置为1

	boolean downLoadContent(User user,Upload_content uc);//下载，点赞，收藏操作
	boolean likeContent(User user,Upload_content uc);
	boolean favoriteContent(User user,Upload_content uc);
	
	List<Upload_content> getOriginalContentList(User user);//查找user发表的内容夹列表
	List<Upload_content>getPostContentList(User user);//查找user转发的内容夹列表
	List<Upload_content> getDownloadContentList(User user);//查找user下载的内容夹列表
	List<Upload_content> getFavoriteContentList(User user);//查找user收藏的内容夹列表
	List<Upload_content>getFredContentList(User user);//查找user关注的人的内容夹列表O
	
	List<Content_file> getContentFile(Upload_content uc);//查找文件夹对应的文件
	List<Tag>getTagOfContentList(Upload_content uc);//一个内容夹的标签列表
	
	List<Upload_content> getTagContentList(String tagname);//根据tag查找对应的内容
	List<Upload_content> getKeyContentList(String keyword);//根据关键词查找对应的内容
	List<Upload_content> getHotContentList();//查找热门内容
}
