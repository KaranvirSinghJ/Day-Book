package com.Daybook.daybook;

import com.Daybook.db.DBAdapter;
import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.ServerManagedPolicy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.widget.TextView;
import android.widget.Toast;

public class Lic extends Activity
{
	private TextView txt;
	AlertDialog.Builder alertDialog ;
		private class MyLicenseCheckerCallback implements LicenseCheckerCallback
		{	
				@Override
				public void allow(int reason) 
				{
					DBAdapter db=new DBAdapter(Lic.this);
					db.activatepr();
					db.close();
					startMainActivity();	
				}
				
				@Override
				public void dontAllow(int reason)
				{
					alertDialog.show();
				}
				@Override
				public void applicationError(int errorCode)
				{
					alertDialog.show();
				}
		}
		private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAplSPqGBkczDLgzO8PVu1zMzaSbzyo8QhuElbBgN5Zfc1wxwPvlLYGe5iyxnRWajBsA/6EAwWxyiu5ns4WMh46HqBx68bXwFP1ZFP0Qzl20E+3RVQ5GWF0YHHREyN3pgkMKpg49DMEpresjdGtuwUN//bdJUpwbCQsr/ITEATi/aXNruXfeV73ycZy/Mv8MnNTyP+BlLI7xqpQR5AgbYXFjISyM/V0xMkzl2Hum13TW6H4D/LeIEmMH8iVbydEsImUa9w2g4L9UEd3VdYB8nlkpP80NOqBruZ65b0lA3h9Q7ffFp+zfhyFNzNQSBsUB56C7mZOoW1VYJ7vtMyBvUBNQIDAQAB";
		private static final byte[] SALT = new byte[] {16,50,54,53,21,124,00,20,10,35,50,10,40,52,77,98,54,-23,36,55};
		private LicenseChecker mChecker;
		private LicenseCheckerCallback mLicenseCheckerCallback;
		private void doCheck() 
		{
		        mChecker.checkAccess(mLicenseCheckerCallback);
		}

		@Override
	    public void onCreate(Bundle savedInstanceState) 
		{
			alertDialog = new AlertDialog.Builder(Lic.this);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.lic_check);
			String deviceId = Secure.getString(getContentResolver(),Secure.ANDROID_ID);
			 txt=(TextView) findViewById(R.id.textView_lic);
			
			mLicenseCheckerCallback = new MyLicenseCheckerCallback();			
			mChecker = new LicenseChecker(this, new ServerManagedPolicy(this,new AESObfuscator(SALT, getPackageName(), deviceId)),BASE64_PUBLIC_KEY);
			doCheck();
			 alertDialog.setMessage("Do you to check license again?");
		        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog,int which) { 
		        
		        	doCheck();
		        }
		      });
		        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            dialog.cancel();
		            }
		        });
		}
	    

		@Override
		protected Dialog onCreateDialog(int id) {
		// We have only one dialog.
				return new AlertDialog.Builder(this)
		        .setTitle("Application Not Licensed")
		        .setCancelable(false)
		        .setMessage(
		                "This application is not licensed. Please purchase it from Android Market")
		        .setPositiveButton("Buy App",
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog,int which) {
		                        Intent marketIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://market.android.com/details?id="+ getPackageName()));
		                        startActivity(marketIntent);
		                	       finish();
		                    }
		                })
		        .setNegativeButton("Exit",
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                        finish();
		                    }
		                }).create();
    }
    @Override
    protected void onDestroy()
    {
		super.onDestroy();
		mChecker.onDestroy();
    }

    private void startMainActivity()
    {
		startActivity(new Intent(this, DayBookActivity.class)); 
		finish();
    }

    public void toast(String string){Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

}