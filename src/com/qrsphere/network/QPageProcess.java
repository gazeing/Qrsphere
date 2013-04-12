package com.qrsphere.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.qrsphere.database.Qrcode;
import com.qrsphere.login.LoginActivity;
import com.qrsphere.widget.MyLog;

public class QPageProcess {

	ProgressDialog pd = null;
	Handler  handler = null;
	String strJSON = null;
	
	public String getStrJSON() {
		return strJSON;
	}

	public QPageProcess(ProgressDialog pd, Handler handler) {
		super();
		this.pd = pd;
		this.handler = handler;
	}

	public ProgressDialog sentToServer (Context context,Qrcode qc,
			final int succussCode,
			String processText){

			if (LoginActivity.isOnline(context)) {
				pd = ProgressDialog.show(context, "", processText, true,
				false);
				//pd.setProgress(888);
				MyLog.i("Dialog"," pd = ProgressDialog.show(context,");
				new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
					
						Thread.sleep(1500);
						//TODO add server communication code here
						strJSON = getResult();
					    handler.sendEmptyMessage(succussCode);
					    MyLog.i("Dialog","handler.sendEmptyMessage(succussCode);");
					} catch (Exception e) {
					    System.out.println("In Cache :");
					    handler.sendEmptyMessage(1);
					    MyLog.i("Dialog","handler.sendEmptyMessage(1);");
					}
				}
			}).start();
			// pd.dismiss();
			} else {
				
			}
			return pd;
	}
	String getResult(){
		
		JSONObject json = new JSONObject();
		try {		
			json.put("Brand", "Brand X");
			json.put("Product", "X product");
			json.put("Id", "12345678901234567890");
			
			json.put("Owner", "Lisa Smith");
			json.put("Tel", "0425555555");
			json.put("Email", "lis@xxxxx.com");
			json.put("Manufacturer", "Brand X");
//			json.put("Year", "2012");
//			json.put("Address", "32 Delhi Rd, North Ryde");

		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return json.toString();
	}
	
	public void startQPage(Context context){
    	if (this!=null){
    		Intent intent= new Intent("com.qrsphere.QPageActivity");
    		Bundle b= new Bundle();
    		b.putString("Result", this.getStrJSON());
    		intent.putExtras(b);
    		context.startActivity(intent);
    		MyLog.i("Dialog","startActivity(intent);");
		
    	}
	}
}
