package com.Daybook.daybook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.Daybook.BO.BASEBO;
import com.Daybook.Domain.Bill_Domain;
import com.Daybook.Domain.Register_DOMAIN;
import com.Daybook.db.DBAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class Reminder extends Activity
{

	private List<Bill_Domain> memberlist;
	private ListView listview;
	ViewHolder localViewHolder;
	private ListAdapter listAdapter;
	DBAdapter db=new DBAdapter(this);
	AlertDialog.Builder alertDialog ;
	private DatePickerDialog datePickerDialog;
	private DatePickerDialog.OnDateSetListener dateSetListener;
	EditText search;
	BASEBO bo=null;
	public void onCreate(Bundle paramBundle)
	{
		bo=new BASEBO();
		initDateSetListener();
		alertDialog = new AlertDialog.Builder(Reminder.this);
		ActionBar act=getActionBar();
		act.setCustomView(R.layout.datelayoutactionbar);
        search = (EditText) act.getCustomView().findViewById(R.id.searchfield_date);
        String str = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        search.setText(str);
		super.onCreate(paramBundle);
	    setContentView(R.layout.daybook);
		memberlist = new ArrayList<Bill_Domain>();
	    listview = ((ListView)findViewById(R.id.listView_pending));
	    listAdapter = new ListAdapter(this, R.layout.view_daybook,memberlist);
	    listview.setAdapter(listAdapter);       
	    act.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
	    registerForContextMenu(listview);
	    search.setOnTouchListener(new View.OnTouchListener()
	    {
		      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
		      {
		        if (paramAnonymousMotionEvent.getAction() == 0) {
		                  showDatePickerDialog();
		        }
		        return false;
		      }
		    });	 
	  }
	 private void searchReceipt(String st)
	  {
		DBAdapter db=new DBAdapter(this);
		memberlist = db.get_reminder_date(st);
		db.close();
	    listAdapter = new ListAdapter(this, R.layout.view_daybook,memberlist);
	    listview.setAdapter(listAdapter);
	    listAdapter.notifyDataSetChanged();
	  }
	 protected void onResume()
	  {
		  String str = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		  searchReceipt(str);
		  super.onResume(); 
	  }
	 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		 
		   super.onCreateContextMenu(menu, v, menuInfo);
		   
		   menu.setHeaderTitle("Action");
		   menu.add(0, v.getId(), 0, "Call");
		   menu.add(0, v.getId(), 0, "Send SMS");
		  
		  
		  }
		  public boolean onContextItemSelected(MenuItem item) {
			 
			  
			  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			  int arrayAdapterPosition = info.position;
			  Bill_Domain dm=new Bill_Domain();
			 
			  dm=(Bill_Domain)memberlist.get(arrayAdapterPosition);
			  Register_DOMAIN rd=new Register_DOMAIN();
			  rd=db.get_CLIENTby_CLIENTid(dm.getClient_id());
			  db.close();
			 if (item.getTitle() == "Call") {
			    	  Intent i=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+rd.getMem_contact()));
			    	  startActivity(i);
			            
				   
				  } 
			      else if (item.getTitle() == "Send SMS")
			      {
			    	  Intent i2=new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",rd.getMem_contact(),null));
			    	  startActivity(i2); 
				   
				  }
			      else if (item.getTitle() == "New Reminder")
			      {
			    	  /*
			    	  Intent localIntent = new Intent(this,Update_Pending.class);
					  localIntent.putExtra("tr_id",dm.getTrans_id()); 
					  localIntent.putExtra("member_name", dm.getMem_name());
					  startActivity(localIntent);
					  */
				   
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
        localViewHolder = new ViewHolder();
        localViewHolder.textviewName = ((TextView)paramView.findViewById(R.id.pending_name));
        localViewHolder.textviewaddr = ((TextView)paramView.findViewById(R.id.pending_city));
        localViewHolder.textviewmobile =((TextView)paramView.findViewById(R.id.pending_total));
        localViewHolder.textcash =((TextView)paramView.findViewById(R.id.textView_cash));
        paramView.setTag(localViewHolder);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) 
        {
        	
        	localViewHolder.textviewName.setText(bo.short20_String(((Bill_Domain)this.data.get(paramInt)).getBill_Duedate()));
            localViewHolder.textviewaddr.setText(bo.short20_String(((Bill_Domain)this.data.get(paramInt)).getBill_detl()+""));
            localViewHolder.textviewmobile.setText(bo.short12_String(((Bill_Domain)this.data.get(paramInt)).getBill_amount()+""));
            localViewHolder.textcash.setText(setdates(((Bill_Domain)this.data.get(paramInt)).getBill_date()+""));
        } else 
        {
        	localViewHolder.textviewName.setText(((Bill_Domain)this.data.get(paramInt)).getBill_Duedate());
            localViewHolder.textviewaddr.setText(((Bill_Domain)this.data.get(paramInt)).getBill_detl());
            localViewHolder.textviewmobile.setText(((Bill_Domain)this.data.get(paramInt)).getBill_amount()+"");
            localViewHolder.textcash.setText(setdates(((Bill_Domain)this.data.get(paramInt)).getBill_date()+""));       
        }
       
        localViewHolder = (Reminder.ViewHolder)paramView.getTag();
        localViewHolder = (ViewHolder)paramView.getTag();
      
	return paramView;
    }
    public String setdates(String str2)
    {
    	String str = new SimpleDateFormat("dd.MMM.yyyy").format(Date.parse(str2));
    	return str;
    }
  }
  static class ViewHolder
  {
    TextView textviewaddr;
    TextView textviewName;
    TextView textviewmobile;
    TextView textcash;
  }
 

  private void showDatePickerDialog()
  {
	    String str = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
	    int i = Integer.parseInt(str.substring(0, 4));
	    int j = -1 + Integer.parseInt(str.substring(5, 7));
	    int k = Integer.parseInt(str.substring(8));
	    datePickerDialog.updateDate(i, j, k);
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
        
    	  String str2=new SimpleDateFormat("dd/MM/yyyy").format(localDate);
    	  search.setText(str2);
    	  searchReceipt(localSimpleDateFormat.format(localDate));
      }
    };
    Calendar localCalendar = Calendar.getInstance();
    datePickerDialog = new DatePickerDialog(this, dateSetListener, localCalendar.get(1), localCalendar.get(2), localCalendar.get(5));
  }
  
}