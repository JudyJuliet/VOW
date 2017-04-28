package com.etc.dao;

import java.util.List;

import com.etc.entity.Comments;
import com.etc.entity.Upload_content;

public interface CommentsDAO {
 
	boolean insertComments(Comments comment);
	List<Comments>findComments(Upload_content uc);//对某内容夹的评论列表
	
	boolean deleteComments(Upload_content uc);
	
}
