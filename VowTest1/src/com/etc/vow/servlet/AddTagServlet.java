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
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


public class AddTagServlet extends HttpServlet {
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
		 String allTagNames=request.getParameter("mNames");
		 
		 Type type=new TypeToken<ArrayList<String>>(){}.getType();
		 List<String>allTagNamesList=gson.fromJson(allTagNames, type);//全部标签名称列表
		System.out.println(allTagNames);
		 
		 List<Tag>oldTagList=new ArrayList<Tag>();
		 oldTagList=tagService.getTagList();
		 List<String>oldTagNamesList=new ArrayList<String>();
		 for(Tag tag:oldTagList){//已有标签的名称列表
			 oldTagNamesList.add(tag.getTagname());
		 }
		 
		 for(String tagname:allTagNamesList){
			if(!oldTagNamesList.contains(tagname)){
				Tag newTag=new Tag();
				newTag.setTagname(tagname);
				if(tagService.addTag(newTag))
				{
					//插入新标签
					System.out.println("插入标签成功");
				}
				
			}
		 }
	}

}

