package com.etc.service;

import java.util.List;

import com.etc.entity.User;

public interface UserService {

	  User login(String username,String password);
	  User register(User user);
	  List<User> getUserList();//全部用户名查询
	  boolean update(User user);
	  boolean updateScore(User user,int score);
	  User getUser(int userid);
	  boolean checkUsername(User user);//检查用户名是否重复
}
