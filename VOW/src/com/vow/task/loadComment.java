package com.vow.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.vow.app.MyApp;
import com.vow.entity.AllOfContent;
import com.vow.entity.Comments;

public class loadComment implements Runnable {

	private int ucid;
	private int what;
	private Handler handler;
	private List<Comments> list;
	

	public loadComment(int ucid,int what, Handler handler) {
		super();
		this.ucid = ucid;
		this.what=what;
		this.handler = handler;
		this.list =new ArrayList<Comments>();
	}


	//得到关于此条内容的评论
	@Override
	public void run() {
		// TODO Auto-generated method stub

		String url=MyApp.url+"VowTest1/ContentCommentListServlet";
		Gson gson=new Gson();
		Message msg=handler.obtainMessage();
		try {
			HttpClient client=new DefaultHttpClient();
			HttpPost request=new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ucid",ucid+""));
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));			
			
			HttpResponse response=client.execute(request);
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
			{
				String listgson=EntityUtils.toString(response.getEntity());
				Type type=new TypeToken<ArrayList<Comments>>(){}.getType();
				list=gson.fromJson(listgson, type);
				
				msg.obj=list;
				msg.what=what;
				handler.sendMessage(msg);
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
