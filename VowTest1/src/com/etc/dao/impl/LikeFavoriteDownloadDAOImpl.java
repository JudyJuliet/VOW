package com.etc.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.etc.dao.LikeFavoriteDownloadDAO;
import com.etc.dbutil.DBManager;
import com.etc.entity.Like_favorite_download;
import com.etc.entity.Upload_content;
import com.etc.entity.User;

public class LikeFavoriteDownloadDAOImpl implements LikeFavoriteDownloadDAO {

DBManager manager=new DBManager();
	
	@Override
	public Boolean insertLikeFavoriteDownload(Like_favorite_download lfd) {
		// TODO Auto-generated method stub
		String sql="insert into like_favorite_download values(?,?,?,?,?)";
		int lines=manager.execUpdate(sql, lfd.getUser().getUserid(),lfd.getUc().getUcid(),lfd.getUclike(),lfd.getFavorite(),lfd.getDownload());
		return lines>0;
	}

	@Override
	public List<Upload_content> getFavoriteList(User user) {
		// TODO Auto-generated method stub
		String sql="select*from like_favorite_download,upload_content where userid=? and like_favorite_download.ucid=upload_content.ucid";
		ResultSet rs=manager.execQuery(sql, user.getUserid());
		List<Upload_content>list=new ArrayList<Upload_content>();
		try {
			while(rs.next()){
				Upload_content uc=new Upload_content(rs.getInt(2),rs.getString(7),rs.getString(8),user,rs.getInt(10),rs.getInt(11),rs.getInt(12),rs.getInt(13));
				list.add(uc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public Boolean deleteLikeFavoriteDownload(Upload_content uc) {
		// TODO Auto-generated method stub
		String sql="delete from like_favorite_download where ucid=?";
		int lines=manager.execUpdate(sql, uc.getUcid());
		return lines>0;
	}

	@Override
	public Boolean checkIfLike(User user, Upload_content uc) {
		// TODO Auto-generated method stub
		String sql="select * from upload_content,like_favorite_download where upload_content.ucid=like_favorite_download.ucid and upload_content.ucid=? and like_favorite_download.userid=?";
		ResultSet rs=manager.execQuery(sql,uc.getUcid(),user.getUserid());
		try {
			if(rs.next()){//在like_favorite_download表里有记录
				
				if(rs.getInt(11)==0){//之前未点赞
				return false;
				}else if(rs.getInt(11)==1){//之前已点赞
					return true;
				}
			}else{
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
		manager.closeConnection();
		return false;	
	
		
	}

	@Override
	public Boolean checkIfFavorite(User user, Upload_content uc) {
		// TODO Auto-generated method stub
		String sql="select * from upload_content,like_favorite_download where upload_content.ucid=like_favorite_download.ucid and upload_content.ucid=? and like_favorite_download.userid=?";
		ResultSet rs=manager.execQuery(sql,uc.getUcid(),user.getUserid());
		try {
			if(rs.next()){//在like_favorite_download表里有记录
				
				if(rs.getInt(12)==0){//之前未收藏
				return false;
				}else if(rs.getInt(12)==1){//之前已收藏---修改成未收藏
					return true;
				}
			}else{
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
					
		manager.closeConnection();
		return false;	
	}

}
