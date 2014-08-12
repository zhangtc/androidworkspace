package com.example.mycanvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class myCanvas extends View {

	public myCanvas(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		canvas.drawColor(Color.WHITE);
		canvas.drawRect(100, 100, 300, 300, paint);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		canvas.drawBitmap(bitmap, 100, 350, paint);
		canvas.drawLine(100, 450, 350, 450, paint);
		canvas.drawText("hello world", 100, 500, paint);
		
		super.onDraw(canvas);
	}

}
