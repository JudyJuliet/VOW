package com.etc.service;

import java.util.List;

import com.etc.entity.Comments;
import com.etc.entity.Upload_content;

public interface CommentsService {

	boolean addComments(Comments comment);
	List<Comments>getComments(Upload_content uc);//对某内容夹的评论列表
}
