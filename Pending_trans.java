package com.Daybook.daybook;

import java.util.ArrayList;
import java.util.List;

import com.Daybook.BO.BASEBO;
import com.Daybook.Domain.trans_DOMAIN;
import com.Daybook.db.DBAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
public class Pending_trans extends Activity 
{
	private List<trans_DOMAIN> memberlist;
	private ListView listview;
	ViewHolder localViewHolder;
	private ListAdapter listAdapter;
	private int bill_ids;
	DBAdapter db=new DBAdapter(this);
	AlertDialog.Builder alertDialog ;
	TextView totpric;
	LinearLayout lays;
	ActionBar act;
	BASEBO bo=null;
	public void onCreate(Bundle paramBundle)
	{
		bo=new BASEBO();
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Pending_trans.this);
		super.onCreate(paramBundle);
	    setContentView(R.layout.pending_trans);
	    act=getActionBar();
	    bill_ids=getIntent().getIntExtra("bill_id", 0);
        act.setTitle("Bill Detail");
		memberlist = new ArrayList<trans_DOMAIN>();
	    listview = ((ListView)findViewById(R.id.viewpendingtrans));
	    listAdapter = new ListAdapter(this, R.layout.view_pending,memberlist);
	    listview.setAdapter(listAdapter);    
	    listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    lays=(LinearLayout) findViewById(R.id.linearlay);
	    lays.setVisibility(View.GONE);
	    Button delbtn=(Button) findViewById(R.id.button_del);
	    delbtn.setOnClickListener(new  View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog.show();
				
			}
		});
	    
	    alertDialog.setTitle("Confirm Delete...");
	      
	      alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog,int which)
	          {
	        	  
			        for (trans_DOMAIN td : listAdapter.getBox())
			        {
			            db.del_pending(td);
			            db.close();
			        }
			        searchReceipt(bill_ids);
	          }
	      });
	      alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int which) {
	          dialog.cancel();
	          }
	      });
	  }
	 private void searchReceipt(int st)
	  {
		DBAdapter db=new DBAdapter(this);
		memberlist = db.getpendingbyid(st);
		db.close();
	    listAdapter = new ListAdapter(this, R.layout.view_pending_trans,memberlist);
	    listview.setAdapter(listAdapter);
	    listAdapter.notifyDataSetChanged();
	  }
	 protected void onResume()
	  {
		 searchReceipt(bill_ids);
		  super.onResume();
	    
	  }
	 
	private class ListAdapter
    extends BaseAdapter
  {
    private Context context;
    private List<trans_DOMAIN> data = null;
    private int layoutId;
    
    public ListAdapter(Context contextobj,int layout_id, List<trans_DOMAIN> paramList)
    {
      
      this.context = contextobj;
      this.layoutId = layout_id;
      this.data = paramList;
    }

    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup)
    {
    	
        paramView = ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(layoutId, paramViewGroup, false);
        localViewHolder = new ViewHolder();
        localViewHolder.textviewdetail = ((TextView)paramView.findViewById(R.id.item_detial2));
        localViewHolder.textviewitemname = ((TextView)paramView.findViewById(R.id.item_name_view));
        localViewHolder.textviewamount =((TextView)paramView.findViewById(R.id.amount_view));
        
        
        localViewHolder.checkbx=(CheckBox) paramView.findViewById(R.id.checkBox_read);
        localViewHolder.checkbx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				
				getProduct((Integer) buttonView.getTag()).box = isChecked;
				int totalAmount=0;
		        for (trans_DOMAIN td : listAdapter.getBox())
		        {
		            totalAmount+=td.getTotamnt();    
		        }
		       
		        if(totalAmount==0)
		        {
		        	 act.setTitle("Balance Transections");
		        }
		        else
		        {
		        	 act.setTitle("Total:- "+totalAmount);
		        }
		       
			}
		});
        localViewHolder.checkbx.setTag(paramInt);
        
        paramView.setTag(localViewHolder);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) 
        {
        	localViewHolder.textviewdetail.setText(bo.short20_String(((trans_DOMAIN)this.data.get(paramInt)).getItem_detail()));
            localViewHolder.textviewitemname.setText(bo.short20_String(((trans_DOMAIN)this.data.get(paramInt)).getItemname()));
            localViewHolder.textviewamount.setText(((trans_DOMAIN)this.data.get(paramInt)).getTotamnt()+"");
        }
        else
        {
        	localViewHolder.textviewdetail.setText(((trans_DOMAIN)this.data.get(paramInt)).getItem_detail());
            localViewHolder.textviewitemname.setText(((trans_DOMAIN)this.data.get(paramInt)).getItemname());
            localViewHolder.textviewamount.setText(((trans_DOMAIN)this.data.get(paramInt)).getTotamnt()+"");
        }
       
        localViewHolder = (Pending_trans.ViewHolder)paramView.getTag();
        localViewHolder = (ViewHolder)paramView.getTag();
       
       
	return paramView;
    }

    private trans_DOMAIN getProduct(int paramInt) {
    	return ((trans_DOMAIN) getItem(paramInt));
	}

	@Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ArrayList<trans_DOMAIN> getBox() {
        ArrayList<trans_DOMAIN> box = new ArrayList<trans_DOMAIN>();
        for (trans_DOMAIN p : data) {
            if (p.isBox())
                box.add(p);
        }
        if(box.isEmpty()==false)
        {
        	lays.setVisibility(View.VISIBLE);
        }
        else
        {
        	lays.setVisibility(View.GONE);
        }
        return box;
    }
  }
  static class ViewHolder
  {
    TextView textviewitemname;
    TextView textviewdetail;
    TextView textviewamount;
    CheckBox checkbx;
  }
}