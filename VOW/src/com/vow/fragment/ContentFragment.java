package com.vow.fragment;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.etc.vow.R;
import com.etc.vow.SelfInfoActivity;
import com.etc.vow.UpdateUserInfoActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vow.adapter.ContentListAdapter;
import com.vow.app.MyApp;
import com.vow.customerView.MoreWindowView;
import com.vow.customerView.VowScrollView;
import com.vow.entity.AllOfContent;
import com.vow.task.loadContent;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi")
public class ContentFragment  extends Fragment implements OnClickListener, OnGestureListener, OnTouchListener {
	private View currentView;
	private TextView date_TextView;
	
	private ImageView tabHome, tabPost, tabPerson;
	
	private ViewFlipper viewFlipper;
	private boolean showNext = true;
	private boolean isRun = true;
	private int currentPage = 0;
	private final int SHOW_NEXT = 0011;
	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;
	private GestureDetector mGestureDetector;
	private int index = 1;
	private MoreWindowView mMoreWindow;
	
	private FragmentManager fragmentManager;
	
	private HomeFragment homeFragment;
	private ListView listview;
	private ContentListAdapter adapter;
	private List<AllOfContent> list=new ArrayList<AllOfContent>();
	private MyApp myapp;
	private Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch(msg.what)
			{
			case 0:
				list=(List<AllOfContent>) msg.obj;
				adapter.notifyDataSetChanged();
				break;
			}
			
		};
	};
	
	
	public void setCurrentParams(FrameLayout.LayoutParams layoutParams) {
		currentView.setLayoutParams(layoutParams);
	}

	public FrameLayout.LayoutParams getCurrentParams() {
		return (LayoutParams) currentView.getLayoutParams();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.layout_content_fragment,
				container, false);
		
		fragmentManager = getFragmentManager();
		viewFlipper = (ViewFlipper)currentView.findViewById(R.id.mViewFliper_vf);
		date_TextView = (TextView) currentView.findViewById(R.id.home_date_tv);
		date_TextView.setTextColor(Color.rgb(122, 223, 184));
        date_TextView.setText(Calendar.getInstance().getTime()+"");
        listview=(ListView) currentView.findViewById(R.id.home_content_listview);
        myapp=(MyApp) getActivity().getApplication();
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<AllOfContent>>(){}.getType();
		list=gson.fromJson(MyApp.listgson, type);
       	adapter=new ContentListAdapter(getActivity().getApplicationContext(), list,myapp.getUser());
		listview.setAdapter(adapter);
