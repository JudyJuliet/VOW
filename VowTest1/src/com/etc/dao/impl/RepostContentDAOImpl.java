package com.etc.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.etc.dao.RepostContentDAO;
import com.etc.dbutil.DBManager;
import com.etc.entity.Repost_content;
import com.etc.entity.Upload_content;

public class RepostContentDAOImpl implements RepostContentDAO {

	DBManager manager=new DBManager();
	
	@Override
	public boolean insertRepostContent(Repost_content rc) {
		// TODO Auto-generated method stub
		String sql="insert into repost_content values(?,?,?)";
		int lines=manager.execUpdate(sql, rc.getUser().getUserid(),rc.getUc().getUcid(),rc.getAddtion());
		return lines>0;
	}

	@Override
	public boolean setDeleteRepostContent(Upload_content uc) {
		// TODO Auto-generated method stub
		String sql="update repost_content set if_deleted=1 where ucid=?";
		int lines=manager.execUpdate(sql, uc.getUcid());
		return lines>0;
	}

	@Override
	public int countRepostTimes(Upload_content uc) {
		// TODO Auto-generated method stub
		String sql="SELECT *from repost_content "+
					" WHERE repost_content.ucid=?";
		ResultSet rs=manager.execQuery(sql,uc.getUcid());
		int times=0;
		try {
			while(rs.next()){
				times++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return times;
	}

	
}
