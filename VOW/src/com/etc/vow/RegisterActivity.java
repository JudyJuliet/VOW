package com.etc.vow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vow.app.MyApp;
import com.vow.entity.User;
import com.vow.login.LoginActivity;
import com.vow.task.SendMessageToServlet;
import com.vow.task.SendMultiMessage;
import com.vow.utils.Utils;
//注册和修改密码界面
public class RegisterActivity extends Activity {

	TextView re_txtPasswordInfor;//页面信息	
	TextView re_oldPass;//显示旧密码或者用户名
	
	EditText re_edtOldPass;//三个输入框
	EditText re_edtNewPass1;
	EditText re_edtNewPass2;
	
	TextView re_txtBack;//三个点击操作
	TextView re_txtSave;
	TextView re_txtMore;
	
	Intent intent;
	String fromActivity="";//判断来源
	
	MyApp myApp;
	User user;
	Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.activity_register);
		
		initView();
		handler = new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what==1)
				{
					if(fromActivity.equals("LoginActivity")){
						
						String gsonResult=msg.obj.toString();
						if(!gsonResult.equals("false\r\n")){
							myApp.setUser(Utils.FromUserGson(gsonResult));
							Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(RegisterActivity.this, ContainerActivity.class);
							startActivity(intent);
							finish();
						}else{
							Toast.makeText(getApplicationContext(), "用户名已存在", Toast.LENGTH_SHORT).show();						
						}
						
						}
					if(fromActivity.equals("UpdateUserInfoActivity"))
					{
						String Result=msg.obj.toString();
						Toast.makeText(getApplicationContext(), Result, Toast.LENGTH_SHORT).show();
					}
				}else
				{
					Toast.makeText(getApplicationContext(), "密码设置失败", Toast.LENGTH_SHORT).show();					
				}
			}			
		};
	}

	private void initView() {
		// TODO Auto-generated method stub
		re_txtPasswordInfor=(TextView)findViewById(R.id.re_txtPasswordInfor);
		re_oldPass=(TextView)findViewById(R.id.re_oldPass);
		re_edtOldPass=(EditText)findViewById(R.id.re_edtOldPass);
		re_edtNewPass1=(EditText)findViewById(R.id.re_edtNewPass1);
		re_edtNewPass2=(EditText)findViewById(R.id.re_edtNewPass2);
		re_txtBack=(TextView)findViewById(R.id.re_txtBack);
		re_txtSave=(TextView)findViewById(R.id.re_txtSave);
		re_txtMore=(TextView)findViewById(R.id.re_txtMore);
		
		re_txtBack.setOnClickListener(new OnClickListenerImpl());
		re_txtSave.setOnClickListener(new OnClickListenerImpl());
		re_txtMore.setOnClickListener(new OnClickListenerImpl());
		
		myApp=(MyApp) this.getApplication();
		if(this.myApp.getUser()!=null)
		{
			user=myApp.getUser();
		}else
		{
			user=new User();
		}
		
		intent=this.getIntent();
		 fromActivity=intent.getStringExtra("from");//判断原界面
		 
		if(fromActivity.equals("LoginActivity")){//从LoginActivity跳转进来的，是注册操作
			re_txtPasswordInfor.setText("注册");
			re_oldPass.setText("用户名/手机号/邮箱");
			re_txtMore.setVisibility(View.VISIBLE);
			re_txtSave.setVisibility(View.GONE);
		}else if(fromActivity.equals("UpdateUserInfoActivity")){//从UpdateUserInfoActivity跳转进来的，是修改密码操作
			re_txtPasswordInfor.setText("修改密码");
			re_oldPass.setText("原密码");
			re_txtMore.setVisibility(View.GONE);
		}
		
	}

	private class OnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch(view.getId()){
			case R.id.re_txtBack://返回
				
				finish();
				
				break;
			case R.id.re_txtMore:
			case R.id.re_txtSave:
				if(!re_edtNewPass1.getText().toString().equals(re_edtNewPass2.getText().toString())){//判断两次新密码填写是否一致
					Toast.makeText(getApplicationContext(), "新密码两次输入不一致！", Toast.LENGTH_SHORT).show();
				}else{
				if(fromActivity.equals("LoginActivity")){//新注册
					User newuser=new User();
					newuser.setUsername(re_edtOldPass.getText().toString());
					newuser.setPassword(re_edtNewPass1.getText().toString());
					//将新的user传给服务器--新用户注册
					String usergson=Utils.ToUserGson(newuser);
					SendMultiMessage SendMultiMessageThread=
							new SendMultiMessage(usergson,"RegisterServlet",handler,MyApp.REGISTERUSER);
					new Thread(SendMultiMessageThread).start();

					//SendMessageToServlet InternetThread=new SendMessageToServlet(usergson, "RegisterServlet", handler,MyApp.REGISTERUSER);
					//new Thread(InternetThread).start();

					}else if(fromActivity.equals("UpdateUserInfoActivity")){//修改密码
						
						if(user.getPassword().equals(re_edtOldPass.getText().toString())){//判断旧密码是否填写正确
							user.setPassword(re_edtNewPass1.getText().toString());
							
							//将新的user传给服务器--用户修改密码
							String usergson=Utils.ToUserGson(user);
							//SendMessageToServlet InternetThread=new SendMessageToServlet(usergson, "UserUpdateServlet", handler,MyApp.MODIFYPASSWORD);
							SendMultiMessage SendMultiMessageThread=
									new SendMultiMessage(usergson,"UserUpdateServlet",handler,MyApp.MODIFYPASSWORD);
							new Thread(SendMultiMessageThread).start();

							//new Thread(InternetThread).start();
																		
						}else{
							Toast.makeText(getApplicationContext(), "原密码填写错误！", Toast.LENGTH_SHORT).show();
						}
					}
				
				}
				break;
				
		}		
	}
	}

}
