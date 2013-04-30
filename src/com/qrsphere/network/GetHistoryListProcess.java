package com.qrsphere.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.qrsphere.database.Qrcode;
import com.qrsphere.widget.MyLog;

public class GetHistoryListProcess extends NetworkingProcess {
	
	

	public GetHistoryListProcess(ProgressDialog pd, Handler handler) {
		super(pd, handler);
		
	}

	@Override
	protected String getResult(Context context, Qrcode qc) {
		String str = null;
		
		JSONObject json = new JSONObject();
		try {
			json.put("test", "test");
		} catch (JSONException e) {
			MyLog.i(e.getMessage());
		}
		String data = json.toString();
		SendDataToServer sd = new SendDataToServer
		("http://192.168.15.119/api/GetHistoryList");
		str = sd.doPost(data, "application/json");
		
		return str;
	}



	@Override
	public ProgressDialog sentToServer(Context context, Qrcode qc) {
		
		return super.sentToServer(context, qc, SuccessCode.GET_LIST_SUCCESS, "Retrieving data...");
	}

}
