package com.etc.vow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.vow.app.MyApp;
import com.vow.customerView.BitmapUtils;
import com.vow.customerView.VowScrollView;
import com.vow.utils.loadPhoto;

public class SelfInfoActivity extends Activity implements OnGestureListener {
	
	private String TAG = VisiterActivity.class.getSimpleName();
	private TextView txtBackSelf,txtSelfSetting;
	private ImageView imgSelfPhoto;
	private TextView txtSelfName,txtSelfAge;
	private TextView txtSelfTag,txtSelfSignature,txtSelfDegree,txtSelfPost,txtSelfLocation;
	private Bitmap overlay = null;
	private FrameLayout bg;
	private GestureDetector mGestureDetector;
	private MyApp myapp;
	private Bitmap userphotobm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_self_info);
		txtBackSelf = (TextView) findViewById(R.id.txtBackSelf);
		
		txtSelfSetting = (TextView) findViewById(R.id.txtSelfSetting);
		imgSelfPhoto = (ImageView) findViewById(R.id.imgSelfPhoto);
		myapp=(MyApp) getApplication();
		userphotobm=loadPhoto.loadImageBitmap(imgSelfPhoto, "image/photo/"+myapp.getUser().getPhoto());
		txtSelfName = (TextView) findViewById(R.id.txtSelfName);
		txtSelfName.setText(myapp.getUser().getUsername());
		txtSelfAge = (TextView) findViewById(R.id.txtSelfAge);
		if(myapp.getUser().getBirthday()!=null)
		{
			txtSelfAge.setText(myapp.getUser().getBirthday());
		}else
		{
			txtSelfAge.setText("该用户尚未填写");
		}
		txtSelfTag = (TextView) findViewById(R.id.txtSelfTag);
		txtSelfSignature = (TextView) findViewById(R.id.txtSelfSignature);
		if(myapp.getUser().getSignature()!=null)
		{
			txtSelfSignature.setText(myapp.getUser().getSignature());
		}else
		{
			txtSelfSignature.setText("该用户尚未填写");
		}
		txtSelfDegree = (TextView) findViewById(R.id.txtSelfDegree);
		if(myapp.getUser().getScore()>=0)
		{
			
			txtSelfDegree.setText(""+((myapp.getUser().getScore())/5)+"级");
		}
		
		txtSelfPost = (TextView) findViewById(R.id.txtSelfPost);
		txtSelfLocation = (TextView) findViewById(R.id.txtSelfLocation);
		if(myapp.getUser().getLocation()!=null)
		{
			txtSelfLocation.setText(myapp.getUser().getLocation());
		}else
		{
			txtSelfLocation.setText("该用户尚未填写");
		}
		bg = (FrameLayout) findViewById(R.id.self_head_bg);
		
		mGestureDetector = new GestureDetector(this); 
		VowScrollView selfScrollView = (VowScrollView) findViewById(R.id.self_scrollview);
		selfScrollView.setOnTouchListener(onTouchListener);
		selfScrollView.setGestureDetector(mGestureDetector);
		initAvatarBackground();
		txtBackSelf.setOnClickListener(new ClickListenerImpl());
		txtSelfSetting.setOnClickListener(new ClickListenerImpl());
		txtSelfPost.setOnClickListener(new ClickListenerImpl());
	}
	private class ClickListenerImpl implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.txtBackSelf:
				//返回上一列表
				//Toast.makeText(getApplicationContext(), "返回上一列表", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case R.id.txtSelfSetting:
				//转到用户设置页
				//Toast.makeText(getApplicationContext(), "添加关注成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(SelfInfoActivity.this,SettingActivity.class);
				startActivity(intent);
				break;
			
			case R.id.txtVisitPost:
				//查看该用户的发布内容
				//可以直接加载列表，支持下拉
				//Toast.makeText(getApplicationContext(), "查看该用户的发布内容", Toast.LENGTH_SHORT).show();
				Intent intentToContent=new Intent(SelfInfoActivity.this,ContentListActivity.class);
				intentToContent.putExtra("operationType", MyApp.OPERATION_TYPE_ORIGINAL_CONTENT);
				startActivity(intentToContent);
				break;
			case R.id.imgSelfPhoto:
				//进入编辑列表
				Intent intentToUpdate=new Intent(SelfInfoActivity.this,UpdateUserInfoActivity.class);
				startActivity(intentToUpdate);
				break;
			default:
				break;
			}
			
		}
		  
	}
	
	@SuppressWarnings("deprecation")
	private void initAvatarBackground() {
		//Drawable drawable = getResources().getDrawable(imgVisitPhoto.getId());
       // Drawable drawable = getResources().getDrawable(R.drawable.default_user_photo);
		Drawable drawable = new BitmapDrawable(this.userphotobm);
     
        Bitmap srcBitmap = BitmapUtils.drawable2Bitmap(drawable);

        /*先黑白图片*/
        float[] src = new float[]{
                0.28F, 0.60F, 0.40F, 0, 0,
                0.28F, 0.60F, 0.40F, 0, 0,
                0.28F, 0.60F, 0.40F, 0, 0,
                0, 0, 0, 1, 0,
        };
        ColorMatrix cm = new ColorMatrix(src);
//        cm.setSaturation(0.0f);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        Bitmap resultBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(100);
        paint.setColorFilter(f);
        canvas.drawBitmap(srcBitmap, 0, 0, paint);
        /*后模糊图片*/
        Bitmap bB = BitmapUtils.blurBitmap(getApplicationContext(), resultBitmap, 15.5f);

        bg.setBackgroundDrawable(new BitmapDrawable(getResources(), bB));
    }
	
	private OnTouchListener onTouchListener = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return mGestureDetector.onTouchEvent(event);
			}
		};
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
