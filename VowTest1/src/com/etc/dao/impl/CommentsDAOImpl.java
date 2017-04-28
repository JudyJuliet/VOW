package com.etc.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.etc.dao.CommentsDAO;
import com.etc.dbutil.DBManager;
import com.etc.entity.Comments;
import com.etc.entity.Upload_content;
import com.etc.entity.User;

public class CommentsDAOImpl implements CommentsDAO {

	DBManager manager=new DBManager();
	
	@Override
	public boolean insertComments(Comments comment) {
		// TODO Auto-generated method stub
		String sql="insert into comments values(null,?,?,?)";
		int lines=manager.execUpdate(sql, comment.getUser().getUserid(),comment.getUc().getUcid(),comment.getComdetail());
		return lines>0;
	}

	@Override
	public List<Comments> findComments(Upload_content uc) {
		// TODO Auto-generated method stub
		String sql="select commentid,comments.comdetail,`user`.username,`user`.userid,`user`.photo"
				+ " from comments ,user,upload_content where comments.ucid=? "+
				" and comments.userid=user.userid and comments.ucid=upload_content.ucid";
		ResultSet rs=manager.execQuery(sql,uc.getUcid());
		List<Comments> list=new ArrayList<Comments>();
		try {
			while(rs.next()){
				User user=new User();
				user.setUserid(rs.getInt(4));
				user.setUsername(rs.getString(3));//这里user值存储了id和名称
				user.setPhoto(rs.getString(5));
				Comments comment=new Comments(rs.getInt(1),user,uc,rs.getString(2));
				list.add(comment);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean deleteComments(Upload_content uc) {
		// TODO Auto-generated method stub
		String sql="delete from comments where ucid=?";
		int lines=manager.execUpdate(sql, uc.getUcid());
		return lines>0;
	}

}
