package com.Daybook.daybook;

import com.Daybook.Domain.Reminder_Domain;
import com.Daybook.db.DBAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

public class Add_reminder extends Activity {
	String str;
	boolean checked_state[]={false,false,false,false,false,false,false};
	CharSequence[] daysofweek={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	CharSequence[] daysofweek2={"Su","Mo","Tu","We","Th","Fr","Sa"};
	DBAdapter db=new DBAdapter(this);
	public void onCreate(Bundle paramBundle)
	{
		final AlertDialog.Builder builder=new AlertDialog.Builder(Add_reminder.this);
		super.onCreate(paramBundle);
		setContentView(R.layout.add_reminder);
		Button btn_save=(Button) findViewById(R.id.button_rec);
		final TimePicker pk=(TimePicker) findViewById(R.id.timePicker1);
		final RadioButton sound=(RadioButton) findViewById(R.id.radioButton1);
		final RadioButton vib=(RadioButton) findViewById(R.id.radioButton2);
		TableRow trr=(TableRow) findViewById(R.id.tableRow3);
		final Reminder_Domain rd=new Reminder_Domain();
		final TextView tv=(TextView) findViewById(R.id.textView_rep);
		trr.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.show();
			}
		});
		btn_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				String str1=pk.getCurrentHour().toString();
				String str2=pk.getCurrentMinute().toString();
				if(str1.length()==1)
				{
					str1="0"+str1;
				}
				if(str2.length()==1)
				{
					str2="0"+str2;
				}
				rd.setRem_time(str1+":"+str2);
				rd.setRem_sound(sound.isChecked()+"");
			    rd.setRem_vib(vib.isChecked()+"");
			    rd.setRem_days(tv.getText().toString());
			    db.insert_reminder(rd);
			    db.close();
			    finish();
			}
		});
		
		builder.setTitle("Select Days");
		builder.setMultiChoiceItems(daysofweek , null, new OnMultiChoiceClickListener()
		{

			@Override
			public void onClick(DialogInterface arg0, int arg1,boolean arg2)
			{
				checked_state[arg1]=arg2;
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    dialog.cancel();
		    }
		});
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    	String st="";
		    	for(int i=0;i<7;i++)
		    	{
			    	if(checked_state[i]==true)
			    	{
			    		st=st+" "+daysofweek2[i];		
			    	}
			    	checked_state[i]=false;
		    	}
		    	tv.setText(st);
		    	
		 	}
		});
		
	}

}
