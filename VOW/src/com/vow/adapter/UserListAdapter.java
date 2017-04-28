package com.vow.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etc.vow.R;
import com.vow.app.MyApp;
import com.vow.entity.User;
import com.vow.task.loadBitmap;

public class UserListAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<User> list;
	private Context context;
	private loadBitmap loadBitmap_task;
	private String imageBaseUrl=MyApp.url+"VowTest1/image/photo";
	public UserListAdapter(Context context, List<User> list,loadBitmap loadBitmap_task) {
		this.list = list;
		this.context = context;
		this.loadBitmap_task=loadBitmap_task;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.user_list_item, null);
			
			holder.imgPhoto=(ImageView)convertView.findViewById(R.id.imageViewUserPhoto);
			holder.txtName = (TextView) convertView.findViewById(R.id.texUsername);
			holder.txtLocation = (TextView) convertView.findViewById(R.id.texLocation);
			holder.txtSignature = (TextView) convertView.findViewById(R.id.texSignature);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//填充数据
		holder.txtName.setText(list.get(position).getUsername());
		holder.txtLocation.setText(list.get(position).getLocation());
		holder.txtSignature.setText(list.get(position).getSignature());
		
		//加载远程图片
		String imageurl=this.imageBaseUrl+"/"+list.get(position).getPhoto();
		Bitmap bm=this.loadBitmap_task.loadBitmap(holder.imgPhoto, imageurl, new loadBitmap.ImageCallback() {
			
			@Override
			public void imageLoad(ImageView imageView, Bitmap bitmap) {
				// TODO Auto-generated method stub
				imageView.setImageBitmap(bitmap);
			}
		});
		if(bm!=null)
		{
			holder.imgPhoto.setImageBitmap(bm);
		}
		return convertView;
	}

	private static class ViewHolder {
		ImageView imgPhoto;
		TextView txtName;
		TextView txtLocation;
		TextView txtSignature;
		
	}

}
