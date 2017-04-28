package com.etc.vow.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etc.entity.User;
import com.etc.service.UserService;
import com.etc.service.impl.UserServiceImpl;
import com.google.gson.Gson;

/**
 * Servlet implementation class UserLoginServlet
 * 用户登陆
 * author：lareinaluo
 */
public class UserLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("text/html;charset=utf-8");	
		
		PrintWriter out = response.getWriter();	   	
	
		request.setCharacterEncoding("utf-8"); 

		String username =request.getParameter("username");
		String password = request.getParameter("password");
		System.out.print(username);
		UserService userService = new UserServiceImpl();

		User user = userService.login(username, password);
		if(user!=null)
		{
			Gson gson = new Gson();
			String userJson = gson.toJson(user);
			System.out.println(userJson);
			out.println(userJson);
		}else
		{
			out.println("notfind");
		}
		
		System.out.println(username);
		System.out.println(password);
		
		
		
		
	}

}
