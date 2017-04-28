package com.vow.task;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.vow.app.MyApp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

//
public class SendMultiMessage implements Runnable {

	private final int FILES_ENTITY=1;//多个文件和一个实体gson
	private final int ENTITYINFO=2;//只有实体gson
	private final int FILEANDINFO=3;//一个文件和实体gson
	//要传的实体类
	private String entitygson;
	//要传的文件
	private String filepath;
	private List<String> filelist;
	private String urlServlet;
	private String baseurl=MyApp.url+"VowTest1/";
	
	private Handler handler;
	private int flag;
	private int kind;
	
	public SendMultiMessage(String entitygson,String filepath, String urlServlet,
			Handler handler,int flag) {
	//一个文件和实体gson
		this.filepath=filepath;
		this.entitygson = entitygson;
		this.urlServlet = urlServlet;
		this.handler = handler;
		this.flag=flag;
		this.kind=FILEANDINFO;
	}
	
	
	public SendMultiMessage(String entitygson, String urlServlet,
			 Handler handler, int flag) {
		//只传个实体过去
		this.entitygson = entitygson;
		this.urlServlet = urlServlet;
		this.handler = handler;
		this.flag = flag;
		this.kind=ENTITYINFO;
	}	

	public SendMultiMessage(String entitygson,List<String> filelist,
			String urlServlet,Handler handler)
	{
		this.entitygson = entitygson;
		this.urlServlet = urlServlet;
		this.handler = handler;
		this.filelist=filelist;
		this.kind=FILES_ENTITY;
	}


	@Override
	public void run() {
		
		String url=baseurl+urlServlet;		
		Message msg=handler.obtainMessage();
		
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		//创建多部分实体对象entity
		MultipartEntity multientity=new MultipartEntity();
		try {
			StringBody kindbody=new StringBody(kind+"",Charset.forName("utf-8"));
			multientity.addPart("kind",kindbody);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("TAG","在对int型kind转化为StringBody这里出错了");
		}
		
		if(kind==ENTITYINFO)
		{
			try {
				StringBody entity=new StringBody(entitygson,Charset.forName("utf-8"));
				multientity.addPart("ServletRequest", entity);
				StringBody flagbody=new StringBody(flag+"",Charset.forName("utf-8"));
				multientity.addPart("flag",flagbody);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("TAG","在对String型entity转化为StringBody这里出错了");
			}
		}else if(kind==FILES_ENTITY)
		{
			//将filelist传入multientity
			int i=0;
			for(String eachfilepath:filelist)
			{
				i++;
				File file=new File(eachfilepath);			
				FileBody fileBody=new FileBody(file);
				multientity.addPart("file"+i,fileBody);
				
			}
			try {
				//传入文件个数
				StringBody filenumber=new StringBody(i+"",Charset.forName("utf-8"));
				multientity.addPart("filenumber",filenumber);
				StringBody entity;
				entity = new StringBody(entitygson,Charset.forName("utf-8"));
				multientity.addPart("Contentgson", entity);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("TAG","在对String型entity转化为StringBody这里出错了");
			}
			
			
		}else//kind==FILEANDENTITY
		{
			try {
				StringBody flagbody=new StringBody(flag+"",Charset.forName("utf-8"));
				multientity.addPart("flag",flagbody);
				StringBody entity=new StringBody(entitygson,Charset.forName("utf-8"));
				multientity.addPart("ServletRequest", entity);
				File file=new File(filepath);
				FileBody fileBody=new FileBody(file);
				multientity.addPart("file",fileBody);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("TAG","在对String型entity转化为StringBody这里出错了");
			}
		}
		try {
			request.setEntity(multientity);
			HttpResponse response=client.execute(request);
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
			{
				msg.obj=EntityUtils.toString(response.getEntity());
				msg.what=1;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.what=-1;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.what=-1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.what=-1;
		}
		handler.sendMessage(msg);
		
	}

}
