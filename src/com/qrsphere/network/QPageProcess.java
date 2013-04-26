package com.qrsphere.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.qrsphere.database.Qrcode;
import com.qrsphere.widget.MyLog;

public class QPageProcess extends NetworkingProcess{



	public QPageProcess(ProgressDialog pd, Handler handler) {
		super(pd, handler);

	}

	public ProgressDialog sentToServer(Context context, Qrcode qc){
		return super.sentToServer(context, qc, SuccessCode.QPAGE_SUCCESS, "Generating Qpage...");
	}
	
	protected String getResult(Context context,Qrcode qc){
		
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
