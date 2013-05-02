package com.qrsphere.login;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.qrsphere.network.AsyncNetworkingProcess;
import com.qrsphere.network.HttpOperation;
import com.qrsphere.network.SuccessCode;
import com.qrsphere.widget.MyLog;

public class LoginAsyncProcess extends AsyncNetworkingProcess {

	String API = "Login";
	public LoginAsyncProcess(Context context,Handler handler) {
		super(handler, SuccessCode.LOGIN_SUCCESS, context, "Login to server...");
		
	}

	@Override
	protected String postData(String qcRawdata) {
		String str = null;
		
		try {
			//Qrcode qc = new Qrcode(qcRawdata);
			JSONObject json = new JSONObject(qcRawdata);
			String username = json.getString("Account");
			String password = json.getString("Password");

			String data = qcRawdata;
			String auth = LoginAuth.setAuth(username, password);
			HttpOperation ho = new HttpOperation(PREFIX+API);
			str= ho.loginPost(data, auth,CONTENTTYPE);

			
		} catch (Exception e) {
			
			MyLog.i(e.getMessage());
		}
		return str;
	}

}
