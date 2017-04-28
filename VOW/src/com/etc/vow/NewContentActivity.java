package com.etc.vow;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vow.app.MyApp;
import com.vow.entity.AllOfContent;
import com.vow.entity.Tag;
import com.vow.entity.Upload_content;
import com.vow.task.SendMultiMessage;
import com.vow.utils.Utils;
import com.vow.utils.XCFlowLayout;
import com.vow.utils.saveBitmapToFile;

public class NewContentActivity extends Activity implements OnClickListener{

	private final int SELECTMUSIC=1,RECORD=2;
	private final int OPENCAMERA=3;
	private final int OPENGALLERY=4;
	private ImageView new_content_music_button;
	private ImageView[] new_content_image;
	private TextView new_content_cancel,new_content_send;
	private TextView new_content_music_filename;
	private EditText new_content_edt;
	private String musicfilename="",musicfilepath="";
	private int pic_count;//已加载图片的总数
	private List<Bitmap> bmlist;//所有已加载的图片
	private Uri imgURI,photoUri;
	private imageDialog imagedlg;
	private Upload_content newuc;
	private MyApp myapp;
	private Handler handler;
	
	//tag
	private XCFlowLayout mFlowLayout;//流式布局 
    private List<String>mNames=new ArrayList<String>();//全部标签(新建标签要加入该列表)
    private List<String>chooseNames=new ArrayList<String>();//选择标签
    private List<Tag>TagList=new ArrayList<Tag>();
    private String tagNamesStr;
    private MarginLayoutParams lp;
   
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_newcontent);
		
		init();
	
		bmlist=new ArrayList<Bitmap>();	
		myapp=(MyApp) getApplication();
		this.newuc=new Upload_content();
		this.newuc.setOriginalUser(myapp.getUser());
		handler=new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what)
				{
				case 1:
					Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(NewContentActivity.this,ContainerActivity.class);
					startActivity(intent);
					finish();
					break;
				case -1:
					Toast.makeText(getApplicationContext(), "发送失败,网络连接出现问题", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					TagList=(List<Tag>) msg.obj;
					for(Tag tag:TagList){
						mNames.add(tag.getTagname());        						
					}//从数据库得到tag列表，把标签名加到mNames列表里
				
				 for(int i = 0; i < mNames.size(); i++){ //向流式布局动态添加TextView组件
			            final TextView view = new TextView(NewContentActivity.this);  
			            view.setText(mNames.get(i)); 
			            view.setTextColor(Color.WHITE); 
			            view.setBackgroundResource(R.drawable.textview_bg);
	
			             view.setOnClickListener(new OnClickListener(){
	
							@Override
							public void onClick(View arg0) {//点击事件选择了该标签
								// TODO Auto-generated method stub
								String tag_text=view.getText().toString();
								
								if(!chooseNames.contains(tag_text)){//还未选择
									chooseNames.add(tag_text);
									view.setBackgroundResource(R.drawable.textview_pressbg);
								}else{//已经选择了，再点击取消选择
									chooseNames.remove(tag_text);
									view.setBackgroundResource(R.drawable.textview_bg);
								}
								
							}});
			                    
			            mFlowLayout.addView(view,lp);  
			        }
			        
			        //最后放一个新建更多标签的标志
			       final TextView view_more = new TextView(NewContentActivity.this);  
			        view_more.setBackgroundResource(R.drawable.more_tag);
			        view_more.setOnClickListener(new OnClickListener(){
	
						@Override
						public void onClick(View v) {//弹出对话框新建标签
							// TODO Auto-generated method stub
							final EditText editNewTag = new EditText(NewContentActivity.this);
							new AlertDialog.Builder(NewContentActivity.this).setTitle("新建标签名")
							.setIcon(R.drawable.add_tag).setView(editNewTag)
							.setPositiveButton("确定",new android.content.DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									//Toast.makeText(getApplicationContext(), "dialog",Toast.LENGTH_SHORT).show();
									mNames.add(editNewTag.getText().toString());
									final TextView view = new TextView(NewContentActivity.this);  
		        			        view.setText(editNewTag.getText().toString()); 
		        			        view.setTextColor(Color.WHITE); 
		        			        view.setBackgroundResource(R.drawable.textview_bg);
		        			        view.setOnClickListener(new OnClickListener(){
		        			        	
	        							@Override
	        							public void onClick(View arg0) {//点击事件选择了该标签
	        								// TODO Auto-generated method stub
	        								String tag_text=view.getText().toString();
	        								
	        								if(!chooseNames.contains(tag_text)){//还未选择
	        									chooseNames.add(tag_text);
	        									view.setBackgroundResource(R.drawable.textview_pressbg);
	        								}else{//已经选择了，再点击取消选择
	        									chooseNames.remove(tag_text);
	        									view.setBackgroundResource(R.drawable.textview_bg);
	        								}
	        								
	        							}});
		        			        mFlowLayout.removeView(view_more);
		        			        mFlowLayout.addView(view,lp);
		        			        mFlowLayout.addView(view_more,lp); 
		        			        
		        			      //插入tag
    								new Thread(new addTagRunner()).start();
		        			        
								}})
							.setNegativeButton("取消",null).show();
						}});
			        mFlowLayout.addView(view_more,lp);  
				break;
				}
			}
		};
		
		  new Thread(new FindTagListRunner()).start();//开启线程从数据库得到标签列表      
	}

	public void init(){
		
		//tag
		 mFlowLayout = (XCFlowLayout) findViewById(R.id.flowlayout);  
	     lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
	     lp.leftMargin = 5;  
	     lp.rightMargin = 5;  
	     lp.topMargin = 5;  
	     lp.bottomMargin = 5;  
		
		pic_count=0;
		new_content_music_button=(ImageView) findViewById(R.id.new_content_music_button);
		
		new_content_cancel=(TextView) findViewById(R.id.newContent_cancel);
		new_content_send=(TextView) findViewById(R.id.newContent_send);
		new_content_music_filename=(TextView) findViewById(R.id.new_content_music_filenametxv);
		new_content_edt=(EditText) findViewById(R.id.new_content_edt);
		new_content_image=new ImageView[9];
		new_content_image[0]=(ImageView)findViewById(R.id.new_content_image1);
		new_content_image[1]=(ImageView)findViewById(R.id.new_content_image2);
		new_content_image[2]=(ImageView)findViewById(R.id.new_content_image3);
		new_content_image[3]=(ImageView)findViewById(R.id.new_content_image4);
		new_content_image[4]=(ImageView)findViewById(R.id.new_content_image5);
		new_content_image[5]=(ImageView)findViewById(R.id.new_content_image6);
		new_content_image[6]=(ImageView)findViewById(R.id.new_content_image7);
		new_content_image[7]=(ImageView)findViewById(R.id.new_content_image8);
		new_content_image[8]=(ImageView)findViewById(R.id.new_content_image9);
	
		new_content_music_button.setOnClickListener(this);
		
		new_content_cancel.setOnClickListener(this);
		new_content_send.setOnClickListener(this);
		for(int i=0;i<9;i++){
			new_content_image[i].setOnClickListener(this);
		}
		
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.newContent_cancel:
			finish();
			break;
		case R.id.newContent_send://发送
			
			
			//插入内容和文件
			String des=this.new_content_edt.getText().toString();
			newuc.setDescription(des);
			newuc.setOriginalUser(myapp.getUser());
			List<String> filelist=new ArrayList<String>();
			if(!this.musicfilepath.equals(""))
			{
				filelist.add(musicfilepath);
			}
			String realpath="";
			for(Bitmap bm:bmlist)
			{
				saveBitmapToFile saveBitmapToFiletask=new saveBitmapToFile();
				 realpath=saveBitmapToFiletask.saveBitmap("user/content", bm);
				 filelist.add(realpath);
			}
			AllOfContent aoc=new AllOfContent();
			aoc.setContent(newuc);
			List<Tag>newTaglist=new ArrayList<Tag>();
			for(String str:chooseNames)
			{
				Tag tag=new Tag();
				tag.setTagname(str);
				newTaglist.add(tag);
			}
			aoc.setTaglist(newTaglist);
			Gson gson=new Gson();
			String aocgson=gson.toJson(aoc);
			
			//然后去对应task上传内容去.
			SendMultiMessage SendMultiMessageThread=
					new SendMultiMessage(aocgson,filelist,"PublishContentServlet",handler);
			new Thread(SendMultiMessageThread).start();
			Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
			break;
		case R.id.new_content_music_button:
			musicDialog musicdialog=new musicDialog(this);
			musicdialog.bindEvent(NewContentActivity.this);
			musicdialog.show();
			break;
		}
		//为9张图片添加响应事件
		for(int j=0;j<9;j++){
			if (v.getId()==new_content_image[j].getId()&&j==pic_count){//点击的是第i张图片且是空白图
				imagedlg=new imageDialog(this);
				imagedlg.bindEvent(NewContentActivity.this);
				imagedlg.show();
			}
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode)
		{
		case SELECTMUSIC:
			if(resultCode==RESULT_OK)
			{
				Uri uri=data.getData();
//				String[] proj = { MediaStore.Images.Media.DATA };     
//				Cursor actualimagecursor = this.managedQuery(uri,proj,null,null,null);    
//				int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);     
//				actualimagecursor.moveToFirst();     
				  
				  
				//musicfilepath = actualimagecursor.getString(actual_image_column_index);    
				this.musicfilepath=Utils.getRealPathFromUri(getApplicationContext(),uri);
				File musicfile=new File(musicfilepath);
				this.musicfilename=musicfile.getName();
				new_content_music_filename.setText(musicfilename);
				new_content_music_filename.setVisibility(View.VISIBLE);
			}
			break;
		case RECORD:
			if(resultCode == RESULT_OK){   //操作成功
				this.musicfilename=data.getStringExtra("recordfilename");
				this.musicfilepath=data.getStringExtra("recordfilepath");
				new_content_music_filename.setText(musicfilename);
				new_content_music_filename.setVisibility(View.VISIBLE);
			}
			break;
		case OPENCAMERA:
			if(resultCode==RESULT_OK)
			{

//				photoUri=this.imagedlg.getPhotoUri();
//				Log.d("photoUri",photoUri.toString());
//				  if (data != null && data.getData() != null) {
//					  imgURI = data.getData();
//					  }
//				  if (imgURI == null) {//防止系统拍照返回的imgURI为空
//					  if (photoUri != null) {
//						  imgURI = photoUri;
//					  }
//					  }
//				 			
//				new_content_image[pic_count].setImageURI(imgURI);
//				pic_count++;
//				if(pic_count<9){
//				new_content_image[pic_count].setVisibility(View.VISIBLE);
//				}
				if(pic_count<9){
				Bundle bundle=data.getExtras();
				Bitmap bm=(Bitmap) bundle.get("data");
				new_content_image[pic_count].setImageBitmap(bm);
				bmlist.add(pic_count, bm);//将bm放在指定的位置上
				pic_count++;
				
				new_content_image[pic_count].setVisibility(View.VISIBLE);
				imagedlg.dismiss();
				}
			}
			break;
		case OPENGALLERY:
			if(resultCode==RESULT_OK)
			{
				ArrayList<String>piclist=data.getStringArrayListExtra("piclist");
				int size=piclist.size();
				for(int i=pic_count;i<pic_count+size;i++){
					if(i<9){
						new_content_image[i].setVisibility(View.VISIBLE);
						 BitmapFactory.Options options=new BitmapFactory.Options(); 
						 options.inSampleSize = 10; 
						 Bitmap bitmap= BitmapFactory.decodeFile(piclist.get(i-pic_count),options);
						//Bitmap bitmap=BitmapFactory.decodeFile(piclist.get(i-pic_count));
						new_content_image[i].setImageBitmap(bitmap);
						bmlist.add(pic_count, bitmap);
					}
				}
				
				pic_count+=size;
				if(pic_count<9){
				new_content_image[pic_count].setVisibility(View.VISIBLE);
				}
			}
			break;
		}
	}

	//-----------------tag-------------------
	private class FindTagListRunner implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			Message msg=handler.obtainMessage();
			HttpClient client = new DefaultHttpClient();			
			HttpPost request = new HttpPost(MyApp.url+"VowTest1/AllTagListServlet");
			
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));				
				HttpResponse response = client.execute(request);						
				if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){					
					tagNamesStr = EntityUtils.toString(response.getEntity());		
					Gson gson=new Gson();
					Type type=new TypeToken<ArrayList<Tag>>(){}.getType();
					TagList=gson.fromJson(tagNamesStr, type);
					msg.obj=TagList;
					msg.what=2;							
				}
			}  catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg.what=-1;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg.what=-1;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg.what=-1;
			}
			handler.sendMessage(msg);	
		}
		
	}
    
    //---------------------------------------------------------
    
    private class addTagRunner implements Runnable{

		@Override
		public void run() {
			HttpClient client = new DefaultHttpClient();			
			HttpPost request = new HttpPost(MyApp.url+"VowTest1/AddTagServlet");
			
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				Gson gson=new Gson();
				params.add(new BasicNameValuePair("mNames", gson.toJson(mNames)));
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));				
				HttpResponse response = client.execute(request);						
			}  catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
	}
	
}

