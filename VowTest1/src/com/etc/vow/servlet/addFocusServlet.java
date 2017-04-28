package com.etc.vow.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etc.entity.User;
import com.etc.service.UserFansService;
import com.etc.service.impl.UserFansServiceImpl;
import com.google.gson.Gson;

/**
 * Servlet implementation class addFocusServlet
 */
public class addFocusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addFocusServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
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
		 String usergson=request.getParameter("user");
		 String oriusergson=request.getParameter("oriuser");
		 String isFocus=request.getParameter("isFocus");
		 Gson gson=new Gson();
		 User user=gson.fromJson(usergson, User.class);//当前用户
		 User oriuser=gson.fromJson(oriusergson, User.class);//即将要关注的用户
		 UserFansService ufs=new UserFansServiceImpl();
		 if(isFocus.equals("true"))//取关
		 {
			System.out.println( ufs.addUserFans(oriuser, user));
			System.out.println("成功/失败加关注");
		 }else//加关
		 {
			 System.out.println( ufs.cancelUserFans(oriuser, user));
			 System.out.println("成功/失败取关");
		 }
		 
	}

}
