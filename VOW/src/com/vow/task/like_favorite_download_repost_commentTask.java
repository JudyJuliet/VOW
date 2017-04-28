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

import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etc.vow.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.vow.app.MyApp;
import com.vow.entity.Comments;
import com.vow.entity.Upload_content;
import com.vow.utils.Utils;

public class like_favorite_download_repost_commentTask extends AsyncTask<String,Void,String>{
		

	private String resultString="";
	private int operationIndex;//标志位--like_favorite_download_repost_comment的哪个操作
    private TextView textview;
    private ImageView image;
    private boolean oldicon;
	private Gson gson=new Gson();
	private View v;

	public like_favorite_download_repost_commentTask(TextView textview) {
		super();
		this.textview=textview;
	}

	
		public like_favorite_download_repost_commentTask(TextView textview,ImageView image,boolean oldicon) {
		super();
		
		this.oldicon=oldicon;
		this.image=image;
		this.textview = textview;
	}

		public like_favorite_download_repost_commentTask()
		{
			
		}
		
	
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			String userstr=args[0];
			String contentstr=args[1];
			String operation_type=args[2];
			
			operationIndex=Integer.parseInt(operation_type);//得到真实参数
			
			String url=MyApp.url+"VowTest1/like_favorite_download_repost_commentServlet";
			
			try {
				HttpClient client=new DefaultHttpClient();
				HttpPost request=new HttpPost(url);
				List<NameValuePair> params=new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("user",userstr));
				params.add(new BasicNameValuePair("content",contentstr));
				params.add(new BasicNameValuePair("operationIndex",operation_type));
				request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));			
				
				HttpResponse response=client.execute(request);
				if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){			
					resultString = EntityUtils.toString(response.getEntity());
					return resultString;
					
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
			return null;			
		}

		
	}




