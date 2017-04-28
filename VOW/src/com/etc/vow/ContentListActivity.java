package com.etc.vow;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.autolistview.widget.AutoListView;
import com.example.autolistview.widget.AutoListView.OnLoadListener;
import com.example.autolistview.widget.AutoListView.OnRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.vow.adapter.ContentListAdapter;
import com.vow.app.MyApp;
import com.vow.entity.AllOfContent;
import com.vow.entity.Upload_content;
import com.vow.entity.User;
import com.vow.task.like_favorite_download_repost_commentTask;
import com.vow.task.loadContent;
import com.vow.utils.Utils;

public class ContentListActivity extends Activity implements OnRefreshListener,OnLoadListener{

	private AutoListView lstv;
	private ContentListAdapter adapter;
	private MyApp myapp;
	private User user;
	private String tagname;
	private String keyword;
	private List<AllOfContent> list = new ArrayList<AllOfContent>();
	//删除对话框
	private Dialog dlgDelete;
	private TextView content_list_back_textbutton;
	private AllOfContent aoc;
	
	//！！！！！！！！！！！！！！！！！！！！！！！！！
	private int operationType;
	private int position;
	//从上一个activity传来操作类型，这里用OPERATION_TYPE_ORIGINAL_CONTENT检测
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			@SuppressWarnings("unchecked")
			List<AllOfContent> ori_result = (List<AllOfContent>) msg.obj;
			List<AllOfContent>result=new ArrayList<AllOfContent>();
			
			if(operationType==MyApp.OPERATION_TYPE_ORIGINAL_CONTENT){//若是查询原创的内容列表，只加载实际上没有被删除的显示
				for(AllOfContent aoc:ori_result){              // 若是查询转发的内容列表，实际上被删除的内容也需要特殊显示出来，加载到adapter上
					if(aoc.getContent().getIf_deleted()==0){
						result.add(aoc);
					}
				}
			}else{
				result=ori_result;
			}
			
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
			}
			lstv.setResultSize(result.size());
			adapter.notifyDataSetChanged();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_content_list);
		
		myapp=(MyApp) getApplication();
		user=myapp.getUser();
		
		content_list_back_textbutton=(TextView) findViewById(R.id.content_list_back_textbutton);
		content_list_back_textbutton.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		lstv = (AutoListView) findViewById(R.id.content_list_lstv);
		
		createDialog();
		operationType=getIntent().getIntExtra("operationType",-1);
		if(operationType==MyApp.OPERATION_TYPE_TAG_CONTENT)
		{
			tagname=getIntent().getStringExtra("tag");
		}else if(operationType==MyApp.OPERATION_TYPE_KEY_CONTENT)
		{
			keyword=getIntent().getStringExtra("description");
		}
		
		adapter = new ContentListAdapter(this, list,user);
		lstv.setAdapter(adapter);
	//	lstv.setOnItemClickListener(new OnClickListenerImplForItem());
		lstv.setOnRefreshListener(this);
		lstv.setOnLoadListener(this);
		lstv.setOnItemLongClickListener(new OnItemLongClickListenerImpl());
		initData();
		
	}

	private void initData() {
		loadData(AutoListView.REFRESH);
	}
	
	public void createDialog()
	{
		Builder builder = new Builder(this);		

     	builder.setTitle("删除确认框");
     	builder.setMessage("是否确认删除这条内容");    

     	builder.setPositiveButton("是", new DeleteOnClickListenerImpl());  //绑定对话框按钮单击的监听器
     	builder.setNegativeButton("否", null);
     		
     	this.dlgDelete = builder.create();
	}
	
	private void loadData(final int what) {
		
		if(operationType==MyApp.OPERATION_TYPE_TAG_CONTENT)
		{
			loadContent task=new loadContent(getApplicationContext(),user,handler,what,operationType,tagname);
			new Thread(task).start();
		}else if(operationType==MyApp.OPERATION_TYPE_KEY_CONTENT)
		{
			loadContent task=new loadContent(getApplicationContext(),user,handler,what,operationType,keyword);
			new Thread(task).start();
		}else if(operationType==MyApp.OPERATION_TYPE_USER_CONTENT)
		{
			User oriuser=(User) getIntent().getSerializableExtra("user");
			loadContent task=new loadContent(getApplicationContext(),oriuser,handler,what,0);
			new Thread(task).start();
		}else
		{
			loadContent task=new loadContent(getApplicationContext(),user,handler,what,operationType);
			new Thread(task).start();
		}
		
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

	private class OnItemLongClickListenerImpl implements OnItemLongClickListener
	{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View view,
				int arg2, long arg3) {
			//arg0:列表框，arg1：视图，arg2：位置 arg3:id
			if(operationType==MyApp.OPERATION_TYPE_ORIGINAL_CONTENT)
			{
			position=arg2-1;
			dlgDelete.show();			
			}
			return false;
		}
		
	}
	
	private class  DeleteOnClickListenerImpl implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			if(arg1==arg0.BUTTON_POSITIVE)
			{
				aoc=list.get(position);
				list.remove(position);
				adapter.notifyDataSetChanged();
				new Thread(new deleteRunner()).start();
				Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
			}
		}
		
	}	
	
	private class deleteRunner implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String url=MyApp.url+"VowTest1/deleteContentServlet";
			HttpClient client=new DefaultHttpClient();
			HttpPost request=new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			String ucgson=Utils.ToContentGson(aoc.getContent());
			params.add(new BasicNameValuePair("ucgson",ucgson));
			try {
				request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));			
				
				HttpResponse response=client.execute(request);
//				if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
//				{
//					
//					handler.sendEmptyMessage(1);
//					
//				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}

