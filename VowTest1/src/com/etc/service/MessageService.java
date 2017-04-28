package com.etc.service;

import java.util.List;

import com.etc.entity.Message;
import com.etc.entity.User;

public interface MessageService {

	public boolean addMessage(Message msg);
	public boolean clearMessage(Message msg);
	public List<Message> getMessageList(User user1,User user2);
}
