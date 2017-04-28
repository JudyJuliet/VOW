package com.vow.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.reflect.TypeToken;
import com.vow.app.MyApp;
import com.vow.entity.User;

import android.os.Handler;
import android.os.Message;

public class SendMessageToServlet implements Runnable {

//	private final int UPDATEUSERINFO=0;
//	private final int MODIFYPASSWORD=1;
//	private final int REGISTERUSER=2;
//	
	private String entitygson;

	private String urlServlet;
	private String baseurl=MyApp.url+"VowTest1/";
	
	private Handler handler;
	
	
	public SendMessageToServlet(String entitygson, String urlServlet,
			Handler handler) {
		super();
		this.entitygson = entitygson;
		this.urlServlet = urlServlet;
		this.handler = handler;
		
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		String url=baseurl+urlServlet;
		Message msg=handler.obtainMessage();
		try {
			HttpClient client=new DefaultHttpClient();
			HttpPost request=new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ServletRequest",entitygson));
			
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));						
			HttpResponse response=client.execute(request);
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
			{
				//返回更新后的实体类Gson串
				msg.obj=EntityUtils.toString(response.getEntity());
				msg.what=100;
				
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.what=-1;
		} catch (ClientProtocolException e) {
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
