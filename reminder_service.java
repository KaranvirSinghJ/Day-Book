package com.Daybook.daybook.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.Daybook.Domain.Reminder_Domain;
import com.Daybook.daybook.Reminder;
import com.Daybook.db.DBAdapter;
import com.Daybook.daybook.R;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;


public class reminder_service extends Service {
    TextToSpeech textsp;
    DBAdapter db=new DBAdapter(this);
    long timeslote=0;
    Date mydate=new Date();
    Timer obj=new Timer();
	@Override
	public IBinder onBind(Intent intent) 
	{
		Intent intt=new Intent(this,reminder_service.class);
		startService(intt);
		
		return null;
	}
	 
	  @Override
	    public int onStartCommand(Intent intent, int flags, int startId)
	    {
		
		 
		  mytask tcs=new mytask();
		  obj.schedule(tcs,40000,1000);
		  
	      return START_STICKY;
	    }
	  private void Notify(String notificationTitle, String notificationMessage) {
    	  NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	  Notification notification = new Notification(R.drawable.ic_launcher,notificationTitle, System.currentTimeMillis());
    	   Intent notificationIntent = new Intent(this, Reminder.class);
    	   
    	   PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);
    	   
    	   notification.setLatestEventInfo(this,notificationTitle,notificationMessage, pendingIntent);
    	   notificationManager.notify(88, notification);
    	  
    	   notification.flags |=Notification.FLAG_AUTO_CANCEL;
    	 
      }
	  class mytask extends TimerTask
	  {

		@Override
		public void run() 
		{
			  
			  
			  String str=new SimpleDateFormat("EE").format(new Date());
			  List<?> lst=db.getallreminder_bytime(str.substring(0, 2));
	      	  Iterator<?> itr=lst.iterator();
			  while(itr.hasNext())
				{
								
								Reminder_Domain dd=new Reminder_Domain();
							    dd=(Reminder_Domain)itr.next();

							    DateFormat df = new SimpleDateFormat("HH:mm:ss");	                            
                                String mytime=df.format(new Date());
                                
                                String ss=dd.getRem_time()+":00";
                             
								if(ss.equals(mytime))
								{
									List<?> memberlist;
								
									String str2date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
									memberlist = db.get_reminder_date(str2date);
									
									if(memberlist.size()>0)
									{
											Notify("Dear User", memberlist.size()+" Clients has collection today");	
											
									}	
								}			
				}
		}
		  
	  }
	  @Override
	  public void onDestroy()
	  {
		  
		  super.onDestroy();
		  startService(new Intent(this,reminder_service.class));
	  }


}
