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
	int m_successCode;
	
	public String getStrJSON() {
		return strJSON;
	}

	public NetworkingProcess(ProgressDialog pd, Handler handler) {
		super();
		this.pd = pd;
		this.handler = handler;
	}


	public ProgressDialog sentToServer (final Context context, final Qrcode qc,
			int succussCode, String processText){
			m_successCode = succussCode;
			if (LoginActivity.isOnline(context)) {
				pd = ProgressDialog.show(context, "", processText, true,
				false);
				//pd.setProgress(888);
				MyLog.i("Dialog"," pd = ProgressDialog.show(context,");
				new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
					
						//Thread.sleep(1500);
						//TODO add server communication code here
						strJSON = getResult(context,qc);
					   // handler.sendEmptyMessage(m_successCode);
					    MyLog.i("Dialog","handler.sendEmptyMessage(succussCode);");
					} catch (Exception e) {
					  //  System.out.println("In Cache :");
					   
					    MyLog.i(e.getMessage());
					}
				}

			}).start();
			// pd.dismiss();
			} else {
				 handler.sendEmptyMessage(SuccessCode.ERROR);
			}
			return pd;
	}

	protected String post(SendDataToServer sd,String data, String contentType){
		return sd.doPost(data, contentType, handler, m_successCode);
	}

	//get response of server here
	abstract protected String getResult(Context context,Qrcode qc);
	//implement the sending logic here
	abstract public ProgressDialog sentToServer (Context context, Qrcode qc);
}
