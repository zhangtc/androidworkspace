package com.android.mybroadcastreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button1 = (Button) findViewById(R.id.button1);
		
		IntentFilter minteFilter  =  new IntentFilter();
		minteFilter.addAction("com.android.mybroadcastreceiver.mBroiBroadcastReceiver");
		registerReceiver(mBroiBroadcastReceiver, minteFilter);
		
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent mIntent = new Intent("com.android.mybroadcastreceiver.mBroiBroadcastReceiver");
				mIntent.putExtra("aaa", "11111");
				sendBroadcast(mIntent);
				
			}
		});
		
	}
	
	BroadcastReceiver mBroiBroadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			Toast.makeText(MainActivity.this, intent.getStringExtra("aaa"), Toast.LENGTH_SHORT).show();
			
			
		}
	};
	
	
	
}
