package com.etc.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.etc.dao.UserFansDAO;
import com.etc.dbutil.DBManager;
import com.etc.entity.User;

public class UserFansDAOImpl implements UserFansDAO {

	DBManager manager=new DBManager();
	
	@Override
	public boolean insertUserFans(User user, User fans) {
		// TODO Auto-generated method stub
		String sql="insert into user_fans values(?,?)";
		int lines=manager.execUpdate(sql, user.getUserid(),fans.getUserid());
		return lines>0;
	}

	@Override
	public boolean deleteUserFans(User user, User fans) {
		// TODO Auto-generated method stub
		String sql="delete from user_fans where user_id=? and fans_id=?";
		int lines=manager.execUpdate(sql, user.getUserid(),fans.getUserid());
		return lines>0;
	}

	@Override
	public List<User> findAttendList(User user) {
		// TODO Auto-generated method stub
		String sql="select * from user , user_fans  where user_fans.user_id=user.userid and user_fans.fans_id=?";
		ResultSet rs=manager.execQuery(sql, user.getUserid());
		List<User> list=new ArrayList<User>();
		try {
			while(rs.next()){
				User idol=new User(rs.getInt(1),
						rs.getInt(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getInt(6),
						rs.getString(7),
						rs.getString(8),
						rs.getString(9),
						rs.getString(10),
						rs.getString(11)
						,rs.getString(12),
						rs.getString(13));
				list.add(idol);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
