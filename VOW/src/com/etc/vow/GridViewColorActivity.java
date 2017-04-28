package com.etc.vow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.vow.sketchData.GridImageViewAdapter;
import com.vow.utils.SketchPadView;

public class GridViewColorActivity extends Activity {

	GridView my_gridview ;
	GridImageViewAdapter myImageViewAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_sketchcolor);
		android.view.WindowManager.LayoutParams layoutParams=this.getWindow().getAttributes();
		layoutParams.width=500;
		layoutParams.height=360;
		//layoutParams.alpha = 0.5f;
		this.getWindow().setAttributes(layoutParams);

	my_gridview = (GridView) findViewById(R.id.grid);
	/* 新建自定义的 IRmageAdapter*/
    myImageViewAdapter=new GridImageViewAdapter(GridViewColorActivity.this);
	/* GridView 对象设置 ImageAdapter*/
	 my_gridview.setAdapter( myImageViewAdapter );
	/*GridView 添加图片 Items 点击事件监听*/
	 my_gridview .setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					int color = 0;
                	switch (arg2) {
        			case 0:
        				color= Color.RED;
        				break;
        			case 1:
        				color=Color.BLUE;
        				break;
        			case 2:
        				color =Color.BLACK;
 				        break;
        			case 3:
        				color = 0xff458B00;
        				break;
  	                case 4:
    				    color = 0xff8B0000 ;
        				break;
        			case 5:
        				color = 0xff7CFC00;
        				break;
        			case 6:
        				color = 0xffFF00FF;
        				break;
        			case 7:
        				color = 0xffEE1289;
        				break;
        			case 8:
        				color = 0xffB23AEE;
        				break;
        			case 9:
        				color = 0xff00FFFF;
        				break;
        			case 10:
        				color = 0xff27408B;
        				break;
        			case 11:
        				color = 0xffFF8247;
        				break;
        			case 12:
        				color = 0xffFFFF00;
        				break;
        			case 13:
        				color = 0xff458B74;
        				break;
        			case 14:
        				color = 0xff8B7500;
        				break;
        			case 15:
        				color = 0xff8C8C8C;
        				break;
        			} 
                	//设置当前的颜色	                    	
                	SketchPadView.setStrokeColor(color);                   	
                	//finish();
					Intent intent = new Intent(GridViewColorActivity.this,SketchActivity.class);
					startActivity(intent);
				}
	 	});
	}

}
