package com.qrsphere.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import com.qrsphere.network.SendDataToServer;
import com.qrsphere.network.SuccessCode;

public class LoginProcess {

	public  boolean Login(JSONObject loginfo,JSONObject userinfo, Handler hd){
		SendDataToServer sd = new SendDataToServer("http://192.168.15.119/api/login");
		Log.i("LoginPostData",loginfo.toString());
		//String test = "{\"Latitude\": 1.1,\"Longitude\": 2.1,\"DeviceModel\": \"sample string 3\",\"DateTime\": \"2013-04-24T14:41:21.8987559+10:00}";
		String acc = null;
		String passwd = null;
		try {
			acc = loginfo.getString("Account");
			passwd = loginfo.getString("Password");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String res = sd.login("sonal@qrsphere.com", "password", userinfo.toString());
		String res = "";
		if ((acc!=null)&&(passwd!=null))
			res = sd.login(acc, passwd, userinfo.toString());
		

		if (hd != null)
			hd.sendEmptyMessage(SuccessCode.DETAIL_SENT_SUCCESS);
		return judgeLoginResponse(res);
		
	}
	
	private static boolean judgeLoginResponse(String response){
		
		if (response.length()>10)
			return true;
		
		
		return false;// TODO: will be set to "false" after test finish.
	}
	

}
