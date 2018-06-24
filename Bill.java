package com.Daybook.daybook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.Daybook.BO.BASEBO;
import com.Daybook.Domain.Bill_Domain;
import com.Daybook.db.DBAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Bill extends Activity
{
	private List<Bill_Domain> memberlist;
	private ListView listview;
	ViewHolder bill_view;
	private ListAdapter listAdapter;
	DBAdapter db=new DBAdapter(this);
	AlertDialog.Builder alertDialog ;
	BASEBO bo=null;
	private String mem_name;
	private int mem_id;
	double balanc=0.0,rec=0.0;
	ActionBar act;
	public void onCreate(Bundle paramBundle)
	{
		bo=new BASEBO();
		mem_id=getIntent().getIntExtra("member_id", 0);
        mem_name=getIntent().getStringExtra("member_name");
        
		alertDialog = new AlertDialog.Builder(Bill.this);
		super.onCreate(paramBundle);
	    setContentView(R.layout.pending);
	    act=getActionBar();
		memberlist = new ArrayList<Bill_Domain>();
        
	    listview = ((ListView)findViewById(R.id.listView_pending));
	    listAdapter = new ListAdapter(this, R.layout.view_bill,memberlist);
	    listview.setAdapter(listAdapter);       
	    listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {
				  Bill_Domain dm=new Bill_Domain();
				  dm=(Bill_Domain)listview.getItemAtPosition(arg2);
				  Intent localIntent = new Intent(Bill.this,Pending_trans.class);
				  localIntent.putExtra("bill_id", dm.getBillid()); 
				  localIntent.putExtra("member_name",mem_name);
				  startActivity(localIntent);       
			}
		});
	  }
	 private void searchReceipt(String st)
	  {
		DBAdapter db=new DBAdapter(this);
		memberlist = db.getbillby_clients(mem_id);
		Iterator itr=memberlist.iterator();
		while(itr.hasNext())
		{
			Bill_Domain dd=new Bill_Domain();
			dd=(Bill_Domain)itr.next();
			System.out.println(dd.getBill_sttus());
			if(dd.getBill_sttus().equals("P"))
			{				
				balanc+=dd.getBill_amount();
			
			}
			else if(dd.getBill_sttus().equals("R"))
			{				
				rec+=dd.getBill_amount();
				
			}
		}
		act.setTitle("Balance: "+(balanc-rec));
		db.close();
	    listAdapter = new ListAdapter(this, R.layout.view_bill,memberlist);
	    listview.setAdapter(listAdapter);
	    listAdapter.notifyDataSetChanged();
	  }
	 protected void onResume()
	  {
		 balanc=0.0;
		 rec=0.0;
		 searchReceipt("");
		 super.onResume();
	    
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
        if((((Bill_Domain)this.data.get(paramInt)).getBill_sttus()).equals("P"))
        {
        	paramView.setBackgroundColor(Color.parseColor("#FEB7B7"));
        }
        else if((((Bill_Domain)this.data.get(paramInt)).getBill_sttus()).equals("R"))
        {
        	paramView.setBackgroundColor(Color.parseColor("#C8FEB7"));
        	bill_view.text_balc.setTextColor(Color.parseColor("#1B6803"));
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) 
        {
        	 bill_view.text_name.setText(setdates(((Bill_Domain)this.data.get(paramInt)).getBill_Duedate()));
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
        	bill_view.text_name.setText(setdates(((Bill_Domain)this.data.get(paramInt)).getBill_Duedate()));
             bill_view.text_detail.setText(((Bill_Domain)this.data.get(paramInt)).getBill_detl());
        }
        
        bill_view.text_balc.setText("Bal "+((Bill_Domain)this.data.get(paramInt)).getBill_amount());
        
        bill_view.text_date.setText(setdates(((Bill_Domain)this.data.get(paramInt)).getBill_date()));
        bill_view = (ViewHolder)paramView.getTag();
      
	return paramView;
    }
    public String setdates(String str2)
    {
    	String str ="";
    	
    	if(!str2.isEmpty())
    	{
    		str = new SimpleDateFormat("dd.MMM.yyyy").format(Date.parse(str2));
    	}
    	return str;
    }
  }
  static class ViewHolder
  {
	    TextView text_name;
	    TextView text_detail;
	    TextView text_balc;
	    TextView text_date;
  }
}