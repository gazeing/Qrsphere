package com.qrsphere.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.qrsphere.database.Qrcode;

public class SendDetail {
	AsyncNetworkingProcess atp;
	Context context;
	ProgressDialog pd;
	Handler handler;
	
	
	public SendDetail(Context context, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
	}


	public ProgressDialog postData(Qrcode q){
		atp = new SendDetailProcess( context,handler);
		atp.execute(q.getRawdata());
		return pd = atp.getPd();
	}


	public String getStrJSON() {
		
		return atp.getStrJSON();
	}
}
