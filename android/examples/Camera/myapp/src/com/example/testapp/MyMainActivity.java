package com.example.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

public class MyMainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        
        Intent cameraIntent;
	    
	    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.KITKAT){
		    cameraIntent = new Intent(this, Camera2Activity.class);
		} else{
		    cameraIntent = new Intent(this, CameraActivity.class);
		}

		startActivity(cameraIntent);
		finish();        
    }
}