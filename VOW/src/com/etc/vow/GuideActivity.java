package com.etc.vow;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vow.login.LoginActivity;
//第一次安装后的引导界面
public class GuideActivity extends Activity {

	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	private ViewPager viewPager;//页卡内容
	private List<View> viewList;// Tab页面列表
	private View view1,view2,view3,view4;//各个页卡
	//private Button btnEnter;
	
	// 底部小点图片  
    private ImageView[] dots; 
    
 // 记录当前选中位置  
    private int currentIndex;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		// 初始化页面
		InitViewPager();
				
		// 初始化底部小点
		initDots();
	}

	private void initDots() {  
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);  
  
        dots = new ImageView[viewList.size()];  
  
        // 循环取得小点图片  
        for (int i = 0; i < viewList.size(); i++) {  
            dots[i] = (ImageView) ll.getChildAt(i);  
            dots[i].setEnabled(true);// 都设为灰色  
        }  
  
        currentIndex = 0;  
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态  
    }  

	private void InitViewPager() {

		viewPager=(ViewPager) findViewById(R.id.vPager);
		LayoutInflater inflater=getLayoutInflater();
		view1=inflater.inflate(R.layout.lay1, null);
		view2=inflater.inflate(R.layout.lay2, null);
		view3=inflater.inflate(R.layout.lay3, null);
		view4=inflater.inflate(R.layout.lay4, null);
		
		viewList=new ArrayList<View>();// 将要分页显示的View装入数组中
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);
		
		PagerAdapter pagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return viewList.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				container.removeView(viewList.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// TODO Auto-generated method stub
				container.addView(viewList.get(position));

				return viewList.get(position);
			}
		};
		
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
	}
	
	//--------------------------------------------------------------------
	       public void enter(View v){
				// 设置已经引导
				setGuided();
				goHome();
							
	       }
		
	//----------------------------------------------------------------	
			private void goHome() {
				// 跳转
				Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
			
			
			
			private void setGuided() {//设置已经引导过了，下次启动不用再次引导
				SharedPreferences preferences = getSharedPreferences(
						SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				// 存入数据
				editor.putBoolean("isFirstIn", false);
				// 提交修改
				editor.commit();
			}
	
			
			 private class MyOnPageChangeListener implements OnPageChangeListener{
			    	// 当滑动状态改变时调用
					public void onPageScrollStateChanged(int arg0) {			
						
					}

					// 当当前页面被滑动时调用
					public void onPageScrolled(int arg0, float arg1, int arg2) {
									
					}

					// 当新的页面被选中时调用
					public void onPageSelected(int arg0) {
						// 设置底部小点选中状态
						setCurrentDot(arg0);
					}   
					
					
					 private void setCurrentDot(int position) {  
					        if (position < 0 || position > viewList.size() - 1  
					                || currentIndex == position) {  
					            return;  
					        }  
					  
					        dots[position].setEnabled(false);  
					        dots[currentIndex].setEnabled(true);  
					  
					        currentIndex = position;  
					    }  
			    }

}
