package com.etc.vow;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vow.app.MyApp;
import com.vow.utils.XCFlowLayout;

public class HomeSearchActivity extends Activity {

	private Spinner search_spn;
	private EditText seach_edt;
	private XCFlowLayout search_tagFlowLayout;//流式布局 
	private ImageView home_search_img;//搜索按钮
	private int searchKind=0;//搜索用户是1,搜索内容是0
	private String keyword="";
	private String[] searchkind={"内容","用户"};
	private List<String>mNames=new ArrayList<String>();//全部标签
	private MarginLayoutParams lp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_homesearch);
		
		mNames.add("自然");mNames.add("纯音乐");mNames.add("K歌之王");
		mNames.add("生活日常");mNames.add("心灵鸡汤");mNames.add("天下论坛");
		
		search_spn=(Spinner) findViewById(R.id.home_search_spin);
		seach_edt=(EditText) findViewById(R.id.home_search_edt);
		search_tagFlowLayout=(XCFlowLayout) findViewById(R.id.home_search_flowlayout);
		home_search_img=(ImageView) findViewById(R.id.home_search_img);
		
		 lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
	     lp.leftMargin = 5;  
	     lp.rightMargin = 5;  
	     lp.topMargin = 5;  
	     lp.bottomMargin = 5;  
		
		//向流式布局添加标签
		 for(int i = 0; i < mNames.size(); i++){ //向流式布局动态添加TextView组件
	            final TextView view = new TextView(HomeSearchActivity.this);  
	            view.setText(mNames.get(i)); 
	            view.setTextColor(Color.WHITE); 
	            view.setBackgroundResource(R.drawable.textview_bg);

	            view.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {//点击事件选择了该标签
						// TODO Auto-generated method stub
						String tag_text=view.getText().toString();							
					    view.setBackgroundResource(R.drawable.textview_pressbg);						
					    seach_edt.setText(tag_text);
					    Intent intent=new Intent(HomeSearchActivity.this,ContentListActivity.class);
					    intent.putExtra("tag",tag_text);
					    intent.putExtra("operationType", MyApp.OPERATION_TYPE_TAG_CONTENT);
					    startActivity(intent);
					}});
	                    
	            search_tagFlowLayout.addView(view,lp);  
	        }
	//-----------------------------------------------------------------------
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchkind);
		//绑定适配器
		search_spn.setAdapter(adapter);
		//绑定监听器
		search_spn.setOnItemSelectedListener(new OnItemSelectedListenerImpl());
		home_search_img.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(searchKind==0)//查找内容
				{
					keyword=seach_edt.getText().toString();
					if(keyword.equals(""))
					{
						Toast.makeText(getApplicationContext(), "您还没有输入!", Toast.LENGTH_SHORT).show();
					}else
					{
						 Intent intent=new Intent(HomeSearchActivity.this,ContentListActivity.class);
						 intent.putExtra("description",keyword);
						 intent.putExtra("operationType",MyApp.OPERATION_TYPE_KEY_CONTENT);
						 startActivity(intent);
					}
				}//查找用户
				{
					keyword=seach_edt.getText().toString();
					if(keyword.equals(""))
					{
						Toast.makeText(getApplicationContext(), "您还没有输入!", Toast.LENGTH_SHORT).show();
					}else
					{
						Intent intent=new Intent(HomeSearchActivity.this,ListViewActivity.class);
						intent.putExtra("keyword", keyword);
						intent.putExtra("index", 0);
						startActivity(intent);
						//finish();
					}
				}
			}
			
		});
	}

	private class OnItemSelectedListenerImpl implements OnItemSelectedListener
	{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			searchKind=position;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}		
		
	}
	
	

}
