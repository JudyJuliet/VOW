package com.etc.vow;

import java.util.ArrayList;
import java.util.List;

import com.vow.utils.loadPhoto;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PicturePagerActivity extends Activity {

	private ViewPager viewPager;//页卡内容
	private List<View> viewList;// Tab页面列表
	private List<String> filenamelist=new ArrayList<String>();
	private TextView exit_pic_pager;
	
	// 底部小点图片  
    private ImageView[] dots;
    
 // 记录当前选中位置  
    private int currentIndex;  
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
				
		setContentView(R.layout.activity_picture_pager);

		        // 初始化页面
				InitViewPager();
						
				// 初始化底部小点
				initDots();
			}

			private void initDots() {  
		   
		        //小点图片
				LinearLayout dotlayout = (LinearLayout) findViewById(R.id.picture_pager_layout);  				 
				dots=new ImageView[filenamelist.size()];
			
		        for (int i = 0; i <filenamelist.size(); i++) {  
		            dots[i] =(ImageView) dotlayout.getChildAt(i); 
		            dots[i].setVisibility(View.VISIBLE);
		            dots[i].setEnabled(true);// 都设为灰色  
		        }  
		  
		        //currentIndex = 0;  
		        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态  
		    }  

			private void InitViewPager() {

				//从上一个活动得到图片文件名列表
				filenamelist=getIntent().getStringArrayListExtra("filenamelist");
				currentIndex=getIntent().getIntExtra("imageindex", 0);
				viewPager=(ViewPager) findViewById(R.id.vPager);	
				exit_pic_pager=(TextView)findViewById(R.id.exit_pic_pager);
				
				exit_pic_pager.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finish();
					}});
				
				LayoutInflater inflater=getLayoutInflater();				
				viewList=new ArrayList<View>();// 将要分页显示的View装入数组中

				
			
				
				for(int i=0;i<filenamelist.size();i++){
				View view=inflater.inflate(R.layout.pic1,null);
				ImageView iv=(ImageView)view.findViewById(R.id.pagerpic);
					String filename=filenamelist.get(i);
//					iv.setImageResource(R.drawable.custom_progress_bar);//改成该有的图片
					loadPhoto.loadImage(iv, "image/contentImage/"+filename);
					viewList.add(view);//每个图片对应一个页卡				
				}
				

			//-----------------------------------------------------------------------------	
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
				//viewPager.setCurrentItem(0);
				viewPager.setCurrentItem(currentIndex);
				viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
				
			}

			//----------------------------------------------------------------	
				
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
