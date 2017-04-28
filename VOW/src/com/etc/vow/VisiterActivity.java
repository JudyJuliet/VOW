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

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vow.app.MyApp;
import com.vow.customerView.BitmapUtils;
import com.vow.customerView.VowScrollView;
import com.vow.entity.User;
import com.vow.utils.Utils;
import com.vow.utils.loadPhoto;

public class VisiterActivity extends Activity implements OnGestureListener {
	
	
	private String TAG = VisiterActivity.class.getSimpleName();
	private TextView txtBackVisit,txtVisitLike;
	private ImageView imgVisitPhoto;
	private TextView txtVisitName,txtVisitAge;
	private TextView txtVistitTag,txtVisitSignature,txtVisitDegree,txtVisitPost,txtVisitLocation;
	private MyApp myapp;
	private User oriuser;
	private Bitmap overlay = null,userphotobm;
	private FrameLayout bg;
	private GestureDetector mGestureDetector;
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_visiter);
		txtBackVisit = (TextView) findViewById(R.id.txtBackVisit);
		txtVisitLike = (TextView) findViewById(R.id.txtVisitLike);
		imgVisitPhoto = (ImageView) findViewById(R.id.imgVisitPhoto);
		txtVisitName = (TextView) findViewById(R.id.txtVisitName);
		txtVisitAge = (TextView) findViewById(R.id.txtVisitAge);
		txtVistitTag = (TextView) findViewById(R.id.txtVisitTag);
		txtVisitSignature = (TextView) findViewById(R.id.txtVisitSignature);
		txtVisitDegree = (TextView) findViewById(R.id.txtVisitDegree);
		txtVisitPost = (TextView) findViewById(R.id.txtVisitPost);
		txtVisitLocation = (TextView) findViewById(R.id.txtVisitLocation);
		bg = (FrameLayout) findViewById(R.id.head_bg);
		myapp=(MyApp) getApplication();
		this.oriuser=(User) getIntent().getSerializableExtra("user");
		
		mGestureDetector = new GestureDetector(this); 
		VowScrollView visitorScrollView = (VowScrollView) findViewById(R.id.visitor_scrollview);
		visitorScrollView.setOnTouchListener(onTouchListener);
		visitorScrollView.setGestureDetector(mGestureDetector);
		//visitorLayout = (RelativeLayout) findViewById(R.id.visitor_layout);
		imgVisitPhoto.setDrawingCacheEnabled(true);
		imgVisitPhoto.buildDrawingCache(true);
		userphotobm=loadPhoto.loadImageBitmap(imgVisitPhoto, "image/photo/"+oriuser.getPhoto());
		txtVisitName.setText(oriuser.getUsername());
		if(oriuser.getBirthday()!=null){
			txtVisitAge.setText(oriuser.getBirthday());
		}else
		{
			txtVisitAge.setText("该用户未填写");
		}
		if(oriuser.getSignature()!=null)
		{
			txtVisitSignature.setText(oriuser.getSignature());
		}else
		{
			txtVisitSignature.setText("该用户未填写");
		}
		
		if(oriuser.getScore()>=0)
		{
			int degree=oriuser.getScore()/5;
			txtVisitDegree.setText(""+degree+"级");
		}
		if(oriuser.getLocation()!=null)
		{
			txtVisitLocation.setText(oriuser.getLocation());
		}else
		{
			txtVisitLocation.setText("该用户未填写");
		}
        //Bitmap bmp = imgVisitPhoto.getDrawingCache();
        //bmp = blur(bmp);
        //BitmapDrawable bd= new BitmapDrawable(bmp); 
       Log.i(TAG,"huanbeijing"+imgVisitPhoto.getResources());
		//visitorLayout.setBackgroundDrawable(bd);
		//接收数据显示查看用户的相关信息
		//Log.i(TAG,"huanbeijing"+bd);
		//imgVisitPhoto.setBackgroundDrawable(bd);
		//Log.i(TAG,"huanbeijing"+imgVisitPhoto);
       	//imgVisitPhoto.set
       //需要传递一个图片的路径或者是资源ID
		initAvatarBackground();
		txtBackVisit.setOnClickListener(new ClickListenerImpl());
		txtVisitLike.setOnClickListener(new ClickListenerImpl());
		txtVisitPost.setOnClickListener(new ClickListenerImpl());
		/*bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAvatarBackground();
            }

        });*/
		
	}
	private class ClickListenerImpl implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.txtBackVisit:
				//返回上一列表
				finish();
				//Toast.makeText(getApplicationContext(), "返回上一列表", Toast.LENGTH_SHORT).show();
				break;
			case R.id.txtVisitLike:
				//将当前两用户成为朋友
				if(txtVisitLike.getText().toString().equals("关注"))
				{
					txtVisitLike.setText("已关注");				
					Toast.makeText(getApplicationContext(), "添加关注成功", Toast.LENGTH_SHORT).show();
					new Thread(new addFocustask(false)).start();
				}else{
					txtVisitLike.setText("关注");
					Toast.makeText(getApplicationContext(), "取消关注成功", Toast.LENGTH_SHORT).show();
					new Thread(new addFocustask(true)).start();
				}
				
				break;
			
			case R.id.txtVisitPost:
				//查看该用户的发布内容
				//可以直接加载列表，支持下拉
				Intent intent=new Intent(VisiterActivity.this,ContentListActivity.class);
				intent.putExtra("operationType", MyApp.OPERATION_TYPE_USER_CONTENT);
				intent.putExtra("user", oriuser);
				startActivity(intent);
				//因为可能回来,所以不能finish()
				//Toast.makeText(getApplicationContext(), "查看该用户的发布内容", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			
		}
		  
	}
	
	@SuppressWarnings("deprecation")
	private void initAvatarBackground() {
		//Drawable drawable = getResources().getDrawable(imgVisitPhoto.getId());
        //Drawable drawable = getResources().getDrawable(R.drawable.default_user_photo);
		Drawable drawable = new BitmapDrawable(userphotobm);
      
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
	
	private class addFocustask implements Runnable
	{

		private boolean isFocus;
		public addFocustask(boolean isFocus)
		{
			this.isFocus=isFocus;
			//还没关注要关注传来就是false
			//要关注现在要取关就是true
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String url=MyApp.url+"VowTest1/addFocusServlet";
			HttpClient client=new DefaultHttpClient();
			HttpPost request=new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();			
			params.add(new BasicNameValuePair("user",Utils.ToUserGson(myapp.getUser())));
			params.add(new BasicNameValuePair("oriuser",Utils.ToUserGson(oriuser)));
			if(isFocus)//取关
			{
				params.add(new BasicNameValuePair("isFocus","true"));
			}else//加关注
			{
				params.add(new BasicNameValuePair("isFocus","false"));
			}
			try {
				request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));			
				
				HttpResponse response=client.execute(request);
				if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
				{
					Log.d("addfocustag","添加关注成功");
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}