class musicDialog extends Dialog
{

	private List<int[]> griditem=new ArrayList<int[]>();
	private GridView gridview;
	
	
	protected musicDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}
	
	public musicDialog(Context context,int theme) {
		// TODO Auto-generated constructor stub
		super(context,theme);
	}
	
	private void initGrid()
	{
		griditem.add(new int[]{R.drawable.add_music,R.string.add_music});
		griditem.add(new int[]{R.drawable.add_record,R.string.add_record});
		List<Map<String,Object>> items=new ArrayList<Map<String,Object>>();
		for(int[]item:griditem)
		{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("image",item[0]);
			map.put("title", getContext().getString(item[1]));
			items.add(map);
		}
		SimpleAdapter adapter=new SimpleAdapter(getContext(), items, R.layout.grid_dialog_item, new String[]{"title","image"}, new int[]{R.id.dialogitem_text,R.id.dialogitem_image});
		gridview=(GridView) findViewById(R.id.mygridview);
		gridview.setAdapter(adapter);
	}
	
	
	public musicDialog(Context context)
	{
		super(context);
		//消除对话框标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.grid_dialog);
		
		setCanceledOnTouchOutside(true);// 点击对话框外部取消对话框显示   
        LayoutParams lp = getWindow().getAttributes();  
        getWindow().setAttributes(lp);  
        getWindow().addFlags(LayoutParams.FLAG_BLUR_BEHIND);// 添加模糊效果   
        //lp.alpha = 0.5f; 
        lp.dimAmount = 0.1f;// 设置对话框显示时的黑暗度，0.0f和1.0f之间
        initGrid();
	}
	
	public void bindEvent(Activity activity) { 
		setOwnerActivity(activity);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				switch(position)
				{
				case 0://选择音乐上传
					selectMusicFile();
					break;
				case 1://跳转至录音界面
					redirectToRecord(RecordActivity.class);
					break;
				}
			}
		});
	}
	
	
	//跳转至录音界面
	private void redirectToRecord(Class<?> cls)
	{
		if(getOwnerActivity().getClass()!=cls)//如果不是对话框绑定的activity那么就跳转
		{
			dismiss();//关闭对话框
			Intent intent=new Intent(getContext(),cls);
			intent.putExtra("from", "NewContentActivity");
			 //getContext().startActivity(intent);
			getOwnerActivity().startActivityForResult(intent, 2);
		}
	}
	
	private void selectMusicFile()
	{
		Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		//getContext().startActivity(Intent.createChooser(intent, "选择想上传的音乐文件"));
		getOwnerActivity().startActivityForResult(Intent.createChooser(intent, "选择想上传的音乐文件"), 1);
	}
}

