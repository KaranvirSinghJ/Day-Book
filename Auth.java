package com.Daybook.daybook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import com.Daybook.db.DBAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Auth extends Activity 
{
	private Context conxt=this;
	private EditText accode;
	AlertDialog.Builder alertDialog2 ;
	public void onCreate(Bundle paramBundle)
	 {
		super.onCreate(paramBundle);
	    setContentView(R.layout.authontication);
	    alertDialog2 = new AlertDialog.Builder(Auth.this);
	    Button act=(Button) findViewById(R.id.button_activate);
	     accode=(EditText) findViewById(R.id.editText_activation);
	    act.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DBAdapter db=new DBAdapter(conxt);
				String str=db.activatepr();
				db.close();
				if(str.equals("false"))
				{
					Toast.makeText(Auth.this, "Invalid Code !!", 1).show();
				}
				else
				{
					Toast.makeText(Auth.this, "Activation Done !!", 1).show();
					Intent localIntent = new Intent(Auth.this,DayBookActivity.class);
					finish();
				    startActivity(localIntent);
				}
			}
		});
	    alertDialog2.setMessage("Restore?");
        alertDialog2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int which) {
       
        try {
               File sd = Environment.getExternalStorageDirectory();
               File data = Environment.getDataDirectory();
            
               if (sd.canWrite()) {
            	   
            	   String backupDBPath = "//data//com.android.daybook//databases//daybook.db";
                   String currentDBPath = "//DayBook_Backup//backup.crypt5";
                   
                   
                   File currentDB = new File(sd, currentDBPath);
                   File backupDB = new File(data, backupDBPath);
                  
                              
               
                   if (currentDB.exists()) {
                	   
                       FileChannel src = new FileInputStream(currentDB).getChannel();
                       FileChannel dst = new FileOutputStream(backupDB).getChannel();
                       dst.transferFrom(src, 0, src.size());
                       src.close();
                       dst.close();
                       Toast.makeText(Auth.this,"Data Restored !!", 1).show();
                       Intent localIntent = new Intent(Auth.this,DayBookActivity.class);
   					finish();
   				    startActivity(localIntent);
                   }
                   else
                   {
                	   Toast.makeText(Auth.this,"Data Not Found !!", 1).show();
                   }
               }
           } catch (Exception e) {
        	
        	 Toast.makeText(Auth.this, "Msg:"+e, 1).show();
        	
           } catch (Throwable e) {
		e.printStackTrace();
	}
          }
      });
    alertDialog2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
      dialog.cancel();
      }
  });
	    
	  }
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        
	        inflater.inflate(R.layout.restoremenu, menu);
	        return super.onCreateOptionsMenu(menu);
	    }
	 
	 public boolean onOptionsItemSelected(MenuItem item)
	    {
	    	
	    	switch (item.getItemId()) {
         
	      
	        case R.id.item_restore:
	        	alertDialog2.show();
             return true;
	        case R.id.item_help:
	        	Intent localIntent3 = new Intent(Auth.this,Help.class);
				startActivityForResult(localIntent3,1001);
             return true;
	        default:
	            
	        return super.onOptionsItemSelected(item);
	    }
}
}
