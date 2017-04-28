package com.etc.service.impl;

import java.util.List;

import com.etc.dao.ContentTagDAO;
import com.etc.dao.impl.ContentTagDAOImpl;
import com.etc.entity.Tag;
import com.etc.entity.Upload_content;
import com.etc.service.ContentTagService;

public class ContentTagServiceImpl implements ContentTagService {

	ContentTagDAO contentTagDAO=new ContentTagDAOImpl();
	
	@Override
	public boolean addContentTag(Upload_content uc, Tag tag) {
		// TODO Auto-generated method stub
		return contentTagDAO.insertContentTag(uc, tag);
	}


	@Override
	public List<Upload_content> findContentOfTagList(Tag tag) {
		// TODO Auto-generated method stub
		return contentTagDAO.findContentOfTagList(tag);
	}


}
