package com.qrsphere.network;

import com.qrsphere.login.LoginActivity;
import com.qrsphere.widget.MyLog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

public abstract class AsyncNetworkingProcess extends AsyncTask<String, Void, String>{

	
	Handler  handler = null;
	int succussCode;
	Context context;
	ProgressDialog pd = null;
	String strJSON = null;
	String dialogText = "Loading...";
	//Qrcode qrcode=null;
	final protected String PREFIX = "http://192.168.15.110/api/";
	final protected String CONTENTTYPE ="application/json";
	boolean isPostSuccuess = false;
	
	boolean isShowPd = true;//some activities doesn't want to show progress dialog
							//will set it to "false", eg. SplashScreen
	
	
	

	
    public void setShowPd(boolean isShowPd) {
		this.isShowPd = isShowPd;
	}


	public AsyncNetworkingProcess( Handler handler,
			int succussCode, Context context,String dialogText) {
		super();
		
		this.handler = handler;
		this.succussCode = succussCode;
		this.context = context;
		
		this.dialogText = dialogText;
		
	}
    

	public ProgressDialog getPd() {
		return pd;
	}


	public String getStrJSON() {
		return strJSON;
	}


	protected void onPreExecute() {
		showProcessDialog();
        MyLog.i("AsyncTask", "onPreExecute");
    }

	@Override
	protected String doInBackground(String... params) {
		strJSON = postData(params[0]);

		return strJSON;
	}
	
	protected void onPostExecute(String res) {
        if (isCancelled()) {
        	res = null;
        }

		isPostSuccuess = (strJSON.length()>8)?true:false;

        if (handler != null){
        	int code = isPostSuccuess?succussCode:SuccessCode.ERROR;
        	handler.sendEmptyMessage(code);
        }
        MyLog.i("AsyncTask", "onPostExecute");
	}
	
	protected ProgressDialog showProcessDialog(){
		if (LoginActivity.isOnline(context)) {
			if (isShowPd)//some activities doesn't want to show progress dialog
				pd = ProgressDialog.show(context, "", dialogText, true,
				false);
			
			MyLog.i("Dialog"," pd = ProgressDialog.show(context,");
			return pd;
		}
		else
			return null;
	}

	protected String httpPost(String api,String data) throws Exception{
		HttpOperation ho = new HttpOperation(PREFIX+api);
		return ho.doPost(data, CONTENTTYPE);
	}

	abstract protected String postData(String qcRawdata);

}
