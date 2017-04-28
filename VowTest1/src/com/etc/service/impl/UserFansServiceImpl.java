package com.etc.service.impl;

import java.util.List;

import com.etc.dao.UserFansDAO;
import com.etc.dao.impl.UserFansDAOImpl;
import com.etc.entity.User;
import com.etc.service.UserFansService;

public class UserFansServiceImpl implements UserFansService {

	UserFansDAO userfans=new UserFansDAOImpl();
	
	@Override
	public boolean addUserFans(User user, User fans) {
		// TODO Auto-generated method stub
		return userfans.insertUserFans(user, fans);
	}

	@Override
	public boolean cancelUserFans(User user, User fans) {
		// TODO Auto-generated method stub
		return userfans.deleteUserFans(user, fans);
	}

	
	@Override
	public List<User> getAttendList(User user) {
		// TODO Auto-generated method stub
		return userfans.findAttendList(user);
	}

	
}
