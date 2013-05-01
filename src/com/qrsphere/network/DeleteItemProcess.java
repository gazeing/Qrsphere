package com.qrsphere.network;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.qrsphere.widget.MyLog;

//public class DeleteItemProcess extends NetworkingProcess {
//
//	public DeleteItemProcess(ProgressDialog pd, Handler handler) {
//		super(pd, handler);
//		
//	}
//
//	@Override
//	protected String getResult(Context context, Qrcode qc) {
//		String str = null;
//		
//		try {
//			JSONObject json = new JSONObject(qc.getRawdata());
//			//json.put("QrScanHistoryID", qc.getQrcodeJSONData().getQrScanHistoryID());
//			String data = json.toString();
//			SendDataToServer sd = new SendDataToServer
//								("http://192.168.15.119/api/DeleteHistory");
//			str = super.post(sd,data, "application/json");
//
//			
//		} catch (JSONException e) {
//			
//			MyLog.i(e.getMessage());
//		}
//		return str;
//	}
//
//	@Override
//	public ProgressDialog sentToServer(Context context, Qrcode qc) {
//		return super.sentToServer(context, qc, SuccessCode.DELETE_SUCCESS,
//		"Deleting...");
//	}
//
//}

public class DeleteItemProcess extends AsyncNetworkingProcess{

	String API = "DeleteHistory";
	public DeleteItemProcess(Context context, Handler handler) {
		super(handler, SuccessCode.DELETE_SUCCESS, context,"Deleting...");
		
	}

	@Override
	protected String postData(String qcRawdata) {
		String str = null;
		try {
			JSONObject json = new JSONObject(qcRawdata);
			String data = json.toString();
			str= super.httpPost(API, data);
		} catch (Exception e) {
			MyLog.i(e.getMessage());
		}
		return str;
	}
}
