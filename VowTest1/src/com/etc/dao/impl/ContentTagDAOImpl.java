package com.etc.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.etc.dao.ContentTagDAO;
import com.etc.dbutil.DBManager;
import com.etc.entity.Tag;
import com.etc.entity.Upload_content;
import com.etc.entity.User;

public class ContentTagDAOImpl implements ContentTagDAO {

DBManager manager=new DBManager();
	
	@Override
	public boolean insertContentTag(Upload_content uc, Tag tag) {
		// TODO Auto-generated method stub
		String sql="insert into content_tag values(?,?)";
		int lines=manager.execUpdate(sql, uc.getUcid(),tag.getTagid());
		return lines>0;
	}

	

	@Override
	public List<Upload_content> findContentOfTagList(Tag tag) {
		// TODO Auto-generated method stub
		String sql="select * from content_tag ,upload_content where tagid=? and content_tag.ucid=upload_content.ucid";
		ResultSet rs=manager.execQuery(sql, tag.getTagid());
		List<Upload_content>list=new ArrayList<Upload_content>();
		try {
			while(rs.next()){
				int userid=rs.getInt(6);
				User oriUser=new UserDAOImpl().findUser(userid);//原创用户
				
				Upload_content uc=new Upload_content(rs.getInt(1),rs.getString(4),rs.getString(5),oriUser,rs.getInt(7),rs.getInt(8),rs.getInt(9),rs.getInt(10));
				list.add(uc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/*@Override
	public boolean deleteContentTag(Upload_content uc, Tag tag) {
		// TODO Auto-generated method stub
		String sql="delete from content_tag where ucid=? and tagid=?";
		int lines=manager.execUpdate(sql, uc.getUcid(),tag.getTagid());
		return lines>0;
	}*/

	@Override
	public boolean deleteContentTag(Upload_content uc) {
		// TODO Auto-generated method stub
		String sql="delete from content_tag where ucid=?";
		int lines=manager.execUpdate(sql, uc.getUcid());
		return lines>0;
	}

}
