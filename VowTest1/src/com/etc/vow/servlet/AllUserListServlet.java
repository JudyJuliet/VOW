package com.etc.vow.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etc.entity.User;
import com.etc.service.UserService;
import com.etc.service.impl.UserServiceImpl;
import com.google.gson.Gson;


public class AllUserListServlet extends HttpServlet {
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
		
		UserService userservice=new UserServiceImpl();
		Gson gson=new Gson();
		List<User> allUserList=userservice.getUserList();
		
		if(allUserList!=null)
		{
			String gsonstr=gson.toJson(allUserList);
			out.println(gsonstr);
			System.out.println(gsonstr);
			out.flush();
			out.close();
		}else
		{
			out.println("notfind");
			System.out.println("notfind");
		}
	}
}
