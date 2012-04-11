package com.victorvieux.fbsdkchecker;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class FBSDKCheckerActivity extends Activity implements OnCheckedChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButtonService);
        toggleButton.setChecked(isServiceRunning());
        
        toggleButton.setOnCheckedChangeListener(this);
        
        handleIntent(getIntent());
        
    }
    
    @Override
    public void onNewIntent(Intent i) {
    	handleIntent(i);
    }
    
    private void handleIntent(Intent i) {
    	
    	if (i != null && i.getStringExtra("line") != null) {
    		String line = i.getStringExtra("line");
    		((TextView)findViewById(R.id.textViewLine)).setText(line);
    		((TextView)findViewById(R.id.textViewDate)).setText(getString(R.string.warning,  line.split(" ")[1]));
    		findViewById(R.id.imageViewLogo).setVisibility(View.VISIBLE);
    	}
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Intent service = new Intent(this, FBSDKCheckerService.class);
		if (isChecked)
			startService(service);
		else {
			stopService(service);
    		((TextView)findViewById(R.id.textViewLine)).setText("");
    		((TextView)findViewById(R.id.textViewDate)).setText("");
    		findViewById(R.id.imageViewLogo).setVisibility(View.GONE);
		}
	}
	
	
	public boolean isServiceRunning(){
	    final ActivityManager activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
	    final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

	    for (RunningServiceInfo runningServiceInfo : services) {
	        if (runningServiceInfo.service.getClassName().equals(getPackageName()+".FBSDKCheckerService")){
	            return true;
	        }
	    }
	    return false;
	 }
}