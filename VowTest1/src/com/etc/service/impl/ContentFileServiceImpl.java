package com.etc.service.impl;

import com.etc.dao.ContentFileDAO;
import com.etc.dao.impl.ContentFileDAOImpl;
import com.etc.entity.Content_file;
import com.etc.service.ContentFileService;

public class ContentFileServiceImpl implements ContentFileService {

	ContentFileDAO contentFileDAO=new ContentFileDAOImpl();
	@Override
	public boolean addContentFile(Content_file cf) {
		// TODO Auto-generated method stub
		return contentFileDAO.insertContentFile(cf);
	}

}
