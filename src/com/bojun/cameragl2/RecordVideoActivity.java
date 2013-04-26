package com.bojun.cameragl2;

import java.io.File;
import java.io.IOException;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;


public class RecordVideoActivity extends Activity {

	private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mMediaRecorder = new MediaRecorder();
	public static final int MEDIA_TYPE_VIDEO = 2;
	protected static final String TAG = "Main";
	private static final int STOP = 2;
	private static final int START = 1;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_video);


		  mCamera = getCameraInstance();
		  mPreview = new CameraPreview(this, mCamera);  //new preview
		  FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview2); //find the widget
		  preview.addView(mPreview); //show preview	
	 
		  Button startButton = (Button) findViewById(R.id.button_start);
          startButton.setOnClickListener(
	            new View.OnClickListener() 
	            {
	                @Override
	                public void onClick(View v) 
	                {	
	          		  startrecording(); 
	                  showText(START);
	                }
	            }
	            );

          
          Button stopButton = (Button) findViewById(R.id.button_stop);
          stopButton.setOnClickListener(
	            new View.OnClickListener() 
	            {
	                @SuppressLint("NewApi")
					@Override
	                public void onClick(View v) 
	                {	
	                   stoprecording();
	     			   showText(STOP);
	     			   finish();			  
	                }
	            }
	            );
	            
	            	
	}
	
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
        	return1();
        	
        }
            return false;
    }      

    public void return1()
	{
		Intent return1 = new Intent(this, CameraMainActivity.class);
		startActivity(return1);
	}
    
	private void startrecording() {
		
		mCamera.unlock();
  	    mMediaRecorder.setCamera(mCamera);

  	    // Step 2: Set sources
  	    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
  	    mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);      	 

  	    // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
  	 //   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));           	
  	    
	    mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	    mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	    mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
	    
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

  	    } catch (IOException e) {

  	        Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());

  	        releaseMediaRecorder();

  	    }

      	mMediaRecorder.start();//start recording
		
	}


	private void stoprecording()
	{
		mMediaRecorder.stop();
    	mMediaRecorder.release();
    	mCamera.lock(); 
    	mCamera.release();
  	}
	
	/** open camera */

	 @SuppressLint("NewApi")
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(0); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	 
	 private static File getOutputMediaFile(int type){
		    // To be safe, you should check that the SDCard is mounted
		    // using Environment.getExternalStorageState() before doing this.

		    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
		              Environment.DIRECTORY_PICTURES), "CameraGL");
		    // This location works best if you want the created images to be shared
		    // between applications and persist after your app has been uninstalled.

		    // Create the storage directory if it does not exist
		    if (! mediaStorageDir.exists()){
		        if (! mediaStorageDir.mkdirs()){
		            Log.d("CameraGL", "failed to create directory");
		            return null;
		        }
		    }
		    

		    // Create a media file name
		  //  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(0));
		    Time time = new Time("GMT-7");
		    time.setToNow(); 
		    String timeStamp = ""+time.year+time.month+time.monthDay+time.hour+time.minute+time.second;

		   	File mediaFile;
		    if(type == MEDIA_TYPE_VIDEO) {
		        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
		        "VID_"+ timeStamp + ".mp4");
		    } else {
		        return null;
		    }

		    return mediaFile;
		}
	
	 private void showText(int number)
	    {
	    	int number1 = number;
	    	switch (number1)
	    	{
	    	case 1: Toast.makeText(this, "Start scanning", Toast.LENGTH_LONG).show();break;
	    	case 2: Toast.makeText(this, "Stop scanning", Toast.LENGTH_LONG).show();break;
	    	default:;break;
	    	}
	    	
	    }
	    
	@Override
	protected void onPause() {
	        super.onPause();
	        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
	        releaseCamera(); 
	    }
	
	@Override
    protected void onStop() {
        super.onStop();
        releaseCamera(); 
    }
	
	 
	private void releaseMediaRecorder(){
	        if (mMediaRecorder != null) {
	        	mMediaRecorder.reset();   // clear recorder configuration
	        	mMediaRecorder.release(); // release the recorder object
	        	mMediaRecorder = null;
	            mCamera.lock();           // lock camera for later use
	        }
	    }	
	
	private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;

            
        }
    }

	

}
