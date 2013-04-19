package com.qrsphere.login;

import org.json.JSONObject;

import android.util.Log;

import com.qrsphere.network.SendDataToServer;

public class LoginProcess {

	public static boolean Login(JSONObject loginfo){
		SendDataToServer sd = new SendDataToServer("http://192.168.15.119/api/qrcode");
		Log.i("LoginPostData",loginfo.toString());
		String test = "{\"QrCodeID\": 4291, \"IsDeleted\": true, \"QrCodeGUID\": \"5397d87e-80a8-4281-90fe-5aa0f6e3cb9b\"}";
		String res = sd.doPost(test, "application/json");
		

		
		return judgeLoginResponse(res);
		
	}
	
	private static boolean judgeLoginResponse(String response){
		
		if (response.length()>40)
			return true;
		
		
		return true;// TODO: will be set to "false" after test finish.
	}
}
