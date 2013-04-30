package com.qrsphere.network;

import com.qrsphere.widget.MyLog;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class HttpAsyncTask extends AsyncTask<String, Void, String> {

	//String strUrl;
	HttpOperation ho;
	Handler  handler = null;
	int succussCode;
	@Override
    protected void onPreExecute() {
        Log.i("AsyncTask", "onPreExecute");
    }
	@Override
	protected String doInBackground(String... arg0) {
		init();
		
		return PostData(arg0);
		
		
	}
	private String PostData(String... arg0)  {
		// TODO Auto-generated method stub
		String tmp = "";
		
		try {
			if (arg0.length==2)
				tmp = ho.doPost(arg0[0],arg0[1]);
			else if ((arg0.length==3)&&true)
					if (arg0[2]=="login")
						tmp = ho.loginPost(arg0[0], arg0[1], arg0[2]);
			else
				tmp= ho.HttpClientPostMethod();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MyLog.i(e.getMessage());
		}
		return tmp;
		//return ho.HttpClientGetMethod();
	}
	private void init() {
		// TODO Auto-generated method stub
		if (ho == null)
			ho = new HttpOperation("http://49.156.19.75");
		
	}

	public HttpAsyncTask(HttpOperation ho, Handler handler, int succussCode) {
		super();
		this.ho = ho;
		this.handler = handler;
		this.succussCode = succussCode;
	}
	protected void onPostExecute(String res) {
        if (isCancelled()) {
        	res = null;
        }
        if (handler != null)
        	handler.sendEmptyMessage(succussCode);
        Log.i("AsyncTask", "onPostExecute");
	}

}
