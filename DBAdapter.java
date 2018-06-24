package com.Daybook.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.Daybook.BO.BASEBO;
import com.Daybook.Domain.Bill_Domain;
import com.Daybook.Domain.Register_DOMAIN;
import com.Daybook.Domain.Reminder_Domain;
import com.Daybook.Domain.trans_DOMAIN;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Base64;
public class DBAdapter extends SQLiteOpenHelper 
{

	 private static final String DATABASE_NAME = "daybook.db";
	 private static final int DATABASE_VERSION = 1; 
	
	 private String TABLE_CLIENTS="CLIENTS";
	 private String TABLE_CLIENT_trans="CLIENT_trans";
	 private String Table_auth="activation";
		 
	 private String CLIENT_NAME="CLIENT_Name";
	 private String CLIENT_ADD="CLIENT_Add";
	 private String CLIENT_MOBILE="CLIENT_Mob";

	 private	String TRANSItemname="trans_itemname";
	 private	String TRANS_Item_detail="trans_itemdetail";
	 private	String TRANStype="trans_type";
	 
	 private	String rem_time="R_TIME";
	 private	String rem_vib="R_VIB";
	 private	String rem_sound="R_SOUND";
	 private	String rem_day="R_DAYS";	 
	 
	 
	 private String ACT_CODE="ac_CODE";
	 private String ACT_STATUS="ac_STATUS";
	 
	 
	 public DBAdapter(Context context )
	 {
		 
		 super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		
		
		String str2="create table CLIENTS(CLIENT_ID INTEGER PRIMARY KEY  AUTOINCREMENT,CLIENT_Name text,CLIENT_Add text,CLIENT_Mob text)";
		String str3="create table CLIENT_trans(trans_ID INTEGER PRIMARY KEY  AUTOINCREMENT,trans_type text,trans_itemname text,trans_itemdetail text,trans_totamt text,trans_bill_id INTEGER)";
		String str4="create table activation(ac_ID INTEGER PRIMARY KEY  AUTOINCREMENT,ac_CODE text,ac_STATUS text)";
		db.execSQL("create table Reminders(R_ID INTEGER PRIMARY KEY AUTOINCREMENT,R_TIME DATETIME,R_VIB text,R_SOUND text,R_DAYS text)");
		db.execSQL("create table BILL_MAST(BILL_ID INTEGER PRIMARY KEY AUTOINCREMENT,BILL_CLIENT_ID INTEGER,BILL_DATE DATETIME,BILL_DUE_DATE DATETIME,BILL_DETAIL text,BILL_AMOUNT text,BILL_STATUS text)");
	
		db.execSQL(str2);
		db.execSQL(str3);
		db.execSQL(str4);
	
	}
	private String convertbye(String str)
	{
		String node="mynode";
		String str2 =Base64.encodeToString((str+node).getBytes(), Base64.NO_WRAP);
		return  str2;
	}
	public String get_auth()
	{
	
		String str="false";
	    String str2 = "SELECT * FROM activation WHERE ac_CODE='"+convertbye(Build.SERIAL)+"'";
	    SQLiteDatabase db=this.getWritableDatabase();
	    Cursor localCursor =db.rawQuery(str2, null);
	    int val=localCursor.getCount();
	    if(val>0)
	    {
	    	str="true";
	    }
		return str;
	}
    public String activatepr()
    {
	        SQLiteDatabase db=this.getWritableDatabase();    		
	    	ContentValues cval1 = new ContentValues();
	    	cval1.put(ACT_CODE,convertbye(Build.SERIAL));
			db.insert(Table_auth, null,cval1);
	    	return "true";
	   
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		
		
	}
	public String insert_trans(trans_DOMAIN dm)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cval = new ContentValues();
		cval.put(TRANS_Item_detail, dm.getItem_detail());
		cval.put(TRANStype, dm.getType());
		cval.put(TRANSItemname, dm.getItemname());
		cval.put("trans_bill_id", dm.getBid());
		cval.put("trans_totamt", dm.getTotamnt());
		db.insert(TABLE_CLIENT_trans, null,cval);
		db.execSQL("update  BILL_MAST set BILL_AMOUNT=BILL_AMOUNT+'"+dm.getTotamnt()+"' where BILL_ID="+dm.getBid());
		return "true";
	}
	public void register_CLIENT(String string, String string2, long string3) {
		
		SQLiteDatabase db=this.getWritableDatabase();
	
		ContentValues cval = new ContentValues();
		cval.put(CLIENT_NAME, string);
		cval.put(CLIENT_ADD,  string2);
		cval.put(CLIENT_MOBILE, string3);
		db.insert(TABLE_CLIENTS, null, cval);
		
	}

	public List<Bill_Domain> get_recordby_date(String date2) {
	
		ArrayList<Bill_Domain> localArrayList = new ArrayList<Bill_Domain>();
	    String str = "SELECT * FROM BILL_MAST INNER JOIN CLIENTS ON BILL_CLIENT_ID=CLIENT_ID WHERE BILL_DATE='" + date2 +"'";
	    SQLiteDatabase db=this.getWritableDatabase();
	    Cursor localCursor =db.rawQuery(str, null);
	    if ((localCursor != null) && (localCursor.moveToFirst())) {
	      do
	      {
	    	  localArrayList.add(new Bill_Domain(localCursor.getInt(localCursor.getColumnIndex("BILL_ID")), localCursor.getInt(localCursor.getColumnIndex("BILL_CLIENT_ID")), localCursor.getString(localCursor.getColumnIndex("BILL_DATE")), localCursor.getString(localCursor.getColumnIndex("CLIENT_Name")), localCursor.getString(localCursor.getColumnIndex("BILL_DETAIL")), localCursor.getDouble(localCursor.getColumnIndex("BILL_AMOUNT")),localCursor.getString(localCursor.getColumnIndex("BILL_STATUS"))));
	                                                                               																							                                                
	      } while (localCursor.moveToNext());
	    }
	    return localArrayList;	
	}
	public void delete_CLIENTby_id(int CLIENTval) {
		
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL("delete from CLIENT_trans where trans_ID IN(select trans_ID from CLIENT_trans inner join Bill_MAST on trans_bill_id=BILL_ID and BILL_CLIENT_ID="+CLIENTval+")");
		db.execSQL("delete from BILL_MAST where BILL_CLIENT_ID="+CLIENTval);
		db.execSQL("delete from CLIENTS where CLIENT_ID="+CLIENTval);
	}

	public Register_DOMAIN get_CLIENTby_CLIENTid(int paramString)
	  {
	
		Register_DOMAIN dm=new Register_DOMAIN();
	    String str = "SELECT * FROM CLIENTS WHERE CLIENT_ID ='" + paramString + "'";
	    SQLiteDatabase db=this.getWritableDatabase();
	    Cursor localCursor =db.rawQuery(str, null);
	    if ((localCursor != null) && (localCursor.moveToFirst())) {
	      do
	      {
	    	  dm=new Register_DOMAIN(localCursor.getInt(localCursor.getColumnIndex("CLIENT_ID")), localCursor.getString(localCursor.getColumnIndex("CLIENT_Name")), localCursor.getString(localCursor.getColumnIndex("CLIENT_Add")), localCursor.getString(localCursor.getColumnIndex("CLIENT_Mob")));
	      } while (localCursor.moveToNext());
	    }
	  
	    return dm;
	  }
     public void check_tables()
     {
    	
    	  
     }

	public List<Bill_Domain> getbill(String mm)
	  {
		
	    ArrayList<Bill_Domain> localArrayList = new ArrayList<Bill_Domain>();
	    System.out.println(mm);
	    String str = "select * from BILL_MAST inner join CLIENTS on BILL_CLIENT_ID=CLIENT_ID where BILL_STATUS='P' and BILL_DATE LIKE '%"+mm+"%' ORDER BY BILL_DATE DESC";
	    SQLiteDatabase db=this.getReadableDatabase();
	    Cursor localCursor =db.rawQuery(str, null);
	    if ((localCursor != null) && (localCursor.moveToFirst())) {
	      do
	      {
	        localArrayList.add(new Bill_Domain(localCursor.getInt(localCursor.getColumnIndex("BILL_ID")), localCursor.getInt(localCursor.getColumnIndex("BILL_CLIENT_ID")), localCursor.getString(localCursor.getColumnIndex("BILL_DATE")), localCursor.getString(localCursor.getColumnIndex("CLIENT_Name")), localCursor.getString(localCursor.getColumnIndex("BILL_DETAIL")), localCursor.getDouble(localCursor.getColumnIndex("BILL_AMOUNT")),localCursor.getString(localCursor.getColumnIndex("BILL_STATUS"))));
	        																																																																								
	      } while (localCursor.moveToNext());
	    }
	    return localArrayList;
	  }
	public List<Register_DOMAIN> searchReceipt(String paramString)
	  {
	
	    ArrayList<Register_DOMAIN> localArrayList = new ArrayList<Register_DOMAIN>();
	    String str = "SELECT * FROM CLIENTS WHERE CLIENT_Name LIKE '%" + paramString + "%' or  CLIENT_Mob LIKE '%" + paramString + "%' ORDER BY CLIENT_Name ASC";
	    SQLiteDatabase db=this.getWritableDatabase();
	    Cursor localCursor =db.rawQuery(str, null);
	    if ((localCursor != null) && (localCursor.moveToFirst())) {
	    	do
	      {
	        localArrayList.add(new Register_DOMAIN(localCursor.getInt(localCursor.getColumnIndex("CLIENT_ID")), localCursor.getString(localCursor.getColumnIndex("CLIENT_Name")), localCursor.getString(localCursor.getColumnIndex("CLIENT_Add")), localCursor.getString(localCursor.getColumnIndex("CLIENT_Mob"))));
	      } while (localCursor.moveToNext());
	    }
	    return localArrayList;
	  }
	public List<trans_DOMAIN> getpendingbyid(int no)
	  {
	
	    ArrayList<trans_DOMAIN> localArrayList = new ArrayList<trans_DOMAIN>();
	    String str = "SELECT * FROM CLIENT_trans where trans_bill_id="+no;
	    SQLiteDatabase db=this.getWritableDatabase();
	    Cursor localCursor =db.rawQuery(str, null);
	    if ((localCursor != null) && (localCursor.moveToFirst())) 
	    {
	    	do
	      {
	             localArrayList.add(new trans_DOMAIN(localCursor.getInt(localCursor.getColumnIndex("trans_ID")), localCursor.getString(localCursor.getColumnIndex("trans_itemname")), localCursor.getString(localCursor.getColumnIndex("trans_type")), localCursor.getString(localCursor.getColumnIndex("trans_itemdetail")),localCursor.getDouble(localCursor.getColumnIndex("trans_totamt")),localCursor.getInt(localCursor.getColumnIndex("trans_bill_id"))));
	      } while (localCursor.moveToNext());
	    }
	    return localArrayList;
	  }
	public void del_pending(trans_DOMAIN dm) 
	{
		SQLiteDatabase db=this.getWritableDatabase();	
		db.delete(TABLE_CLIENT_trans,"trans_ID="+dm.getTrans_id(), null);
		db.execSQL("update  BILL_MAST set BILL_AMOUNT=BILL_AMOUNT-'"+dm.getTotamnt()+"' where BILL_ID="+dm.getBid());
	}
    public List<Bill_Domain> get_reminder_date(String date2)
    {
    	
    	ArrayList<Bill_Domain> localArrayList = new ArrayList<Bill_Domain>();
 	    
 	    String str = "select * from BILL_MAST inner join CLIENTS on BILL_CLIENT_ID=CLIENT_ID where BILL_STATUS='P' and BILL_DUE_DATE='"+date2+"'";
 	    SQLiteDatabase db=this.getReadableDatabase();
 	    Cursor localCursor =db.rawQuery(str, null);
 	    if ((localCursor != null) && (localCursor.moveToFirst())) {
 	      do
 	      {
 	        localArrayList.add(new Bill_Domain(localCursor.getInt(localCursor.getColumnIndex("BILL_ID")), localCursor.getInt(localCursor.getColumnIndex("BILL_CLIENT_ID")), localCursor.getString(localCursor.getColumnIndex("BILL_DATE")), localCursor.getString(localCursor.getColumnIndex("CLIENT_Name")), localCursor.getString(localCursor.getColumnIndex("BILL_DETAIL")), localCursor.getDouble(localCursor.getColumnIndex("BILL_AMOUNT")),localCursor.getString(localCursor.getColumnIndex("BILL_STATUS"))));																																																																								
 	      } while (localCursor.moveToNext());
 	    }
 	    return localArrayList;
    		
    }
    public BASEBO get_rec_by_trans_id(int id)
    {
    	trans_DOMAIN dm=null;
    	String str = "SELECT * FROM CLIENT_trans WHERE trans_ID='" + id +"'";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor localCursor =db.rawQuery(str, null);
        if ((localCursor != null) && (localCursor.moveToFirst())) 
	    {
        	dm=new trans_DOMAIN(localCursor.getInt(localCursor.getColumnIndex("trans_ID")), localCursor.getString(localCursor.getColumnIndex("trans_itemname")), localCursor.getString(localCursor.getColumnIndex("trans_type")), localCursor.getString(localCursor.getColumnIndex("trans_itemdetail")),localCursor.getDouble(localCursor.getColumnIndex("trans_totamt")),localCursor.getInt(localCursor.getColumnIndex("trans_bill_id")));
	    }
		return dm;	
    }
  public void update_pending_reminder(trans_DOMAIN dm) 
  {
		//SQLiteDatabase db=this.getWritableDatabase();
		//ContentValues cval = new ContentValues();
		//db.update(TABLE_CLIENT_trans, cval, "trans_ID="+dm.getTrans_id(), null);	
  }
  public void insert_reminder(Reminder_Domain rd)
  {
	    SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cval = new ContentValues();
		cval.put(rem_time, rd.getRem_time());
		cval.put(rem_vib, rd.getRem_vib());
		cval.put(rem_sound,rd.getRem_sound());
		cval.put(rem_day, rd.getRem_days());
		db.insert("Reminders", null, cval);
  }
  public List<Reminder_Domain> getallreminder(String str2)
  {
	  ArrayList<Reminder_Domain> localArrayList = new ArrayList<Reminder_Domain>();
	    String str = "SELECT * FROM Reminders where R_DAYS LIKE '%"+str2+"%'";
	    SQLiteDatabase db=this.getWritableDatabase();
	    Cursor localCursor =db.rawQuery(str, null);
	    if ((localCursor != null) && (localCursor.moveToFirst())) 
	    {
	      do
	      {
	        localArrayList.add(new Reminder_Domain(localCursor.getInt(localCursor.getColumnIndex("R_ID")), localCursor.getString(localCursor.getColumnIndex("R_TIME")),localCursor.getString(localCursor.getColumnIndex("R_VIB")),localCursor.getString(localCursor.getColumnIndex("R_SOUND")),localCursor.getString(localCursor.getColumnIndex("R_DAYS"))));                                                                           																							                                                
	      } while (localCursor.moveToNext());
	    }
	    return localArrayList;	
  }
  public List<Reminder_Domain> getallreminder_bytime(String str2)
  {
	  
	  ArrayList<Reminder_Domain> localArrayList = new ArrayList<Reminder_Domain>();
	  String dformate=new SimpleDateFormat("HH:mm").format(new Date());
	    String str = "SELECT * FROM Reminders where R_DAYS LIKE '%"+str2+"%' AND R_TIME>='"+dformate+"' ORDER BY R_TIME ASC LIMIT 0,1";
	   
	    SQLiteDatabase db=this.getWritableDatabase();
	    Cursor localCursor =db.rawQuery(str, null);
	    if ((localCursor != null) && (localCursor.moveToFirst())) 
	    {
	      do
	      {
	        localArrayList.add(new Reminder_Domain(localCursor.getInt(localCursor.getColumnIndex("R_ID")), localCursor.getString(localCursor.getColumnIndex("R_TIME")),localCursor.getString(localCursor.getColumnIndex("R_VIB")),localCursor.getString(localCursor.getColumnIndex("R_SOUND")),localCursor.getString(localCursor.getColumnIndex("R_DAYS"))));                                                                           																							                                                
	      } while (localCursor.moveToNext());
	    }
	    return localArrayList;	
  }
  public void delete_reminder(int rem_id)
  {
	    SQLiteDatabase db=this.getWritableDatabase();
		db.delete("Reminders","R_ID='"+rem_id+"'", null);
  }
  public int insert_bill(Bill_Domain dm)
  {
	    SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cval = new ContentValues();
		cval.put("BILL_CLIENT_ID",dm.getClient_id());
		cval.put("BILL_DATE",dm.getBill_date());
		cval.put("BILL_DUE_DATE",dm.getBill_Duedate());
		
		cval.put("BILL_AMOUNT", "0");
		if(dm.getBill_detl().equals("R"))
		{
			cval.put("BILL_DETAIL","Payment Received");
			cval.put("BILL_STATUS", "R");
		}
		else
		{
			cval.put("BILL_DETAIL"," ");
			cval.put("BILL_STATUS", "P");
		}
		
		db.insert("BILL_MAST", null, cval);
		int id=0;
		String str = "SELECT * FROM BILL_MAST ORDER BY BILL_ID DESC LIMIT 0,1";
		
	    Cursor localCursor =db.rawQuery(str, null);
	    if ((localCursor != null) && (localCursor.moveToFirst())) 
	    {
	      do
	      {
	    	  id=localCursor.getInt(localCursor.getColumnIndex("BILL_ID"));                                                                           																							                                                
	      } while (localCursor.moveToNext());
	    }
		
	    return id; 
  }
	public List<Bill_Domain> getbillby_clients(int id)
	  {
		
	    ArrayList<Bill_Domain> localArrayList = new ArrayList<Bill_Domain>();
	    String str = "select * from BILL_MAST where BILL_CLIENT_ID="+id;
	    SQLiteDatabase db=this.getReadableDatabase();
	    Cursor localCursor =db.rawQuery(str, null);
	    if ((localCursor != null) && (localCursor.moveToFirst())) {
	      do
	      {
	        localArrayList.add(new Bill_Domain(localCursor.getInt(localCursor.getColumnIndex("BILL_ID")), localCursor.getInt(localCursor.getColumnIndex("BILL_CLIENT_ID")), localCursor.getString(localCursor.getColumnIndex("BILL_DATE")), localCursor.getString(localCursor.getColumnIndex("BILL_DUE_DATE")), localCursor.getString(localCursor.getColumnIndex("BILL_DETAIL")), localCursor.getDouble(localCursor.getColumnIndex("BILL_AMOUNT")),localCursor.getString(localCursor.getColumnIndex("BILL_STATUS"))));
	        																																																																								
	      } while (localCursor.moveToNext());
	    }
	    return localArrayList;
	  }
	public void delete_bill_by_id(int billid)
	{
		SQLiteDatabase db=this.getReadableDatabase();
		db.execSQL("delete from BILL_MAST where BILL_ID="+billid);
		db.execSQL("delete from CLIENT_trans where trans_bill_id="+billid);
	}
	public void update_bill(int billid) {
		SQLiteDatabase db=this.getWritableDatabase();
		
		ContentValues cval = new ContentValues();
		String str2;
		str2 = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		cval.put("BILL_DUE_DATE",str2);
		cval.put("BILL_STATUS", "R");
		db.update("BILL_MAST", cval, "BILL_ID="+billid, null);
		
	}
	public Bill_Domain getbill_by_bill_id(int id)
	{
		Bill_Domain bd=new Bill_Domain();
	    String str = "select * from BILL_MAST where BILL_ID="+id;
	    SQLiteDatabase db=this.getReadableDatabase();
	    Cursor localCursor =db.rawQuery(str, null);
	    if ((localCursor != null) && (localCursor.moveToFirst())) {
	      do
	      {
	       bd=new Bill_Domain(localCursor.getInt(localCursor.getColumnIndex("BILL_ID")), localCursor.getInt(localCursor.getColumnIndex("BILL_CLIENT_ID")), localCursor.getString(localCursor.getColumnIndex("BILL_DATE")), localCursor.getString(localCursor.getColumnIndex("BILL_DUE_DATE")), localCursor.getString(localCursor.getColumnIndex("BILL_DETAIL")), localCursor.getDouble(localCursor.getColumnIndex("BILL_AMOUNT")),localCursor.getString(localCursor.getColumnIndex("BILL_STATUS")));
	        																																																																								
	      } while (localCursor.moveToNext());
	    }
	    return bd;
	}
	public void update_half_paid(Bill_Domain bd)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cval = new ContentValues();
		cval.put("BILL_DUE_DATE", bd.getBill_Duedate());
		cval.put("BILL_DETAIL", bd.getBill_detl());
		cval.put("BILL_AMOUNT", bd.getBill_amount());
		db.update("BILL_MAST", cval, "BILL_ID="+bd.getBillid(), null);
	}
	public String name_suggestion(String str)
	{
		String str2=null;
		
		String str3 = "select DISTINCT trans_itemname from CLIENT_trans";
	    SQLiteDatabase db=this.getReadableDatabase();
	    Cursor localCursor =db.rawQuery(str3, null);
	    if ((localCursor != null) && (localCursor.moveToFirst())) {
	      do
	      {
	    	  
	    	  str2=str2+"/"+localCursor.getString(localCursor.getColumnIndex("trans_itemname"));
	        																																																																								
	      } while (localCursor.moveToNext());
	    }
	   
		
		return str2;
	}
}
 