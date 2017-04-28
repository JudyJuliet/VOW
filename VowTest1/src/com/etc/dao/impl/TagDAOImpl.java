package com.etc.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.etc.dao.TagDAO;
import com.etc.dbutil.DBManager;
import com.etc.entity.Tag;

public class TagDAOImpl implements TagDAO {

	DBManager manager=new DBManager();
	
	@Override
	public List<Tag> findTagList() {
		// TODO Auto-generated method stub
		String sql="select * from tag";
		ResultSet rs=manager.execQuery(sql);
		List<Tag>tagList=new ArrayList<Tag>();
		try {
			while(rs.next()){
				Tag tag=new Tag(rs.getInt(1),rs.getString(2));
				tagList.add(tag);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tagList;
		
	}

	@Override
	public boolean insertTag(Tag tag) {
		// TODO Auto-generated method stub
		String sql="insert into tag values(null,?)";
		int lines=manager.execUpdate(sql, tag.getTagname());
		return lines>0;
	}

	@Override
	public Tag findTag(String tagname) {
		// TODO Auto-generated method stub
		String sql="SELECT *FROM tag WHERE tagname=?";
		ResultSet rs=manager.execQuery(sql, tagname);
		try {
			if(rs.next())
			{
				Tag tag=new Tag(rs.getInt(1),tagname);
				return tag;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
