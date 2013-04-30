package com.qrsphere.network;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.qrsphere.login.LoginAuth;
import com.qrsphere.widget.MyLog;

import android.os.Handler;
import android.util.Log;

public class SendDataToServer {
	
	String url;
	
	
	public SendDataToServer(String url) {
		super();
		this.url = url;
	}


	public String doPost(Map<String,String> params,Handler handler, int succussCode){
		HttpOperation ho = new HttpOperation(url);
		 if (params != null) {  
	            Iterator<Entry<String, String>> iter = params.entrySet().iterator();  
	            while(iter.hasNext()) {  
	                Entry<String, String> entry = (Entry<String, String>)iter.next();  
	                //data.add(new BasicNameValuePair(entry.getKey(), entry.getValue())); 
	                ho.AddValueToParams(entry.getKey(), entry.getValue());
	            }  
		 }
		
		HttpAsyncTask hat = new HttpAsyncTask(ho, handler, succussCode);
//		try {
			//String res = ho.HttpClientPostMethod("q", "helloworld");
		String res ="";
		try {
			res = hat.execute("").get();
			//res = ho.HttpClientPostMethod();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			MyLog.i("HttpAsyncTask",res);
		} 
			if (res==""||res==null)
				res = "No result!";
			MyLog.i("HTTPresponce",res);
		
		
		return res;
		
	}
	
    public String doPost( String data, String contentType,Handler handler, int succussCode)  {
    	String res="";
    	HttpOperation ho = new HttpOperation(url);
    	


		try {
			
			HttpAsyncTask hat = new HttpAsyncTask(ho, handler, succussCode);
			res = hat.execute(data,contentType).get();
			//res = ho.HttpClientPostMethod();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MyLog.i("HttpAsyncTask",res);
		} 
			if (res==""||res==null)
				res = "No result!";
			Log.i("HTTPresponce",res);
    	
    	return res;
    }
    public String login(String username,String password,String rawdata,Handler handler, int succussCode){
    	String res="";
    	HttpOperation ho = new HttpOperation(url);
    	


		try {
			String auth = LoginAuth.setAuth(username, password);
			HttpAsyncTask hat = new HttpAsyncTask(ho, handler, succussCode);
			res = hat.execute(rawdata,auth,"login").get();
			//res = ho.HttpClientPostMethod();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MyLog.i("HttpAsyncTask",res);
		} 
			if (res==""||res==null)
				res = "err";
			Log.i("HTTPresponce",res);
    	
    	return res;
    }
	  
 
}

