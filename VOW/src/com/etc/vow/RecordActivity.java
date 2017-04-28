package com.etc.vow;

import java.io.File;
import java.io.IOException;

import org.tecunhuman.AndroidJNI;
import org.tecunhuman.ExtAudioRecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vow.sketchDraw.FileOper;
import com.vow.utils.Utils;

public class RecordActivity extends Activity implements OnClickListener {

	private Button start;
	private Button stop;
	private Button change;
	private Button convert;

	private EditText l;//节拍
	private EditText h;//音调
	private EditText s;//播放速率

	private EditText edt_recordName;
	private TextView txt_back;
	private TextView txtConfirmPost;//发布
	private Intent intent;
	private String from="";
	
	ExtAudioRecorder extAudioRecorder = null;//录音对象
	private  FileOper fileOper = new FileOper();
	String dataPath;
	String recordname="";
	boolean issave=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_record);
		dataPath = fileOper.getSoundFilePath();
		System.loadLibrary("soundtouch");
		System.loadLibrary("soundstretch");
		start = (Button) findViewById(R.id.startRecord);
		stop = (Button) findViewById(R.id.stopRecord);
		change = (Button) findViewById(R.id.changeVoice);
		convert = (Button) findViewById(R.id.convert2Mp3);

		l = (EditText) findViewById(R.id.l);
		h = (EditText) findViewById(R.id.h);
		s = (EditText) findViewById(R.id.s);
		edt_recordName=(EditText) findViewById(R.id.edt_recordName);
		txt_back=(TextView) findViewById(R.id.txtCancelPost);

		intent=this.getIntent();
		from=intent.getStringExtra("from");
		
		start.setOnClickListener(this);
		stop.setOnClickListener(this);
		change.setOnClickListener(this);
		convert.setOnClickListener(this);
		txt_back.setOnClickListener(this);
	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.startRecord://开始录音
			if (extAudioRecorder == null)
				extAudioRecorder = extAudioRecorder.getInstanse(false);
			extAudioRecorder.setOutputFile(dataPath + "source.wav");
			extAudioRecorder.prepare();
			extAudioRecorder.start();
			Toast.makeText(RecordActivity.this, "开始录音", 0).show();
			break;
		case R.id.stopRecord://ֹͣ停止录音
			extAudioRecorder.stop();
			extAudioRecorder.release();
			extAudioRecorder = null;
			Toast.makeText(RecordActivity.this, "录音结束", 0).show();
			break;
		case R.id.changeVoice://变音
			AndroidJNI.soundStretch.process(dataPath + "source.wav", dataPath
					+ "target.wav", Float.parseFloat(l.getText().toString()),
					Float.parseFloat(h.getText().toString()),
					Float.parseFloat(s.getText().toString()));
			Toast.makeText(RecordActivity.this, "变声成功", 0).show();
			break;
		case R.id.convert2Mp3://保存，转化为MP3格式

			mp3.Main main = new mp3.Main();
			
			recordname=edt_recordName.getText().toString();
			
			if(recordname.equals(""))
			{
				Toast.makeText(RecordActivity.this, "文件名不能为空！", 0).show();
			}else
			{
			try {
				main.convertWAVToMP3(dataPath + "target");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			File mp3File = new File(dataPath + "target.mp3");
			if (mp3File.length() == 0) {
				int retryTimes = 0;
				while (true) {
					// sleep(2000);
					mp3File = new File(dataPath + "target.mp3");
					if (mp3File.length() > 0 || retryTimes == 50)
						break;
					retryTimes++;
				}
				if (mp3File.length() == 0) {
					try {
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
			
			mp3File.renameTo(new File(dataPath + recordname + ".mp3"));
			Toast.makeText(RecordActivity.this, "已保存至："+dataPath+recordname+".mp3" , 0).show();
			issave=true;
			}
			break;
		case R.id.txtCancelPost:
			if(from.equals("NewContentActivity"))
			{
				setResult(RESULT_CANCELED, intent);
				finish();
				break;
			}else{
				finish();
				break;
			}
		case R.id.txtConfirmPost:
			String recordfilepath="";
			//判断用户是否保存了录音文件,如果保存了,那么将文件名传给newcontent页面,
			//如果没有保存,就自动帮他保存,再把文件名传给newcontent页面
			if(issave)//保存了
			{
				recordfilepath+=dataPath + recordname + ".mp3";
			}else//没保存
			{
				mp3.Main main1 = new mp3.Main();
				recordname=edt_recordName.getText().toString();
				if(recordname.equals(""))
				{
					recordname=Utils.convertFilename();					
				}
				try {
					main1.convertWAVToMP3(dataPath + "target");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}//先转化为MP3格式的
				File mp3File = new File(dataPath + "target.mp3");
				if (mp3File.length() == 0) {
					int retryTimes = 0;
					while (true) {
						// sleep(2000);
						mp3File = new File(dataPath + "target.mp3");
						if (mp3File.length() > 0 || retryTimes == 50)
							break;
						retryTimes++;
					}
					if (mp3File.length() == 0) {
						try {
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
				}
				mp3File.renameTo(new File(dataPath + recordname + ".mp3"));
				recordfilepath+=dataPath + recordname + ".mp3";
			}
			//再判断它是哪个页面传过来的
			if(from.equals("NewContentActivity"))//从new那页跳转过来的,直接finish
			{
				intent.putExtra("recordfilepath", recordfilepath);
				intent.putExtra("recordfilename", recordname+".mp3");
				setResult(RESULT_OK, intent);
				finish();
			}else
			{
				Intent toNewContentActivity=new Intent(RecordActivity.this,NewContentActivity.class);
				toNewContentActivity.putExtra("recordfilepath", recordfilepath);
				toNewContentActivity.putExtra("recordfilename", recordname+".mp3");
				startActivity(toNewContentActivity);
				finish();
			}

			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (extAudioRecorder != null) {
			extAudioRecorder.release();
			extAudioRecorder = null;
		}
	}
}
