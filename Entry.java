package com.Daybook.daybook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.Daybook.Domain.Bill_Domain;
import com.Daybook.Domain.trans_DOMAIN;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Entry extends Activity {
	EditText qnty,price,amount,duedate;
	AutoCompleteTextView nm;
	Spinner spinner2;
	String product;
	int memid;
	String mem_name;
	Double qntty=0.0,pric=0.0,tot;
	String str,strdate;
	trans_DOMAIN dm=new trans_DOMAIN();
	DBAdapter db=new DBAdapter(this);
	private DatePickerDialog datePickerDialog;
	private DatePickerDialog.OnDateSetListener dateSetListener;
	int bill_id=0;
	private int billids;
	ArrayAdapter name;
	String namestring[] ;
	protected double qntty2;
	protected double pric2;
	protected double tot2;
	private EditText currentdate;
	private DatePickerDialog datePickerDialog2;
	private OnDateSetListener dateSetListener2;
	public void onCreate(Bundle paramBundle)
	{
		
		initDateSetListener();
		initDateSetListener2();
		strdate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		memid=getIntent().getIntExtra("member_id", 0);
        mem_name=getIntent().getStringExtra("member_name");
        ActionBar act=getActionBar();
        act.setTitle("Create Bill");
        billids=getIntent().getIntExtra("bill_id", 0);
        if(billids!=0)
        {
        	bill_id=billids;
        }
		super.onCreate(paramBundle);
	    setContentView(R.layout.entry);
	    
	    str = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
	    nm=(AutoCompleteTextView) findViewById(R.id.edit_name);
	    qnty=(EditText) findViewById(R.id.edit_quantity);
	    price=(EditText) findViewById(R.id.edit_price);
		price.addTextChangedListener(new TextWatcher() 
		{
					
					public void onTextChanged(CharSequence s, int start, int before, int count) {	
					}
					
					
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
					}
					
					
					public void afterTextChanged(Editable s) {
						
						 if (qnty.getText().toString().isEmpty()||price.getText().toString().isEmpty())
						 {
							 
						 }
						 else
						 {
							     qntty=Double.parseDouble(qnty.getText().toString());
								 pric=Double.parseDouble(price.getText().toString());
								 tot=qntty*pric;
								 amount.setText(String.format("%.2f",tot));
						 }	
					}
		});
		qnty.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!qnty.getText().toString().isEmpty())
				{
					if(!price.getText().toString().isEmpty())
					{
						 qntty2=Double.parseDouble(qnty.getText().toString());
						 pric2=Double.parseDouble(price.getText().toString());
						 tot2=qntty2*pric2;
						 amount.setText(String.format("%.2f",tot2));
					}
					else
					{
						
						
						amount.setText("0.00");
					}
				}	
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				
			}
		});
		String newstr=null;
		newstr=db.name_suggestion(nm.getText().toString());
		db.close();
		if(newstr!=null)
		{
		namestring=newstr.split("/");
		name=new ArrayAdapter(this,android.R.layout.simple_list_item_1,namestring);
		nm.setThreshold(1);
		nm.setAdapter(name);
		}
		currentdate=(EditText) findViewById(R.id.editText_current_date);
		currentdate.setText(strdate);
	    amount=(EditText) findViewById(R.id.edit_amount);
	    duedate=(EditText) findViewById(R.id.edit_remind_date);
	    duedate.setOnTouchListener(new View.OnTouchListener()
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
        currentdate.setOnTouchListener(new View.OnTouchListener()
	    {
	    	@Override  
	    	public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
		      {
		        if (paramAnonymousMotionEvent.getAction() == 0) {
		                  showDatePickerDialog2();
		        }
		        return false;
		      }
		});
		spinner2=(Spinner) findViewById(R.id.spinner1);
		duedate=(EditText) findViewById(R.id.edit_remind_date);	
		List<String> list = new ArrayList<String>();
		list.add("Item");
		list.add("Cash Received");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter);
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				product = arg0.getItemAtPosition(arg2).toString();
				 if(product.equals("Cash Received"))
				 {
					 nm.setText("");
					 nm.setEnabled(false);
					 qnty.setText("");
					 qnty.setEnabled(false);
					 price.setText("");
					 price.setEnabled(false);
					 amount.setEnabled(true);
					 amount.setText("");
					 duedate.setEnabled(false);
					 
				 }
				 else
				 {
					 nm.setEnabled(true);
					 amount.setText("");
					 qnty.setEnabled(true);
					 price.setEnabled(true);
					 amount.setEnabled(false);
					
				 }
				
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});

		
	    
		Button finish=(Button) findViewById(R.id.button_finish);
		finish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if(!amount.getText().toString().isEmpty())
				{
					Toast.makeText(Entry.this, "Save the entry then click finish", 1).show();
				}
				else{
				finish();
				}
			}
		});
		Button addbtn=(Button) findViewById(R.id.button_add);
		addbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bill_Domain bd=new Bill_Domain();
				if(bill_id==0)
				{
					
					bd.setClient_id(memid);
					bd.setBill_date(currentdate.getText().toString());
					bd.setBill_Duedate(duedate.getText().toString());
					if(product=="Cash Received")
					{
						bd.setBill_detl("R");
					}
					else
					{
						bd.setBill_detl("P");
					}
					bill_id=db.insert_bill(bd);
					db.close();
					dm.setBid(bill_id);	
				}
				else
				{
					dm.setBid(bill_id);
				}
				
				product=spinner2.getSelectedItem().toString();
				if(product=="Cash Received")
				{
					if(amount.getText().toString().isEmpty()||amount.getText().toString().equals(null))
					{
						 Toast.makeText(Entry.this, "Enter Amount", 1).show();
					}
					else
					{
						dm.setClient_id(memid);						
					    dm.setItem_detail("");
						dm.setItemname("Cash Received");
						dm.setTotamnt(Double.parseDouble(amount.getText().toString()));
						dm.setType(product);						
						db.insert_trans(dm);
						finish();
						nm.setText("");
						qnty.setText("");
						price.setText("");
						amount.setText("");
						duedate.setText("");
					}
						
				}
				else if(product=="Item")
				{
					if(nm.getText().toString().isEmpty())
					{
						
						Toast.makeText(Entry.this, "Enter Name", 1).show();
					}
					else if(qnty.getText().toString().isEmpty())
					{
						Toast.makeText(Entry.this, "Enter Quantity", 1).show();
					}
					else if(price.getText().toString().isEmpty())
					{
						Toast.makeText(Entry.this, "Enter Price", 1).show();
					}
					else
					{
						dm.setClient_id(memid);
						dm.setItem_detail(qnty.getText().toString()+" X "+price.getText().toString());
						dm.setItemname(nm.getText().toString());	
						dm.setTotamnt(Double.parseDouble(amount.getText().toString()));
						dm.setType(product);					
						db.insert_trans(dm);
						db.close();
						nm.setText("");
						qnty.setText("");
						price.setText("");
						amount.setText("");
						duedate.setText("");
					}
				}				
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
	private void showDatePickerDialog2()
	{
	    int i = Integer.parseInt(strdate.substring(0, 4));
	    int j = -1 + Integer.parseInt(strdate.substring(5, 7));
	    int k = Integer.parseInt(strdate.substring(8));
	    System.out.println(i+".."+j+".."+k+"..");
	    datePickerDialog2.getDatePicker();
	    datePickerDialog2.show();
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
	 private void initDateSetListener2()
	  {
	    dateSetListener2 = new DatePickerDialog.OnDateSetListener()
	    {
	      public void onDateSet(DatePicker paramAnonymousDatePicker, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
	      {
	        Date localDate = new Date(paramAnonymousInt1 - 1900, paramAnonymousInt2, paramAnonymousInt3);
	        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
	        if (currentdate != null) {currentdate.setText(localSimpleDateFormat.format(localDate));
	        }
	      }
	    };
	    Calendar localCalendar = Calendar.getInstance();
	    datePickerDialog2 = new DatePickerDialog(this, dateSetListener2, localCalendar.get(1), localCalendar.get(2), localCalendar.get(5));
	}
	 
}
