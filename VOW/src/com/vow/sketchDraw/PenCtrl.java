package com.vow.sketchDraw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.vow.sketchInterfaces.SketchPadInterface;

public class PenCtrl implements SketchPadInterface{
	
	private Path m_path=new Path();
	private Paint m_paint=new Paint();
	private boolean m_hasDrawn = false;
	private float mX=0, mY=0;
	private static final float TOUCH_TOLERANCE = 4;
	
	
	//设置画笔笔迹及颜色
	public PenCtrl(int penSize, int penColor)
	{
		m_paint.setAntiAlias(true);
		m_paint.setDither(true);
		m_paint.setColor(penColor);
		m_paint.setStyle(Paint.Style.STROKE);
		m_paint.setStrokeJoin(Paint.Join.ROUND);
		m_paint.setStrokeCap(Paint.Cap.ROUND);
		m_paint.setStrokeWidth(penSize);
	}
	@Override
	public void draw(Canvas canvas) {
		
		if (null != canvas)
		{
			canvas.drawPath(m_path, m_paint);
		}
		
	}

	@Override
	public boolean hasDraw() {
		
		return m_hasDrawn;
	
	}

	@Override
	public void cleanAll() {
		
		m_path.reset();
		
	}

	@Override
	public void touchDown(float x, float y) {
		//画线
		m_path.moveTo(x, y);
		m_path.lineTo(x, y);
        mX = x;
        mY = y;
		
	}

	@Override
	public void touchMove(float x, float y) {
		
		float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
        	//平滑曲线
            m_path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            m_hasDrawn = true;
            mX = x;
            mY = y;
            //两点练成线
            m_path.lineTo(x, y);
        }
		
	}

	@Override
	public void touchUp(float x, float y) {
		
		m_path.lineTo(mX, mY);
		mX=x;
		mY=y;
		
	}

}
