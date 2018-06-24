package com.Daybook.daybook;
import java.util.ArrayList;
import java.util.List;

import com.Daybook.Domain.Reminder_Domain;
import com.Daybook.db.DBAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Reminder_Setter extends Activity {
	
    private Builder alertDialog;
	private List<Reminder_Domain> reminderlist;
	private ListView listview;
	private ListAdapter listAdapter;
	private ViewHolder localViewHolder;
	private DBAdapter db=new DBAdapter(this);
	public void onCreate(Bundle paramBundle)
	  {
		alertDialog = new AlertDialog.Builder(Reminder_Setter.this);  
		super.onCreate(paramBundle);
	    setContentView(R.layout.reminder_setting);
	    ActionBar act=getActionBar();
	    act.setTitle("Reminder");
		reminderlist = new ArrayList<Reminder_Domain>();
	    listview = ((ListView)findViewById(R.id.listView_reminder));
	    listAdapter = new ListAdapter(this, R.layout.view_reminders,reminderlist);
	    listview.setAdapter(listAdapter);
	    listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	   
	    act=getActionBar();
		act.setTitle("Reminder Setting");
	   
	      alertDialog.setMessage("Do you want to delete this?");
	      alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	         

			public void onClick(DialogInterface dialog,int which) {
	        	  
	        	
			        for (Reminder_Domain td : listAdapter.getBox())
			        {
			        	
			            db.delete_reminder(td.getRem_id());
			        }
			        searchReceipt("");
	          }
	      });
	      alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int which) {
	          dialog.cancel();
	          }
	      });   
	  }
	 private void searchReceipt(String paramString)
	  {
		
		reminderlist = db.getallreminder("");
		db.close();
	    listAdapter = new ListAdapter(this, R.layout.view_reminders,reminderlist);
	    listview.setAdapter(listAdapter);
	    listAdapter.notifyDataSetChanged();
	    
	  }
	 protected void onResume()
	  {
	    searchReceipt("");
	    super.onResume();
	  }
	private class ListAdapter
    extends BaseAdapter
  {
    private Context context;
    private List<Reminder_Domain> data = null;
    private int layoutId;
	
    
    public ListAdapter(Context contextobj,int layout_id, List<Reminder_Domain> paramList)
    {
      
      this.context = contextobj;
      this.layoutId = layout_id;
      this.data = paramList;
    }

    public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      
        paramView = ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(layoutId, paramViewGroup, false);
        localViewHolder = new ViewHolder();
        
        localViewHolder.textdays = ((TextView)paramView.findViewById(R.id.textView_dayss));
        localViewHolder.buttontime = ((Button)paramView.findViewById(R.id.button_times));
        localViewHolder.checkstatus= ((CheckBox)paramView.findViewById(R.id.checkBox_sett));
        localViewHolder.checkstatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				
				getProduct((Integer) buttonView.getTag()).box = isChecked;
				  
			}
		});
        localViewHolder.checkstatus.setTag(paramInt);
        paramView.setTag(localViewHolder);
        String str[]=(((Reminder_Domain)this.data.get(paramInt)).getRem_time()).split(":");
        int hr=Integer.parseInt(str[0]);
        int sethr=0;
        String ampm="";
        if(hr>=12)
        {
        	if(hr==12)
        	{
        		sethr=12;
        		ampm="PM";
        	}
        	else
        	{
        	sethr=hr-12;
        	ampm="PM";
        	}
        }
        else
        {
        	sethr=hr;
        	ampm="AM";
        }
        localViewHolder.buttontime.setText(sethr+":"+str[1]+" "+ampm);
        localViewHolder.textdays.setText(((Reminder_Domain)this.data.get(paramInt)).getRem_days());
        localViewHolder = (Reminder_Setter.ViewHolder)paramView.getTag();
      
	return paramView;
    }

    private Reminder_Domain getProduct(int paramInt) {
    	return ((Reminder_Domain) getItem(paramInt));
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

    ArrayList<Reminder_Domain> getBox() {
        ArrayList<Reminder_Domain> box = new ArrayList<Reminder_Domain>();
        for (Reminder_Domain p : data) {
            if (p.isBox())
                box.add(p);
        }
      
        return box;
    }
  }
  static class ViewHolder
  {
    TextView textdays;
    Button buttontime;
    CheckBox checkstatus;
  }
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      MenuInflater inflater = getMenuInflater();
	      
	      inflater.inflate(R.layout.menu_add_reminder, menu);
	      
	      return super.onCreateOptionsMenu(menu);
	  }
	public boolean onOptionsItemSelected(MenuItem item)
	  {
	  	
	  	switch (item.getItemId()) {
	  
	      case R.id.add_rem:

	    	  Intent localIntent = new Intent(this,Add_reminder.class);
			  startActivity(localIntent);
	          return true;
	      case R.id.del_rem:
	    	  if(listAdapter.getBox().size()==0)
	    	  {
	    		  Toast.makeText(this, "Select Reminder to delete",1).show();
	    	  }
	    	  else
	    	  {
	    		  alertDialog.show();
	    	  }
	    	  listAdapter.getBox().size();
	          return true;
	      default:
	          
	      return super.onOptionsItemSelected(item);
	  }
	}

}