class imageDialog extends Dialog
{
	private List<int[]> griditem=new ArrayList<int[]>();
	private GridView gridview;
	private static Uri photoUri;
	
	
	public Uri getPhotoUri() {
		Log.d("return uri", photoUri.toString());
		return photoUri;
	}

	

	protected imageDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}
	
	public imageDialog(Context context,int theme) {
		// TODO Auto-generated constructor stub
		super(context,theme);
	}
	
	private void initGrid()
	{
		griditem.add(new int[]{R.drawable.add_camera,R.string.add_camera});
		griditem.add(new int[]{R.drawable.add_gallery,R.string.add_gallery});
		List<Map<String,Object>> items=new ArrayList<Map<String,Object>>();
		for(int[]item:griditem)
		{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("image",item[0]);
			map.put("title", getContext().getString(item[1]));
			items.add(map);
		}
		SimpleAdapter adapter=new SimpleAdapter(getContext(), items, R.layout.grid_dialog_item, new String[]{"title","image"}, new int[]{R.id.dialogitem_text,R.id.dialogitem_image});
		gridview=(GridView) findViewById(R.id.mygridview);
		gridview.setAdapter(adapter);
	}
	
	public imageDialog(Context context)
	{
		super(context);
		//消除对话框标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.grid_dialog);
		
		setCanceledOnTouchOutside(true);// 点击对话框外部取消对话框显示   
        LayoutParams lp = getWindow().getAttributes();  
        getWindow().setAttributes(lp);  
        getWindow().addFlags(LayoutParams.FLAG_BLUR_BEHIND);// 添加模糊效果   
        //lp.alpha = 0.5f; 
        lp.dimAmount = 0.1f;// 设置对话框显示时的黑暗度，0.0f和1.0f之间
        initGrid();
	}
	
	public void bindEvent(Activity activity) { 
		setOwnerActivity(activity);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				switch(position)
				{
				case 0://选择相机
//					 Intent intentToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//					 String sdPath=Environment.getExternalStorageDirectory().getPath();
//					 String filename=sdPath+"/vow/user/photo/"+System.currentTimeMillis()+".jpg";
//					 ContentValues values = new ContentValues();
//					 values.put(Media.TITLE, filename);
//
//					 photoUri =getContext().getContentResolver().insert(
//					 MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//					 intentToCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//
//					 getOwnerActivity().startActivityForResult(intentToCamera, 3);
					
					Intent intentToCamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					getOwnerActivity().startActivityForResult(intentToCamera,3);
					
					break;
				case 1://跳转至图库
					Intent intentToGallery=new Intent(getContext(),FindPicturesActivity.class);
					getOwnerActivity().startActivityForResult(intentToGallery, 4);
					break;
				}
			}
		});
	}
}
