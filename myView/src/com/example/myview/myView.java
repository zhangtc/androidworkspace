package com.example.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class myView extends LinearLayout{
	private ImageView ivImageView;
	private TextView tView;

	public myView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.myview, this);
		ivImageView = (ImageView) findViewById(R.id.myiv);
		tView = (TextView) findViewById(R.id.mytv);
		
	}
	
	
	public void setImageResource(int id){
		
		ivImageView.setImageResource(id);
		
	}
	public void setTextView(String string){
		
		tView.setText(string);
		
	}

}
