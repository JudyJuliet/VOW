package com.vow.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etc.vow.ContainerActivity;
import com.etc.vow.ListViewActivity;
import com.etc.vow.R;
import com.etc.vow.RegisterActivity;
import com.etc.vow.UpdateUserInfoActivity;
import com.google.gson.Gson;
import com.vow.app.MyApp;
import com.vow.entity.User;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
public class LoginActivity extends Activity implements OnClickListener {
	
	private TextView mBtnLogin,mBtnSignup,mBtnBack;
	//登陆,注册,返回按钮
	
	private String username,password;
	private View progress;//登陆进度条
	
	private ProgressBar ProgressBar2;
	private View mInputLayout;//输入布局
	private EditText edtUsername;//用户名&密码
	private EditText edtPassword;
	
	private Handler handler;
	
	private String responseText;
	
	private String url = MyApp.url+"VowTest1/UserLoginServlet";
	
	private float mWidth, mHeight;
	
	private LinearLayout mName, mPsw;
	private CheckBox checkAucLogin;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		initView();
		
		handler = new Handler(){
				
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);

					if(msg.what==1){
						if(checkAucLogin.isChecked()){//选择记住密码
							SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
							Editor editor = sp.edit();
							editor.putString("username",edtUsername.getText().toString());
							editor.putString("password",edtPassword.getText().toString());
							editor.commit();
						}
						
						progress.setVisibility(View.GONE);
						ProgressBar2.setVisibility(View.GONE);
						Gson gson = new Gson();
						User user = gson.fromJson(responseText, User.class);						
						MyApp myApp = (MyApp)getApplication();
						myApp.setUser(user);
							
//						Intent intent = new Intent(LoginActivity.this, ListViewActivity.class);
						Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
						startActivity(intent);
						finish();
						}else{
							
							if(msg.what==-1){
							Toast.makeText(getApplicationContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();	
							Intent intent =new Intent(LoginActivity.this,LoginActivity.class);
							startActivity(intent);
							finish();
							}else{
								Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();										
								Intent intent =new Intent(LoginActivity.this,LoginActivity.class);
								startActivity(intent);
								finish();
							}
							}
				}				
			};
	}
	 private void initView() {
		    mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
		    mBtnSignup=(TextView)findViewById(R.id.title_layout_sign);
		    mBtnBack=(TextView)findViewById(R.id.title_layout_back);
		    
		    progress = findViewById(R.id.layout_progress);
		    mInputLayout = findViewById(R.id.input_layout);
		    mName = (LinearLayout) findViewById(R.id.input_layout_name);
		    mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
		    ProgressBar2=(ProgressBar)findViewById(R.id.progressBar2);
		    
		    this.edtUsername=(EditText)findViewById(R.id.edtUsername);
		    this.edtPassword=(EditText)findViewById(R.id.edtPassword);
		    this.checkAucLogin=(CheckBox) findViewById(R.id.rememberpass);


		    mBtnLogin.setOnClickListener(this);
		    this.mBtnSignup.setOnClickListener(this);
		    this.mBtnBack.setOnClickListener(this);
		  }

		  @Override
		  public void onClick(View v) {

			  switch(v.getId())
			  {
			  case R.id.main_btn_login:
				    // 计算出控件的高与宽
				    mWidth = mBtnLogin.getMeasuredWidth();
				    mHeight = mBtnLogin.getMeasuredHeight();
				   username = edtUsername.getText().toString();
					password = edtPassword.getText().toString();
				    // 隐藏输入框
				    mName.setVisibility(View.INVISIBLE);
				    mPsw.setVisibility(View.INVISIBLE);
				    
				    inputAnimator(mInputLayout, mWidth, mHeight);
				    
				    new Thread(new loginRunner()).start();
		
				    break;
			  case R.id.title_layout_sign:
				  Intent intent =new Intent(LoginActivity.this,RegisterActivity.class);
				  intent.putExtra("from", "LoginActivity");
				  startActivity(intent);
				  
				  break;
			  case R.id.title_layout_back:
				  System.exit(0);
				  break;
			  }
		  }

		  /**
		   * 输入框的动画效果
		   * 
		   * @param view
		   *      控件
		   * @param w
		   *      宽
		   * @param h
		   *      高
		   */
		  @TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") 
		  private void inputAnimator(final View view, float w, float h) {

		    AnimatorSet set = new AnimatorSet();

		    ValueAnimator animator = ValueAnimator.ofFloat(0, w);
		    animator.addUpdateListener(new AnimatorUpdateListener() {

		      @Override
		      public void onAnimationUpdate(ValueAnimator animation) {
		        float value = (Float) animation.getAnimatedValue();
		        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) view
		            .getLayoutParams();
		        params.leftMargin = (int) value;
		        params.rightMargin = (int) value;
		        view.setLayoutParams(params);
		      }
		    });

		    ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
		        "scaleX", 1f, 0.5f);
		    set.setDuration(500);
		    set.setInterpolator(new AccelerateDecelerateInterpolator());
		    set.playTogether(animator, animator2);
		    set.start();
		    set.addListener(new AnimatorListener() {

		      @Override
		      public void onAnimationStart(Animator animation) {

		      }

		      @Override
		      public void onAnimationRepeat(Animator animation) {

		      }

		      @Override
		      public void onAnimationEnd(Animator animation) {
		        /**
		         * 动画结束后，先显示加载的动画，然后再隐藏输入框
		         */
		        progress.setVisibility(View.VISIBLE);
		        progressAnimator(progress);
		        mInputLayout.setVisibility(View.INVISIBLE);

		      }

		      @Override
		      public void onAnimationCancel(Animator animation) {

		      }
		    });

		  }

		  /**
		   * 出现进度动画
		   * 
		   * @param view
		   */
		  private void progressAnimator(final View view) {
		    PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
		        0.5f, 1f);
		    PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
		        0.5f, 1f);
		    ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
		        animator, animator2);
		    animator3.setDuration(1000);
		    animator3.setInterpolator(new JellyInterpolator());
		    animator3.start();

		  }
		  private class loginRunner implements Runnable{

				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					
					HttpClient client = new DefaultHttpClient();
					
					
					
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
						Log.d("INFO", ""+response.getStatusLine().getStatusCode());
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


