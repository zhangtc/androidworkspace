package com.example.servicetest;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class MainActivity extends Activity {
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button bu1 = (Button) findViewById(R.id.bu1);
		Button bu2 = (Button) findViewById(R.id.bu2);
		
		final MyInterface inter = new AAA();
		
		
		intent = new Intent(this, MyService.class);
		bu1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent.putExtra("start", "start");
				startService(intent);
				System.out.println("--------------inter == "+inter.str());
			}
		});
		
		bu2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent.putExtra("stop", "stop");
			stopService(intent);
				
			}
		});
		
	}




}
