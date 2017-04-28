package com.vow.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.etc.vow.CommentListActivity;
import com.etc.vow.PicturePagerActivity;
import com.etc.vow.R;
import com.etc.vow.SelfInfoActivity;
import com.etc.vow.VisiterActivity;
import com.vow.app.MyApp;
import com.vow.entity.AllOfContent;
import com.vow.entity.Tag;
import com.vow.entity.User;
import com.vow.task.like_favorite_download_repost_commentTask;
import com.vow.utils.AnimationTools;
import com.vow.utils.HttpDownloadUtil;
import com.vow.utils.Utils;
import com.vow.utils.loadPhoto;

public class ContentListAdapter extends BaseAdapter {

	
	private Context context;
	private ViewHolder holder;
	private List<AllOfContent> list;//传进来的list如果是查询原创内容的全是if_deleted=0，若是查询转发的有可能有if_deleted=1的，显示时候直接判断
	private User user; 
	
	
//	private String musicname = "jay_tearing.mp3"; 
//	private Uri uri=Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+"/"+musicname));
//	private MediaPlayer player = MediaPlayer.create(context,uri);
//	private Handler handler=new Handler();
//	private HttpDownloadUtil httpDownloadUtil=new HttpDownloadUtil();
	
	
//	private Runnable updateThread = new Runnable(){
//	     public void run() {     
//	    	 holder.content_list_item_playseekbar.setProgress(player.getCurrentPosition());   
//	         handler.postDelayed(updateThread, 100); 
//	     }
//	    };
	
	
	public ContentListAdapter(Context context, List<AllOfContent> list,User user) {
		this.list = list;
		this.context = context;
		this.user=user;
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
		convertView = LayoutInflater.from(context).inflate(R.layout.content_list_item, null);
		//给这个view设置点击的监听器
		
		//实例化组件
		initHolder(position, convertView);	
		convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		//填充数据
    	//用户头像
    	loadPhoto.loadImage(holder.content_list_item_userphoto,"image/photo/"+list.get(position).getContent().getOriginalUser().getPhoto());			
		//用户名
		holder.content_list_item_username .setText(list.get(position).getContent().getOriginalUser().getUsername());
		
	    if(list.get(position).getContent().getIf_deleted()==0){//内容实际没有被删除
	    		    	
			//加载内容标签
	    	List<Tag> taglist=list.get(position).getTaglist();
	    	for(int i=0;i<3&&i<taglist.size();i++)
	    	{
	    		holder.content_list_item_tag[i].setText("#"+taglist.get(i).getTagname()+"#");
	    	}
			//加载文字描述
			holder.content_list_item_description.setText(list.get(position).getContent().getDescription());
			//加载文件
	    	//音乐暂时不缓存,只有按下播放按钮后,才开始缓存音乐
			//先判断是否有声音或图片,有的才加载按钮,没有就不加载按钮		
			List<String> filenamelist=list.get(position).getFilelist();
			if(filenamelist.size()!=0){
				String filename1=filenamelist.get(0);
				if(filename1.substring(filename1.lastIndexOf(".")+1).equals("mp3"))
				{
					//存在音频的情况
					setMusicVisible();
					//如果存在图片
					if(filenamelist.size()>=2)
					{
						int imageNumbers=filenamelist.size()-1;
						//留下需要的
						for(int i=0;i<imageNumbers;i++)
						{
							holder.content_list_item_image[i].setVisibility(View.VISIBLE);
							//加载图片至合适大小,显示到手机里
							loadPhoto.loadImage(holder.content_list_item_image[i],"image/contentImage/"+filenamelist.get(i+1));	
						}
					}else
					{//如果不存在图片
						setImageGone();					
					}
						
				}else//如果第一个文件不是音频,是图片的话:
				{
					int imageNumbers=filenamelist.size();
					//留下需要的
					for(int i=0;i<imageNumbers;i++)
					{
						holder.content_list_item_image[i].setVisibility(View.VISIBLE);
						//加载图片至合适大小,显示到手机里
						loadPhoto.loadImage(holder.content_list_item_image[i],"image/contentImage/"+filenamelist.get(i));	
					}
				}
			}
	    	//设置是否收藏,是否赞
			if(list.get(position).isFavor())
			{
				holder.content_list_item_favoriteImage.setImageResource(R.drawable.favorite_true);
				holder.content_list_item_favorite.setText("已收藏");
				
			}else
			{
				holder.content_list_item_favoriteImage.setImageResource(R.drawable.favorite_false);
				holder.content_list_item_favorite.setText("收藏");
				
			}
			if(list.get(position).isLike())
			{
				holder.content_list_item_likeImage.setImageResource(R.drawable.like_true);
				
			}else
			{
				holder.content_list_item_likeImage.setImageResource(R.drawable.like_false);
				
			}
			//赞的次数
			int times=0;
			times=list.get(position).getContent().getLiketimes();
			if(times<=0)
			{
				holder.content_list_item_liketimes.setText("赞");
			}else
			{
				holder.content_list_item_liketimes.setText(times+"");
			}
			
//			//评论次数
			times=list.get(position).getComments_number();
			if(times<=0)
			{
				holder.content_list_item_commenttimes.setText("评论");
			}else
			{
				holder.content_list_item_commenttimes.setText(times+"");
			}
			
			
	    }else if(list.get(position).getContent().getIf_deleted()==1){
			//内容实际被删除	
			//显示内容已删除的提示
			holder.content_list_item_deleted.setVisibility(View.VISIBLE);
			holder.content_list_item_favoriteImage.setImageResource(R.drawable.favorite_false);
			//将组件颜色设置成灰色 
			holder.content_list_item_likeImage.setImageResource(R.drawable.like_false);
			holder.content_list_item_liketimes.setText("赞");
			holder.content_list_item_commenttimes.setText("评论");
			holder.content_list_item_commentButton.setBackgroundColor(Color.DKGRAY);
			holder.content_list_item_likeButton.setBackgroundColor(Color.DKGRAY);
			holder.content_list_item_favoriteButton.setBackgroundColor(Color.DKGRAY);
			
			holder.content_list_item_commentButton.setClickable(false);
			holder.content_list_item_likeButton.setClickable(false);
			holder.content_list_item_favoriteButton.setClickable(false);
		}
				
		return convertView;
	}
	

