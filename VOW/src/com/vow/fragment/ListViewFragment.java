package com.vow.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.etc.vow.R;
import com.vow.adapter.ContentListAdapter;
import com.vow.app.MyApp;
import com.vow.entity.AllOfContent;
import com.vow.entity.User;
import com.vow.task.loadContent;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi")
public class ListViewFragment extends Fragment{

	private ListView listview;
	private ContentListAdapter adapter;
	private View homeView;
	private MyApp myapp;
	private User user;
	private List<AllOfContent> list=new ArrayList<AllOfContent>();
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
						
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 初始化列表
		homeView = inflater.inflate(R.layout.home_listview, container, false);
		listview=(ListView) homeView.findViewById(R.id.home_content_listview);
		myapp=(MyApp) getActivity().getApplication();
		user=myapp.getUser();
		
		adapter=new ContentListAdapter(getActivity(), list,user);
		listview.setAdapter(adapter);
		initdata();
		return homeView;
			}

	private void initdata() {
		// TODO Auto-generated method stub
		loadContent task=new loadContent(getActivity(),user,handler,0,10);
		new Thread(task).start();
	}

	

}
