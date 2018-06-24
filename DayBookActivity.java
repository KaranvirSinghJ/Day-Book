package com.Daybook.daybook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import com.Daybook.db.DBAdapter;



import android.app.Activity;


import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DayBookActivity extends Activity {
	public static boolean isService = false;
	AlertDialog.Builder alertDialog,alertDialog2;
	
	DBAdapter db=new DBAdapter(this);
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	
    	db.check_tables();   	
    	db.close();
    	
        super.onCreate(savedInstanceState);
        alertDialog = new AlertDialog.Builder(DayBookActivity.this);
        alertDialog2 = new AlertDialog.Builder(DayBookActivity.this);
      
        setContentView(R.layout.main);
        Button btn=(Button) findViewById(R.id.button_entry);
        Button custbtn=(Button)findViewById(R.id.button_custm);
        custbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent localIntent = new Intent(DayBookActivity.this, Customer.class);	
				startActivity(localIntent);
			}
		});
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent localIntent = new Intent(DayBookActivity.this, List_Client.class);	
				startActivity(localIntent);
			}
		});
        Button pend=(Button) findViewById(R.id.button_pending);
        pend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent localIntent = new Intent(DayBookActivity.this, Pending.class);	
				startActivity(localIntent);
			}
		});
        Button rec=(Button) findViewById(R.id.button_daybook);
        rec.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent localIntent = new Intent(DayBookActivity.this, Daybook.class);	
				startActivity(localIntent);
			}
		});
        Button btnrem=(Button)findViewById(R.id.button_rem);
        btnrem.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent localIntent = new Intent(DayBookActivity.this, Reminder.class);	
				startActivity(localIntent);
				
			}
		});
        Button btnhelp=(Button) findViewById(R.id.buttonhelp);
        btnhelp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent localIntent = new Intent(DayBookActivity.this, Help.class);	
				startActivity(localIntent);
				
			}
		});
     backup();
     restore();
        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        
        inflater.inflate(R.layout.menu, menu);
        
        return super.onCreateOptionsMenu(menu);
    }
  public boolean onOptionsItemSelected(MenuItem item)
    {
    	
    	switch (item.getItemId()) {
    
        case R.id.item_aboutsw:
            
            return true;
        case R.id.item_backup:
        	alertDialog.show();
            return true;
        case R.id.item_restore:
        	alertDialog2.show();
            return true;
        case R.id.item_help:
        	
            return true;
        default:
            
        return super.onOptionsItemSelected(item);
    }
  }
  private void backup()
  {
	    alertDialog.setMessage("Create Backup?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int which) {
        System.out.println("m here");
        try {
               File sd = Environment.getExternalStorageDirectory();
               File data = Environment.getDataDirectory();
               
               if (sd.canWrite()) 
               {
            	   
                   String currentDBPath = "//data//com.Daybook.daybook//databases//daybook.db";
                   String backupDBPath = "//Daybook//DayBook_Backup//backup.crypt5";
                   String backupDBPathdr = "//Daybook//DayBook_Backup//";
                   File backupDBdr = new File(sd, backupDBPathdr);
                   File currentDB = new File(data, currentDBPath);
                   File backupDB = new File(sd, backupDBPath);
                   if(!backupDBdr.exists())
                   {
                   backupDBdr.mkdirs();        
                   }
                   if (currentDB.exists())
                   {
                	   FileChannel src = new FileInputStream(currentDB).getChannel();
                       FileChannel dst = new FileOutputStream(backupDB).getChannel();
                       dst.transferFrom(src, 0, src.size());
                       src.close();
                       dst.close();
                       Toast.makeText(DayBookActivity.this,"Backup Created !!", 1).show();
                   }
               }
           } catch (Exception e) {
        	
        	 Toast.makeText(DayBookActivity.this, "Msg:"+e, 1).show();
        	 System.out.println(e);
           } catch (Throwable e) {
			e.printStackTrace();
		}
          }
      });
    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
        }
    });
  }
  private void restore()
  {
	  alertDialog2.setMessage("Restore?");
      alertDialog2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog,int which) {
      System.out.println("m here");
      try {
             File sd = Environment.getExternalStorageDirectory();
             File data = Environment.getDataDirectory();
             System.out.println(sd);
             if (sd.canWrite()) 
             {
                 String backupDBPath = "//data//com.Daybook.daybook//databases//daybook.db";
                 String currentDBPath = "//Daybook//DayBook_Backup//backup.crypt5";
                 File currentDB = new File(sd, currentDBPath);
                 File backupDB = new File(data, backupDBPath);
                 if (currentDB.exists()) {
              	   
                     FileChannel src = new FileInputStream(currentDB).getChannel();
                     FileChannel dst = new FileOutputStream(backupDB).getChannel();
                     dst.transferFrom(src, 0, src.size());
                     src.close();
                     dst.close();
                     Toast.makeText(DayBookActivity.this,"Data Restored !!", 1).show();
                     Intent intt=getIntent();
				       finish();
				       startActivity(intt);
                 }
             }
         } catch (Exception e) {
      	
      	 Toast.makeText(DayBookActivity.this, "Msg:"+e, 1).show();
      	 System.out.println(e);
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
  protected void onResume()
  {
	  super.onResume();
  }
  
}