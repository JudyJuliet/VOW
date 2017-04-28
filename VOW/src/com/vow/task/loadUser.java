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
import com.vow.entity.User;

public class loadUser implements Runnable{

	private Context context;
	private User user;
	private String keyname;
	private List<User> userlist;
	private Handler handler;
	private int what;
	private int index;
	
	public loadUser(Context context,User user,String keyname,Handler handler,int what,int index) {
		super();
		this.context = context;
		this.user=user;
		this.keyname=keyname;
	    this.handler=handler;
	    this.what=what;
	    this.index=index;
		userlist=new ArrayList<User>();
	}
	
	//查看用户关注人列表
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String url="";
		if(index==1){
		     url=MyApp.url+"VowTest1/UserFindFrdListServlet";
		}else if(index==0){
			 url=MyApp.url+"VowTest1/AllUserListServlet";
		}
		
		Gson gson=new Gson();
		String list="";
		Message msg=handler.obtainMessage();
		try {
			HttpClient client=new DefaultHttpClient();
			HttpPost request=new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userid",user.getUserid()+""));
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));			
			
			HttpResponse response=client.execute(request);
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
			{
				list=EntityUtils.toString(response.getEntity());
				Type type=new TypeToken<ArrayList<User>>(){}.getType();
				userlist=gson.fromJson(list, type);
				
				if(keyname==null||keyname.equals("")){//文本框无内容，全部显示
					msg.obj=userlist;
				}else{//文本框有内容，模糊查询
					List<User>key_userlist=new ArrayList<User>();
					for(User useritem:userlist){
						if(useritem.getUsername().contains(keyname)){
							key_userlist.add(useritem);
						}
					}
					msg.obj=key_userlist;
				}
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


