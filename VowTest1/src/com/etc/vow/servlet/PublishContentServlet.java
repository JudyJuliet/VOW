package com.etc.vow.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etc.entity.AllOfContent;
import com.etc.entity.Content_file;
import com.etc.entity.Tag;
import com.etc.entity.Upload_content;
import com.etc.service.ContentFileService;
import com.etc.service.ContentTagService;
import com.etc.service.TagService;
import com.etc.service.UploadContentService;
import com.etc.service.impl.ContentFileServiceImpl;
import com.etc.service.impl.ContentTagServiceImpl;
import com.etc.service.impl.TagServiceImpl;
import com.etc.service.impl.UploadContentServiceImpl;
import com.google.gson.Gson;
import com.jspsmart.upload.SmartFile;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

/**
 * Servlet implementation class PublishContentServlet
 */
public class PublishContentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("text/html;charset=utf-8");				
		 PrintWriter out = response.getWriter();	   		
		 request.setCharacterEncoding("utf-8"); 
		 
		 
		 UploadContentService ucService=new UploadContentServiceImpl();
		 ContentFileService cfService=new ContentFileServiceImpl();
		 
		Gson gson=new Gson();
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
					System.out.println("上传文件到服务器的临时内存中-时出现cuowu");
				}
		
		 //插入内容描述和user
		 String allcontentStr=smartUpload.getRequest().getParameter("Contentgson");
		 AllOfContent aoc=gson.fromJson(allcontentStr, AllOfContent.class);
		 Upload_content uc=aoc.getContent();
		int ucid=ucService.insertUploadContent(uc);//得到插入的uc的ucid
		 System.out.println(ucid);
		 uc.setUcid(ucid);//把ucid赋值给uc
		 
		 String filenumberstr=smartUpload.getRequest().getParameter("filenumber");
		 int filenumber=Integer.parseInt(filenumberstr);
		 System.out.println(filenumber);

		 //插入文件
		  for(int i=0;i<filenumber;i++)
		  {
			  if(i==0)
			  {
				  SmartFile smartfile=smartUpload.getFiles().getFile(i);
				  String serverfilePath=request.getRealPath("image/contentImage");
					File file=new File(serverfilePath);
					if(!file.exists())
					{
						file.mkdirs();
					}
					String filename=smartfile.getFileName();
					if(filename.contains(".mp3"))
					{
						serverfilePath=request.getRealPath("music");
						System.out.println(serverfilePath+"/"+filename);
						try {
							smartfile.saveAs(serverfilePath+"/"+filename);
							System.out.println("yinpin文件保存至服务器时出现成功");
							Content_file cf=new Content_file();
							cf.setFilename(filename);
							cf.setUc(uc);
							if(cfService.addContentFile(cf))
							{
								System.out.println("true");
							}
						} catch (SmartUploadException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("yinpin文件保存至服务器时出现cuowu");
						}
						
					}
			  }
			  //提取上传文件
			 SmartFile smartfile=smartUpload.getFiles().getFile(i);
			//准备上传文件的存储路径和文件名
				//保证文件名唯一
				String serverfilePath=request.getRealPath("image/contentImage");
				File file=new File(serverfilePath);
				if(!file.exists())
				{
					file.mkdirs();
				}
				String filename=smartfile.getFileName();
				try {
					System.out.println(serverfilePath+"/"+filename);
					smartfile.saveAs(serverfilePath+"/"+filename);
					System.out.println("第"+i+"个文件保存至服务器时出现成功");
					Content_file cf=new Content_file();
					cf.setFilename(filename);
					cf.setUc(uc);
					if(cfService.addContentFile(cf))
					{
						System.out.println("true");
					}
				} catch (SmartUploadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("第"+i+"个文件保存至服务器时出现错误");
				}
				
		  }
		 //List<Content_file>cflist=new ArrayList<Content_file>();
		//插入tag与内容之间的关系
		  TagService ts=new TagServiceImpl();
		  ContentTagService cts=new ContentTagServiceImpl();
		  List<Tag> choosetag=new ArrayList<Tag>();
		  choosetag=aoc.getTaglist();
		 for(Tag tag:choosetag)
		 {
			 Tag newtag=ts.findTag(tag.getTagname());
			 if(cts.addContentTag(uc, newtag))
			 {
			 System.out.println("tagcontent表插入成功");
			 }
		 }
		
	}

}
