package com.etc.vow.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etc.entity.Comments;
import com.etc.service.CommentsService;
import com.etc.service.impl.CommentsServiceImpl;
import com.google.gson.Gson;

/**
 * Servlet implementation class PublishCommentServlet
 */
public class PublishCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PublishCommentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("text/html;charset=utf-8");				
		 PrintWriter out = response.getWriter();	   		
		 request.setCharacterEncoding("utf-8"); 
		 CommentsService commentService=new CommentsServiceImpl();
		 
		 String commentGson=request.getParameter("ServletRequest");
		 Gson gson=new Gson();
		 Comments comment=gson.fromJson(commentGson, Comments.class);//user
		 boolean succeed= commentService.addComments(comment);
		 System.out.println(succeed);
		 //修改allcontent表的评论次数属性
		 if(succeed){
		 out.println("succeed");
		 }else
		 {
			 out.println("fail");
		 }
		
	}

}
