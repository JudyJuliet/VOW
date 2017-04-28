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
import com.etc.service.TagService;
import com.etc.service.impl.TagServiceImpl;
import com.google.gson.Gson;


public class AllTagListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("text/html;charset=utf-8");				
		 PrintWriter out = response.getWriter();	   		
		 request.setCharacterEncoding("utf-8"); 
		 TagService tagService=new TagServiceImpl();
		 
		 Gson gson=new Gson();
		 List<Tag>allTagList=new ArrayList<Tag>();
		 allTagList=tagService.getTagList();
		 String allTagListStr=gson.toJson(allTagList);
		 out.println(allTagListStr);
		 System.out.println(allTagListStr);
	}

}