package com.vow.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etc.vow.ContainerActivity;
import com.etc.vow.ContentListActivity;
import com.etc.vow.ListViewActivity;
import com.etc.vow.R;
import com.etc.vow.SelfInfoActivity;
import com.etc.vow.SettingActivity;
import com.etc.vow.UpdateUserInfoActivity;
import com.vow.app.MyApp;
import com.vow.utils.loadPhoto;

@SuppressLint("NewApi") 
public class MenuFragment extends Fragment implements View.OnClickListener {

	private View currentView;
	private Button msg, trans, favorite, post, fans, download;
	private Button menu_setting_btn,menu_night_btn;
	private TextView menu_text_name,menu_text_signature;
	private ImageView imgUser;
	private Intent intent;
	private MyApp myapp;
	private final int OPERATION_TYPE_ORIGINAL_CONTENT=0;//查找原创内容夹操作
	private final int OPERATION_TYPE_FRD_LIST=1;//查找好友列表操作
	private final int OPERATION_TYPE_FAVORITE_CONTENT=2;//查找收藏内容夹操作
	private final int OPERATION_TYPE_DOWNLOAD_CONTENT=3;//查找下载内容夹操作
	private final int OPERATION_TYPE_FRED_CONTENT=4;//查找关注的人的内容夹操作
	
	public View getCurrentView() {
		return currentView;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.slidemenu_layout,
				container, false);
		//消息列表
		msg = (Button) currentView.findViewById(R.id.bt_msg);
		//好友列表
		trans = (Button) currentView.findViewById(R.id.bt_trans);
		//好友动态
		fans =(Button) currentView.findViewById(R.id.bt_fans);
		//我的收藏
		favorite = (Button) currentView.findViewById(R.id.bt_favorite);
		//我的发布
		post = (Button) currentView.findViewById(R.id.bt_post);
		//我的下载
		download = (Button) currentView.findViewById(R.id.bt_download);
		//我的头像
		imgUser = (ImageView) currentView.findViewById(R.id.photo);
		menu_setting_btn=(Button) currentView.findViewById(R.id.btnSetting);
		menu_night_btn=(Button) currentView.findViewById(R.id.btnNightmode);
		myapp=(MyApp) getActivity().getApplication();
		loadPhoto.loadImage(imgUser, "image/photo/"+myapp.getUser().getPhoto());
		//用户名
		menu_text_name=(TextView) currentView.findViewById(R.id.menu_text_name);
		menu_text_name.setText(myapp.getUser().getUsername());
		//用户签名
		menu_text_signature=(TextView) currentView.findViewById(R.id.menu_text_signature);
		if(myapp.getUser().getSignature()!=null)
		{
			menu_text_signature.setText(myapp.getUser().getSignature());
		}else
		{
			menu_text_signature.setText("您还没有填写个性签名");
		}
		
		msg.setOnClickListener(this);
		trans.setOnClickListener(this);
		fans.setOnClickListener(this);
		favorite.setOnClickListener(this);
		post.setOnClickListener(this);
		download.setOnClickListener(this);
		imgUser.setOnClickListener(this);
		menu_text_signature.setOnClickListener(this);
		menu_setting_btn.setOnClickListener(this);
		menu_night_btn.setOnClickListener(this);
		return currentView;
	}

	@Override
	public void onClick(View v) {
		((ContainerActivity) getActivity()).getSlidingPane().closePane();
		switch(v.getId()){
		
		case R.id.photo:
			//点击头像进入个人信息页
			intent = new Intent(getActivity(),SelfInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_msg:
			//进入我的消息提示页
			intent=new Intent(getActivity(),ContentListActivity.class);
			intent.putExtra("operationType", OPERATION_TYPE_ORIGINAL_CONTENT);
			startActivity(intent);
			break;
		case R.id.bt_trans:
			//我的好友列表
			intent=new Intent(getActivity(),ListViewActivity.class);
			intent.putExtra("operationType", OPERATION_TYPE_FRD_LIST);
			intent.putExtra("index",1);
			startActivity(intent);
			break;
		case R.id.bt_fans:
			//我的关注
			intent=new Intent(getActivity(),ContentListActivity.class);
			intent.putExtra("operationType", OPERATION_TYPE_FRED_CONTENT);
			startActivity(intent);
			break;
		case R.id.bt_favorite:
			//我的收藏
			intent=new Intent(getActivity(),ContentListActivity.class);
			intent.putExtra("operationType", OPERATION_TYPE_FAVORITE_CONTENT);
			startActivity(intent);
			break;
		case R.id.bt_post:
			//我发布的内容
			intent=new Intent(getActivity(),ContentListActivity.class);
			intent.putExtra("operationType", OPERATION_TYPE_ORIGINAL_CONTENT);
			startActivity(intent);
			break;
		case R.id.bt_download:
			//我下载的内容
			intent=new Intent(getActivity(),ContentListActivity.class);
			intent.putExtra("operationType", OPERATION_TYPE_DOWNLOAD_CONTENT);
			startActivity(intent);
			break;
		case R.id.btnSetting:
			//跳转setting页
			intent = new Intent(getActivity(),SettingActivity.class);
			startActivity(intent);
			break;
		case R.id.btnNightmode:
			//换夜间模式
			Toast.makeText(getActivity(), "夜间模式", Toast.LENGTH_SHORT).show();
			
		case R.id.menu_text_signature:
			//填写个性签名
			intent = new Intent(getActivity(),UpdateUserInfoActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		
		}
	}
}

