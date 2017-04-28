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
import com.etc.entity.Content_file;
import com.etc.entity.Upload_content;
import com.etc.service.CommentsService;
import com.etc.service.impl.CommentsServiceImpl;
import com.google.gson.Gson;

/**
 * Servlet implementation class ContentConmmentListServlet
 */
public class ContentCommentListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		 response.setContentType("text/html;charset=utf-8");				
		 PrintWriter out = response.getWriter();	   		
		 request.setCharacterEncoding("utf-8"); 
		 
		 CommentsService cs=new CommentsServiceImpl();
		 String contentid=request.getParameter("ucid");
		 System.out.println(contentid);
		 int ucid=Integer.parseInt(contentid);
		 Upload_content uc=new Upload_content();
		 uc.setUcid(ucid);
		 Gson gson=new Gson();		 
		 List<Comments>CommentsList=new ArrayList<Comments>();
		 CommentsList=cs.getComments(uc);
		 String CommentsListStr=gson.toJson(CommentsList);
		 out.println(CommentsListStr);
		System.out.println(CommentsListStr);
	}
}