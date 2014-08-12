package com.example.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
String tag ="service";
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(tag, "onbind");
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(tag, "onCreate");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.i(tag, "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onRebind(Intent intent) {
		Log.i(tag, "onRebind");
		super.onRebind(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(tag, "onStartCommand");
		String str = intent.getStringExtra("start");
		System.out.println("---------------onstart=="+str);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(tag, "onUnbind");
		return super.onUnbind(intent);
	}
	

}
class AAA implements MyInterface{

	@Override
	public String str() {
		
		System.out.println("我的接口");
		return "12312354658525";
	}
	
	
	
}