	private static class ViewHolder {
		
		ImageView content_list_item_userphoto;
		//用户名,标签,文字描述
		TextView content_list_item_username;
		TextView[] content_list_item_tag;
		TextView content_list_item_description;
		TextView content_list_item_deleted;
		//播放按钮,下载按钮,进度条
		ImageView content_list_item_playbutton,content_list_item_pausebutton,
					content_list_item_download;
		SeekBar content_list_item_playseekbar;
		ProgressBar content_list_item_downloadProgressBar;
		
		//九张图
		ImageView[] content_list_item_image;
		//收藏,赞,评论按钮
		LinearLayout content_list_item_favoriteButton,
		content_list_item_likeButton,
		content_list_item_commentButton;
		//收藏,赞图标
		ImageView content_list_item_likeImage,
		content_list_item_favoriteImage;
		
		//收藏,赞,评论次数
		TextView content_list_item_favorite;
		TextView content_list_item_liketimes;
		TextView content_list_item_commenttimes;
		
		
		
	}
	
	private void  initHolder(int position, View convertView)
	{
		final AllOfContent aoc=list.get(position);
		//用户头像
		holder.content_list_item_userphoto=(ImageView)convertView.findViewById(R.id.content_list_item_userphoto);
		holder.content_list_item_userphoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(aoc.getContent().getOriginalUser().getUserid()!=user.getUserid())
				{
				Intent intent=new Intent(context,VisiterActivity.class);
				intent.putExtra("user", aoc.getContent().getOriginalUser());
				context.startActivity(intent);
				}else
				{
					Intent intent=new Intent(context,SelfInfoActivity.class);
					context.startActivity(intent);
				}
			}
		});
		
		//用户名,内容标签,文字描述
		holder.content_list_item_username = (TextView) convertView.findViewById(R.id.content_list_item_username);
		holder.content_list_item_tag=new TextView[3];
		
		holder.content_list_item_tag[0] = (TextView) convertView.findViewById(R.id.content_list_item_tag1);
		holder.content_list_item_tag[1]= (TextView) convertView.findViewById(R.id.content_list_item_tag2);
		holder.content_list_item_tag[2]= (TextView) convertView.findViewById(R.id.content_list_item_tag3);
		
		holder.content_list_item_description=(TextView)convertView.findViewById(R.id.content_list_item_description);
		holder.content_list_item_deleted=(TextView)convertView.findViewById(R.id.content_list_item_deleted);

		//音乐播放按钮,播放进度条,下载按钮,下载进度条
		holder.content_list_item_playbutton=(ImageView) convertView.findViewById(R.id.content_list_item_playbutton);
		holder.content_list_item_pausebutton=(ImageView) convertView.findViewById(R.id.content_list_item_stopbutton);
		holder.content_list_item_playseekbar = (SeekBar) convertView.findViewById(R.id.content_list_item_playseekbar);
