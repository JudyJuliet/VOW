package com.etc.service.impl;

import java.util.List;

import com.etc.dao.UserDAO;
import com.etc.dao.impl.UserDAOImpl;
import com.etc.entity.User;
import com.etc.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDAO userDAO=new UserDAOImpl();
	
	@Override
	public User login(String username, String password) {
		// TODO Auto-generated method stub
		User user=userDAO.findUser(username, password);		
		if(user!=null){
			if(userDAO.updateScore(user, 1)){
				//user.setScore(user.getScore()+1);//因为要返回对象，所以更新数据库同时还要更新user
				return user;
			}
		}else
		{
		return null;
		}
		return null;
	}

	@Override
	public User register(User user) {
		// TODO Auto-generated method stub
        if(userDAO.insertUser(user)){//先插入新用户
			
			//注册成功后直接登录
			user=userDAO.findUser(user.getUsername(), user.getPassword());
			return user;
		}else{
		    return null;
		    }	
	}

	@Override
	public List<User> getUserList() {
		// TODO Auto-generated method stub
		List<User> list=userDAO.findUserList();
		return list;
	}

	@Override
	public boolean update(User user) {
		// TODO Auto-generated method stub
		return userDAO.update(user);
	}

	@Override
	public boolean updateScore(User user, int score) {
		// TODO Auto-generated method stub
		return userDAO.updateScore(user, score);
	}

	@Override
	public User getUser(int userid) {
		// TODO Auto-generated method stub
		return userDAO.findUser(userid);
	}

	@Override
	public boolean checkUsername(User user) {
		// TODO Auto-generated method stub
		int userid=userDAO.findUsername(user.getUsername());
		if(userid==0)return true;//检查通过,没有重复的用户名
		else if(user.getUserid()!=0)//表示这个用户是已注册用户.
		{
			if(userid==user.getUserid())
			{
				return true;//表示这个查出来的用户就是要更改个人信息的用户
			}else
			{
				return false;//表示该用户更改用户名后与现有用户重名
			}
		}else
		{
			return false;//表示这个正在注册的用户用户名已存在.
		}
	}

}
