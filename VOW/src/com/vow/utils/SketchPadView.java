package com.vow.utils;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.etc.vow.R;
import com.vow.sketchData.CommonDef;
import com.vow.sketchDraw.BitmapCtrl;
import com.vow.sketchDraw.BitmapUtil;
import com.vow.sketchDraw.EraserCtrl;
import com.vow.sketchDraw.PenCtrl;
import com.vow.sketchInterfaces.SketchPadInterface;
import com.vow.sketchInterfaces.UndoRedoCommandInterface;

@SuppressLint("DrawAllocation") 
public class SketchPadView extends View implements UndoRedoCommandInterface{

	//初始化笔迹
	public static final int STROKE_PEN = 12;//自由笔
	public static final int STROKE_ERASER = 2;    //橡皮
	public static final int UNDO_SIZE = 20;       //撤销栈的大小
	public static final int BITMAP_WIDTH = 320;		//画布大小
	public static final int BITMAP_HEIGHT = 400;
    
	private int m_strokeType = STROKE_PEN;   //初始化绘制画布的自由笔
    private static int m_strokeColor = Color.RED;   //笔迹设置为红色
    private static int m_penSize = CommonDef.SMALL_PEN_WIDTH; //设置笔迹粗细
    private static int m_eraserSize = CommonDef.LARGE_ERASER_WIDTH;
    
    
    private boolean m_isEnableDraw = true;   //标记是否可以画
    private boolean m_isDirty = false;     //标记
    private boolean m_isTouchUp = false;    //标记鼠标是否抬起
    private boolean m_isSetForeBmp = false;   //是否设置了bitmap
    private int m_bkColor = Color.WHITE;   //设置背景
    
    private int m_canvasWidth = 100;    //画布大小
    private int m_canvasHeight = 100;
    private boolean m_canClear = true;   //标注是否可以清除
    
    private Bitmap m_foreBitmap = null;     //用于显示的bitmap
    private Bitmap m_tempForeBitmap = null; //用于缓冲的bitmap
    private Bitmap m_bkBitmap = null;       //背景bitmap
    
    private Canvas m_canvas;     //画布
    private Paint m_bitmapPaint = null;   //画笔
    private SketchPadUndoStack m_undoStack = null;//栈存放执行的操作
    private SketchPadInterface m_curTool = null;   //记录操作的对象画笔类  󻭱���
    
    int antiontemp = 0;//获取鼠标点击画布的event
 	private Bitmap bgBitmap = null;
    ///////////////// paint and Bk//////////////////////////////
    //画布参数设计
 	 public boolean isDirty(){
         return m_isDirty;   //
     }
     public void setDrawStrokeEnable(boolean isEnable){
         m_isEnableDraw = isEnable;  //确定是否可以绘图
     }   
     public void setBkColor(int color){
     	if (m_bkColor != color){
     		m_bkColor = color;
     		invalidate();
     	}
     }
     public static void setStrokeSize(int size, int type){
 		//设置画笔和橡皮擦大小
     	switch(type){
     	case STROKE_PEN:
     		m_penSize = size;
     		break;

     	case STROKE_ERASER:
     		m_eraserSize = size;
     		break;
     	}
     }

     public static void setStrokeColor(int color){ 
    	 //设置颜色
     	m_strokeColor = color;
     }

     public static int getStrokeSize(){ 
    	//设置笔迹
     	return m_penSize;
     }

