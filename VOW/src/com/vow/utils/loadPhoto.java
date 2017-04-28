package com.vow.utils;

import com.etc.vow.R;
import com.vow.app.MyApp;
import com.vow.task.loadBitmap;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class loadPhoto {
	
	private static String imagePhotoBaseUrl=MyApp.url+"VowTest1";
	
	
	public static void loadImage(ImageView imgphoto,String filename)
	{//加载用户头像
			String imageurl=imagePhotoBaseUrl+"/"+filename;
			loadBitmap loadBitmap_task=new loadBitmap();
	        Bitmap bm=loadBitmap_task.loadBitmap(imgphoto, imageurl, new loadBitmap.ImageCallback() {
				
				@Override
				public void imageLoad(ImageView imageView, Bitmap bitmap) {
					// TODO Auto-generated method stub
					imageView.setImageBitmap(bitmap);
				}
			});
			if(bm!=null)
			{				
				imgphoto.setImageBitmap(bm);
				//imgphoto.setImageResource(R.drawable.default_user_photo);
			}else
			{
				imgphoto.setImageResource(R.drawable.default_user_photo);
			}
	}

	public static Bitmap loadImageBitmap(ImageView imgphoto,String filename)
	{//加载用户头像
			String imageurl=imagePhotoBaseUrl+"/"+filename;
			loadBitmap loadBitmap_task=new loadBitmap();
	        Bitmap bm=loadBitmap_task.loadBitmap(imgphoto, imageurl, new loadBitmap.ImageCallback() {
				
				@Override
				public void imageLoad(ImageView imageView, Bitmap bitmap) {
					// TODO Auto-generated method stub
					imageView.setImageBitmap(bitmap);
				}
			});
			if(bm!=null)
			{				
				imgphoto.setImageBitmap(bm);
				return bm;
				//imgphoto.setImageResource(R.drawable.default_user_photo);
			}else
			{
				//imgphoto.setImageResource(R.drawable.default_user_photo);
				Bitmap default_bm=loadBitmap_task.loadBitmap(imgphoto, 
						imagePhotoBaseUrl+"/"+"default_user_photo.png",
						new loadBitmap.ImageCallback() {
					
					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						// TODO Auto-generated method stub
						imageView.setImageBitmap(bitmap);
					}
				});
				imgphoto.setImageBitmap(default_bm);
				return default_bm ;
			}
	}
}
