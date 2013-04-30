package com.qrsphere.network;

import com.qrsphere.widget.MyLog;

import android.os.AsyncTask;
import android.os.Handler;

public class AsyncNetworkingProcess extends AsyncTask<String, Void, String>{

	HttpOperation ho;
	Handler  handler = null;
	int succussCode;
	
	
	public AsyncNetworkingProcess(HttpOperation ho, Handler handler,
			int succussCode) {
		super();
		this.ho = ho;
		this.handler = handler;
		this.succussCode = succussCode;
	}

	@Override
	protected String doInBackground(String... params) {
		return PostData(params);
	}

	private String PostData(String... arg0)  {
		
		String tmp = "";
		if (ho == null)
			return null;
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
}
