package com.etc.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.etc.dao.MessageDAO;
import com.etc.dbutil.DBManager;
import com.etc.entity.Message;
import com.etc.entity.Upload_content;
import com.etc.entity.User;

public class MessageDAOImpl implements MessageDAO {

	DBManager manager=new DBManager();
	
	@Override
	public boolean insertMessage(Message msg) {
		// TODO Auto-generated method stub
		String sql="insert into Message values(null,?,?,?,now())";
		int lines=manager.execUpdate(sql,msg.getUser1().getUserid(),msg.getUser2().getUserid(),msg.getMessage());
		return lines>0;
	}

	@Override
	public boolean deleteMessage(Message msg) {
		// TODO Auto-generated method stub
		String sql="delete from Message where msgid=?";
		int lines=manager.execUpdate(sql, msg.getMsgid());
		
		return lines>0;
	}

	@Override
	public List<Message> findMessageList(User user1, User user2) {
		// TODO Auto-generated method stub
		String sql="select * from Message where user1id=? and user2id=?";
		ResultSet rs=manager.execQuery(sql, user1.getUserid(),user2.getUserid());
		List<Message>list=new ArrayList<Message>();
		try {
			while(rs.next()){
				Message msg=new Message(rs.getInt(1),user1,user2,rs.getString(4),rs.getString(5));
				list.add(msg);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	

}
