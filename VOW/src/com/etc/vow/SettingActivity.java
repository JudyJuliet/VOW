package com.etc.vow;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class SettingActivity extends Activity {

	private TextView txtSettingBack;
	private Button btn_myaccount,btn_theme,btn_help, btn_about;
	private TextView btn_exit;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		txtSettingBack = (TextView) findViewById(R.id.txtSettingBack);
		btn_myaccount = (Button) findViewById(R.id.btn_myaccount);
		btn_theme = (Button) findViewById(R.id.btn_theme);
		btn_help = (Button) findViewById(R.id.btn_help);
		btn_about = (Button) findViewById(R.id.btn_about);
		btn_exit = (TextView) findViewById(R.id.txtexit);
		
		txtSettingBack.setOnClickListener(new ClickListenerImpl());
		btn_myaccount.setOnClickListener(new ClickListenerImpl());
		btn_theme.setOnClickListener(new ClickListenerImpl());
		btn_help.setOnClickListener(new ClickListenerImpl());
		btn_about.setOnClickListener(new ClickListenerImpl());
		btn_exit.setOnClickListener(new ClickListenerImpl());
	}

	private class ClickListenerImpl implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.txtSettingBack:
				//从设置回到首页
				intent = new Intent(SettingActivity.this,ContainerActivity.class);
				startActivity(intent);
				finish();
				break;
			case R.id.btn_myaccount:
				//跳到个人资料页，带返回值跳转
				intent = new Intent(SettingActivity.this,UpdateUserInfoActivity.class);
				startActivity(intent);
				finish();
				break;
			case R.id.btn_theme:
				//换主题啦啦啦
				Toast.makeText(getApplicationContext(), "主题更换", Toast.LENGTH_LONG).show();
				break;
			case R.id.btn_help:
				//弹出对话框，显示帮助
				Toast.makeText(getApplicationContext(), "去首页看看吧~", Toast.LENGTH_LONG).show();
				break;
			case R.id.btn_about:
				//对话框显示关于软件
				Toast.makeText(getApplicationContext(), "显示关于", Toast.LENGTH_LONG).show();
				break;
			case R.id.txtexit:
				//退出程序
				System.exit(0);
				break;
			default:
				break;
			}
			
		}
		
	}

}
