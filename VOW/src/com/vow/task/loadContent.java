package com.vow.task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vow.app.MyApp;
import com.vow.entity.AllOfContent;
import com.vow.entity.Upload_content;
import com.vow.entity.User;

public class loadContent implements Runnable{

	private Context context;
	private User user;	
	private Handler handler;
	private int what;
	private List<AllOfContent> contentlist;
	private String searchstr;
	private int operationType;//标志位---加载的内容夹是查看自己上传的内容还是转发的内容
	
	public loadContent(Context context,User user,Handler handler,int what,int operationType) {
		super();
		this.context = context;
		this.user=user;
	    this.handler=handler;
	    this.what=what;
	    this.operationType=operationType;
		contentlist=new ArrayList<AllOfContent>();
	}
	
	public loadContent(Context context,User user,Handler handler,int what,int operationType,String searchstr) {
		super();
		this.context = context;
		this.user=user;
	    this.handler=handler;
	    this.what=what;
	    this.operationType=operationType;
	    this.searchstr=searchstr;
		contentlist=new ArrayList<AllOfContent>();
	}
	
	
	//得到用户的说说
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		String url=MyApp.url+"VowTest1/ContentListServlet";
		Gson gson=new Gson();
		Message msg=handler.obtainMessage();
		try {
			HttpClient client=new DefaultHttpClient();
			HttpPost request=new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			String gstrUser=gson.toJson(user);
			params.add(new BasicNameValuePair("user",gstrUser));
			params.add(new BasicNameValuePair("operationType",operationType+""));
			if(operationType==MyApp.OPERATION_TYPE_TAG_CONTENT
					||operationType==MyApp.OPERATION_TYPE_KEY_CONTENT)
			{
				params.add(new BasicNameValuePair("searchstr",searchstr));
			}
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));			
			
			HttpResponse response=client.execute(request);
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
			{
				String list=EntityUtils.toString(response.getEntity());
				Type type=new TypeToken<ArrayList<AllOfContent>>(){}.getType();
				contentlist=gson.fromJson(list, type);
				
				msg.obj=contentlist;
				msg.what=what;
				
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


