package com.insigma.service;

import com.insigma.service.service.MyService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;

public class MyService0513Activity extends Activity {
    /** Called when the activity is first created. */
	Intent intent;
	MyService  myService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MyService0513Activity.this, MyService.class) ;
				startService(intent);
			}
		});
        findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService(intent);
			}
		});
	findViewById(R.id.button3).setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					intent = new Intent(MyService0513Activity.this, MyService.class) ;
					MyService0513Activity.this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
				}
			});
	findViewById(R.id.button4).setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			unbindService(conn);
		}
	});
    }
    
    
    ServiceConnection conn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
				myService = ((MyService.LocalBind)service).getService();
		}
	};
}