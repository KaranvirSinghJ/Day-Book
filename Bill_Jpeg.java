package com.Daybook.daybook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.Daybook.Domain.Bill_Domain;
import com.Daybook.Domain.Register_DOMAIN;
import com.Daybook.Domain.trans_DOMAIN;
import com.Daybook.db.DBAdapter;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Bill_Jpeg extends Activity {

	
	private int bill_ids;
	private int client_ids;
	TableLayout tl;
	TableLayout.LayoutParams ll;
	EditText txt;
	public void onCreate(Bundle paramBundle)
	{
		DBAdapter db=new DBAdapter(this);
		super.onCreate(paramBundle);
		setContentView(R.layout.bill_jpg);
		bill_ids=getIntent().getIntExtra("bill_id", 0);
		client_ids=getIntent().getIntExtra("mem_id", 0);
		Register_DOMAIN rg2=new Register_DOMAIN();
	    Bill_Domain bd2=new Bill_Domain();
	    rg2=db.get_CLIENTby_CLIENTid(client_ids);
        bd2=db.getbill_by_bill_id(bill_ids);
        List newlsy=db.getpendingbyid(bill_ids);
        TextView nm=(TextView) findViewById(R.id.textView1);
        nm.setText(rg2.getMem_name());
        TextView nm2=(TextView) findViewById(R.id.textView2);
        nm2.setText(rg2.getMem_addr());
        TextView nm3=(TextView) findViewById(R.id.textViewdd);
        nm3.setText(setdates(bd2.getBill_date()));
        db.close();
        txt=(EditText) findViewById(R.id.editText1);
        txt.setText(bd2.getBillid()+" "+rg2.getMem_name());
        String text = rg2.getMem_name()+"\t"+rg2.getMem_addr();
        text+="\n "+bd2.getBill_date();
        text+="\n ";
        Iterator itr=newlsy.iterator();
        
        tl = (TableLayout) findViewById(R.id.tableLayout_jpg);
        tl.buildDrawingCache();
        tl.setDrawingCacheEnabled(true);
        
        
        while (itr.hasNext())
        {
        	trans_DOMAIN td2=new trans_DOMAIN();
        	td2=(trans_DOMAIN)itr.next();
        	TableRow tr = new TableRow(this);
        	tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));    
        	TextView tv=new TextView(this);        	
        	tv.setText(td2.getItemname()+"("+td2.getItem_detail()+")");
        	TextView tv2=new TextView(this);
        	tv2.setText(td2.getTotamnt()+"");        
        	tr.addView(tv);
        	TableRow.LayoutParams lp=new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        	lp.gravity=Gravity.RIGHT;
        	tv2.setLayoutParams(lp);
        	tr.addView(tv2);
        	tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        }
        TableRow tr = new TableRow(this);      
    	tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));    
    	TextView tv=new TextView(this); 
    	tv.setWidth(150);
    	tv.setText(bd2.getBill_detl());
    	 
    	tr.addView(tv);
    	TableRow.LayoutParams lp=new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
    	lp.gravity=Gravity.RIGHT;
    	
    	 
    	
    	tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
    	TextView tv2=new TextView(this);
    	tv2.setText(bd2.getBill_amount()+""); 
    	tv2.setLayoutParams(lp);
    	
    	TableRow tr2 = new TableRow(this);
    	tr2.addView(tv2);
    	tl.addView(tr2, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        Button cancel =(Button) findViewById(R.id.button1);
    	
        cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
    	Button save =(Button) findViewById(R.id.button2);
    	
    	save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 try {
		               File sd = Environment.getExternalStorageDirectory();
		              
		               if (sd.canWrite()) 
		               {
		            	   final Bitmap bmp =Bitmap.createBitmap(tl.getWidth(), tl.getHeight(),Bitmap.Config.ARGB_8888 );
		            	   bmp.eraseColor(Color.WHITE);
		            	   Canvas mCanvas = new Canvas(bmp);
		            	   tl.draw(mCanvas);
		            	   
		            	   if(!txt.getText().toString().isEmpty())
		            		   
		            	   {
		            	   File fl=new  File(sd,"//Daybook//"+txt.getText().toString()+".jpg");
		            	   
		            	   File f2=new  File(sd,"//Daybook//");
		            	   if(!f2.exists())
		            	   {
		            		   f2.mkdir();
		            		   
		            	   }
		            	   if(!fl.isFile())
		            	   {
		            		   fl.createNewFile();
		            	   }
		            	   FileOutputStream stream = new FileOutputStream(fl); 
		            	   bmp.compress(CompressFormat.JPEG, 100, stream);
		            	   stream.close();
		            	   Toast.makeText(Bill_Jpeg.this, "Bill exported", 1).show();   
		            	   finish();
		            	   }
		            	   else
		            	   {
		            		   Toast.makeText(Bill_Jpeg.this, "Enter File Name", 1).show();
		            	   }
		            	  
		               }
		    	  } catch (Exception e) {
		          	
		         	 Toast.makeText(Bill_Jpeg.this,""+e, 1).show();
		         	 System.out.println(e);
		            } catch (Throwable e) {
		 			e.printStackTrace();
		 		}
				
			}
		});  
	}
	public String setdates(String str2)
    {
    	String str = new SimpleDateFormat("dd.MMM.yyyy").format(Date.parse(str2));
    	return str;
    }
}
