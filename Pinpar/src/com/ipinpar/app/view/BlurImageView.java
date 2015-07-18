package com.ipinpar.app.view;

import com.ipinpar.app.util.ImageBlurUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BlurImageView extends ImageView {

	public BlurImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public BlurImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (getDrawable() == null) {
            return;
        }
       Bitmap bitmap = Bitmap.createBitmap(getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight(), Config.ARGB_8888);
		Paint paint = new Paint();
		canvas.drawBitmap(ImageBlurUtil.doBlur(bitmap, 2, false), 0, 0, paint);
	}
	
	

}
