package com.vow.sketchInterfaces;

import android.graphics.Canvas;
//绘制图像接口
public interface SketchPadInterface {
	public void draw(Canvas canvas);
	public boolean hasDraw();
	public void cleanAll();
	public void touchDown(float x, float y);
	public void touchMove(float x, float y);
	public void touchUp(float x, float y);
}
