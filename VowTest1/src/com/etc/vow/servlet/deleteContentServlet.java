package com.etc.vow.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etc.entity.Comments;
import com.etc.entity.Upload_content;
import com.etc.service.CommentsService;
import com.etc.service.UploadContentService;
import com.etc.service.impl.CommentsServiceImpl;
import com.etc.service.impl.UploadContentServiceImpl;
import com.google.gson.Gson;


public class deleteContentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 response.setContentType("text/html;charset=utf-8");				
		 PrintWriter out = response.getWriter();	   		
		 request.setCharacterEncoding("utf-8"); 
		 UploadContentService ucService=new UploadContentServiceImpl();
		 
		 String ucgson=request.getParameter("ucgson");
		 Gson gson=new Gson();
		 Upload_content uc=gson.fromJson(ucgson, Upload_content.class);
		 boolean succeed= ucService.deleteUploadContent(uc);
		 out.print(succeed);
		 System.out.print(succeed);
	}

}
