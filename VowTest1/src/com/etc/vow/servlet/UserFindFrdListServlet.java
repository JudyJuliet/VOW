package com.etc.vow.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etc.entity.User;
import com.etc.service.UserFansService;
import com.etc.service.UserService;
import com.etc.service.impl.UserFansServiceImpl;
import com.etc.service.impl.UserServiceImpl;
import com.google.gson.Gson;

/**
 * Servlet implementation class UserFindFrdListServlet
 */
public class UserFindFrdListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserFindFrdListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
response.setContentType("text/html;charset=utf-8");	
		
		PrintWriter out = response.getWriter();	   	
	
		request.setCharacterEncoding("utf-8"); 
		UserFansService ufservice=new UserFansServiceImpl();
		UserService userservice=new UserServiceImpl();
		
		String userids=request.getParameter("userid");
		int userid=Integer.parseInt(userids);
		Gson gson=new Gson();
		User user =userservice.getUser(userid);
		System.out.println(gson.toJson(user));
		List<User> userfrdlist=ufservice.getAttendList(user);
		if(userfrdlist!=null)
		{
			String gsonstr=gson.toJson(userfrdlist);
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
