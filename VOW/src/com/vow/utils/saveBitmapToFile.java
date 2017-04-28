package com.vow.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class saveBitmapToFile {

		//SD卡缓存
	private String sdcardCacheDir;
	
	public saveBitmapToFile(){
		this.sdcardCacheDir=Environment.getExternalStorageDirectory().getPath()+"/vow/";
	}
	
	//返回图片存储路径
	public String saveBitmap(String path,Bitmap bitmap){
		
		String filename=sdcardCacheDir+path+"/"+Utils.convertFilename()+".png";	
		File folder=new File(sdcardCacheDir+path);
		if(!folder.exists())
		{
			folder.mkdirs();
		}
		File imagefile=new File(filename);
		if(imagefile.exists())
		{
			imagefile.delete();
		}
		
		FileOutputStream out;
	
	
				try {
					out = new FileOutputStream(imagefile);
					bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
					out.flush();
					out.close();
					Log.d("TAG","成功保存图片");
					return filename;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("TAG","没有保存图片");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("TAG","没有保存图片");
				}
		
		
		return null;
	}
}
