package com.example.myattrs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class myView extends View {
	private String mString;
	private int textSize;
	public myView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray taArray = context.obtainStyledAttributes(attrs,
				R.styleable.ToolBar);
		mString = taArray.getString(R.styleable.ToolBar_myname);
		textSize = taArray.getInteger(R.styleable.ToolBar_namesize, 20);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setTextSize(textSize);
		canvas.drawText(mString, 100, 100, paint);
		
		super.onDraw(canvas);
	}
}
