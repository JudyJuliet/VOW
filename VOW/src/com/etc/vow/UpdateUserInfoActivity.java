package com.etc.vow;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vow.app.MyApp;
import com.vow.entity.User;
import com.vow.task.SendMessageToServlet;
import com.vow.task.SendMultiMessage;
import com.vow.utils.AvatarImageView;
import com.vow.utils.Utils;
import com.vow.utils.loadPhoto;
import com.vow.utils.saveBitmapToFile;
//用户个人修改信息界面
public class UpdateUserInfoActivity extends Activity {

	public final int OPENCAMERA=1;
	public final int OPENGALLERY=2;
	    //用户名
		private EditText up_edtUsername;	
		//个性签名
		private EditText up_edtSignature;
		//生日
		private EditText up_edtBirthday;
		//地点
		private EditText up_edtLocation;
		//邮箱
		private EditText up_edtEmail;
		//电话	
		private EditText up_edtPhone;	
		//性别
		private RadioGroup up_radiogroupGender;
		private RadioButton up_radioButtonfemale,up_radioButtonMale,up_radioButtonUnknown;		
		
		private TextView up_txtBack;//返回
		private TextView up_txtSave;//保存
		private TextView up_txtModifyPassword;//修改密码
		
		//用户头像
		private AvatarImageView up_imagephoto;
		//用户更新头像后的头像uri地址
		private Uri up_user_photo;
		//用户拍照后的Bitmap
		private Bitmap bm;
		//用来判断用户到底是拍照上传头像还是从图库中选择图像
		//camera是true,gallery是false,默认就false
		private boolean camera_or_gallery=false;
		