//		holder.content_list_item_playseekbar.setMax(player.getDuration());
		
		holder.content_list_item_download=(ImageView) convertView.findViewById(R.id.content_list_item_download);
		holder.content_list_item_downloadProgressBar=(ProgressBar) convertView.findViewById(R.id.content_list_item_downloadProgressBar);
		//播放或暂停按钮
//		设置播放,下载监听器
//		holder.content_list_item_playbutton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				holder.content_list_item_playbutton.setVisibility(View.GONE);
//				holder.content_list_item_pausebutton.setVisibility(View.VISIBLE);
//				 player.start();	     
//	             handler.post(updateThread);
//			}
//		});
//		holder.content_list_item_pausebutton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				holder.content_list_item_playbutton.setVisibility(View.VISIBLE);
//				holder.content_list_item_pausebutton.setVisibility(View.GONE);
//				player.pause(); 
//			}
//		});
//		
//		//播放进度条监听器
//		holder.content_list_item_playseekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//			
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onProgressChanged(SeekBar seekBar, int progress,
//					boolean fromUser) {
//				// TODO Auto-generated method stub
//				 if(fromUser==true){  
//				     player.seekTo(progress);
//				    }
//			}
//		});
//		    	       
//		holder.content_list_item_download.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//				holder.content_list_item_download.setVisibility(View.GONE);
//				holder.content_list_item_downloadProgressBar.setVisibility(View.VISIBLE);
//				 String url=MyApp.url+"VowTest1/image/tearlinermusic.mp3";
//
//				  int result=httpDownloadUtil.downfile(url, "test/", "aa.mp3"); 
//				  if(result==0)  
//                 {  
//                     Toast.makeText(context, "下载成功！", Toast.LENGTH_SHORT).show();  
//                 }  
//                 else if(result==1) {  
//                     Toast.makeText(context, "已有文件！", Toast.LENGTH_SHORT).show();  
//               }  
//                 else if(result==-1){  
//                     Toast.makeText(context, "下载失败！", Toast.LENGTH_SHORT).show();  
//                 }   
//			}
//		}) ;
		
		//九张图
		holder.content_list_item_image=new ImageView[9];
		
		holder.content_list_item_image[0]=(ImageView) convertView.findViewById(R.id.content_list_item_image1);
		holder.content_list_item_image[1]=(ImageView) convertView.findViewById(R.id.content_list_item_image2);
		holder.content_list_item_image[2]=(ImageView) convertView.findViewById(R.id.content_list_item_image3);
		holder.content_list_item_image[3]=(ImageView) convertView.findViewById(R.id.content_list_item_image4);
		holder.content_list_item_image[4]=(ImageView) convertView.findViewById(R.id.content_list_item_image5);
		holder.content_list_item_image[5]=(ImageView) convertView.findViewById(R.id.content_list_item_image6);
		holder.content_list_item_image[6]=(ImageView) convertView.findViewById(R.id.content_list_item_image7);
		holder.content_list_item_image[7]=(ImageView) convertView.findViewById(R.id.content_list_item_image8);
		holder.content_list_item_image[8]=(ImageView) convertView.findViewById(R.id.content_list_item_image9);

		//收藏,转发,点赞,评论按钮
		holder.content_list_item_favoriteButton=(LinearLayout) convertView.findViewById(R.id.content_list_item_favoriteButton);
		holder.content_list_item_likeButton=(LinearLayout) convertView.findViewById(R.id.content_list_item_likeButton);
		holder.content_list_item_commentButton=(LinearLayout) convertView.findViewById(R.id.content_list_item_commentButton);
		//收藏,点赞的图
		holder.content_list_item_favoriteImage=(ImageView) convertView.findViewById(R.id.content_list_item_favoriteImage);
		holder.content_list_item_likeImage=(ImageView) convertView.findViewById(R.id.content_list_item_likeImage);
					
		//收藏\评论\赞的次数
		holder.content_list_item_favorite=(TextView)convertView.findViewById(R.id.content_list_item_favorite);
		holder.content_list_item_commenttimes = (TextView) convertView.findViewById(R.id.content_list_item_commenttimes);
		holder.content_list_item_liketimes = (TextView) convertView.findViewById(R.id.content_list_item_liketimes);
		
		
		//设置图片的监听器
		for(int i=0;i<holder.content_list_item_image.length;i++)
		{
			final  int index=i;
			holder.content_list_item_image[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context,PicturePagerActivity.class);
					String filename1=aoc.getFilelist().get(0);
							
					if(filename1.substring(filename1.lastIndexOf(".")+1).equals("mp3"))
					{
						List<String> imagefilelist=Utils.copyfileList(aoc.getFilelist());
						imagefilelist.remove(0);
						intent.putStringArrayListExtra("filenamelist", (ArrayList<String>) imagefilelist);					
						
					}else
					{
						intent.putStringArrayListExtra("filenamelist", (ArrayList<String>) aoc.getFilelist());
					}
					intent.putExtra("imageindex", index);
					context.startActivity(intent);
				}
			});
		}
		//设置收藏,点赞,评论的监听器
		//收藏
		holder.content_list_item_favoriteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(aoc.getContent().getIf_deleted()==0){
				boolean isfavor=aoc.isFavor();
				aoc.setFavor(!isfavor);	
				ContentListAdapter.this.notifyDataSetChanged();
				AnimationTools.scale(holder.content_list_item_favoriteImage);
				new like_favorite_download_repost_commentTask().execute(Utils.ToUserGson(user),Utils.ToContentGson(aoc.getContent()),MyApp.FAVORITE+"");
				}
				}
		});
		//点赞
		holder.content_list_item_likeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(aoc.getContent().getIf_deleted()==0){
				boolean islike=aoc.isLike();
				aoc.setLike(!islike);
				int liketimes=aoc.getContent().getLiketimes();
				if(islike)
				{
					aoc.getContent().setLiketimes(liketimes-1);
				}else
				{
					aoc.getContent().setLiketimes(liketimes+1);
				}
				ContentListAdapter.this.notifyDataSetChanged();
				AnimationTools.scale(holder.content_list_item_likeImage);
				new like_favorite_download_repost_commentTask().execute(Utils.ToUserGson(user),Utils.ToContentGson(aoc.getContent()),MyApp.LIKE+"");

				}
			}
		});
		//评论
		holder.content_list_item_commentButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(aoc.getContent().getIf_deleted()==0){
				Intent intent=new Intent(context,CommentListActivity.class);
				intent.putExtra("ucid", aoc.getContent().getUcid());
				intent.putExtra("ucgson", Utils.ToContentGson(aoc.getContent()));
				context.startActivity(intent);
				}
			}
		});
		
	}
	
	
	
	private  void setMusicGone()
	{
		//音乐播放按钮,播放进度条,下载按钮,下载进度条
		holder.content_list_item_playbutton.setVisibility(View.GONE);
		holder.content_list_item_playseekbar.setVisibility(View.GONE);
		holder.content_list_item_download.setVisibility(View.GONE);
		holder.content_list_item_downloadProgressBar.setVisibility(View.GONE);
		
		
	}
	private void setImageGone()
	{
		//九张图
		for(int i=0;i<9;i++)
		{
			holder.content_list_item_image[i].setVisibility(View.GONE);
		}		
	}
	private void setMusicVisible()
	{
		//音乐播放按钮,播放进度条,下载按钮,下载进度条
				holder.content_list_item_playbutton.setVisibility(View.VISIBLE);
				holder.content_list_item_playseekbar.setVisibility(View.VISIBLE);
				holder.content_list_item_download.setVisibility(View.VISIBLE);
//				holder.content_list_item_downloadProgressBar.setVisibility(View.VISIBLE);
								
	}
	private void setImageVisible()
	{
		//九张图
		for(int i=0;i<9;i++)
		{
			holder.content_list_item_image[i].setVisibility(View.VISIBLE);
		}	
	}
	

	
	
}
