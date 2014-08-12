package com.example.mybroadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class myBrodcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		
		Toast.makeText(arg0,"name: "+arg1.getStringExtra("name") , Toast.LENGTH_LONG).show();
	}

}
