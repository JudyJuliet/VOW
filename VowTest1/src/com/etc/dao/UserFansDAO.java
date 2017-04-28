package com.etc.dao;

import java.util.List;
import com.etc.entity.User;

public interface UserFansDAO{

		boolean insertUserFans(User user,User fans);//关注某人
		boolean deleteUserFans(User user,User fans);//取消关注某人
		List<User> findAttendList(User user);//我关注的人列表
		         
}
