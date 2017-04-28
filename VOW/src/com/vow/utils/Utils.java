package com.vow.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;

import com.google.gson.Gson;
import com.vow.entity.Comments;
import com.vow.entity.Upload_content;
import com.vow.entity.User;

public class Utils {


	private static Gson gson=new Gson();
		// TODO Auto-generated constructor stub
		public static String getCurrentTime(String format) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
			String currentTime = sdf.format(date);
			return currentTime;
		}

		public static String getCurrentTime() {
			return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
		}
	

		public static String ToUserGson(User user)
		{
			return gson.toJson(user);
		}
		
		public static User FromUserGson(String usergson)
		{
			return gson.fromJson(usergson, User.class);
		}
		
		public static String ToContentGson(Upload_content uc)
		{
			return gson.toJson(uc);
		}
		

		public static Upload_content FromContentGson(String ucgson)
		{
			return gson.fromJson(ucgson,Upload_content.class);
		}
		
		public static String ToCommentGson(Comments comment)
		{
			return gson.toJson(comment);
		}
		public static Comments FromCommentGson(String comgson)
		{
			return gson.fromJson(comgson, Comments.class);
		}
		public static User copyUser(User olduser)
		{
			User user=new User();
			user.setUserid(olduser.getUserid());
			user.setUsername(olduser.getUsername());
			user.setPassword(olduser.getPassword());
			user.setThemeid(olduser.getThemeid());
			user.setRegtime(olduser.getRegtime());
			if(olduser.getBirthday()!=null)
			{
				user.setBirthday(olduser.getBirthday());
			}
			if(olduser.getEmail()!=null)
			{
				user.setEmail(olduser.getEmail());
			}
			if(olduser.getGender()!=null)
			{
				user.setGender(olduser.getGender());
			}
			if(olduser.getLocation()!=null)
			{
				user.setLocation(olduser.getLocation());
			}
			if(olduser.getPhone()!=null)
			{
				user.setPhone(olduser.getPhone());
			}
			if(olduser.getPhoto()!=null)
			{
				user.setPhoto(olduser.getPhoto());
			}
			if(olduser.getSignature()!=null)
			{
				user.setSignature(olduser.getSignature());
			}
			
			return user;
		}
		
		public static List<String> copyfileList(List<String> filelist)
		{
			List<String> copylist=new ArrayList<String>();
			for(String name:filelist)
			{
				copylist.add(name);
			}
			return copylist;
		}
		public static String convertFilename()
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");			
			Date date = new Date();
			String now=sdf.format(date);
			Random rd=new Random();
			int random=rd.nextInt(900)+100;
			return now+random;
		}
		
		public static String getRealPathFromUri(final Context context, final Uri uri)
		{
			if (null == uri)
				return null;

	 		final String scheme = uri.getScheme();

			String path = "";

			if (scheme == null)
				{
				  path = uri.getPath();
				  return path;
				}
			else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
				  path = uri.getPath();
				  return path;
			} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {

				Cursor cursor = context.getContentResolver().query(uri, new String[]{ImageColumns.DATA}, null, null, null);

				if (null != cursor) {

					if (cursor.moveToFirst()) {

						int index = cursor.getColumnIndex(ImageColumns.DATA);

						if (index > -1) {
							path = cursor.getString(index);
						}
					}

					String realpath="";
					realpath+=path;
	 				cursor.close();
					return realpath;
				}
			}
			return path;
		
		}
		
		
}
