package com.etc.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.etc.dao.UserDAO;
import com.etc.dbutil.DBManager;
import com.etc.entity.User;

public class UserDAOImpl implements UserDAO{

	DBManager manager=new DBManager();
	
	public User findUser(String username,String password){
		String sql="select * from user where username=? and password=?";		
		ResultSet rs = manager.execQuery(sql,username,password);
		try {
			 
			if(rs.next()){			
			User user=new User(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13));			
			return user;
			
			}else{						
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			manager.closeConnection();
		}
		return null;
	}
	
	//----------------------------------------------------------------------------------------
	
	public boolean insertUser(User user){
		String sql="insert into user (themeid,username,password,regtime,photo) values(1,?,?,now(),'default_user_photo.png')";
		int lines=manager.execUpdate(sql,user.getUsername(),user.getPassword());
		manager.closeConnection();
		return (lines>0);	
	}
	
	//----------------------------------------------------------------------------------------
	
	public List<User> findUserList(){
		String sql="select * from user";
		ResultSet rs = manager.execQuery(sql);
		List<User>list=new ArrayList<User>();
		try {
			while(rs.next()){
				User user=new User(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13));			
				list.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	//--------------------------------------------------------------------------------------
	public boolean update(User user){
		int lines=0;
		if(user.getBirthday().equals(""))//用户没填生日
		{
			String sql="update user set username=?,"
					+ "password=?,"
					+ "email=?,"
					+ "location=?,"
					+ "signature=?,"
					+ "photo=?,"
					+ "gender=?,"
					+ "phone=?"
					+ "where userid=?";

			lines=manager.execUpdate(sql,				
					user.getUsername(),
					user.getPassword(),
					user.getEmail(),
					user.getLocation(),
					user.getSignature(),
					user.getPhoto(),
					user.getGender(),
					user.getPhone(),
					user.getUserid());
		}else
		{
			String sql="update user set username=?,password=?,email=?,location=?,signature=?,photo=?,gender=?,phone=?,birthday=? where userid=?";
			lines=manager.execUpdate(sql,
					//user.getThemeid(),
					user.getUsername(),
					user.getPassword(),
					user.getEmail(),
					user.getLocation(),
					user.getSignature(),
					user.getPhoto(),
					user.getGender(),
					user.getPhone(),
					user.getBirthday(),
					user.getUserid());
		}
		manager.closeConnection();
		return (lines>0);
	}
	//----------------------------------------------------------------------------------
	
	public boolean updateScore(User user, int score) {
		// TODO Auto-generated method stub
		String sql="update user set score=? where userid=?";
		int lines=manager.execUpdate(sql, user.getScore()+score,user.getUserid());
		user.setScore(user.getScore()+score);
		manager.closeConnection();
		return lines>0;
	}
	//-----------------------------------------------------------------------------------

	@Override
	public User findUser(int userid) {
		String sql="select * from user where userid=?";		
		ResultSet rs = manager.execQuery(sql,userid);
		try {
			 
			if(rs.next()){			
			User user=new User(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13));			
			return user;
			
			}else{						
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			manager.closeConnection();
		}
		return null;
	}

	@Override
	public int findUsername(String Username) {
		// TODO Auto-generated method stub
		String sql="select *from user where username=?";
		ResultSet rs=manager.execQuery(sql, Username);
		try {
			if(rs.next())
			{
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
