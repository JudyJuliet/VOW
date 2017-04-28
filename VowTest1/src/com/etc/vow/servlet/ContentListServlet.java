package com.etc.vow.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etc.entity.AllOfContent;
import com.etc.entity.Comments;
import com.etc.entity.Content_file;
import com.etc.entity.Tag;
import com.etc.entity.Upload_content;
import com.etc.entity.User;
import com.etc.service.CommentsService;
import com.etc.service.LikeFavoriteDownloadService;
import com.etc.service.UploadContentService;
import com.etc.service.UserService;
import com.etc.service.impl.CommentsServiceImpl;
import com.etc.service.impl.LikeFavoriteDownloadServiceImpl;
import com.etc.service.impl.UploadContentServiceImpl;
import com.etc.service.impl.UserServiceImpl;
import com.google.gson.Gson;

public class ContentListServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 final int OPERATION_TYPE_ORIGINAL_CONTENT=0;//查找原创内容夹操作
		 final int OPERATION_TYPE_POST_CONTENT=1;//查找转发内容夹操作
		 final int OPERATION_TYPE_FAVORITE_CONTENT=2;//查找收藏内容夹操作
		 final int OPERATION_TYPE_DOWNLOAD_CONTENT=3;//查找下载内容夹操作
		 final int OPERATION_TYPE_FRED_CONTENT=4;//查找关注的人的内容夹操作
		  final int OPERATION_TYPE_TAG_CONTENT=5;//按标签查找内容
		final int OPERATION_TYPE_KEY_CONTENT=6;//按关键词描述查找内容
		
		response.setContentType("text/html;charset=utf-8");				
		 PrintWriter out = response.getWriter();	   		
		 request.setCharacterEncoding("utf-8"); 
		 
		 UserService userservice=new UserServiceImpl();
		 UploadContentService ucService=new UploadContentServiceImpl();
		 CommentsService cs=new CommentsServiceImpl();
		 LikeFavoriteDownloadService lfdService=new LikeFavoriteDownloadServiceImpl();
		 
		 String userstr=request.getParameter("user");
		// System.out.println(userstr);
		 Gson gson=new Gson();
		 User user=gson.fromJson(userstr, User.class);//user		 
		 user=userservice.getUser(user.getUserid());
		 String operation_type_str=request.getParameter("operationType");
		 String searchstr="";
		// System.out.println(operation_type_str);
		 int operation_type=Integer.parseInt(operation_type_str);//操作类型
		 if(operation_type==OPERATION_TYPE_TAG_CONTENT
				 ||operation_type==OPERATION_TYPE_KEY_CONTENT)
		 {
			 searchstr=request.getParameter("searchstr");
		 }
		 List<Upload_content>contentList=new ArrayList<Upload_content>();
		 List<AllOfContent>allofcontentlist=new ArrayList<AllOfContent>();
		 
		
		 
		 switch(operation_type){
		 case OPERATION_TYPE_ORIGINAL_CONTENT://查找原创内容夹操作
			  contentList =ucService.getOriginalContentList(user);
			  break;
		 case OPERATION_TYPE_POST_CONTENT://查找转发内容夹操作
			  contentList=ucService.getPostContentList(user);
			  break;
		 case OPERATION_TYPE_FAVORITE_CONTENT://查找收藏内容夹操作
			 contentList=ucService.getFavoriteContentList(user);
			 break;
		 case OPERATION_TYPE_DOWNLOAD_CONTENT://查找下载内容夹操作
			 contentList=ucService.getDownloadContentList(user);
			 break;
		 case OPERATION_TYPE_FRED_CONTENT://查找关注的人的内容夹操作
			 contentList=ucService.getFredContentList(user);
			 break;
		 case OPERATION_TYPE_TAG_CONTENT://根据标签查找内容夹
			 contentList=ucService.getTagContentList(searchstr);
			 break;
		 case OPERATION_TYPE_KEY_CONTENT://根据关键词查找内容
			 contentList=ucService.getKeyContentList(searchstr);
			 break;
		 case 10://查找热门内容
			 contentList=ucService.getHotContentList();
			 break;
			 
		 }
		
		// System.out.println(gson.toJson(contentList));
		 for(Upload_content uc:contentList)
		 {
			 AllOfContent aoc=new AllOfContent();
			 //内容本身
			 aoc.setContent(uc);
			 List<Tag>taglist=new ArrayList<Tag>();
			 //内容标签列表
			 taglist=ucService.getTagOfContentList(uc);			
			 aoc.setTaglist(taglist);
			 //文件名列表
			 List<Content_file>fileList=new ArrayList<Content_file>();
			 fileList=ucService.getContentFile(uc);
			 List<String>filenamelist=new ArrayList<String>();
			 for(Content_file file:fileList)
			 {
				 filenamelist.add(file.getFilename());
			 }
			 aoc.setFilelist(filenamelist);
			 //评论次数
			 List<Comments>CommentsList=new ArrayList<Comments>();
			 CommentsList=cs.getComments(uc);
			 aoc.setComments_number(CommentsList.size());
			 //是否点赞
			 boolean checkIfLike=lfdService.checkIfLike(user, uc);
			 aoc.setLike(checkIfLike);
			 //是否收藏
			 boolean checkIfFavorite=lfdService.checkIfFavorite(user, uc);
			 aoc.setFavor(checkIfFavorite);
			 allofcontentlist.add(aoc);
		 }
		
		 
		 String result=gson.toJson(allofcontentlist);
		 
		 if(result!=null)
		   {
				out.println(result);
				System.out.println(result);
				out.flush();
				out.close();
			}else
			{
				out.println("notfind");
				System.out.println("notfind");
			}		 
	}

}
