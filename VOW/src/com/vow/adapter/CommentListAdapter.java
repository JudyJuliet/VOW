package com.vow.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etc.vow.R;
import com.vow.entity.Comments;
import com.vow.utils.loadPhoto;

public class CommentListAdapter extends BaseAdapter {

	private Context context;
	private List<Comments> list;

	public CommentListAdapter(Context context, List<Comments> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(
				R.layout.comment_list_item, null);
		ImageView comment_list_item_userphoto=(ImageView) convertView.findViewById(R.id.comment_list_item_userphoto);
		TextView comment_list_item_username=(TextView) convertView.findViewById(R.id.comment_list_item_username);
		TextView comment_list_item_usercomment=(TextView) convertView.findViewById(R.id.comment_list_item_usercomment);
		//填充数据
		//加载评论的用户头像
		
		loadPhoto.loadImage(comment_list_item_userphoto, "image/photo/"+list.get(position).getUser().getPhoto());
		//加载评论用户名
		comment_list_item_username.setText(list.get(position).getUser().getUsername());
		//加载评论
		comment_list_item_usercomment.setText(list.get(position).getComdetail());
		return convertView;
	}

}
