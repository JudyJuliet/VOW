package com.vow.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class loadBitmap {

		//内存缓存
		private HashMap<String,SoftReference<Bitmap>>imageCache;  //url-->bitmap缓存块
		//SD卡缓存
		private String sdcardCacheDir;
		public loadBitmap() {
			this.imageCache=new HashMap<String,SoftReference<Bitmap>>();
			this.sdcardCacheDir=Environment.getExternalStorageDirectory().getPath()+"/vow/user/images";
		}
		
		//加载图片方法
		public Bitmap loadBitmap(final ImageView imageview,final String imageUrl,final ImageCallback imageCallback){
			
			//检查内存缓存
			if(imageCache.containsKey(imageUrl)){
				SoftReference<Bitmap> reference=imageCache.get(imageUrl);
				Bitmap bitmap=reference.get();
				if(bitmap!=null){
					System.out.println(">>>已经获取内存图片！");
					return bitmap;
				}
			}
			
			//检查SD卡
			String bitmapName=imageUrl.substring(imageUrl.lastIndexOf("/")+1);//从URL中得到图片文件名
			File cacheDir=new File(sdcardCacheDir);
			if(cacheDir.exists()){
				//获取缓存中的所有文件
				File[]cacheFiles=cacheDir.listFiles();
				int i=0;
				for(;i<cacheFiles.length;i++){
					if(bitmapName.equals(cacheFiles[i].getName()))
						break;
				}
				if(i<cacheFiles.length){
					System.out.println(">>>已加载SD卡缓存图片！");
					Bitmap bitmap=BitmapFactory.decodeFile(sdcardCacheDir+"/"+bitmapName);
					imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
					return bitmap;
				}
			}
			//定义handler
			final Handler handler=new Handler(){
				public void handleMessage(Message msg){
					//调用回调函数
					imageCallback.imageLoad(imageview,(Bitmap)msg.obj);
				}
			};
			
			//远程加载
		   new Thread(){
			public void run(){
			 
			try {
				  URL url = new URL(imageUrl); 
				  System.out.println(">>>开始加载远程图片！");
			      InputStream is = url.openStream();
			      Bitmap bitmap=BitmapFactory.decodeStream(is);	
			      is.close();
			      
			      //通知handler
			      Message msg=handler.obtainMessage();
			      msg.obj=bitmap;
			      handler.sendMessage(msg);
			      
			      //加入内存缓存
			      imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
			      
			      //缓存到SD卡
			      File dir=new File(sdcardCacheDir);
			      if(!dir.exists()){
			    	  dir.mkdirs();
			      }
			      File bitmapFiles=new File(sdcardCacheDir+"/"+imageUrl.substring(imageUrl.lastIndexOf("/")+1));
			      FileOutputStream fos=new FileOutputStream(bitmapFiles);
			      bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fos);
			      fos.close();
			      
			    } catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				   e.printStackTrace();
			    } catch (IOException e) {
				// TODO Auto-generated catch block
				   e.printStackTrace();
			    }		  
			}
		}.start();
			
			return null;
		}
		
		//定义回调接口
		public interface ImageCallback{
			public void imageLoad(ImageView imageView,Bitmap bitmap);
						
		}
		

}
