package com.etc.dao.impl;

import com.etc.dao.ContentFileDAO;
import com.etc.dbutil.DBManager;
import com.etc.entity.Content_file;
import com.etc.entity.Upload_content;

public class ContentFileDAOImpl implements ContentFileDAO {

	DBManager manager=new DBManager();
	
	@Override
	public boolean insertContentFile(Content_file cf) {
		// TODO Auto-generated method stub
		String sql="insert into content_file values(null,?,?)";
		int lines=manager.execUpdate(sql, cf.getUc().getUcid(),cf.getFilename());
		return lines>0;
	}

	@Override
	public boolean deleteContentFile(Upload_content uc) {
		// TODO Auto-generated method stub
		String sql="delete from content_file where ucid=?";
		int lines=manager.execUpdate(sql, uc.getUcid());
		return lines>0;
	}

	

}
