package com.etc.dao;

import java.util.List;

import com.etc.entity.Message;
import com.etc.entity.User;

public interface MessageDAO {

	public boolean insertMessage(Message msg);
	public boolean deleteMessage(Message msg);
	public List<Message> findMessageList(User user1,User user2);
}
