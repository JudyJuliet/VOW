package com.etc.service;

import java.util.List;

import com.etc.entity.User;

public interface UserFansService {
	boolean addUserFans(User user,User fans);//关注某人
	boolean cancelUserFans(User user,User fans);//取消关注某人
	List<User> getAttendList(User user);//我关注的人列表
}
