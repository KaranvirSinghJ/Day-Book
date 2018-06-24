package com.Daybook.daybook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import com.Daybook.BO.BASEBO;
import com.Daybook.Domain.Bill_Domain;
import com.Daybook.Domain.Register_DOMAIN;
import com.Daybook.Domain.trans_DOMAIN;
import com.Daybook.db.DBAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Pending extends Activity
{
	private List<Bill_Domain> memberlist;
	
	private ListView listview;
	ViewHolder bill_view;
	private ListAdapter listAdapter;
	int billid;
	DBAdapter db=new DBAdapter(this);
	AlertDialog.Builder alertDialog ;
	BASEBO bo=null;
	
	private AlertDialog.Builder alertDialog_payment;

	private DatePickerDialog datePickerDialog;

	private OnDateSetListener dateSetListener;

	private String strdate;

	private ActionBar act;

	private Builder alertDialog_filename;
	int clientids=0;
	int bill_ids=0;
	EditText input ;
	public void onCreate(Bundle paramBundle)
	{
		bo=new BASEBO();
		
		alertDialog = new AlertDialog.Builder(Pending.this);
		alertDialog_payment = new AlertDialog.Builder(Pending.this);
		alertDialog_filename = new AlertDialog.Builder(Pending.this);
		initDateSetListener();
		strdate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		super.onCreate(paramBundle);
	    setContentView(R.layout.pending);
	     act=getActionBar();
	    act.setTitle("Balance");
		memberlist = new ArrayList<Bill_Domain>();
	    listview = ((ListView)findViewById(R.id.listView_pending));
	    listAdapter = new ListAdapter(this, R.layout.view_bill,memberlist);
	    listview.setAdapter(listAdapter);       
	    registerForContextMenu(listview);
	    listview.setOnItemClickListener(new OnItemClickListener()
	    {
	    	public void onItemClick(AdapterView<?> arg0, View arg1,	int arg2, long arg3) 
			{
				  Bill_Domain dm=new Bill_Domain();
				  dm=(Bill_Domain)listview.getItemAtPosition(arg2);
				  Intent localIntent = new Intent(Pending.this,Pending_trans.class);
				  localIntent.putExtra("bill_id", dm.getBillid());  
				  startActivity(localIntent);
			          
			}
		});
	    delete_confirm();
	    payment_confirm();
	    file_name();
	    
	  }
	 private void searchReceipt(String st)
	  {
		DBAdapter db=new DBAdapter(this);
		memberlist = db.getbill(st);
		db.close();
	    listAdapter = new ListAdapter(this, R.layout.view_bill,memberlist);
	    listview.setAdapter(listAdapter);
	    listAdapter.notifyDataSetChanged();
	  }
	 protected void onResume()
	  {
		 searchReceipt("");
		 super.onResume();
	  }
	 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	 {
		   super.onCreateContextMenu(menu, v, menuInfo);
		   menu.setHeaderTitle("Action");
		   menu.setHeaderIcon(R.drawable.ic_launcher);
		   menu.add(0, v.getId(), 0, "Export As JPEG");
		   menu.add(0, v.getId(), 0, "Payment Clear");
		   menu.add(0, v.getId(), 0, "Paid Half");
		   menu.add(0, v.getId(), 0, "Add New Item");
		   menu.add(0, v.getId(), 0, "Call Client");
		   menu.add(0, v.getId(), 0, "Send SMS");
		   menu.add(0, v.getId(), 0, "Delete Bill");
		  
	 }
	 public boolean onContextItemSelected(MenuItem item)
	 {
			  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			  int arrayAdapterPosition = info.position;
			  Bill_Domain dm=new Bill_Domain();
			  dm=(Bill_Domain)memberlist.get(arrayAdapterPosition);
			  billid=dm.getBillid();
			  Register_DOMAIN rd=new Register_DOMAIN();
	    	  rd=db.get_CLIENTby_CLIENTid(dm.getClient_id());
	    	  db.close();
			    if (item.getTitle() == "Delete Bill")
			     {
				  	alertDialog.show();
				  } 
			      else if (item.getTitle() == "Call Client") 
			      {
			    	  
			    	  Intent i=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+rd.getMem_contact()));
			    	  startActivity(i); 
				  } 
			      else if (item.getTitle() == "Send SMS")
			      {
			    	  Intent i2=new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",rd.getMem_contact(),null));
			    	  startActivity(i2); 
				  }
			      else if(item.getTitle()=="Add New Item")
			      {
			    	  Intent localIntent = new Intent(Pending.this,Entry.class);
					  localIntent.putExtra("bill_id", dm.getBillid()); 
					  startActivity(localIntent);
			      }
			      else if(item.getTitle()=="Payment Clear")
			      {
			    	  alertDialog_payment.show();
			      }
			      else if(item.getTitle()=="Paid Half")
			      {
			    	  Intent localIntent = new Intent(Pending.this,Halfpaid.class);
					  localIntent.putExtra("bill_id", dm.getBillid()); 
					  startActivity(localIntent);
			      }
			      else if(item.getTitle()=="Export As JPEG")
			      {
			    	  clientids=dm.getClient_id();
			    	  bill_ids=billid;
			    	  Intent localIntent = new Intent(Pending.this,Bill_Jpeg.class);
					  localIntent.putExtra("bill_id", dm.getBillid()); 
					  localIntent.putExtra("mem_id", dm.getClient_id());
					  startActivity(localIntent);
			          
			      }
			      else if(item.getTitle()=="Export As Doc")
			      {
			    	  
			      }
				  return true;
		}
	 
	private class ListAdapter
    extends ArrayAdapter<Bill_Domain>
  {
    private Context context;
    private List<Bill_Domain> data = null;
    private int layoutId;
    
    public ListAdapter(Context contextobj,int layout_id, List<Bill_Domain> paramList)
    {
      super(contextobj,layout_id,paramList);
      this.context = contextobj;
      this.layoutId = layout_id;
      this.data = paramList;
    }

    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      
    	paramView = ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(layoutId, paramViewGroup, false);
        bill_view = new ViewHolder();
        bill_view.text_name   =((TextView)paramView.findViewById(R.id.bill_name));
        bill_view.text_detail =((TextView)paramView.findViewById(R.id.bill_det));
        bill_view.text_balc   =((TextView)paramView.findViewById(R.id.bill_balance));
        bill_view.text_date   =((TextView)paramView.findViewById(R.id.bill_date));
        paramView.setTag(bill_view);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) 
        {
        	 bill_view.text_name.setText(bo.short20_String(((Bill_Domain)this.data.get(paramInt)).getBill_Duedate()));
        	 if(((Bill_Domain)this.data.get(paramInt)).getBill_detl()==null||((Bill_Domain)this.data.get(paramInt)).getBill_detl().equals(""))
        	 {
        		 bill_view.text_detail.setText("");
        	 }
        	 else
        	 {
             bill_view.text_detail.setText(bo.short20_String(((Bill_Domain)this.data.get(paramInt)).getBill_detl()));
        	 }
        }
        else
        {
        	 bill_view.text_name.setText(((Bill_Domain)this.data.get(paramInt)).getBill_Duedate());
             bill_view.text_detail.setText(((Bill_Domain)this.data.get(paramInt)).getBill_detl());
        }
        
        bill_view.text_balc.setText("Bal "+((Bill_Domain)this.data.get(paramInt)).getBill_amount());
        
        bill_view.text_date.setText(setdates(((Bill_Domain)this.data.get(paramInt)).getBill_date()));
        bill_view = (ViewHolder)paramView.getTag();
      
	return paramView;
    }
    
  }
  static class ViewHolder
  {
	    TextView text_name;
	    TextView text_detail;
	    TextView text_balc;
	    TextView text_date;
  }
    public void delete_confirm()
	{
		    alertDialog.setMessage("Delete this ?");
	        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	        

			public void onClick(DialogInterface dialog,int which)
			{ 
				db.delete_bill_by_id(billid);
				db.close();
				searchReceipt("");
				Toast.makeText(Pending.this, "Deleted Successfully !!",1).show();
	        }
	      });
	        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	            }
	        });
	}
    public void payment_confirm()
    {
    	alertDialog_payment.setMessage("Payment Confirmed ?");
    	alertDialog_payment.setPositiveButton("YES", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog,int which)
		{ 
			db.update_bill(billid);
			db.close();
			searchReceipt("");
			Toast.makeText(Pending.this, "Bill Updated !!",1).show();
        }
      });
    	alertDialog_payment.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	
            dialog.cancel();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        
        inflater.inflate(R.layout.menu_balance_date, menu);
        return super.onCreateOptionsMenu(menu);
    }
  public boolean onOptionsItemSelected(MenuItem item)
    {
    	
    	switch (item.getItemId()) {
        case R.id.action_search_bay_date:
      	  
        	showDatePickerDialog();      
        return true;
        default:
            
        return super.onOptionsItemSelected(item);
    }
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
	        String ss=localSimpleDateFormat.format(localDate);
	        act.setTitle(setdates(ss.toString()));
	        searchReceipt(localSimpleDateFormat.format(localDate));
	      }
	    };
	    Calendar localCalendar = Calendar.getInstance();
	    datePickerDialog = new DatePickerDialog(this, dateSetListener, localCalendar.get(1), localCalendar.get(2), localCalendar.get(5));
	}
	public void file_name()
	{
		    
		    alertDialog_filename.setMessage("Bill Name");
		  
		    
		   
	    	alertDialog_filename.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which)
			{ 
				Register_DOMAIN rg2=new Register_DOMAIN();
			    Bill_Domain bd2=new Bill_Domain();
			    rg2=db.get_CLIENTby_CLIENTid(clientids);
		        bd2=db.getbill_by_bill_id(bill_ids);
		        List newlsy=db.getpendingbyid(billid);
		        db.close();
                String text = rg2.getMem_name()+"\t"+rg2.getMem_addr();
                text+="\n "+bd2.getBill_date();
                text+="\n ";
                Iterator itr=newlsy.iterator();
                while (itr.hasNext())
                {
                	 trans_DOMAIN td2=new trans_DOMAIN();
                	 td2=(trans_DOMAIN)itr.next();
                	
                	 text+="\n "+td2.getItemname()+"("+td2.getItem_detail()+")"+td2.getTotamnt();
                }
                
                text+="\n"+bd2.getBill_detl();
                
                text+="\n "+bd2.getBill_amount();
				 try {
		               File sd = Environment.getExternalStorageDirectory();
		               File data = Environment.getDataDirectory();
		               if (sd.canWrite()) 
		               {
		            	   
		            	  

		            	   final Rect bounds = new Rect();
		            	   TextPaint textPaint = new TextPaint() {
		            	       {
		            	           setColor(Color.BLACK); 
		            	           setTextSize(14f);
		            	           setTextAlign(TextPaint.Align.RIGHT);   
		            	           setAntiAlias(true);
		            	           
		            	       }
		            	   };
		            	   
		            	   textPaint.getTextBounds(text, 0, text.length(), bounds);
		            	   StaticLayout mTextLayout = new StaticLayout(text, textPaint,
		            	               600, Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
		            	   int maxWidth = -1;
		            	   for (int i = 0; i < mTextLayout.getLineCount(); i++) {
		            	       if (maxWidth < mTextLayout.getLineWidth(i)) {
		            	           maxWidth = (int) mTextLayout.getLineWidth(i);
		            	       }
		            	   }
		            	   
		            	   final Bitmap bmp = Bitmap.createBitmap(400 , 500,
		            	               Bitmap.Config.ARGB_8888);
		            	   bmp.eraseColor(Color.WHITE);// just adding black background
		            	   
		            	   final Canvas canvas = new Canvas(bmp);
		            	  
		            	   mTextLayout.draw(canvas);
		            	   
		            	   File fl=new  File(sd,"//Daybook//"+bill_ids+".jpg");
		            	   if(!fl.exists())
		            	   {
		            		   fl.createNewFile();
		            	   }
		            	   FileOutputStream stream = new FileOutputStream(fl); //create your FileOutputStream here
		            	   bmp.compress(CompressFormat.JPEG, 100, stream);
		            	   bmp.recycle();
		            	   stream.close();
		            	   Toast.makeText(Pending.this, "Bill exported", 1).show();
		               }
		    	  } catch (Exception e) {
		          	
		         	 Toast.makeText(Pending.this,""+e, 1).show();
		         	 System.out.println(e);
		            } catch (Throwable e) {
		 			e.printStackTrace();
		 		}
	        }
	      });
	    	alertDialog_filename.setNegativeButton("NO", new DialogInterface.OnClickListener()
	    	{
	            public void onClick(DialogInterface dialog, int which) {
	          
	           
	            dialog.cancel();
	            }
	        });
	    }
	 public String setdates(String str2)
	    {
	    	String str = new SimpleDateFormat("dd.MMM.yyyy").format(Date.parse(str2));
	    	return str;
	    }
}