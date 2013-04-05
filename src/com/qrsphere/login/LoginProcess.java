package com.qrsphere.login;

import org.json.JSONObject;

import android.util.Log;

import com.qrsphere.network.SendDataToServer;

public class LoginProcess {

	public static boolean Login(JSONObject loginfo){
		SendDataToServer sd = new SendDataToServer("http://192.168.15.141/RestTest.svc");
		Log.i("LoginPostData",loginfo.toString());
		String res = sd.doPost(loginfo.toString(), "/json/100"/*"application/json"*/);
		

		
		return judgeLoginResponse(res);
		
	}
	
	private static boolean judgeLoginResponse(String response){
		
		if (response.length()>40)
			return true;
		
		return false;
	}
}
