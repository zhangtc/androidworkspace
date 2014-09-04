package com.insigma.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
public class MyServiceTestActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	MyService myservice = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.srart).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.bind).setOnClickListener(this);
        findViewById(R.id.unbind).setOnClickListener(this);
        
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MyServiceTestActivity.this, MyService.class);
		switch (v.getId()) {
		case R.id.srart:
			startService(intent);
			break;
		case R.id.stop:
			stopService(intent);
			break;
		case R.id.bind:
			this.bindService(intent, con, Context.BIND_AUTO_CREATE);
			break;
		case R.id.unbind:
			unbindService(con);
			break;
		default:
			break;
		}
	}
	
	ServiceConnection con = new ServiceConnection() {
		
		public void onServiceDisconnected(ComponentName name) {
			
		}
		
		public void onServiceConnected(ComponentName name, IBinder service) {
			myservice = ((MyService.LocalBinder)service).getService();
			
		}
	};
}