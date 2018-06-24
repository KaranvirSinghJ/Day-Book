package com.Daybook.daybook;

import com.Daybook.db.DBAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Addcostmer extends Activity {

	private EditText name,address,mobile;
	private String opt;
	public void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);
		setContentView(R.layout.addcustomer);
		final DBAdapter db=new DBAdapter(this);
		opt=getIntent().getStringExtra("operation");
		name = (EditText)findViewById(R.id.editText1_name);
		address  = (EditText) findViewById(R.id.editText_address);
		mobile = (EditText) findViewById(R.id.editText_mobile);
		ActionBar act=getActionBar();
		act.setTitle("Add New Client");
		Button regbtn=(Button)findViewById(R.id.button_register);
		
		regbtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			    if(name.getText().toString().isEmpty())
				{
					Toast.makeText(Addcostmer.this, "Enter Name", 1).show();
				}
				
				else
				{
					try{
						
						long num=Long.parseLong(mobile.getText().toString());
						db.register_CLIENT(name.getText().toString(),address.getText().toString(),num);
						db.close();
						Toast.makeText(Addcostmer.this, "Member saved !!", 1).show();
						name.setText("");
						address.setText("");
						mobile.setText("");
						if(opt.equals("Customer"))
						{
							
							finish();
						}
						else
						{
							
							finish();
						}
                        
					}catch(NumberFormatException e)
					{
						Toast.makeText(Addcostmer.this, "Enter Correct No !!", 1).show();
					}
				
				}
			}
		});
	}
}
