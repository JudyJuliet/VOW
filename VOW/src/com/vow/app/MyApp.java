package com.vow.app;

import com.vow.entity.User;

import android.app.Application;

public class MyApp extends Application {
	//活动类型
	public static final int UPDATEUSERINFO=0;
	public static final int MODIFYPASSWORD=1;
	public static final int REGISTERUSER=2;

	//内容夹操作类型
	public static final int LIKE=1;
	public static final int FAVORITE=2;
	public static final int DOWNLOAD=3;
	public static final int COMMENT=5;
	public static final int LIKEORFAVOR=6;
	
	public  static final int OPERATION_TYPE_ORIGINAL_CONTENT=0;//查找原创内容夹操作
	public  static final int OPERATION_TYPE_FAVORITE_CONTENT=2;//查找收藏内容夹操作
	public  static final int OPERATION_TYPE_DOWNLOAD_CONTENT=3;//查找下载内容夹操作
	public  static final int OPERATION_TYPE_FRED_CONTENT=4;//查找关注的人的内容夹操作
	public  static final int OPERATION_TYPE_TAG_CONTENT=5;//按标签查找内容
	public  static final int OPERATION_TYPE_KEY_CONTENT=6;//按关键词描述查找内容
	public  static final int OPERATION_TYPE_USER_CONTENT=7;//点击用户发布内容后进入
	
	public static final String listgson="[{'content':{'ucid':1,'uploadtime':'2016-08-05 12:32:29.0','description':'清晨郎朗书声','originalUser':{'userid':1,'themeid':1,'username':'xjj','password':'1','regtime':'2016-08-24 14:31:39.0','score':178,'email':'2952203838@qq.com','location':'天津','signature':'每个人都有一首属于自己的歌，轻轻地吟唱','photo':'photo1.png','gender':'蒙面侠','phone':'13920398611','birthday':'1998-01-01'},'loadtimes':2,'liketimes':3,'favoritetimes':2,'if_deleted':0},'taglist':[{'tagid':4,'tagname':'生活日常'},{'tagid':5,'tagname':'心灵鸡汤'}],'filelist':['morning.jpg','library.jpg','morningfamily.jpg','morningforest.jpg','reading.jpg'],'comments_number':1,'like':false,'favor':false},"
			+"{'content':{'ucid':2,'uploadtime':'2016-08-19 12:33:07.0','description':'雨落湖面','originalUser':{'userid':1,'themeid':1,'username':'xjj','password':'1','regtime':'2016-08-24 14:31:39.0','score':178,'email':'2952203838@qq.com','location':'天津','signature':'每个人都有一首属于自己的歌，轻轻地吟唱','photo':'photo1.png','gender':'蒙面侠','phone':'13920398611','birthday':'1998-01-01'},'loadtimes':0,'liketimes':8,'favoritetimes':3,'if_deleted':0},'taglist':[{'tagid':1,'tagname':'自然'}],'filelist':['tearliner.mp3','raining.jpg'],'comments_number':0,'like':true,'favor':true},"
			+"{'content':{'ucid':17,'uploadtime':'2016-08-24 11:33:43.0','description':'今天朋友过生日,生日快乐,这是为你唱的生日快乐歌','originalUser':{'userid':1,'themeid':1,'username':'xjj','password':'1','regtime':'2016-08-24 14:31:39.0','score':178,'email':'2952203838@qq.com','location':'天津','signature':'每个人都有一首属于自己的歌，轻轻地吟唱','photo':'photo1.png','gender':'蒙面侠','phone':'13920398611','birthday':'1998-01-01'},'loadtimes':0,'liketimes':0,'favoritetimes':0,'if_deleted':0},'taglist':[{'tagid':4,'tagname':'生活日常'},{'tagid':5,'tagname':'心灵鸡汤'}],'filelist':['20160202010029.jpeg','20151007001409.jpeg'],'comments_number':0,'like':false,'favor':false}]";
			
			
	public User user;
	public static String url="http://10.0.2.2:8080/";//"http://192.168.42.84:8080/";
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	

}
