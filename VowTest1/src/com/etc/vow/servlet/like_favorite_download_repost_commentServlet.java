package com.etc.vow.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etc.entity.Comments;
import com.etc.entity.Repost_content;
import com.etc.entity.Upload_content;
import com.etc.entity.User;
import com.etc.service.CommentsService;
import com.etc.service.LikeFavoriteDownloadService;
import com.etc.service.RepostContentService;
import com.etc.service.UploadContentService;
import com.etc.service.impl.CommentsServiceImpl;
import com.etc.service.impl.LikeFavoriteDownloadServiceImpl;
import com.etc.service.impl.RepostContentServiceImpl;
import com.etc.service.impl.UploadContentServiceImpl;
import com.etc.util.StringUtil;
import com.google.gson.Gson;


public class like_favorite_download_repost_commentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final int LIKE=1;
	private final int FAVORITE=2;
	private final int DOWNLOAD=3;
	private final int REPOST=4;
	private final int COMMENT=5;
	private final int LIKEORFAVOR=6	;
	 LikeFavoriteDownloadService lfdService=new LikeFavoriteDownloadServiceImpl();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("text/html;charset=utf-8");				
		 PrintWriter out = response.getWriter();	   		
		 request.setCharacterEncoding("utf-8"); 
		 UploadContentService ucService=new UploadContentServiceImpl();
		 CommentsService cs=new CommentsServiceImpl();
		 RepostContentService rcs=new RepostContentServiceImpl() ;
		 String userstr=request.getParameter("user");
		 String contentstr=request.getParameter("content");
		 String operationIndexStr=request.getParameter("operationIndex");
		 
		
		 
		 Gson gson=new Gson();
		 User user=gson.fromJson(userstr, User.class);//user
		 Upload_content content=gson.fromJson(contentstr, Upload_content.class);//content
		 int operationIndex=Integer.parseInt(operationIndexStr);//operationIndex---like_favorite_download_repost_comment
	
		 switch(operationIndex){
		 case LIKE:
			 boolean succeed=ucService.likeContent(user, content);
			 out.println(gson.toJson(content));
			 System.out.println("like...Servlet:liketimes="+content.getLiketimes());
			 break;
		 case FAVORITE://收藏
			 boolean succeed1=ucService.favoriteContent(user,content);
			 out.println(gson.toJson(content));
			 System.out.println("favorite...Servlet");
			 break;
		 case DOWNLOAD:
			 boolean succeed2=ucService.downLoadContent(user,content);
			 out.println(gson.toJson(content));
			 System.out.println("download...Servlet");
			 break;
		
		 case COMMENT:
			 List<Comments>CommentsList=new ArrayList<Comments>();
			 CommentsList=cs.getComments(content);
			 String CommentsListStr=gson.toJson(CommentsList);
			 out.println(CommentsListStr);
			 System.out.println("Comment...Servlet评论的操作");
			 break;
			 
		 

		 }
	
	}

}
