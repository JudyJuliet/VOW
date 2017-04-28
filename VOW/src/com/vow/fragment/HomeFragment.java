package com.vow.fragment;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.etc.vow.HomeSearchActivity;
import com.etc.vow.R;
import com.vow.app.MyApp;
import com.vow.utils.loadPhoto;



@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HomeFragment extends Fragment implements OnClickListener{
	
	private View homeView;

	private ImageButton igbShake;
	private ImageView avgPhoto;
	private MyApp myapp;
	private EditText home_search_edt;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		homeView = inflater.inflate(R.layout.tab_home_layout, container, false);
		igbShake = (ImageButton) homeView.findViewById(R.id.title_shake_bn);
		avgPhoto = (ImageView) homeView.findViewById(R.id.avgPhoto);
		home_search_edt=(EditText) homeView.findViewById(R.id.home_search_edt);
		
		myapp=(MyApp) getActivity().getApplication();
		loadPhoto.loadImage(avgPhoto, "image/photo/"+myapp.getUser().getPhoto());
		igbShake.setOnClickListener(this);
		avgPhoto.setOnClickListener(this);
		home_search_edt.setOnClickListener(this);
		return homeView;
	}

	@Override
	public void onClick(View v) {
		// //进入摇一摇筛选附近用户
		switch(v.getId()){
		case R.id.title_shake_bn:
			Toast.makeText(getActivity(), "摇一摇！", Toast.LENGTH_SHORT).show();
			break;
		case R.id.avgPhoto:
			break;
		case R.id.home_search_edt:
			Intent intent=new Intent(getActivity(),HomeSearchActivity.class);
			startActivity(intent);
			break;
		}
	}}
