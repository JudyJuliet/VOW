package com.etc.vow;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;

import com.example.autolistview.widget.AutoListView;
import com.example.autolistview.widget.AutoListView.OnLoadListener;
import com.example.autolistview.widget.AutoListView.OnRefreshListener;
import com.vow.adapter.UserListAdapter;
import com.vow.app.MyApp;
import com.vow.entity.User;
import com.vow.task.loadBitmap;
import com.vow.task.loadUser;


	//用户关注人列表界面
	public class ListViewActivity extends Activity implements OnRefreshListener,
	OnLoadListener{

		private int index;//request类型
		private String keyname="";
		
		private Drawable mIconSearchDefault; // 搜索文本框默认图标
		private Drawable mIconSearchClear; // 搜索文本框清除文本内容图标
		private EditText mSearchView;//搜索框
		
		
		private AutoListView lstv;
		private UserListAdapter adapter;
		private MyApp myapp;
		private List<User> list = new ArrayList<User>();
		//加载图片任务
		private loadBitmap loadBitmap_task;
		private Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				@SuppressWarnings("unchecked")
				List<User> result = (List<User>) msg.obj;
				switch (msg.what) {
				case AutoListView.REFRESH:
					lstv.onRefreshComplete();
					list.clear();
					list.addAll(result);
					break;
				case AutoListView.LOAD:
					lstv.onLoadComplete();
					list.addAll(result);
					break;
				case 100://模糊查询
					list.clear();
					
					list.addAll(result);
					break;
				}
				lstv.setResultSize(result.size());
				adapter.notifyDataSetChanged();
			};
		};
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_list_view);
			
			 final Resources res = getResources();
		      mIconSearchDefault = res.getDrawable(R.drawable.txt_search_default);
		      mIconSearchClear = res.getDrawable(R.drawable.txt_search_clear);
			mSearchView = (EditText) findViewById(R.id.txtSearch);
		      mSearchView.addTextChangedListener(tbxSearch_TextChanged);
		      mSearchView.setOnTouchListener(txtSearch_OnTouch);
			
			index=getIntent().getIntExtra("index",0);
			if(index==0){
				keyname=getIntent().getStringExtra("keyword");
				//mSearchView.setText(keyname);
			}	      
		      
		      
			loadBitmap_task=new loadBitmap();
			
			lstv = (AutoListView) findViewById(R.id.lstv);
			adapter = new UserListAdapter(this, list,loadBitmap_task);
			lstv.setAdapter(adapter);
			lstv.setOnRefreshListener(this);
			lstv.setOnLoadListener(this);
			lstv.setOnItemClickListener(new OnItemClickListenerImpl() );
			initData();
		}

		private void initData() {
			loadData(AutoListView.REFRESH);
		}
		

		private void loadData(final int what) {
			myapp=(MyApp) getApplication();
			User user=myapp.getUser();
			loadUser task=new loadUser(this,user,keyname,handler,what,index);
			new Thread(task).start();		
		}
		@Override
		public void onLoad() {
			// TODO Auto-generated method stub
			loadData(AutoListView.LOAD);
		}

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			loadData(AutoListView.REFRESH);
		}
		
		
	//////////////////////////////////////////////
		  private TextWatcher tbxSearch_TextChanged = new TextWatcher() {

		        //缓存上一次文本框内是否为空
		        private boolean isnull = true;

		        @Override
		        public void afterTextChanged(Editable s) {
		            if (TextUtils.isEmpty(s)) {
		                if (!isnull) {
		                    mSearchView.setCompoundDrawablesWithIntrinsicBounds(null,
		                            null, mIconSearchDefault, null);	                    
		                    isnull = true;
		                    keyname="";
		                    new Thread(new loadUser(getApplicationContext(),myapp.getUser(),keyname,handler,100,index)).start();

		                }
		            } else {
		                if (isnull) {
		                    mSearchView.setCompoundDrawablesWithIntrinsicBounds(null,
		                            null, mIconSearchClear, null);	                    
		                    isnull = false;
		                    //////
		                    
		                    keyname=mSearchView.getText().toString();
		                    new Thread(new loadUser(getApplicationContext(),myapp.getUser(),keyname,handler,100,index)).start();
		                   //////
		                }
		            }
		        }

		        @Override
		        public void beforeTextChanged(CharSequence s, int start, int count,
		                int after) {
		        }

		        
		         // 随着文本框内容改变动态改变列表内容        
		        @Override
		        public void onTextChanged(CharSequence s, int start, int before,
		                int count) {
		            
		        }
		    };
		    
		    	    
		    private OnTouchListener txtSearch_OnTouch = new OnTouchListener() {
		        @Override
		        public boolean onTouch(View v, MotionEvent event) {
		            switch (event.getAction()) {
		            case MotionEvent.ACTION_UP:
		                int curX = (int) event.getX();
		                if (curX > v.getWidth() - 50
		                        && !TextUtils.isEmpty(mSearchView.getText())) {
		                    mSearchView.setText("");
		                    int cacheInputType = mSearchView.getInputType();// backup  the input type
		                    mSearchView.setInputType(InputType.TYPE_NULL);// disable soft input
		                    mSearchView.onTouchEvent(event);// call native handler
		                    mSearchView.setInputType(cacheInputType);// restore input  type
		                    return true;// consume touch even
		                }
		                break;
		            }
		            return false;
		        }
		    };

		    private class OnItemClickListenerImpl implements OnItemClickListener
		    {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(ListViewActivity.this,VisiterActivity.class);
					intent.putExtra("user", list.get(position-1));
					startActivity(intent);
				}
		    	
		    }
	}

