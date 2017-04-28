package com.etc.vow;

import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.vow.utils.SketchPadView;


//画板界面
public class SketchActivity extends Activity {

	private TextView txtCancelDraw;
	private TextView txtSaveDraw;
	private SeekBar skbLine;
	private SketchPadView m_View;
	private ImageView imgUndo;
	private ImageView imgRedo;
	private ImageView imgPen;
	private ImageView imgEraser;
	private ImageView imgColor;
	private int m_bkColor = Color.WHITE;
	private static final int REQUEST_TYPE_A = 1;
	private static final int REQUEST_TYPE_B = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sketch);
		txtCancelDraw =(TextView) findViewById(R.id.txtCancelDraw);
		txtSaveDraw =(TextView) findViewById(R.id.txtSaveDraw);
		m_View =(SketchPadView) findViewById(R.id.spDraw);
		skbLine =(SeekBar) findViewById(R.id.skbLine);
		imgUndo =(ImageView) findViewById(R.id.imgUndo);
		imgRedo =(ImageView) findViewById(R.id.imgRedo);
		imgPen =(ImageView) findViewById(R.id.imgPen);
		imgEraser =(ImageView) findViewById(R.id.imgEraser);
		imgColor =(ImageView) findViewById(R.id.imgColor);
		
		m_View.setBkColor(m_bkColor);
		
		txtCancelDraw.setOnClickListener(new ClickListenerImpl());
		txtSaveDraw.setOnClickListener(new ClickListenerImpl());
		imgUndo.setOnClickListener(new ClickListenerImpl());
		imgRedo.setOnClickListener(new ClickListenerImpl());
		imgPen.setOnClickListener(new ClickListenerImpl());
		imgEraser.setOnClickListener(new ClickListenerImpl());
		imgColor.setOnClickListener(new ClickListenerImpl());	
		skbLine.setOnSeekBarChangeListener(new skbChangeListener());
		
	}

	private class ClickListenerImpl implements View.OnClickListener{

		@SuppressWarnings("static-access")
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.txtCancelDraw:
				Intent intent = new Intent(SketchActivity.this,ContainerActivity.class);
				startActivity(intent);
				finish();
				break;
			case R.id.txtSaveDraw:
				startActivityForResult(new Intent(SketchActivity.this,SaveSketchActivity.class),REQUEST_TYPE_B);
				//Toast.makeText(getApplicationContext(), "保存", Toast.LENGTH_SHORT).show();
				break;
			case R.id.imgPen:
				m_View.setStrokeType(m_View.STROKE_PEN);
				break;
			case R.id.imgEraser:
				m_View.setStrokeType(m_View.STROKE_ERASER);
				break;
			case R.id.imgColor:
				Intent intentColor =new Intent(SketchActivity.this,GridViewColorActivity.class);
				startActivity(intentColor);
				//finish();
				break;
			case R.id.imgRedo:
				m_View.redo();
				break;
			case R.id.imgUndo:
				m_View.undo();
				break;
			default:
				break;
			}
			
		}
		
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				try {
					Bitmap bmp = null;
					Bundle bundle = data.getExtras();
					bmp = bundle.getParcelable("bmp");				
					//m_view.setForeBitmap(bmp);
					m_View.setBkBitmap(bmp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
				try {
					String filename = null;
					Bundle bundle = data.getExtras();
					filename = bundle.getString("filePath");
					
					File imagefile=new File(filename);
			        Bitmap bmp = m_View.getCanvasSnapshot();
			        if (null != bmp)
			        {
			        	FileOutputStream out;			        	
						try {
							out = new FileOutputStream(imagefile);
							bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
							out.flush();
							out.close();
							Log.d("TAG","成功保存图片");
						
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.d("TAG","没有保存图片");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.d("TAG","没有保存图片");
						}
			            //BitmapUtil.saveBitmapToSDCard(bmp, filename);
			            Toast.makeText(getApplicationContext(), "文件已存入"+filename, Toast.LENGTH_LONG).show();
			        }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}
	

	private class skbChangeListener implements OnSeekBarChangeListener {

		@SuppressWarnings("static-access")
		@Override
		public void onProgressChanged(SeekBar skbLine, int progress, boolean fromUser) {
			m_View.setStrokeSize((skbLine.getProgress()/40)*5+5, m_View.STROKE_PEN);
            m_View.setStrokeSize((skbLine.getProgress()/40)*5+5, m_View.STROKE_ERASER);
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
