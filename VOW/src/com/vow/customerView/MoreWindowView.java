package com.vow.customerView;

import java.util.Calendar;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.etc.vow.NewContentActivity;
import com.etc.vow.R;
import com.etc.vow.RecordActivity;
import com.etc.vow.SketchActivity;
import com.vow.postAnim.KickBackAnimator;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class MoreWindowView extends PopupWindow implements OnClickListener {
	
	private String TAG = MoreWindowView.class.getSimpleName();
	Activity mContext;
	private int mWidth;
	private int mHeight;
	private int statusBarHeight ;
	private Bitmap mBitmap= null;
	private Bitmap overlay = null;
	
	private Button btn_audio;
	private Button btn_picture;
	private Button btn_string;
	
	private TextView txt_day;
	private TextView txt_week;
	private TextView txt_monthyear;
	
	private String week;
	
	private Handler mHandler = new Handler();
	
	private Intent intent;

	
	
	public MoreWindowView(Activity context) {
		mContext = context;
	}
	
	public void init() {
		Rect frame = new Rect();
		mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;
		DisplayMetrics metrics = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		mWidth = metrics.widthPixels;
		mHeight = metrics.heightPixels;
		
		setWidth(mWidth);
		setHeight(mHeight);
	}
	
	//背景模糊化
	private Bitmap blur() {
		if (null != overlay) {
			return overlay;
		}
		long startMs = System.currentTimeMillis();
		//截屏
		View view = mContext.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache(true);
		mBitmap = view.getDrawingCache();
		
		float scaleFactor = 8;//ͼƬ���ű�����
		float radius = 10;//ģ���̶�
		int width = mBitmap.getWidth();
		int height =  mBitmap.getHeight();

		overlay = Bitmap.createBitmap((int) (width / scaleFactor),(int) (height / scaleFactor),Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(mBitmap, 0, 0, paint);

		overlay = com.vow.postAnim.FastBlur.doBlur(overlay, (int) radius, true);
		Log.i(TAG, "blur time is:"+(System.currentTimeMillis() - startMs));
		return overlay;
	}
	
	private Animation showAnimation1(final View view,int fromY ,int toY) {
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation go = new TranslateAnimation(0, 0, fromY, toY);
		go.setDuration(300);
		TranslateAnimation go1 = new TranslateAnimation(0, 0, -10, 2);
		go1.setDuration(100);
		go1.setStartOffset(250);
		set.addAnimation(go1);
		set.addAnimation(go);

		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {

			}

		});
		return set;
	}
	public void showMoreWindow(View anchor,int bottomMargin) {
		
		final RelativeLayout layout = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.activity_post, null);
		
		setContentView(layout);
		
		TextView close= (TextView)layout.findViewById(R.id.txtCancelPost);
		//更改日期
		btn_audio=(Button)layout.findViewById(R.id.btn_audio);
		btn_picture=(Button)layout.findViewById(R.id.btn_picture);
		btn_string=(Button)layout.findViewById(R.id.btn_string);
      
        txt_day=(TextView)layout.findViewById(R.id.txt_day);
        txt_week=(TextView)layout.findViewById(R.id.txt_week);
        txt_monthyear=(TextView)layout.findViewById(R.id.txt_monthyear);
        //添加事件响应函数
        btn_audio.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_string.setOnClickListener(this);
        
        getDate();  
		android.widget.RelativeLayout.LayoutParams params =new android.widget.RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.bottomMargin = bottomMargin;
		params.addRule(RelativeLayout.BELOW, R.id.btn_string);//文字
		params.addRule(RelativeLayout.RIGHT_OF, R.id.btn_audio);//语音
		params.topMargin = 20;
		params.leftMargin = 18;
		//close.setLayoutParams(params);
		
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShowing()) {
					closeAnimation(layout);
				}
			}

		});
		
		showAnimation(layout);
		setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
		setOutsideTouchable(true);
		setFocusable(true);
		showAtLocation(anchor, Gravity.BOTTOM, 0, statusBarHeight);
	}

	private void showAnimation(ViewGroup layout){
		for(int i=0;i<layout.getChildCount();i++){
			final View child = layout.getChildAt(i);
			if(child.getId() == R.id.txtCancelPost){
				continue;
			}
			child.setOnClickListener(this);
			child.setVisibility(View.INVISIBLE);
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					child.setVisibility(View.VISIBLE);
					ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
					fadeAnim.setDuration(300);
					KickBackAnimator kickAnimator = new KickBackAnimator();
					kickAnimator.setDuration(150);
					fadeAnim.setEvaluator(kickAnimator);
					fadeAnim.start();
				}
			}, i * 50);
		}
		
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") 
	private void closeAnimation(ViewGroup layout){
		for(int i=0;i<layout.getChildCount();i++){
			final View child = layout.getChildAt(i);
			if(child.getId() == R.id.txtCancelPost){
				continue;
			}
			child.setOnClickListener(this);
			mHandler.postDelayed(new Runnable() {
				
				@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") @Override
				public void run() {
					child.setVisibility(View.VISIBLE);
					ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, 600);
					fadeAnim.setDuration(200);
					KickBackAnimator kickAnimator = new KickBackAnimator();
					kickAnimator.setDuration(100);
					fadeAnim.setEvaluator(kickAnimator);
					fadeAnim.start();
					fadeAnim.addListener(new AnimatorListener() {
						
						@Override
						public void onAnimationStart(Animator animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationRepeat(Animator animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd(Animator animation) {
							child.setVisibility(View.INVISIBLE);
						}
						
						@Override
						public void onAnimationCancel(Animator animation) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			}, (layout.getChildCount()-i-1) * 30);
			
			if(child.getId() == com.etc.vow.R.id.txt_day){
				//文字
				mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						dismiss();
					}
				}, (layout.getChildCount()-i) * 30 + 80);
			}
		}
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_audio:
			//跳转到录音界面
			intent = new Intent(mContext,RecordActivity.class);
			intent.putExtra("from", "MoreWindowView");
			//mintent.setClass(mContext,"***");
			mContext.startActivity(intent);
			//mContext.finish();生命周期问题
			break;
		case R.id.btn_string:
			intent =new Intent(mContext,NewContentActivity.class);
			mContext.startActivity(intent);			
			//发表文字页
			break;
		case R.id.btn_picture:
			//发表涂鸦页
			intent = new Intent(mContext,SketchActivity.class);
			//mintent.setClass(mContext,"***");
			mContext.startActivity(intent);
			//mContext.finish();
			break;
		default:
			break;
		}
	}
	public void getDate(){
		 Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR);   
	        int month = c.get(Calendar.MONTH)+1;
	        if(month==13)
	        	month=1;
	        int date = c.get(Calendar.DATE);
	        
	        txt_day.setText(date+"");
	        txt_monthyear.setText(month+"/"+year);
	        
	        Calendar now = Calendar.getInstance();
		      //一周第一天是否为星期天
		    boolean isFirstSunday = (now.getFirstDayOfWeek() == Calendar.SUNDAY);
		      //获取周几
		      int weekDay = now.get(Calendar.DAY_OF_WEEK);
		      //若一周第一天为星期天，则-1
		      if(isFirstSunday){
		        weekDay = weekDay - 1;
		        if(weekDay == 0){
		          weekDay = 7;
		        }
		      }
		     switch(weekDay){
		     case 1:
		    	 week="星期一";
		    	 break;
		     case 2:
		    	 week="星期二";
		    	 break;
		     case 3:
		    	 week="星期三";
		    	 break;
		     case 4:
		    	 week="星期四";
		    	 break;
		     case 5:
		    	 week="星期五";
		    	 break;
		     case 6:
		    	 week="星期六";
		    	 break;
		     case 7:
		    	 week="星期日";
		    	 break;
		     }
		      txt_week.setText(week);
	}

}
