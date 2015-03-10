package com.example.testapp;

import android.app.Activity;
import android.os.Bundle;

import android.content.Context;
import android.content.pm.PackageManager;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Button;


import java.io.File;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

//onPreviewFrame
public class CameraActivity extends Activity
{
	private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mMediaRecorder;
    private MediaRecorder mRecorder = null;

    private boolean isRecording = false;

    private static final String TAG = "MyMainActivity";
    private static String SOCKET_ADDRESS = "your.local.socket.address";
    private static String hostname = "192.168.1.31";
	
    private static final int MEDIA_TYPE_IMAGE = 1;
	private static final int MEDIA_TYPE_VIDEO = 2;
	private static final int MEDIA_TYPE_AUDIO = 3;
	private static final int port = 8000;

	private LocalServerSocket server;
	private LocalSocket sender;
	private LocalSocket receiver;

	private ParcelFileDescriptor pfd = null;
	private SSLSocket  socket = null;
	private SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
	
	private FileOutputStream inputStream;
	
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
		            	// stop recording and release camera
		                Log.d(TAG, "isRecording: ");
		            	
		                releaseMediaRecorder(); // release the MediaRecorder object

		                try {
		                	if (socket != null) {
		                		socket.close();
		                	}
		                } catch (IOException e) {
		                	Log.d(TAG, "IOException: " + e.getMessage());
		                }

	                	mCamera.lock();         // take camera access back from MediaRecorder
		                isRecording = false;

		            } else {
		            	// initialize video camera
		                if (prepareVideoRecorder()) {
		                    // Camera is available and unlocked, MediaRecorder is prepared,
		                    // now you can start recording
		                    mMediaRecorder.start();

		                    // inform the user that recording has started
		                    //setCaptureButtonText("Stop");
		                    isRecording = true;
		                } else {
		                    // prepare didn't work, release the camera
		                    releaseMediaRecorder();
		                    // inform user
		                }
		            }
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
	    try {
		    
		    // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
   			
   			

			mMediaRecorder.setOutputFile(getFileDescriptorFromFileOutputStream());
	    	//mMediaRecorder.setOutputFile(getFileDescriptorFromLocalServerSocket());
   			//mMediaRecorder.setOutputFile(getFileDescriptorFromParcelFileDescriptorSSLSock());
			//mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

		    // Step 5: Set the preview output
		    mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

		    // Step 6: Prepare configured MediaRecorder
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
        	Log.d(TAG, "releaseMediaRecorder");
	        mMediaRecorder.stop();  // stop the recording
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

    private FileDescriptor getFileDescriptorFromParcelFileDescriptorSSLSock() {
    	try {
			socket = (SSLSocket) f.createSocket(hostname, port);
			pfd = ParcelFileDescriptor.fromSocket(socket);

		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			Log.d(TAG, "UnknownHostException");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Log.d(TAG, "IOException: " + e1.toString());
		}

		return pfd.getFileDescriptor();
    }

    private FileDescriptor getFileDescriptorFromFileOutputStream() throws IOException{
    	File file = getOutputMediaFile(MEDIA_TYPE_VIDEO);
    	inputStream = new FileOutputStream(file);

    	new Thread(new Runnable() {
    		public void run() {

    			try {
    				while (true) {
			            //LocalSocket receiver = server.accept();
			            if (receiver != null) {
			                FileInputStream input = new FileInputStream(inputStream.getFD());
			                
			                // simply for java.util.ArrayList
			                int readed = input.read();
			                int size = 0;
			                int capacity = 0;
			                byte[] bytes = new byte[capacity];
			                
			                // reading
			                while (readed != -1) {
			                    // java.util.ArrayList.Add(E e);
			                    capacity = (capacity * 3)/2 + 1;
			                    //bytes = Arrays.copyOf(bytes, capacity);
			                    byte[] copy = new byte[capacity];
			                    System.arraycopy(bytes, 0, copy, 0, bytes.length);
			                    bytes = copy;
			                    bytes[size++] = (byte)readed;
			                    
			                    // read next byte
			                    readed = input.read();
			                }
			                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");

			                File file = new File(mediaStorageDir.getPath() + File.separator +"manar.mp4");
			    			FileOutputStream outStream = new FileOutputStream(file);
			    			outStream.write(bytes);
			            }
			        }
    			} catch (IOException e) {

    			}	
    		}
    	}).start();

    	return inputStream.getFD();
    }
    private FileDescriptor getFileDescriptorFromLocalServerSocket () throws IOException {
    	server = new LocalServerSocket(SOCKET_ADDRESS);
    	sender = new LocalSocket();

    	sender.connect(server.getLocalSocketAddress());

    	new Thread(new Runnable() {
    		public void run() {

    			try {
    				while (true) {
			            LocalSocket receiver = server.accept();
			            if (receiver != null) {
			                InputStream input = receiver.getInputStream();
			                
			                // simply for java.util.ArrayList
			                int readed = input.read();
			                int size = 0;
			                int capacity = 0;
			                byte[] bytes = new byte[capacity];
			                
			                // reading
			                while (readed != -1) {
			                    // java.util.ArrayList.Add(E e);
			                    capacity = (capacity * 3)/2 + 1;
			                    //bytes = Arrays.copyOf(bytes, capacity);
			                    byte[] copy = new byte[capacity];
			                    System.arraycopy(bytes, 0, copy, 0, bytes.length);
			                    bytes = copy;
			                    bytes[size++] = (byte)readed;
			                    
			                    // read next byte
			                    readed = input.read();
			                }
			                
			                File file = getOutputMediaFile(MEDIA_TYPE_VIDEO);
			    			FileOutputStream outStream = new FileOutputStream(file);
			    			outStream.write(bytes);
			            }
			        }
    			} catch (IOException e) {

    			}	
    		}
    	}).start();
    	
    	
    	return server.getFileDescriptor();
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
}
