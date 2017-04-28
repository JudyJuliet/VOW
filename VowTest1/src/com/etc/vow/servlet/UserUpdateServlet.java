package com.etc.vow.servlet;

import java.io.File;
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
import com.jspsmart.upload.SmartFile;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

/**
 * Servlet implementation class UserUpdateServlet
 */
public class UserUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private final int UPDATEUSERINFO=0;
	private final int MODIFYPASSWORD=1;
	private final int FILE=1;
	private final int ENTITYINFO=2;
	private final int FILEANDINFO=3;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        response.setContentType("text/html;charset=utf-8");	
		
		PrintWriter out = response.getWriter();	   	
	
		request.setCharacterEncoding("utf-8"); 
		UserService userservice=new UserServiceImpl();
		
		Gson gson = new Gson();
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

		String flags=smartUpload.getRequest().getParameter("flag");
		String kinds=smartUpload.getRequest().getParameter("kind");
		int kind=Integer.parseInt(kinds);
		
//		String gson_user=request.getParameter("ServletRequest");
//		String flags=request.getParameter("flag");
		int flag=Integer.parseInt(flags);
		if(flag==UPDATEUSERINFO){
			if(kind==FILEANDINFO){//如果有文件,就把文件也取出来
				//提取上传文件
				SmartFile smartfile=smartUpload.getFiles().getFile(0);
				//准备上传文件的存储路径和文件名
				//保证文件名唯一
				String serverfilePath=request.getRealPath("image/photo");
				File file=new File(serverfilePath);
				if(!file.exists())
				{
					file.mkdirs();
				}
				String filename=smartfile.getFileName();
				try {
					smartfile.saveAs(serverfilePath+"/"+filename);
				} catch (SmartUploadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("文件保存至服务器时出现错误");
				}
				
				user.setPhoto(filename);
				
			}
			boolean check=userservice.checkUsername(user);
			if(check){
				boolean succeed=userservice.update(user);		
				if(succeed){
					out.println(gson.toJson(user, User.class));
					System.out.println("succeed");
					System.out.println(gson.toJson(user, User.class));
		
				}else{
					out.println("fail");
					System.out.println("fail");
				}
			}else
			{
				out.println("already exist");
			}
		}else//MODIFYPASSWORD
		{
			
			boolean succeed=userservice.update(user);		
			if(succeed){
				out.println("更新成功");
				System.out.println("succeed");
	
			}else{
				out.println("更新失败");
				System.out.println("fail");
			}
		}
			
			
	}

	
}
