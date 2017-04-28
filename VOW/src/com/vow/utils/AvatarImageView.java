package com.vow.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff; 
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AvatarImageView extends ImageView {  
   private Paint paint = new Paint();   
   public AvatarImageView(Context context) {  
        super(context);  
   }  

   public AvatarImageView(Context context, AttributeSet attrs) {  
       super(context, attrs);  
   }  
  public AvatarImageView(Context context, AttributeSet attrs, int defStyle) {  
       super(context, attrs, defStyle);  
    }  
  
   //灏嗗ご鍍忔寜姣斾緥缂╂斁  
    private Bitmap scaleBitmap(Bitmap bitmap){  
        int width = getWidth();  
        //涓�畾瑕佸己杞垚float 涓嶇劧鏈夊彲鑳藉洜涓虹簿搴︿笉澶�鍑虹幇 scale涓� 鐨勯敊璇� 
        float scale = (float)width/(float)bitmap.getWidth();         
        Matrix matrix = new Matrix();  
        matrix.postScale(scale, scale);  
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
  
    }  
    //灏嗗師濮嬪浘鍍忚鍓垚姝ｆ柟褰� 
   private Bitmap dealRawBitmap(Bitmap bitmap){  
        int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
        //鑾峰彇瀹藉害  
        int minWidth = width > height ?  height:width ;  
        //璁＄畻姝ｆ柟褰㈢殑鑼冨洿  
       int leftTopX = (width - minWidth)/2;  
        int leftTopY = (height - minWidth)/2;  
       //瑁佸壀鎴愭鏂瑰舰  
       Bitmap newBitmap = Bitmap.createBitmap(bitmap,leftTopX,leftTopY,minWidth,minWidth,null,false);  
        return  scaleBitmap(newBitmap);  
   }  
    @Override  
    protected void onDraw(Canvas canvas) {  
        Drawable drawable = getDrawable();  
        if (null != drawable) {  
            Bitmap rawBitmap =((BitmapDrawable)drawable).getBitmap();  
 
           //澶勭悊Bitmap 杞垚姝ｆ柟褰� 
           Bitmap newBitmap = dealRawBitmap(rawBitmap);  
           //灏唍ewBitmap 杞崲鎴愬渾褰� 
           Bitmap circleBitmap = toRoundCorner(newBitmap, 14);  
 
           final Rect rect = new Rect(0, 0, circleBitmap.getWidth(), circleBitmap.getHeight());  
            paint.reset();  
           //缁樺埗鍒扮敾甯冧笂  
            canvas.drawBitmap(circleBitmap, rect, rect, paint);  
        } else {  
            super.onDraw(canvas);  
        }  
   }  
  
    private Bitmap toRoundCorner(Bitmap bitmap, int pixels) {  

     //鎸囧畾涓�ARGB_4444 鍙互鍑忓皬鍥剧墖澶у皬  
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);  
       Canvas canvas = new Canvas(output);  

        final int color = 0xff424242;  
        final Rect rect = new Rect(0, 0,bitmap.getWidth(), bitmap.getHeight());  
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color);  
        int x = bitmap.getWidth();  
        canvas.drawCircle(x / 2, x / 2, x / 2, paint);  
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint);  
        return output;  
   }  
}  
