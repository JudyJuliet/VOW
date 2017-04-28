package com.etc.vow;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import com.etc.vow.R;
import com.vow.fragment.ContentFragment;
import com.vow.fragment.MenuFragment;
//APP首页
public class ContainerActivity extends Activity{
	private SlidingPaneLayout slidingPane;
	private MenuFragment menuFragment;
	private ContentFragment contentFragment;
	private int maxMargin = 0;
	private DisplayMetrics mMetrics = new DisplayMetrics();

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
		setContentView(R.layout.activity_container);
		slidingPane = (SlidingPaneLayout) findViewById(R.id.slidingpanellayout);
		menuFragment = new MenuFragment();
		contentFragment = new ContentFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.slidingpane_menu, menuFragment);
		transaction.replace(R.id.slidingpane_content, contentFragment);
		transaction.commit();
		
		maxMargin = mMetrics.heightPixels/10;
		slidingPane.setPanelSlideListener(new PanelSlideListener() {
			
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
			public void onPanelSlide(View view, float slideOffset) {
				int contentMargin = (int) (slideOffset*maxMargin);
				FrameLayout.LayoutParams contentParams = contentFragment.getCurrentParams();
				contentParams.setMargins(0, contentMargin, 0, contentMargin);
				contentFragment.setCurrentParams(contentParams);
				
				float scale = 1 - ((1 - slideOffset) * maxMargin * 2)
						/ (float) mMetrics.heightPixels;
				View currentView = menuFragment.getCurrentView();
				currentView.setScaleX(scale);// 设置缩放的基准点
				currentView.setScaleY(scale);// 设置缩放的基准点
				currentView.setPivotX(0);// 设置缩放和�?择的�?
				currentView.setPivotY(mMetrics.heightPixels / 2);
				currentView.setTranslationX(-100 + slideOffset * 100);
				currentView.setAlpha(slideOffset);
			}
			
			@Override
			public void onPanelOpened(View arg0) {
				
			}
			
			@Override
			public void onPanelClosed(View arg0) {
				
			}
		});
	}
	
	public SlidingPaneLayout getSlidingPane(){
		return slidingPane;
	}
}
