package com.etc.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.etc.dao.UploadContentDAO;
import com.etc.dbutil.DBManager;
import com.etc.entity.Content_file;
import com.etc.entity.Tag;
import com.etc.entity.Upload_content;
import com.etc.entity.User;

public class UploadContentDAOImpl implements UploadContentDAO {

	DBManager manager=new DBManager();


	@Override
	public int insertUploadContent(Upload_content uc) {
		// TODO Auto-generated method stub
		String sql="insert into upload_content values(null,now(),?,?,0,0,0,0)";
		int lines=manager.execUpdate(sql,uc.getDescription(),uc.getOriginalUser().getUserid());
		
		String sql2="select * from upload_content";
		ResultSet rs=manager.execQuery(sql2);
		if(lines>0){
		try {
			int ucid =0;
			while(rs.next()){	
				 ucid = rs.getInt(1);
			}
			
			return ucid;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;		
		}
		return 0;
	}


	@Override
	public List<Upload_content> getOriginalContentList(User user) {
		// TODO Auto-generated method stub
		String sql="select * from upload_content where original_up_userid=? ";
		ResultSet rs=manager.execQuery(sql, user.getUserid());
		List<Upload_content>list=new ArrayList<Upload_content>();
		try {
			while(rs.next()){
				Upload_content uc=new Upload_content(rs.getInt(1),rs.getString(2),rs.getString(3),user,rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getInt(8));
				list.add(uc);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	

//---------------------------------------------------------------------------------------
	@Override
	public boolean downLoadContent(User user,Upload_content uc) {//下载
		// TODO Auto-generated method stub
		boolean flag=true;
		String sql="select * from upload_content,like_favorite_download where upload_content.ucid=like_favorite_download.ucid and upload_content.ucid=? and like_favorite_download.userid=?";
		ResultSet rs=manager.execQuery(sql,uc.getUcid(),user.getUserid());
		try {
			if(rs.next()){//在like_favorite_download表里有记录，则修改两个表里的记录
				
				if(rs.getInt(13)==0){//之前未下载---修改成已下载
				String sql1="update like_favorite_download set download=1 where ucid=? and userid=?";
				int line1=manager.execUpdate(sql1,uc.getUcid(),user.getUserid());
				
				String sql2="update upload_content set loadtimes=? where ucid=?";
				int line2=manager.execUpdate(sql2, uc.getLoadtimes()+1,uc.getUcid());
			//	uc.setLoadtimes(uc.getLoadtimes()+1);
				flag=(line1>0)&&(line2>0);
				}else if(rs.getInt(13)==1){//之前已下载---修改成未下载
					String sql1="update like_favorite_download set download=0 where ucid=? and userid=?";
					int line1=manager.execUpdate(sql1,uc.getUcid(),user.getUserid());
					
					String sql2="update upload_content set loadtimes=? where ucid=?";
					int line2=manager.execUpdate(sql2, uc.getLoadtimes()-1,uc.getUcid());
				//	uc.setLoadtimes(uc.getLoadtimes()-1);
					flag=(line1>0)&&(line2>0);
				}
			}else{
				//在like_favorite_download表里没有记录，新建记录，一定是set download=1
				String sql1="insert into like_favorite_download values(?,?,0,0,1)";
				int line1=manager.execUpdate(sql1,user.getUserid(),uc.getUcid());
				
				String sql2="update upload_content set loadtimes=? where ucid=?";
				int line2=manager.execUpdate(sql2, uc.getLoadtimes()+1,uc.getUcid());
				//uc.setLoadtimes(uc.getLoadtimes()+1);
				flag=(line1>0)&&(line2>0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		manager.closeConnection();
		return flag;
	}

//---------------------------------------------------------------------------------------------
	@Override
	public boolean likeContent(User user,Upload_content uc) {//like
		// TODO Auto-generated method stub
		boolean flag=true;
		String sql="select * from upload_content,like_favorite_download where upload_content.ucid=like_favorite_download.ucid and upload_content.ucid=? and like_favorite_download.userid=?";
		ResultSet rs=manager.execQuery(sql,uc.getUcid(),user.getUserid());
		try {
			if(rs.next()){//在like_favorite_download表里有记录，则修改两个表里的记录
				
				if(rs.getInt(11)==0){//之前未点赞---修改成点赞
					
					String sql2="update upload_content set liketimes=? where ucid=?";
					uc.setLiketimes(uc.getLiketimes()+1);
					int line2=manager.execUpdate(sql2, uc.getLiketimes(),uc.getUcid());
					
				String sql1="update like_favorite_download set uclike=1 where ucid=? and userid=?";
				int line1=manager.execUpdate(sql1,uc.getUcid(),user.getUserid());
				
//				String sql2="update upload_content set liketimes=? where ucid=?";
//				uc.setLiketimes(uc.getLiketimes()+1);
//				int line2=manager.execUpdate(sql2, uc.getLiketimes(),uc.getUcid());
				
				flag=(line1>0)&&(line2>0);
				}else if(rs.getInt(11)==1){//之前已点赞---修改成未点赞
				
					uc.setLiketimes(uc.getLiketimes()-1);
					String sql2="update upload_content set liketimes=? where ucid=?";
					int line2=manager.execUpdate(sql2, uc.getLiketimes(),uc.getUcid());
					
					String sql1="update like_favorite_download set uclike=0 where ucid=? and userid=?";
					int line1=manager.execUpdate(sql1,uc.getUcid(),user.getUserid());
					
//					uc.setLiketimes(uc.getLiketimes()-1);
//					String sql2="update upload_content set liketimes=? where ucid=?";
//					int line2=manager.execUpdate(sql2, uc.getLiketimes(),uc.getUcid());
				
					flag=(line1>0)&&(line2>0);
				}
			}else{
				//在like_favorite_download表里没有记录，新建记录，一定是set ...=1
				
				String sql2="update upload_content set liketimes=? where ucid=?";
				uc.setLiketimes(uc.getLiketimes()+1);
				int line2=manager.execUpdate(sql2, uc.getLiketimes(),uc.getUcid());
				
				String sql1="insert into like_favorite_download values(?,?,1,0,0)";
				int line1=manager.execUpdate(sql1,user.getUserid(),uc.getUcid());
				
//				String sql2="update upload_content set liketimes=? where ucid=?";
//				uc.setLiketimes(uc.getLiketimes()+1);
//				int line2=manager.execUpdate(sql2, uc.getLiketimes(),uc.getUcid());
				
				flag=(line1>0)&&(line2>0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		manager.closeConnection();
		return flag;
	}

//------------------------------------------------------------------------------------------
	@Override
	public boolean favoriteContent(User user,Upload_content uc) {//favorite
		// TODO Auto-generated method stub
		boolean flag=true;
		String sql="select * from upload_content,like_favorite_download where upload_content.ucid=like_favorite_download.ucid and upload_content.ucid=? and like_favorite_download.userid=?";
		ResultSet rs=manager.execQuery(sql,uc.getUcid(),user.getUserid());
		try {
			if(rs.next()){//在like_favorite_download表里有记录，则修改两个表里的记录
				
				if(rs.getInt(12)==0){//之前未收藏---修改成收藏
				String sql1="update like_favorite_download set favorite=1 where ucid=? and userid=?";
				int line1=manager.execUpdate(sql1,uc.getUcid(),user.getUserid());
				
				String sql2="update upload_content set favoritetimes=? where ucid=?";
				uc.setFavoritetimes(uc.getFavoritetimes()+1);
				int line2=manager.execUpdate(sql2, uc.getFavoritetimes(),uc.getUcid());
				
				flag=(line1>0)&&(line2>0);
				}else if(rs.getInt(12)==1){//之前已收藏---修改成未收藏
					String sql1="update like_favorite_download set favorite=0 where ucid=? and userid=?";
					int line1=manager.execUpdate(sql1,uc.getUcid(),user.getUserid());
					
					uc.setFavoritetimes(uc.getFavoritetimes()-1);
					String sql2="update upload_content set favoritetimes=? where ucid=?";
					int line2=manager.execUpdate(sql2, uc.getFavoritetimes(),uc.getUcid());
				
					flag=(line1>0)&&(line2>0);
				}
			}else{
				//在like_favorite_download表里没有记录，新建记录，一定是set download=1
				String sql1="insert into like_favorite_download values(?,?,0,1,0)";
				int line1=manager.execUpdate(sql1,user.getUserid(),uc.getUcid());
				
				String sql2="update upload_content set favoritetimes=? where ucid=?";
				uc.setFavoritetimes(uc.getFavoritetimes()+1);
				int line2=manager.execUpdate(sql2, uc.getFavoritetimes(),uc.getUcid());
			
				flag=(line1>0)&&(line2>0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		manager.closeConnection();
		return flag;
		
	}

///////////////////////////////////////////////////////////////////////////////////
	@Override
	public List<Upload_content> getPostContentList(User user) {
		// TODO Auto-generated method stub
		String sql="select * from upload_content,repost_content where original_up_userid=? and upload_content.ucid=repost_content.ucid";
		ResultSet rs=manager.execQuery(sql, user.getUserid());
		List<Upload_content>list=new ArrayList<Upload_content>();
		try {
			while(rs.next()){
				Upload_content uc=new Upload_content(rs.getInt(1),rs.getString(2),rs.getString(3),user,rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getInt(8));
				list.add(uc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean setDeleteContent(Upload_content uc) {
		// TODO Auto-generated method stub
		String sql="update upload_content set if_deleted=1 where ucid=?";
		int lines=manager.execUpdate(sql, uc.getUcid());
		return lines>0;
	}
////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public List<Upload_content> getFavoriteContentList(User user) {//查看我的收藏
		// TODO Auto-generated method stub
		String sql="select * from upload_content,like_favorite_download where userid=? and upload_content.ucid=like_favorite_download.ucid and favorite=1";
		ResultSet rs=manager.execQuery(sql, user.getUserid());
		List<Upload_content>list=new ArrayList<Upload_content>();
		try {
			while(rs.next()){
				User oriuser=new UserDAOImpl().findUser(rs.getInt(4));
				Upload_content uc=new Upload_content(rs.getInt(1),rs.getString(2),rs.getString(3),oriuser,rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getInt(8));
				list.add(uc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public List<Upload_content> getDownloadContentList(User user) {//查看我的下载
		// TODO Auto-generated method stub
		String sql="select * from upload_content,like_favorite_download where userid=? and upload_content.ucid=like_favorite_download.ucid and download=1";
		ResultSet rs=manager.execQuery(sql, user.getUserid());
		List<Upload_content>list=new ArrayList<Upload_content>();
		try {
			while(rs.next()){
				User oriuser=new UserDAOImpl().findUser(rs.getInt(4));
				Upload_content uc=new Upload_content(rs.getInt(1),rs.getString(2),rs.getString(3),oriuser,rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getInt(8));
				list.add(uc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public List<Upload_content> getFredContentList(User user) {
		// TODO Auto-generated method stub
		String sql="select * from upload_content,user_fans where user_id=? and upload_content.original_up_userid=user_fans.fans_id";
		ResultSet rs=manager.execQuery(sql, user.getUserid());
		List<Upload_content>list=new ArrayList<Upload_content>();
		try {
			while(rs.next()){
				User oriuser=new UserDAOImpl().findUser(rs.getInt(4));
				Upload_content uc=new Upload_content(rs.getInt(1),rs.getString(2),rs.getString(3),oriuser,rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getInt(8));
				list.add(uc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public List<Content_file> findContentFile(Upload_content uc) {
		// TODO Auto-generated method stub
		String sql="SELECT content_file.fileid,content_file.ucid,content_file.filename "
					+"FROM upload_content,content_file "
					+"WHERE upload_content.ucid=? "
					+"and upload_content.ucid=content_file.ucid ";

		ResultSet rs=manager.execQuery(sql, uc.getUcid());
		List<Content_file>list=new ArrayList<Content_file>();
		try {
			while(rs.next()){		
				Content_file file=new Content_file(rs.getInt(1),rs.getString(3),uc);
				list.add(file);		
}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	
	@Override
	public List<Tag> findTagOfContentList(Upload_content uc) {
		// TODO Auto-generated method stub
		String sql="select * from content_tag ,tag where ucid=? and content_tag.tagid=tag.tagid";
		ResultSet rs=manager.execQuery(sql, uc.getUcid());
		List<Tag>list=new ArrayList<Tag>();
		try {
			while(rs.next()){
				Tag tag=new Tag(rs.getInt(2),rs.getString(4));
				list.add(tag);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public List<Upload_content> getTagContentList(String tagname) {
		// TODO Auto-generated method stub
		String sql="SELECT * FROM upload_content,content_tag,tag "+
					" WHERE upload_content.ucid=content_tag.ucid "+
					" and content_tag.tagid=tag.tagid "+
					" and tagname=? "	;
		ResultSet rs=manager.execQuery(sql, tagname);
		List<Upload_content>list=new ArrayList<Upload_content>();
		try {
			while(rs.next())
			{
				User oriuser=new UserDAOImpl().findUser(rs.getInt(4));
				Upload_content uc=new Upload_content(rs.getInt(1),rs.getString(2),rs.getString(3),oriuser,rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getInt(8));
				list.add(uc);				
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public List<Upload_content> getKeyContentList(String keyword) {
		// TODO Auto-generated method stub
		String sql="SELECT * FROM upload_content WHERE description LIKE ?";
		ResultSet rs=manager.execQuery(sql, "%"+keyword+"%");
		List<Upload_content>list=new ArrayList<Upload_content>();
				try {
					while(rs.next())
					{
						User oriuser=new UserDAOImpl().findUser(rs.getInt(4));
						Upload_content uc=new Upload_content(rs.getInt(1),rs.getString(2),rs.getString(3),oriuser,rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getInt(8));
						list.add(uc);				
					}
					return list;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
		return null;
	}


	@Override
	public List<Upload_content> getHotContentList() {
		// TODO Auto-generated method stub
		String sql="SELECT * FROM upload_content ORDER BY liketimes DESC";
		ResultSet rs=manager.execQuery(sql);
		List<Upload_content>list=new ArrayList<Upload_content>();
		try {
			for(int i=0;(rs.next())&&i<10;i++)
			{
				User oriuser=new UserDAOImpl().findUser(rs.getInt(4));
				Upload_content uc=new Upload_content(rs.getInt(1),rs.getString(2),rs.getString(3),oriuser,rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getInt(8));
				list.add(uc);	
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
	