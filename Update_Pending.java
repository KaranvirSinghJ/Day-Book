package com.Daybook.daybook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.Daybook.Domain.trans_DOMAIN;
import com.Daybook.db.DBAdapter;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class Update_Pending extends Activity {
	EditText nm,qnty,price,amount,duedate;
	Spinner spinner2;
	String product;
	int tid;
	String mem_name;
	Double qntty=0.0,pric=0.0,tot;
	String str,strdate;
	trans_DOMAIN dm=new trans_DOMAIN();
	DBAdapter db=new DBAdapter(this);
	private DatePickerDialog datePickerDialog;
	private DatePickerDialog.OnDateSetListener dateSetListener;
	private trans_DOMAIN tdm;
	public void onCreate(Bundle paramBundle)
	{
		initDateSetListener();
		strdate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		tid=getIntent().getIntExtra("tr_id", 0);
        mem_name=getIntent().getStringExtra("member_name");
		super.onCreate(paramBundle);
		tdm = new trans_DOMAIN();
		tdm=(trans_DOMAIN) db.get_rec_by_trans_id(tid);
	    setContentView(R.layout.update_pendings);
	    str = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
	    nm=(EditText) findViewById(R.id.edit_name);
	    nm.setText(tdm.getItemname());
	    String newstr[]=tdm.getItem_detail().split(" X ");
	    qnty=(EditText) findViewById(R.id.edit_quantity);
	    qnty.setText(newstr[0]);
	    price=(EditText) findViewById(R.id.edit_price);
	    price.setText(newstr[1]);
	    amount=(EditText) findViewById(R.id.edit_amount);
	    amount.setText(tdm.getTotamnt()+"");
	    duedate=(EditText) findViewById(R.id.edit_remind_date);
		spinner2=(Spinner) findViewById(R.id.spinner1);
		
	

		duedate.setOnTouchListener(new View.OnTouchListener()
	    {
		      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
		      {
		        if (paramAnonymousMotionEvent.getAction() == 0) {
		                  showDatePickerDialog();
		        }
		        return false;
		      }
		    });

		
		List<String> list = new ArrayList<String>();
		list.add(tdm.getType());
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter);
		Button finish=(Button) findViewById(R.id.button_finish);
		finish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent localIntent = new Intent(Update_Pending.this, Reminder.class);	
				startActivity(localIntent);
				finish();
			}
		});
		Button addbtn=(Button) findViewById(R.id.button_add);
		addbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				trans_DOMAIN tr=new trans_DOMAIN();
				tr.setTrans_id(tid);
				db.update_pending_reminder(tr);
				db.close();
				Toast.makeText(Update_Pending.this, "Updated Successfully !", 1).show();
				Intent localIntent = new Intent(Update_Pending.this, Reminder.class);	
				startActivity(localIntent);
				finish();
			}
		});	
		
	}
	
	private void showDatePickerDialog()
	{
	    int i = Integer.parseInt(strdate.substring(0, 4));
	    int j = -1 + Integer.parseInt(strdate.substring(5, 7));
	    int k = Integer.parseInt(strdate.substring(8));
	    System.out.println(i+".."+j+".."+k+"..");
	    datePickerDialog.getDatePicker();
	    datePickerDialog.show();
	}
	 private void initDateSetListener()
	  {
	    dateSetListener = new DatePickerDialog.OnDateSetListener()
	    {
	      public void onDateSet(DatePicker paramAnonymousDatePicker, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
	      {
	        Date localDate = new Date(paramAnonymousInt1 - 1900, paramAnonymousInt2, paramAnonymousInt3);
	        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
	        if (duedate != null) {duedate.setText(localSimpleDateFormat.format(localDate));
	        }
	      }
	    };
	    Calendar localCalendar = Calendar.getInstance();
	    datePickerDialog = new DatePickerDialog(this, dateSetListener, localCalendar.get(1), localCalendar.get(2), localCalendar.get(5));
	  }
}
