package com.bojun.cameragl2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
 
public class BrowseActivity extends ListActivity {
    private List<String> items = null;
	private List<String> paths = null;
    private String rootpath = "/sdcard/Pictures/CameraGL/";
    private TextView mpath;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        setTitle("Scan Files");   
        mpath = (TextView) findViewById(R.id.mpath);
        getFileDir(rootpath);
        
    }
    
    /** Listen to back button*/
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
        	return1();
        	
        }
            return false;
    }      


    
 
    public void getFileDir(String filePath) {
        mpath.setText(filePath);
        items = new ArrayList<String>();
        paths = new ArrayList<String>();
        File f = new File(filePath);
        File[] files = f.listFiles();
 
        if (!filePath.equals(rootpath)) {
            items.add("Back to" + rootpath);
            paths.add(rootpath);
            items.add("Back to ../ (last folder)");
            paths.add(f.getParent());
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            items.add(file.getName());
            paths.add(file.getPath());
        }
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.file, items);
        setListAdapter(fileList);
    }
   
    public void onListItemClick(ListView l, View v, int position, long id) {
        final File file = new File(paths.get(position));
        if (file.canRead()) {
            if (file.isDirectory()) {
                getFileDir(paths.get(position));
            }
            else {
            	Intent it = getVideoFileIntent("/sdcard/Pictures/CameraGL/"+file.getName());  
            	startActivity( it );
             /*   new AlertDialog.Builder(this).setTitle("Message").setMessage(
                        "Do you want to play this file?")
                        .setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                            int arg1) {
                                    	
                                    }
                                }).show();
             */
            }
        }
        else {
            new AlertDialog.Builder(this).setTitle("Message")
                    .setMessage("È¨ÏÞ²»¹»").setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                        int arg1) {
                                    // TODO Auto-generated method stub
                                }
                            }).show();
        }
    }
    
  
    /** Play MP4 file*/
	public static Intent getVideoFileIntent( String param )  
   
   {  
	   
	      Intent intent = new Intent("android.intent.action.VIEW");  
	  
	     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	   
	     intent.putExtra("oneshot", 0);  
	   
	     intent.putExtra("configchange", 0);  
	   
	    Uri uri = Uri.fromFile(new File(param ));  
	   
	     intent.setDataAndType(uri, "video/*");  
	   
	     return intent;  
   }  

	/** Return to main activity*/
	public void return1()
		{
			Intent return1 = new Intent(this, CameraMainActivity.class);
			startActivity(return1);
		}
    
    
    
    
    
    
    
}
