package com.etc.service.impl;

import java.util.List;

import com.etc.dao.CommentsDAO;
import com.etc.dao.impl.CommentsDAOImpl;
import com.etc.entity.Comments;
import com.etc.entity.Upload_content;
import com.etc.service.CommentsService;

public class CommentsServiceImpl implements CommentsService {

	CommentsDAO commentsDAO=new CommentsDAOImpl();
	
	@Override
	public boolean addComments(Comments comment) {
		// TODO Auto-generated method stub
		return commentsDAO.insertComments(comment);
	}

	@Override
	public List<Comments> getComments(Upload_content uc) {
		// TODO Auto-generated method stub
		return commentsDAO.findComments(uc);
	}

}
