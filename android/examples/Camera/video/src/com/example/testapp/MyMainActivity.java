package com.example.testapp;

import android.app.Activity;
import android.os.Bundle;

import android.content.Context;
import android.content.pm.PackageManager;

import android.hardware.Camera;
import android.media.CamcorderProfile;

import android.widget.FrameLayout;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;

import android.util.Log;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import android.net.Uri;
import android.os.Environment;
import java.util.Date;
import java.text.SimpleDateFormat;

//onPreviewFrame
public class MyMainActivity extends Activity
{
	private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mMediaRecorder;
    private MediaRecorder mRecorder = null;

    private boolean isRecording = false;

    private static final String TAG = "MyMainActivity";
    public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int MEDIA_TYPE_AUDIO = 3;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        checkCameraHardware(this);
        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        // Add a listener to the Capture button
		Button captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(
		    new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {

		            if (isRecording) {
		            	Log.d(TAG, "isRecording");
		                // // stop recording and release camera
		                // mMediaRecorder.stop();  // stop the recording
		                // releaseMediaRecorder(); // release the MediaRecorder object
		                // mCamera.lock();         // take camera access back from MediaRecorder

		                // // inform the user that recording has stopped
		                // //setCaptureButtonText("Capture");

		                stopRecording();
		                isRecording = false;
		            } else {
		            	Log.d(TAG, "isRecording NOT");
		            	startRecording();
		                // // initialize video camera
		                // if (prepareVideoRecorder()) {
		                // 	Log.d(TAG, "prepareVideoRecorder");
		                //     // Camera is available and unlocked, MediaRecorder is prepared,
		                //     // now you can start recording
		                //     mMediaRecorder.start();

		                //     // inform the user that recording has started
		                //     //setCaptureButtonText("Stop");
		                //     isRecording = true;
		                // } else {
		                //     // prepare didn't work, release the camera
		                //     releaseMediaRecorder();
		                //     // inform user
		                // }
		                isRecording = true;
		            }

		            //isRecording = !isRecording;
		            mPreview.setIsRecording(isRecording);
		        }
		    }
		);
    }

    /** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        Log.d(TAG, "this device has a camera");
	        return true;
	    } else {
	        // no camera on this device
	        Log.d(TAG, "no camera on this device");
	        return false;
	    }
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	    	Log.d(TAG, "not null");
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}


	private boolean prepareVideoRecorder(){

	    //mCamera = getCameraInstance();
	    mMediaRecorder = new MediaRecorder();

	    // Step 1: Unlock and set camera to MediaRecorder
	    mCamera.unlock();
	    mMediaRecorder.setCamera(mCamera);

	    // Step 2: Set sources
	    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
	    mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

	    // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
	    mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

	    // Step 4: Set output file
	    mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

	    // Step 5: Set the preview output
	    mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

	    // Step 6: Prepare configured MediaRecorder
	    try {
	        mMediaRecorder.prepare();
	    } catch (IllegalStateException e) {
	        Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	    } catch (IOException e) {
	        Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	    }
	    return true;
	}

	@Override
    protected void onPause() {
        super.onPause();
        //releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            //mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    /** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else if(type == MEDIA_TYPE_AUDIO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "AUD_"+ timeStamp + ".mp3");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}


	private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_AUDIO).toString());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}
