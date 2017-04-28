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
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
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
			UserService userservice=new UserServiceImpl();
			
			Gson gson = new Gson();	
//			String gson_user=request.getParameter("ServletRequest");
//			System.out.print(gson_user);
//			User user = gson.fromJson(gson_user, User.class);	
//			
			//创建上传组件
			SmartUpload smartUpload=new SmartUpload("utf-8");
			//初始化上传组件
			smartUpload.initialize(getServletConfig(), request, response);
			//上传文件到服务器的临时内存中--用upload()方法
			try {
				smartUpload.upload();
			} catch (SmartUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String gson_user=smartUpload.getRequest().getParameter("ServletRequest");
			User  user = gson.fromJson(gson_user, User.class);	

			boolean checkUsername=userservice.checkUsername(user);
			//检查用户名是否重复
		        if(checkUsername){
		        	//用户名无重复	        
				user=userservice.register(user);//注册成功后直接登录				
				String gsonRegistedUser=gson.toJson(user);//注册成功后的user串			
				out.println(gsonRegistedUser);
				System.out.println(gsonRegistedUser);
				}else
				{
					out.println("false");//重复
					System.out.println("false");//重复
				}
					
		}
}
