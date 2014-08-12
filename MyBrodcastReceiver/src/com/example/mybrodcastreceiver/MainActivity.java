package com.example.mybrodcastreceiver;

import java.util.zip.Inflater;

import com.example.mybroadcastreceiver.myBrodcastReceiver;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
	myBrodcastReceiver myReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("myReceiver");
				intent.putExtra("name", "nishishui");
				sendBroadcast(intent);
				
				
			}
		});
		
		
	}
	
	@Override
	protected void onResume() {
		
		myReceiver = new myBrodcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("myReceiver");
		registerReceiver(myReceiver, filter);
		
		
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		unregisterReceiver(myReceiver);
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
