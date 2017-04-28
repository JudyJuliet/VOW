package com.etc.vow;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autolistview.widget.AutoListView;
import com.example.autolistview.widget.AutoListView.OnLoadListener;
import com.example.autolistview.widget.AutoListView.OnRefreshListener;
import com.vow.adapter.CommentListAdapter;
import com.vow.app.MyApp;
import com.vow.entity.Comments;
import com.vow.task.SendMessageToServlet;
import com.vow.task.loadComment;
import com.vow.utils.Utils;

public class CommentListActivity extends Activity implements OnRefreshListener,
OnLoadListener {

	private AutoListView commentlistview;
	private EditText commentText;
	private TextView commentButton,backButton;
	private CommentListAdapter commentlistadapter;
	private MyApp myapp;
	private int ucid;
	private String ucgson;
	private List<Comments> commentlist = new ArrayList<Comments>();
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			@SuppressWarnings("unchecked")
			List<Comments> result;
			
			switch (msg.what) {
			
			case AutoListView.REFRESH:
				 result = (List<Comments>) msg.obj;
				commentlistview.onRefreshComplete();
				commentlist.clear();
				commentlist.addAll(result);
				commentlistview.setResultSize(result.size());
				commentlistadapter.notifyDataSetChanged();
				break;
			case AutoListView.LOAD:
				 result = (List<Comments>) msg.obj;
				commentlistview.onLoadComplete();
				commentlist.addAll(result);
				commentlistview.setResultSize(result.size());
				commentlistadapter.notifyDataSetChanged();
				break;
			case 100://从发表评论那里传回来的
				if(((String)msg.obj).equals("succeed\r\n"))
				{
					//commentlistadapter.notifyDataSetChanged();
					commentText.setText("");
					initData();//重新加载一遍
					
				}else
				{
					Toast.makeText(getApplicationContext(), "网络连接出现问题,请稍候再试", Toast.LENGTH_SHORT).show();
				}
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_comment_list);
		
		this.commentlistview = (AutoListView) findViewById(R.id.commentListView);
		this.commentText=(EditText) findViewById(R.id.comment_edittext);
		this.commentButton=(TextView) findViewById(R.id.comment_list_bottom_sendText);
		this.backButton=(TextView) findViewById(R.id.comment_list_back_textbutton);
		
		//然后添加这两个按钮的响应事件.
		this.commentButton.setOnClickListener(new OnClickListenerImpl());
		this.backButton.setOnClickListener(new OnClickListenerImpl());
		this.ucgson=getIntent().getStringExtra("ucgson");
		//取需要查看评论的内容id
		ucid=getIntent().getIntExtra("ucid", 0);
		//取当前用户
		this.myapp=(MyApp) getApplication();
		//new adapter,因为只是一个显示列表,不能再评论列表上做操作
		//所以还需在此activity上加上一行"我也评一句..."
		this.commentlistadapter = new CommentListAdapter(this, this.commentlist);
		this.commentlistview.setAdapter(commentlistadapter);
		this.commentlistview.setOnRefreshListener(this);
		this.commentlistview.setOnLoadListener(this);
		initData();
	}

	private void initData() {
		loadData(AutoListView.REFRESH);
	}
	

	private void loadData(final int what) {
		loadComment task=new loadComment(ucid,what,handler);
		new Thread(task).start();		
	}
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		loadData(AutoListView.LOAD);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		loadData(AutoListView.REFRESH);
	}


	private class OnClickListenerImpl implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.comment_list_back_textbutton:
				finish();
				break;
			case R.id.comment_list_bottom_sendText:
				String usercomment=commentText.getText().toString();
				if(!usercomment.equals(""))
				{
					Comments comment=new Comments();
					comment.setComdetail(usercomment);
					comment.setUc(Utils.FromContentGson(ucgson));
					comment.setUser(myapp.getUser());
					String commentgson=Utils.ToCommentGson(comment);
					SendMessageToServlet thread=new SendMessageToServlet(commentgson,"PublishCommentServlet",handler);
					new Thread(thread).start();
				}else
				{
					Toast.makeText(getApplicationContext(), "您还没有填写评论", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
		
	}
	
}
