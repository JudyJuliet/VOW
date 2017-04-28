package com.etc.service.impl;

import com.etc.dao.RepostContentDAO;
import com.etc.dao.impl.RepostContentDAOImpl;
import com.etc.entity.Repost_content;
import com.etc.entity.Upload_content;
import com.etc.service.RepostContentService;

public class RepostContentServiceImpl implements RepostContentService {

	RepostContentDAO repostContentDAO=new RepostContentDAOImpl();
	
	@Override
	public boolean insertRepostContent(Repost_content rc) {
		// TODO Auto-generated method stub
		return repostContentDAO.insertRepostContent(rc);
	}

	@Override
	public int countRepostTimes(Upload_content uc) {
		// TODO Auto-generated method stub
		return repostContentDAO.countRepostTimes(uc);
	}

}
