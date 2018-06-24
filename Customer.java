package com.Daybook.daybook;

import java.util.ArrayList;
import java.util.List;

import com.Daybook.Domain.Register_DOMAIN;
import com.Daybook.db.DBAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class Customer extends Activity
{
	private List<Register_DOMAIN> memberlist;
	private EditText edittextKeyword;
	ActionBar act;
	private ListView listview;
	ViewHolder localViewHolder;
	private ListAdapter listAdapter;
	
	DBAdapter db=new DBAdapter(this);
	AlertDialog.Builder alertDialog ;
	private int memval;
	public void onCreate(Bundle paramBundle)
	{
		alertDialog = new AlertDialog.Builder(Customer.this);
		super.onCreate(paramBundle);
		act=getActionBar();
		act.setTitle("Customer");
	    setContentView(R.layout.customers);
		memberlist = new ArrayList<Register_DOMAIN>();
	    listview = ((ListView)findViewById(R.id.listview));
	    listAdapter = new ListAdapter(this,R.layout.view_customer,memberlist);
	    listview.setAdapter(listAdapter);
	    listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {
				 
				  Register_DOMAIN dm=new Register_DOMAIN();
				  dm=(Register_DOMAIN)listview.getItemAtPosition(arg2);
				  Intent localIntent = new Intent(Customer.this,Bill.class);
				  localIntent.putExtra("member_id", dm.getMem_id()); 
				  localIntent.putExtra("member_name", dm.getMem_name());
				  startActivity(localIntent);
	      
			}
		});
	    registerForContextMenu(listview);
	    delete_confirm();
	  }
	 private void searchReceipt(String paramString)
	  {
		
		DBAdapter db=new DBAdapter(this);
		memberlist = db.searchReceipt(paramString);
		db.close();
	    listAdapter = new ListAdapter(this, R.layout.view_customer,memberlist);
	    listview.setAdapter(listAdapter);
	    
	    listAdapter.notifyDataSetChanged();
	    
	  }
	 protected void onResume()
	  {
	    searchReceipt("");
	    super.onResume();
	  }
	 
	 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		 
		   super.onCreateContextMenu(menu, v, menuInfo);
		   
		   menu.setHeaderTitle("Action");
		   menu.add(0, v.getId(), 0, "Call");
		   menu.add(0, v.getId(), 0, "Send SMS");
		   menu.add(0, v.getId(), 0, "Delete Client");
		  }
		  public boolean onContextItemSelected(MenuItem item) {
			 
			  
			  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			  int arrayAdapterPosition = info.position;
			  Register_DOMAIN dm=new Register_DOMAIN();
			 
			  dm=(Register_DOMAIN)memberlist.get(arrayAdapterPosition);
			  
			  if (item.getTitle() == "Delete Client") {
				 
				  memval=dm.getMem_id();
				  alertDialog.show();
				  
				  } 
			      else if (item.getTitle() == "Call") {
			    	  Intent i=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+dm.getMem_contact()));
			    	    startActivity(i);
			            
				   
				  } 
			      else if (item.getTitle() == "Send SMS")
			      {
			    	  Intent i2=new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",dm.getMem_contact(),null));
			    	    startActivity(i2); 
				   
				  }
				  return true;
		}
	 
	 
	 
	private class ListAdapter
    extends ArrayAdapter<Register_DOMAIN>
  {
    private Context context;
    private List<Register_DOMAIN> data = null;
    private int layoutId;
    
    public ListAdapter(Context contextobj,int layout_id, List<Register_DOMAIN> paramList)
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
        localViewHolder.textviewName = ((TextView)paramView.findViewById(R.id.textview_name));
        localViewHolder.textviewaddr = ((TextView)paramView.findViewById(R.id.textaddr));
        paramView.setTag(localViewHolder);
        localViewHolder.textviewName.setText(((Register_DOMAIN)this.data.get(paramInt)).getMem_name());
        localViewHolder.textviewaddr.setText(((Register_DOMAIN)this.data.get(paramInt)).getMem_addr());
        localViewHolder = (Customer.ViewHolder)paramView.getTag();
      
	return paramView;
    }
  }
  static class ViewHolder
  {
    TextView textviewaddr;
    TextView textviewName;
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      
      inflater.inflate(R.layout.menucustmer, menu);
      return super.onCreateOptionsMenu(menu);
  }
public boolean onOptionsItemSelected(MenuItem item)
  {
  	
  	switch (item.getItemId()) {
      case R.id.action_search:
    	  act.setCustomView(R.layout.customer_search_layout);
    	  act.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
    	  edittextKeyword =((EditText)findViewById(R.id.edittext_keyword));
    	  InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    	  keyboard.showSoftInput(edittextKeyword, 0);
  	      edittextKeyword.addTextChangedListener(new TextWatcher()
  	      {
  	       public void afterTextChanged(Editable paramAnonymousEditable)
  	       {
  	           String str = edittextKeyword.getText().toString();
  	           searchReceipt(str);
  	       }
  	      public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
  	      public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
  	    });
          return true;
      case R.id.action_add_user:
    	  Intent localIntent = new Intent(Customer.this, Addcostmer.class);	
    	  localIntent.putExtra("operation", "Customer");
		  startActivity(localIntent);
		 
          return true;
   
      default:
          
      return super.onOptionsItemSelected(item);
  }
}
	public void delete_confirm()
	{
		    alertDialog.setMessage("Delete this ?");
	        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog,int which) { 
	        db.delete_CLIENTby_id(memval);
	        db.close();
	           searchReceipt("");
	        }
	      });
	        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	            }
	        });
	}

}
