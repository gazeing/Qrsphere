package com.qrsphere.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.qrsphere.database.Qrcode;
import com.qrsphere.widget.MyLog;

public class DeleteItemProcess extends NetworkingProcess {

	public DeleteItemProcess(ProgressDialog pd, Handler handler) {
		super(pd, handler);
		
	}

	@Override
	protected String getResult(Context context, Qrcode qc) {
		String str = null;
		
		try {
			JSONObject json = new JSONObject(qc.getRawdata());
			//json.put("QrScanHistoryID", qc.getQrcodeJSONData().getQrScanHistoryID());
			String data = json.toString();
			SendDataToServer sd = new SendDataToServer
								("http://192.168.15.119/api/DeleteHistory");
			str = super.post(sd,data, "application/json");

			
		} catch (JSONException e) {
			
			MyLog.i(e.getMessage());
		}
		return str;
	}

	@Override
	public ProgressDialog sentToServer(Context context, Qrcode qc) {
		return super.sentToServer(context, qc, SuccessCode.DELETE_SUCCESS,
		"Deleting...");
	}

}
