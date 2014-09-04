package com.insigma.service.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {
	private Thread t;
	private MyThread myThread;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		System.out.println("MyService onBind");
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		System.out.println("MyService onCreate");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("MyService onDestroy");
		if(myThread != null){
		myThread.setNeedStop(true);
		t = null;
	}
		super.onDestroy();
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("MyService onRebind");
		super.onRebind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		System.out.println("MyService onStart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(t == null){
		myThread = new MyThread();
		t = new Thread(myThread);
		t.start();
	}
		System.out.println("MyService onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("MyService onUnbind");
		return super.onUnbind(intent);
	}
	
	public class LocalBind extends Binder{
		
		public MyService getService(){
			
			return MyService.this;
		}
	}
	

	class MyThread implements Runnable{
		private boolean isNeedStop;
		public void setNeedStop(boolean isNeedStop){
			this.isNeedStop = isNeedStop; 
		}
		
		@Override
		public void run() {
			int i = 0;
			while(!isNeedStop && i++ < 10){
				try {
					Thread.sleep(1000);
					System.out.println("i===="+i);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			System.out.println("耗时动作已完成");
		}
		
	}
}