		//全局变量
		private MyApp myapp;
		private User user,olduser;
		private Handler handler=new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what==1)
				{
					String result=msg.obj.toString();
					if(result.equals("fail\r\n"))
					{
						Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_SHORT).show();				

					}else if(result.equals("already exist\r\n"))
					{
						Toast.makeText(getApplicationContext(), "用户名已存在!", Toast.LENGTH_SHORT).show();					

					}else
					{
						User newuser=Utils.FromUserGson(result);
						myapp.setUser(newuser);
						Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();									

					}
				}else
				{
					Toast.makeText(getApplicationContext(), "网络连接失败,请稍后再试", Toast.LENGTH_SHORT).show();					
				}
			}
		};
		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_update_user_info);
		
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		this.up_edtUsername=(EditText) findViewById(R.id.up_edtUsername);
		this.up_edtSignature=(EditText)findViewById(R.id.up_edtSignature);
		this.up_edtBirthday=(EditText)findViewById(R.id.up_edtBirthday);
		this.up_edtLocation=(EditText)findViewById(R.id.up_edtLocation);		
		this.up_edtEmail=(EditText)findViewById(R.id.up_edtEmail);
		this.up_edtPhone=(EditText)findViewById(R.id.up_edtPhone);
				
		this.up_radiogroupGender=(RadioGroup) findViewById(R.id.up_radioGroupGender);
	    this.up_radioButtonfemale=(RadioButton) findViewById(R.id.up_radiofemale);
	    this.up_radioButtonMale=(RadioButton) findViewById(R.id.up_radiomale);
	    this.up_radioButtonUnknown=(RadioButton) findViewById(R.id.up_radiounknown);
	    
		
		this.up_imagephoto=(AvatarImageView) findViewById(R.id.up_photo);
		
		this.up_txtBack=(TextView) findViewById(R.id.up_txtBack);
		this.up_txtModifyPassword=(TextView) findViewById(R.id.up_txtModifyPassword);
		this.up_txtSave=(TextView) findViewById(R.id.up_txtSave);
		
		myapp=(MyApp) this.getApplication();
		if(myapp.getUser()!=null)
		{			
			olduser=myapp.getUser();
			user=Utils.copyUser(olduser);
			up_edtUsername.setText(olduser.getUsername());			
			up_edtEmail.setText(olduser.getEmail());
			up_edtPhone.setText(olduser.getPhone());
			//判断是否为空
			if(olduser.getSignature()!=null){
				up_edtSignature.setText(olduser.getSignature());
			}
			if(olduser.getBirthday()!=null){
				up_edtBirthday.setText(olduser.getBirthday());
			}
			if(olduser.getLocation()!=null){
				up_edtLocation.setText(olduser.getLocation());
			}
			//加载用户头像
			if(olduser.getPhoto()!=null){
				loadPhoto.loadImage(up_imagephoto,"image/photo/"+olduser.getPhoto());
			}
			
			//set性别
			if(olduser.getGender()!=null){
				if(olduser.getGender().equals("男"))
				{
					this.up_radioButtonMale.setChecked(true);
				}else if(olduser.getGender().equals("女"))
				{
					this.up_radioButtonfemale.setChecked(true);
				}else{
					this.up_radioButtonUnknown.setChecked(true);
				}
			}else
			{
				this.up_radioButtonUnknown.setChecked(true);
			}
		}
		
		View.OnClickListener ClickListener=new OnClickListenerImpl();
		this.up_radiogroupGender.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
		
		this.up_imagephoto.setOnClickListener(ClickListener);
		
		this.up_txtBack.setOnClickListener(ClickListener);
		this.up_txtModifyPassword.setOnClickListener(ClickListener);
		this.up_txtSave.setOnClickListener(ClickListener);
		this.up_edtBirthday.setOnClickListener(ClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.update_user_info, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case R.id.up_cancel_change_photo:
			break;
		case R.id.up_take_photo:
			Intent intentToCamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intentToCamera, OPENCAMERA);
			break;
		case R.id.up_select_photo:
			Intent intentToMedia=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intentToMedia, OPENGALLERY);
			break;
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub				
		//super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode)
		{
		case OPENCAMERA:
			if(resultCode==RESULT_OK)
			{
				Bundle bundle=data.getExtras();
				bm=(Bitmap) bundle.get("data");
				this.up_imagephoto.setImageBitmap(bm);
				this.camera_or_gallery=true;
			}
			break;
		case OPENGALLERY:
			if(resultCode==RESULT_OK)
			{
				up_user_photo=data.getData();
				this.up_imagephoto.setImageURI(up_user_photo);
				this.camera_or_gallery=false;
			}
			break;
		}
	}
	
	
	private class OnClickListenerImpl implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			//返回
			case R.id.up_txtBack:
				finish();
				break;
			case R.id.up_txtSave:
				if(up_edtUsername.getText().toString().equals("")){
					//用户名不可为空
					Toast.makeText(getApplicationContext(), "用户名不可以为空!", Toast.LENGTH_SHORT).show();
				}else{
				user.setUsername(up_edtUsername.getText().toString());
				}
				user.setSignature(up_edtSignature.getText().toString());
				user.setBirthday(up_edtBirthday.getText().toString());
				user.setLocation(up_edtLocation.getText().toString());		
				user.setEmail(up_edtEmail.getText().toString());
				user.setPhone(up_edtPhone.getText().toString());
				
				//检查是否更改头像
				//如果没有更改则无需上传到服务器
				//如果改了,就把头像上传到服务器中
				String realpath=null;//图片路径
				if(camera_or_gallery)//拍照
				{
					if(bm!=null){
						//得到保存图像后的真实路径
						saveBitmapToFile saveBitmapToFiletask=new saveBitmapToFile();
						realpath=saveBitmapToFiletask.saveBitmap("user/photo", bm);
					}

				}else{//从图库中得取
					if(up_user_photo!=null)
					{
						realpath=Utils.getRealPathFromUri(getApplicationContext(), up_user_photo);
						
					}
				}
								
				String usergson=Utils.ToUserGson(user);
				
				if(realpath!=null)//传用户新换的头像和修改后的个人信息
				{
					SendMultiMessage SendMultiMessageThread=
							new SendMultiMessage(usergson,realpath,"UserUpdateServlet",handler,MyApp.UPDATEUSERINFO);
					new Thread(SendMultiMessageThread).start();
				}else//只传修改后的个人信息
				{
					SendMultiMessage SendMultiMessageThread=
							new SendMultiMessage(usergson,"UserUpdateServlet",handler,MyApp.UPDATEUSERINFO);
					new Thread(SendMultiMessageThread).start();
				}
			
//				SendMessageToServlet InternetThread=new SendMessageToServlet(usergson, "UserUpdateServlet", handler,MyApp.UPDATEUSERINFO);
//				new Thread(InternetThread).start();
//				
				break;
			case R.id.up_txtModifyPassword:
				Intent intent =new Intent(UpdateUserInfoActivity.this,RegisterActivity.class);
				intent.putExtra("from", "UpdateUserInfoActivity");
				startActivity(intent);
				break;
			case R.id.up_photo:
				UpdateUserInfoActivity.this.openOptionsMenu();
				break;
			case R.id.up_edtBirthday:
				Calendar c=Calendar.getInstance();
				new DatePickerDialog(UpdateUserInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						// TODO Auto-generated method stub
						up_edtBirthday.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
						
					}
				}, c.get(Calendar.YEAR), 
				c.get(Calendar.MONTH), 
				c.get(Calendar.DAY_OF_MONTH)).show();
				
				break;
			}
		}		
	}
	
	
	private class OnCheckedChangeListenerImpl implements OnCheckedChangeListener
	{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch(checkedId)
			{
			case R.id.up_radiofemale:
				user.setGender("女");
				break;
			case R.id.up_radiomale:
				user.setGender("男");
				break;
			case R.id.up_radiounknown:
				user.setGender("蒙面侠");
				break;
				
			}
		}
		
	}
}
