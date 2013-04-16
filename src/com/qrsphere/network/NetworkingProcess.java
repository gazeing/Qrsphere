package com.qrsphere.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.qrsphere.database.Qrcode;
import com.qrsphere.login.LoginActivity;
import com.qrsphere.widget.MyLog;

public abstract class NetworkingProcess {

	ProgressDialog pd = null;
	Handler  handler = null;
	String strJSON = null;
	Qrcode qrcode=null;
	
	public String getStrJSON() {
		return strJSON;
	}

	public NetworkingProcess(ProgressDialog pd, Handler handler) {
		super();
		this.pd = pd;
		this.handler = handler;
	}


	public ProgressDialog sentToServer (Context context, Qrcode qc,
			final int succussCode, String processText){

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
					    handler.sendEmptyMessage(SuccessCode.ERROR);
					    MyLog.i("Dialog","handler.sendEmptyMessage(1);");
					}
				}

			}).start();
			// pd.dismiss();
			} else {
				
			}
			return pd;
	}

	//get response of server here
	abstract protected String getResult();
	//implement the sending logic here
	abstract public ProgressDialog sentToServer (Context context, Qrcode qc);
}
