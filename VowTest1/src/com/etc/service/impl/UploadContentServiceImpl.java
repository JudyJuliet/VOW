package com.etc.service.impl;

import java.util.List;

import com.etc.dao.CommentsDAO;
import com.etc.dao.ContentFileDAO;
import com.etc.dao.ContentTagDAO;
import com.etc.dao.LikeFavoriteDownloadDAO;
import com.etc.dao.RepostContentDAO;
import com.etc.dao.UploadContentDAO;
import com.etc.dao.impl.CommentsDAOImpl;
import com.etc.dao.impl.ContentFileDAOImpl;
import com.etc.dao.impl.ContentTagDAOImpl;
import com.etc.dao.impl.LikeFavoriteDownloadDAOImpl;
import com.etc.dao.impl.RepostContentDAOImpl;
import com.etc.dao.impl.UploadContentDAOImpl;
import com.etc.entity.Content_file;
import com.etc.entity.Tag;
import com.etc.entity.Upload_content;
import com.etc.entity.User;
import com.etc.service.UploadContentService;

public class UploadContentServiceImpl implements UploadContentService {


	UploadContentDAO ucDAO=new UploadContentDAOImpl();
	RepostContentDAO repostContentDAO=new RepostContentDAOImpl();
//	OriginalUploadDAO originalUploadDAO=new OriginalUploadDAOImpl();
	LikeFavoriteDownloadDAO likeFavoriteDownloadDAO=new LikeFavoriteDownloadDAOImpl();
	ContentTagDAO contentTagDAO=new ContentTagDAOImpl();
	ContentFileDAO contentFileDAO=new ContentFileDAOImpl();
	CommentsDAO commentsDAO=new CommentsDAOImpl();
	
	@Override
	public int insertUploadContent(Upload_content uc) {
		// TODO Auto-generated method stub
		return ucDAO.insertUploadContent(uc);
	}

	@Override
	public boolean deleteUploadContent(Upload_content uc) {
		// TODO Auto-generated method stub
		boolean flag1=ucDAO.setDeleteContent(uc);
		boolean flag3=likeFavoriteDownloadDAO.deleteLikeFavoriteDownload(uc);
		boolean flag4=contentTagDAO.deleteContentTag(uc);
		boolean flag5=contentFileDAO.deleteContentFile(uc);
		boolean flag6=commentsDAO.deleteComments(uc);
		return (flag1);
	}

	@Override
	public boolean downLoadContent(User user,Upload_content uc) {
		// TODO Auto-generated method stub
		return ucDAO.downLoadContent(user,uc);
	}

	@Override
	public boolean likeContent(User user,Upload_content uc) {
		// TODO Auto-generated method stub
		return ucDAO.likeContent(user,uc);
	}

	@Override
	public boolean favoriteContent(User user,Upload_content uc) {
		// TODO Auto-generated method stub
		return ucDAO.favoriteContent(user,uc);
	}

	@Override
	public List<Upload_content> getOriginalContentList(User user) {
		// TODO Auto-generated method stub
		return ucDAO.getOriginalContentList(user);
	}

	@Override
	public List<Upload_content> getPostContentList(User user) {
		// TODO Auto-generated method stub
		return ucDAO.getPostContentList(user);
	}

	@Override
	public List<Upload_content> getDownloadContentList(User user) {
		// TODO Auto-generated method stub
		return ucDAO.getDownloadContentList(user);
	}

	@Override
	public List<Upload_content> getFavoriteContentList(User user) {
		// TODO Auto-generated method stub
		return ucDAO.getFavoriteContentList(user);
	}

	@Override
	public List<Upload_content> getFredContentList(User user) {
		// TODO Auto-generated method stub
		return ucDAO.getFredContentList(user);
	}

	@Override
	public List<Content_file> getContentFile(Upload_content uc) {
		// TODO Auto-generated method stub
		return ucDAO.findContentFile(uc);
	}
	
	@Override
	public List<Tag> getTagOfContentList(Upload_content uc) {
		// TODO Auto-generated method stub
		return ucDAO.findTagOfContentList(uc);
	}

	@Override
	public List<Upload_content> getTagContentList(String tagname) {
		// TODO Auto-generated method stub
		return ucDAO.getTagContentList(tagname);
	}

	@Override
	public List<Upload_content> getKeyContentList(String keyword) {
		// TODO Auto-generated method stub
		return ucDAO.getKeyContentList(keyword);
	}

	@Override
	public List<Upload_content> getHotContentList() {
		// 查找热门内容
		return ucDAO.getHotContentList();
	}
	
}
