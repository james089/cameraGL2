package com.bojun.cameragl2;

import com.bojun.cameragl2.R;
import com.bojun.cameragl2.CameraPreview;


import android.hardware.Camera;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CameraMainActivity extends Activity {

	
	private Camera mCamera;
    private CameraPreview mPreview;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_main);
		
		//check if there is a camera
				if (checkCameraHardware(this)==true)
				{
					Toast.makeText(this, "Camera detected", Toast.LENGTH_LONG).show();;
				}
				
					
		        getCameraPreview();
		        
		        /** button 1*/
				Button captureButton = (Button) findViewById(R.id.button_scan);
				captureButton.setOnClickListener(
				new View.OnClickListener()
				{
				  @Override
				  public void onClick(View v) 
				  {	
				    recordvideo();
				  }
				}
				);
				
				/** button 2*/
				Button browseButton = (Button) findViewById(R.id.button_browse);
				browseButton.setOnClickListener(
				new View.OnClickListener() 
				{
				  @Override
				  public void onClick(View v) 
				  {	
				     browse();
				                	//setTitle("…µ±∆µ„Œ““™À¿∞°£°£°£°");
				  }
				}
			    );		        
					
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_camera_main, menu);
		return true;
	}

	/** check camera */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}
	
	
	private void getCameraPreview()
	{
		//=======================<show camera preview=============================================================    
	   
	     mCamera = getCameraInstance();
	     mPreview = new CameraPreview(this, mCamera);  //new preview
		 FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview); //find the widget
		 preview.addView(mPreview); //show preview
	//=======================</show camera preview>============	
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
	
	public void recordvideo()
	{
		Intent record = new Intent(CameraMainActivity.this, RecordVideoActivity.class);
		startActivity(record);
	}
	
	public void browse()
	{
		Intent browse = new Intent(CameraMainActivity.this, BrowseActivity.class);
		startActivity(browse);
	}
	

	@Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }
    protected void onStop() {
        super.onPause();
        releaseCamera();
       // mCamera.stopPreview();// release the camera immediately on pause event
    }
	/*
	@Override
	public void onRestart() {
	    super.onRestart();  // Always call the superclass method first

	    // Get the Camera instance as the activity achieves full user focus
	    if (mCamera == null) {
	    	mCamera = getCameraInstance(); // Local method to handle camera init
	    }
	}
   */
	private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

}
