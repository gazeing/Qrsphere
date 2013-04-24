package com.qrsphere.login;

import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import com.qrsphere.network.SendDataToServer;
import com.qrsphere.network.SuccessCode;

public class LoginProcess {

	public  boolean Login(JSONObject loginfo, Handler hd){
		SendDataToServer sd = new SendDataToServer("http://192.168.15.119/api/qrcode");
		Log.i("LoginPostData",loginfo.toString());
		String test = "{\"QrCodeID\": 4291, \"IsDeleted\": true, \"QrCodeGUID\": \"5397d87e-80a8-4281-90fe-5aa0f6e3cb9b\"}";
		String res = sd.doPost(test, "application/json");
		

		if (hd != null)
			hd.sendEmptyMessage(SuccessCode.DETAIL_SENT_SUCCESS);
		return judgeLoginResponse(res);
		
	}
	
	private static boolean judgeLoginResponse(String response){
		
		if (response.length()>40)
			return true;
		
		
		return false;// TODO: will be set to "false" after test finish.
	}
}
