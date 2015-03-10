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
	    
	    cameraIntent = new Intent(this, CameraActivity.class);
		
		startActivity(cameraIntent);
		finish();        
    }
}