//		loadContent task=new loadContent(getActivity(),myapp.getUser(),handler,0,10);
//		new Thread(task).start();
		
        mGestureDetector = new GestureDetector(this);
        viewFlipper.setOnTouchListener(this);
        viewFlipper.setLongClickable(true);
        viewFlipper.setOnClickListener(clickListener);
        displayRatio_selelct(currentPage);
        
        
        VowScrollView myScrollView = (VowScrollView) currentView.findViewById(R.id.viewflipper_scrollview);
        myScrollView.setOnTouchListener(onTouchListener);
        myScrollView.setGestureDetector(mGestureDetector);
        
        thread.start();
		
		init();
		return currentView;
	}
	
	/**
	 * ��ʼ���ؼ�
	 */
	@SuppressLint("NewApi") 
	private void init() {
		tabHome = (ImageView) currentView.findViewById(R.id.tab_home);
		tabPost = (ImageView) currentView.findViewById(R.id.tab_post);
		tabPerson = (ImageView) currentView.findViewById(R.id.tab_person);
		tabHome.setOnClickListener(this);
		tabPost.setOnClickListener(this);
		tabPerson.setOnClickListener(this);
		
		tabHome.setBackgroundResource(R.drawable.tab_home_selected);
		tabPost.setBackgroundResource(R.drawable.tab_post_normal);
		tabPerson.setBackgroundResource(R.drawable.tab_person_normal);
		//
		setDefaultFragment();
	}
	
	/**
	 * ����Ĭ����ʾ��fragment
	 */
	@SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setDefaultFragment() {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		homeFragment = new HomeFragment();
		transaction.replace(R.id.content_layout, homeFragment);
		transaction.commit();
	}
	
	/**
	 *�л�fragment
	 * @param newFragment
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void replaceFragment(Fragment newFragment) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (!newFragment.isAdded()) {
			transaction.replace(R.id.content_layout, newFragment);
			transaction.commit();
		} else {
			transaction.show(newFragment);
		}
	}

	/**
	 * �ı����󿨵�ѡ��״̬
	 */
	private void clearStatus() {
		if (index == 1) {
			tabHome.setBackgroundResource(R.drawable.tab_home_normal);
		} else if (index == 2) {
			tabPost.setBackgroundResource(R.drawable.tab_post_normal);
		} else if (index == 3) {
			tabPerson.setBackgroundResource(R.drawable.tab_person_normal);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		clearStatus();
		switch (v.getId()) {
		case R.id.tab_home:
			if (homeFragment == null) {
				homeFragment = new HomeFragment();
			}
			replaceFragment(homeFragment);
			tabHome.setBackgroundResource(R.drawable.tab_home_selected);
			index = 1;
			break;
		case R.id.tab_post:
			tabPost.setBackgroundResource(R.drawable.tab_post_selected);
			index = 2;
			//跳转到发布页面
			showMoreWindow(v);
			break;
		case R.id.tab_person:
			tabPerson.setBackgroundResource(R.drawable.tab_person_selected);
			index = 3;
			//跳转到个人主页
			Intent intentPerson = new Intent(getActivity(),SelfInfoActivity.class);
			startActivity(intentPerson);
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.e("view", "onFling");
		if (e1.getX() - e2.getX()> FLING_MIN_DISTANCE  
                && Math.abs(velocityX) > FLING_MIN_VELOCITY ) {
			Log.e("fling", "left");
			showNextView();
			showNext = true;
//			return true;
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE  
                && Math.abs(velocityX) > FLING_MIN_VELOCITY){
			Log.e("fling", "right");
			showPreviousView();
			showNext = false;
//			return true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Toast.makeText(getActivity(), "推荐", Toast.LENGTH_LONG).show();
		}
	};
    private OnTouchListener onTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return mGestureDetector.onTouchEvent(event);
		}
	};
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case SHOW_NEXT:
				if (showNext) {
					showNextView();
				} else {
					showPreviousView();
				}
				break;

			default:
				break;
			}
		}
    	
    };
    
    Thread thread = new Thread(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(isRun){
				try {
					Thread.sleep(1000 * 8);
					Message msg = new Message();
					msg.what = SHOW_NEXT;
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	};
	
	private void showNextView(){

		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_out));		
		viewFlipper.showNext();
		currentPage ++;
		if (currentPage == viewFlipper.getChildCount()) {
			displayRatio_normal(currentPage - 1);
			currentPage = 0;
			displayRatio_selelct(currentPage);
		} else {
			displayRatio_selelct(currentPage);
			displayRatio_normal(currentPage - 1);
		}
		Log.e("currentPage", currentPage + "");		
		
	}
	
	private void showPreviousView(){
		displayRatio_selelct(currentPage);
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_out));
		viewFlipper.showPrevious();
		currentPage --;
		if (currentPage == -1) {
			displayRatio_normal(currentPage + 1);
			currentPage = viewFlipper.getChildCount() - 1;
			displayRatio_selelct(currentPage);
		} else {
			displayRatio_selelct(currentPage);
			displayRatio_normal(currentPage + 1);
		}
		Log.e("currentPage", currentPage + "");		
	}
	private void displayRatio_selelct(int id){
		int[] ratioId = {R.id.home_ratio_img_04, R.id.home_ratio_img_03, R.id.home_ratio_img_02, R.id.home_ratio_img_01};
		ImageView img = (ImageView)currentView.findViewById(ratioId[id]);
		img.setSelected(true);
	}
	private void displayRatio_normal(int id){
		int[] ratioId = {R.id.home_ratio_img_04, R.id.home_ratio_img_03, R.id.home_ratio_img_02, R.id.home_ratio_img_01};
		ImageView img = (ImageView)currentView.findViewById(ratioId[id]);
		img.setSelected(false);
	}
	private void showMoreWindow(View view) {
		if (null == mMoreWindow) {
			mMoreWindow = new MoreWindowView(getActivity());
			mMoreWindow.init();
		}

		mMoreWindow.showMoreWindow(view,20);
	}


}
