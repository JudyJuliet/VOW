package com.etc.vow;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import com.google.gson.Gson;
import com.vow.app.MyApp;
import com.vow.entity.User;
import com.vow.login.LoginActivity;


//欢迎界面
public class SplashActivity extends Activity {

  boolean isFirstIn = false;  
  
  private static final int GO_HOME = 1000;  
  private static final int GO_GUIDE = 1001;  
  // 延迟3秒  
  private static final long SPLASH_DELAY_MILLIS = 3000;  

  private static final String SHAREDPREFERENCES_NAME = "first_pref";  
  
	private String username;
	private String password;
	private String responseText="";

  /** 
   * Handler:跳转到不同界面 
   */  
  private Handler handler = new Handler() {  

      @Override  
      public void handleMessage(Message msg) {  
          switch (msg.what) {  
          case GO_HOME:  
              goHome();  
              break;  
          case GO_GUIDE:  
              goGuide();  
              break;  
          case 1://  登陆成功  
        	 
					Gson gson=new Gson();
					User user=gson.fromJson(responseText, User.class);
					MyApp myApp=(MyApp) getApplication();
					myApp.setUser(user);
				    Intent intent1 = new Intent(SplashActivity.this, ContainerActivity.class);  
			        startActivity(intent1);  
			        finish();  
			        break;
          case 2:	//登录失败
          case -1://网络连接有问题,请稍候再试	
					 Intent intent2 = new Intent(SplashActivity.this, LoginActivity.class);  
					    startActivity(intent2);  
					    finish();  
				
        	  break;
         
          }  
          super.handleMessage(msg);  
      }  
  };  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		init(); 
	}


	 private void init() {  
	        // 读取SharedPreferences中需要的数据  
	        // 使用SharedPreferences来记录程序的使用次数  
	        SharedPreferences preferences = getSharedPreferences(  
	                SHAREDPREFERENCES_NAME, MODE_PRIVATE);  
	  
	        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值  
	        isFirstIn = preferences.getBoolean("isFirstIn", true);  
	  
	        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面  
	        if (!isFirstIn) {  
	            // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity  
	            handler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);  
	        } else {  
	            handler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);  
	        }  
	  
	    }  
	  
	    private void goHome() {  
	    	SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
			
			if(sp.contains("username")&&sp.contains("password")){
					username=sp.getString("username",null);
					password=sp.getString("password",null);
					new Thread(new loginRunner()).start();
					
					
				}else{
				    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);  
				    startActivity(intent);  
				    finish();  
	        }
	    }  
	  
	    private void goGuide() {  
	        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);  
	        startActivity(intent);  
	        finish();  
	    }  
	    
//	    private class AutoLoginTask extends AsyncTask<String,Void, String>{
//
//			@Override
//			protected String doInBackground(String... args) {
//				// TODO Auto-generated method stub
//				String username=args[0];
//				String password=args[1];
//				String url="http://10.0.2.2:8080/VowTest1/UserLoginServlet";
//				
//				HttpClient client=new DefaultHttpClient();
//				HttpPost request=new HttpPost(url);
//				List<NameValuePair> params=new ArrayList<NameValuePair>();
//				params.add(new BasicNameValuePair("username",username));
//				params.add(new BasicNameValuePair("password",password));
//				try {
//					request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));	
//				    HttpResponse response = client.execute(request);
//				
//				    if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){			
//				    	responseText = EntityUtils.toString(response.getEntity());
//						return responseText;
//					  }
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}	
//
//				return null;
//			}
//		
//		}
	    private class loginRunner implements Runnable{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				
				HttpClient client = new DefaultHttpClient();
				
				
				String url=MyApp.url+"VowTest1/UserLoginServlet";
				HttpPost request = new HttpPost(url);
				
				try {
					//封装请求参数
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username", username));
					params.add(new BasicNameValuePair("password", password));

					//设置请求参数
					request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					
					HttpResponse response = client.execute(request);
								
					if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
						
						
						responseText = EntityUtils.toString(response.getEntity());
						if(!responseText.equals("notfind\r\n"))//请求成功		
						{
						//向主线程发消息
						handler.sendEmptyMessage(1);	
						}else//请求失败
						{
							handler.sendEmptyMessage(2);		
						}
					}
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
					handler.sendEmptyMessage(-1);
					
				} catch (ClientProtocolException e) {
					
					e.printStackTrace();
					Log.d("ClientProtocolException", "客户端ClientProtocolException");
					handler.sendEmptyMessage(-1);
					
				} catch (ParseException e) {
					
					e.printStackTrace();
					Log.d("ParseException转换出错", "ParseException");
					
					handler.sendEmptyMessage(-1);
				} catch (IOException e) {
					
					e.printStackTrace();
					handler.sendEmptyMessage(-1);//请求失败
				}
			}
			
		}
	
}



