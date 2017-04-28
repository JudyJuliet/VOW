package com.etc.dao;

import java.util.List;

import com.etc.entity.User;

public interface UserDAO {

	//根据用户名密码得出User
  User findUser(String username,String password);
  //插入User
  boolean insertUser(User user);
  //全部用户查询
  List<User> findUserList();
  //修改User
  boolean update(User user);
  //给User加分
  boolean updateScore(User user,int score);
  //根据id找user
  User findUser(int userid);
  //是否存在此用户名
  int findUsername(String Username);
}
