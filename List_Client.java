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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class List_Client extends Activity
{
	private List<Register_DOMAIN> memberlist;
	private EditText edittextKeyword;
	private ListView listview;
	ViewHolder localViewHolder;
	private ListAdapter listAdapter;
	ActionBar act;
	DBAdapter db=new DBAdapter(this);
	AlertDialog.Builder alertDialog ;
	private int memval;
	public void onCreate(Bundle paramBundle)
	{
		alertDialog = new AlertDialog.Builder(List_Client.this);
		super.onCreate(paramBundle);
	    setContentView(R.layout.list_client);
	    
		memberlist = new ArrayList<Register_DOMAIN>();
	    listview = ((ListView)findViewById(R.id.listview));
	    listAdapter = new ListAdapter(this, R.layout.view_list_client,memberlist);
	    listview.setAdapter(listAdapter);
	    listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {
				 
				  Register_DOMAIN dm=new Register_DOMAIN();
				  dm=(Register_DOMAIN)listview.getItemAtPosition(arg2);
				  Intent localIntent = new Intent(List_Client.this,Entry.class);
				  localIntent.putExtra("member_id", dm.getMem_id());  
				  startActivity(localIntent);      
				  
			}
		});
	    act=getActionBar();
		act.setTitle("Select Client");
	   
	      alertDialog.setMessage("Do you want to delete this?");
	      alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog,int which) {
	        	  db.delete_CLIENTby_id(memval);
	        	  searchReceipt(edittextKeyword.getText().toString());
				  db.close();
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
		
		DBAdapter db=new DBAdapter(this);
		memberlist = db.searchReceipt(paramString);
		db.close();
	    listAdapter = new ListAdapter(this, R.layout.view_list_client,memberlist);
	    listview.setAdapter(listAdapter);
	    listAdapter.notifyDataSetChanged();
	    
	  }
	 protected void onResume()
	  {
	    searchReceipt("");
	    super.onResume();
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
        localViewHolder = (List_Client.ViewHolder)paramView.getTag();
      
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
      
      inflater.inflate(R.layout.menusearchadd, menu);
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
          
    	  Intent localIntent = new Intent(List_Client.this, Addcostmer.class);	
    	  localIntent.putExtra("operation", "List_Client");
		  startActivity(localIntent);
		
      	
          return true;
      default:
          
      return super.onOptionsItemSelected(item);
  }
}
}
