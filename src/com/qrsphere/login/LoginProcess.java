package com.qrsphere.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import com.qrsphere.network.AsyncNetworkingProcess;

public class LoginProcess {

//	public  boolean Login(JSONObject loginfo,JSONObject userinfo, Handler hd){
//		SendDataToServer sd = new SendDataToServer("http://192.168.15.119/api/login");
//		Log.i("LoginPostData",loginfo.toString());
//		
//		String acc = null;
//		String passwd = null;
//		try {
//			acc = loginfo.getString("Account");
//			passwd = loginfo.getString("Password");
//		} catch (JSONException e) {
//			
//			MyLog.i(e.getMessage());
//		}
//		
//		String res = "";
//		if ((acc!=null)&&(passwd!=null))
//			res = sd.login(acc, passwd, userinfo.toString(),hd,SuccessCode.DETAIL_SENT_SUCCESS);
//		
//
////		if (hd != null)
////			hd.sendEmptyMessage(SuccessCode.DETAIL_SENT_SUCCESS);
//		return judgeLoginResponse(res);
//		
//	}
	AsyncNetworkingProcess atp;
	Context context;
	ProgressDialog pd;
	Handler handler;
	
	public String getResponse(){
		if (atp != null)
			return atp.getStrJSON();
		else
			return "";
	}
	
	public LoginProcess(Context context, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
	}


	public ProgressDialog Login(String jsonStr,boolean isShowPd){
		atp = new LoginAsyncProcess( context,handler);
		atp.setShowPd(isShowPd);
		atp.execute(jsonStr);
		return pd = atp.getPd();
	}
	
	public boolean judgeLoginResponse(){
		
		if (atp.getStrJSON().length()>10)
			return true;
		
		
		return false;// TODO: will be set to "false" after test finish.
	}
	

}
