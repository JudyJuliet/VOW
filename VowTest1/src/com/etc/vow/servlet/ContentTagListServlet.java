package com.etc.vow.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etc.entity.Tag;
import com.etc.entity.Upload_content;
import com.etc.service.UploadContentService;
import com.etc.service.impl.UploadContentServiceImpl;
import com.google.gson.Gson;


public class ContentTagListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		 response.setContentType("text/html;charset=utf-8");				
		 PrintWriter out = response.getWriter();	   		
		 request.setCharacterEncoding("utf-8"); 
		 UploadContentService ucService=new UploadContentServiceImpl();
		 
		 String contentstr=request.getParameter("content");
		 Gson gson=new Gson();
		 Upload_content uc=gson.fromJson(contentstr,Upload_content.class);//user
		 
		 List<Tag>tagList=new ArrayList<Tag>();
		 tagList=ucService.getTagOfContentList(uc);
		 String tagListStr=gson.toJson(tagList);
		 out.println(tagListStr);
		System.out.println("TagServlet:"+tagListStr);
	}

}
