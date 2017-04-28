package com.vow.sketchData;

public class VertexDefine {
	//定义坐标
		private float x;
		private float y;
		
		public VertexDefine(float x, float y) {
			this.x = x;
			this.y = y;
		}
		public VertexDefine() {
			this(0,0);
		}
		public float getX() {
			return x;
		}
		public void setX(float x) {
			this.x = x;
		}
		public float getY() {
			return y;
		}
		public void setY(float y) {
			this.y = y;
		}
		public void setPoint(float x,float y) {
			this.x = x;
			this.y = y;
		}	
}