     public static int getEraser(){  
    	//橡皮尺寸
     	return  m_eraserSize;
     }
     public static int getStrokeColor(){ 
    	 //返回笔迹颜色
     	return m_strokeColor;
     }
     ////////////////////////////////////////////////////////////
     public void clearAllStrokes(){ 
    	 //清空所有笔迹
     	if (m_canClear){
     		// 清空撤销栈
     		m_undoStack.clearAll();  
     		// 设置当前的bitmap对象为空
     		if (null != m_tempForeBitmap){
     			m_tempForeBitmap.recycle();
     			m_tempForeBitmap = null;
     		} 
     		// Create a new fore bitmap and set to canvas.
     		createStrokeBitmap(m_canvasWidth, m_canvasHeight);

     		invalidate();
     		m_isDirty = true;
     		m_canClear = false;
     	}
     }
   //构造方法
    public SketchPadView(Context context) {
 		super(context);
 		// TODO Auto-generated constructor stub
 		initialize();
 	}
    public SketchPadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		 bgBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.pic1)).getBitmap();
		// TODO Auto-generated constructor stub
		initialize();
	}
    public SketchPadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initialize();
	}
    
	@Override
	public boolean canRedo() {
		// 是否可以重做
		if (null != m_undoStack){
			return m_undoStack.canUndo();
		}
		return false;
	}	
	public boolean canUndo() {
		// 是否可以撤销
		if (null != m_undoStack){
            return m_undoStack.canRedo();
        }
		return false;
	}
	public void onDeleteFromRedoStack() {
		// TODO Auto-generated method stub	
	}	
	public void onDeleteFromUndoStack() {
		// TODO Auto-generated method stub		
	}	
	public void redo() {
		// TODO Auto-generated method stub
		if (null != m_undoStack){
            m_undoStack.redo();
        }
	}	
	public void undo() {
		// TODO Auto-generated method stub
		if (null != m_undoStack){
            m_undoStack.undo();
            Log.i("sada022", "undo00");
        }
	}	
	
	///////////////////////bitmap/////////////////////
    //保存时对当前绘图板的图片进行快照
    public Bitmap getCanvasSnapshot(){
        setDrawingCacheEnabled(true);
        buildDrawingCache(true);
        Bitmap bmp = getDrawingCache(true);   
        if (null == bmp){
            android.util.Log.d("leehong2", "getCanvasSnapshot getDrawingCache == null");
        }      
        return BitmapCtrl.duplicateBitmap(bmp);
    } 
    /*打开图像文件时，设置当前视图为foreBitmap*/
    public void setForeBitmap(Bitmap foreBitmap){	
        if (foreBitmap != m_foreBitmap && null != foreBitmap){
        	// Recycle the bitmap.
            if (null != m_foreBitmap){
            	m_foreBitmap.recycle();
            }
            // Here create a new fore bitmap to avoid crashing when set bitmap to canvas. 
            m_foreBitmap = BitmapUtil.duplicateBitmap(foreBitmap);
            if (null != m_foreBitmap && null != m_canvas){
            	m_canvas.setBitmap(m_foreBitmap);
            }    
            invalidate();
        }
    }    
    public Bitmap getForeBitmap(){
        return m_bkBitmap;
    }
    public void setBkBitmap(Bitmap bmp){   //设置背景bitmap
        if (m_bkBitmap != bmp){
            m_bkBitmap = BitmapUtil.duplicateBitmap(bmp);
            invalidate();
        }
    }
    public Bitmap getBkBitmap(){
        return m_bkBitmap;
    }         
    protected void createStrokeBitmap(int w, int h){
        m_canvasWidth = w;
        m_canvasHeight = h;  
        Bitmap bitmap = Bitmap.createBitmap(m_canvasWidth, m_canvasHeight, Bitmap.Config.ARGB_8888);
        if (null != bitmap){
            m_foreBitmap = bitmap;
            // Set the fore bitmap to m_canvas to be as canvas of strokes.
            m_canvas.setBitmap(m_foreBitmap);
        }
    }
    protected void setTempForeBitmap(Bitmap tempForeBitmap){
        if (null != tempForeBitmap){
            if (null != m_foreBitmap){
                m_foreBitmap.recycle();
            }            
            m_foreBitmap = BitmapCtrl.duplicateBitmap(tempForeBitmap);  
            if (null != m_foreBitmap && null != m_canvas) {
                m_canvas.setBitmap(m_foreBitmap);
                invalidate();
            }
        }
    }
    
    protected void setCanvasSize(int width, int height)
    {//设置画布大小
        if (width > 0 && height > 0){
            if (m_canvasWidth != width || m_canvasHeight != height){
                m_canvasWidth = width;
                m_canvasHeight = height;
                createStrokeBitmap(m_canvasWidth, m_canvasHeight);
            }
        }
    }
    //初始化
	protected void initialize(){
		
		m_canvas = new Canvas();
		m_bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
		m_undoStack = new SketchPadUndoStack(this, UNDO_SIZE);

	}
    //设置画笔的大小和颜色
    public void setStrokeType(int type){
    	m_strokeColor=SketchPadView.getStrokeColor();
	    m_penSize=SketchPadView.getStrokeSize();
    	switch(type){
    	case STROKE_PEN:
    		m_curTool = new PenCtrl(m_penSize, m_strokeColor);
    		Log.i("sada022", "pen实例化");
    		break;
    	case STROKE_ERASER:
    		m_curTool = new EraserCtrl(m_eraserSize);
    		break;
    	}
    	
    	m_strokeType = type;
    }
	/////////////undo/////////////
	public class SketchPadUndoStack{
		private int m_stackSize = 0;   //撤销栈大小
		private SketchPadView m_sketchPad = null;  
		private ArrayList<SketchPadInterface> m_undoStack = new ArrayList<SketchPadInterface>();
		private ArrayList<SketchPadInterface> m_redoStack = new ArrayList<SketchPadInterface>();
		private ArrayList<SketchPadInterface> m_removedStack = new ArrayList<SketchPadInterface>();

		public SketchPadUndoStack(SketchPadView sketchPad, int stackSize){
			m_sketchPad = sketchPad;
			m_stackSize = stackSize;
		}
		public void push(SketchPadInterface sketchPadTool){    
			if (null != sketchPadTool){
				if (m_undoStack.size() == m_stackSize && m_stackSize > 0){
					SketchPadInterface removedTool = m_undoStack.get(0);
					m_removedStack.add(removedTool);
					m_undoStack.remove(0);
				}               
				m_undoStack.add(sketchPadTool);
			}
		}
		
		public void clearAll(){
			m_redoStack.clear();
			m_undoStack.clear();
			m_removedStack.clear();
		}
		public void undo(){
			if (canUndo() && null != m_sketchPad){        
				Log.i("sada022", "undo���");
				SketchPadInterface removedTool = m_undoStack.get(m_undoStack.size() - 1);
				m_redoStack.add(removedTool);
				m_undoStack.remove(m_undoStack.size() - 1);

				if (null != m_tempForeBitmap){
					// Set the temporary fore bitmap to canvas.                    
					m_sketchPad.setTempForeBitmap(m_sketchPad.m_tempForeBitmap);
				}
				else{
					// Create a new bitmap and set to canvas.
					m_sketchPad.createStrokeBitmap(m_sketchPad.m_canvasWidth, m_sketchPad.m_canvasHeight);
				}
				Canvas canvas = m_sketchPad.m_canvas;                
				// First draw the removed tools from undo stack.
				for (SketchPadInterface sketchPadTool : m_removedStack){
					sketchPadTool.draw(canvas);
				}          
				for (SketchPadInterface sketchPadTool : m_undoStack){
					sketchPadTool.draw(canvas);
				}  
				m_sketchPad.invalidate();
			}
		}

		public void redo(){
			if (canRedo() && null != m_sketchPad){
				SketchPadInterface removedTool = m_redoStack.get(m_redoStack.size() - 1);
				m_undoStack.add(removedTool);
				m_redoStack.remove(m_redoStack.size() - 1);

				if (null != m_tempForeBitmap){
					// Set the temporary fore bitmap to canvas.                    
					m_sketchPad.setTempForeBitmap(m_sketchPad.m_tempForeBitmap);
				}
				else{
					// Create a new bitmap and set to canvas.
					m_sketchPad.createStrokeBitmap(m_sketchPad.m_canvasWidth, m_sketchPad.m_canvasHeight);
				}             
				Canvas canvas = m_sketchPad.m_canvas;

				// First draw the removed tools from undo stack.
				for (SketchPadInterface sketchPadTool : m_removedStack){
					sketchPadTool.draw(canvas);
				}               
				for (SketchPadInterface sketchPadTool : m_undoStack){
					sketchPadTool.draw(canvas);
				}             
				m_sketchPad.invalidate();
			}
		}
		public boolean canUndo(){
			return (m_undoStack.size() > 0);	       
		}
		public boolean canRedo(){
			return (m_redoStack.size() > 0);
		}
	}
	///////////////////////////////////////////  
	@SuppressLint("DrawAllocation") 
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		if (null != m_bkBitmap){
			RectF dst = new RectF(getLeft(), getTop(), getRight(), getBottom());
			Rect  rst = new Rect(0, 0, m_bkBitmap.getWidth(), m_bkBitmap.getHeight());
			canvas.drawBitmap(m_bkBitmap, rst, dst, m_bitmapPaint);
		}
		if (null != m_foreBitmap){
			canvas.drawBitmap(m_foreBitmap, 0, 0, m_bitmapPaint);
		}
		if (null != m_curTool){
			if (STROKE_ERASER != m_strokeType){
				if (!m_isTouchUp){   
					m_curTool.draw(canvas);
				}
			}
		}
	}
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		if (!m_isSetForeBmp){
			setCanvasSize(w, h);
		}
		Log.i("sada022", "Canvas");
		m_canvasWidth = w;
		m_canvasHeight = h;
		m_isSetForeBmp = false;
	}
	public boolean onTouchEvent(MotionEvent event) {
		
		if (m_isEnableDraw)   
		{
			m_isTouchUp = false;  
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:			
				setStrokeType(m_strokeType);
				m_curTool.touchDown(event.getX(), event.getY());
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				m_curTool.touchMove(event.getX(), event.getY());	
				if (STROKE_ERASER == m_strokeType){
					m_curTool.draw(m_canvas);
				}
				invalidate();
				m_isDirty = true;
				m_canClear = true;
				break;
			case MotionEvent.ACTION_UP:
				m_isTouchUp = true;
				if (m_curTool.hasDraw()){
					m_undoStack.push(m_curTool);
				}
				m_curTool.touchUp(event.getX(), event.getY());
				// Draw strokes on bitmap which is hold by m_canvas.
				m_curTool.draw(m_canvas);
				invalidate();
				m_isDirty = true;
				m_canClear = true;		
				break;
		}
	}
	return true;
	}
}
