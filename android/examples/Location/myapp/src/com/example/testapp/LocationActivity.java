package com.example.testapp;

import android.app.Activity;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity
{
	LocationManager locationManager;
	LocationListener locationListener;
	Criteria criteria;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		criteria = new Criteria();
        
        criteria.setSpeedRequired(true);
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
    }

    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		        
        String bestProvider = locationManager.getBestProvider(criteria, true);
       
        if ((bestProvider != null) && (bestProvider.contains("gps"))){
        	locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
        }
        else{
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("No GPS!")
        	       .setCancelable(true)
        	       .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	        	   Intent switchIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        	               startActivityForResult(switchIntent, 0);
        	           }
        	       })
        	       .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	        	   locationManager.removeUpdates(locationListener);
           	       		
	        	       	   finish();
        	           }
        	       });
        	AlertDialog alert = builder.create();
        	alert.show();
        }
           	
        //Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPause() {
		locationManager.removeUpdates(locationListener);
		super.onPause();
	}
}
