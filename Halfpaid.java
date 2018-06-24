package com.Daybook.daybook;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.Daybook.Domain.Bill_Domain;
import com.Daybook.db.DBAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class Halfpaid extends Activity
{
	private EditText billdate;
	private EditText balnc;
	private EditText recv;
	private EditText remain;
	private EditText newdat;
	private Button btncancel;
	private Button btnsave;
	private int billid;
	Bill_Domain bd=new Bill_Domain();
	private String strdate;
	private DatePickerDialog datePickerDialog;
	private OnDateSetListener dateSetListener;
	DBAdapter db=new DBAdapter(this);
	public void onCreate(Bundle paramBundle)
	{
		initDateSetListener();
		strdate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		super.onCreate(paramBundle);
	    setContentView(R.layout.half_paid);
	    billid=getIntent().getIntExtra("bill_id", 0);
	    ActionBar act=getActionBar();
	    act.setTitle("Update Bill");
	    bd=db.getbill_by_bill_id(billid);
	    db.close();
	    billdate=(EditText) findViewById(R.id.bill_date);
	    billdate.setText(bd.getBill_date());
	    balnc=(EditText)findViewById(R.id.bill_balance);
	    balnc.setText(bd.getBill_amount()+"");
	    recv=(EditText)findViewById(R.id.bill_received);
	    recv.addTextChangedListener(new TextWatcher()
	    {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(!recv.getText().toString().isEmpty())
				{
					
					double bal=Double.parseDouble(recv.getText().toString());
					remain.setText(bd.getBill_amount()-bal+"");
					
				}
			}
		});
	    remain=(EditText)findViewById(R.id.bill_remain);
	    newdat=(EditText)findViewById(R.id.bill_remind_date);
	    btncancel=(Button)findViewById(R.id.bill_cancel);
	    btncancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
	    btnsave=(Button)findViewById(R.id.bill_save);
	    btnsave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(recv.getText().toString().isEmpty())
				{
					Toast.makeText(Halfpaid.this, "Sorry Recored Not Updated !!",1).show();
					finish();
				}
				else
				{
					Bill_Domain bdm=new Bill_Domain();
					bdm.setBill_Duedate(newdat.getText().toString());
					bdm.setBillid(billid);
					bdm.setBill_amount(Double.parseDouble(remain.getText().toString()));
					String datef=new SimpleDateFormat("dd.MMM").format(new Date());
					String billstr=datef+"-"+recv.getText().toString()+" "+bd.getBill_detl();
					bdm.setBill_detl(billstr);
					db.update_half_paid(bdm);
					db.close();
					Toast.makeText(Halfpaid.this, "Updated Successfully !!",1).show();
					finish();
				}
				
			}
		});
	    newdat.setOnTouchListener(new View.OnTouchListener()
	    {
	    	@Override  
	    	public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
		      {
		        if (paramAnonymousMotionEvent.getAction() == 0) {
		                  showDatePickerDialog();
		        }
		        return false;
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
	        if (newdat != null) {newdat.setText(localSimpleDateFormat.format(localDate));
	        }
	      }
	    };
	    Calendar localCalendar = Calendar.getInstance();
	    datePickerDialog = new DatePickerDialog(this, dateSetListener, localCalendar.get(1), localCalendar.get(2), localCalendar.get(5));
	}
}
