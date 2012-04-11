package com.victorvieux.fbsdkchecker;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FBSDKCheckerService extends Service{
	final String LOG_TAG = "FBSDKCheckerService";
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStart(intent, startId);
		
		Thread t = new Thread(){
			public void run() {
				Process process = null;
				BufferedReader reader = null;
				String line = null;
				while (true)
				{
					try {

						process = Runtime.getRuntime().exec("/system/bin/logcat -v time -b main");
						
						reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
						
			            while ((line = reader.readLine()) != null)
			            {
			                    if (line.contains("Login Success!") || line.contains("access_token"))
			                    {
			                    	displayNotification(line);
			                    }
			            }


					} catch (Exception e) {
						Log.e(LOG_TAG, e.getMessage());
						e.printStackTrace();
					}	
				}
			}
		};
		t.start();
	    return START_STICKY;
	}
	
	public void displayNotification(String line) {
			Notification  notification = new Notification(R.drawable.ic_launcher, getString(R.string.title) ,System.currentTimeMillis());;
			notification.defaults = Notification.DEFAULT_ALL;
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			Intent i = new Intent(this, FBSDKCheckerActivity.class);
			i.putExtra("line", line);
			notification.setLatestEventInfo(this, getString(R.string.app_name), getString(R.string.title), PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_ONE_SHOT));
			((NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0x42, notification);
	}
}
