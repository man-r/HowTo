package com.example.testapp;

import java.io.IOException;

import  android.view.SurfaceView;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.content.Context;
import android.view.MotionEvent;
import android.widget.Toast;
import android.hardware.Camera.PreviewCallback;

import android.graphics.YuvImage;
import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.ImageFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Environment;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private static final String TAG = "MyMainActivity";

    public boolean isRecording = false;

    public void setIsRecording (boolean recording) {
        isRecording = recording;
    }
    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mCamera.setDisplayOrientation(90);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        //mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            // Thread thread = new Thread(new Runnable() {
            //     public void run() {
            //         mCamera.setPreviewCallback(new PreviewCallback() {
            //             public void onPreviewFrame(byte[] data, Camera camera){
            //                 if (isRecording) {
                                
            //                     Parameters parameters = camera.getParameters();
            //                     int imageFormat = parameters.getPreviewFormat();
            //                     Log.d(TAG,"imageFormat: " + imageFormat);
            //                     Log.d(TAG,"1onPreviewFrame called at: " + System.currentTimeMillis());
            //                 }

                            
            //             }
            //         });
            //     }
            // });
            // thread.start();
            
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        Log.d(TAG,"surfaceDestroyed");
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
          // preview surface does not exist
            Log.d(TAG,"preview surface does not exist: " + System.currentTimeMillis());
          return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            // Thread thread = new Thread(new Runnable() {
            //     public void run() {
            //         mCamera.setPreviewCallback(new PreviewCallback() {
            //             public void onPreviewFrame(byte[] data, Camera camera){
            //                 if (isRecording) {
            //                     Size previewSize = camera.getParameters().getPreviewSize(); 
            //                     YuvImage yuvimage=new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height, null);
            //                     ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //                     yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 80, baos);
            //                     byte[] jdata = baos.toByteArray();

            //                     // Convert to Bitmap
            //                     Bitmap bmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
                                

            //                     File pictureFile = getOutputMediaFile();
            //                     try {
            //                         FileOutputStream fos = new FileOutputStream(pictureFile);
            //                         fos.write(jdata);
            //                         fos.close();
            //                     } catch (FileNotFoundException e) {
            //                         //Log.d(TAG, "File not found: " + e.getMessage());
            //                     } catch (IOException e) {
            //                         //Log.d(TAG, "Error accessing file: " + e.getMessage());
            //                     }
                            

            //                     Parameters parameters = camera.getParameters();
            //                     int imageFormat = parameters.getPreviewFormat();
            //                     Log.d(TAG,"imageFormat: " + imageFormat);
            //                     Log.d(TAG,"2onPreviewFrame called at: " + System.currentTimeMillis());
            //                 }
            //             }
            //         });
            //     }
            // });
            // thread.start();
        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, " finger touches the screen");
                break;

            case MotionEvent.ACTION_MOVE:
                // finger moves on the screen
                Log.d(TAG, "finger moves on the screen");
                break;

            case MotionEvent.ACTION_UP:   
                // finger leaves the screen
                Log.d(TAG, "finger leaves the screen");
                break;
        }

        return true;
    }


    private static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
        

        return mediaFile;
    }

}