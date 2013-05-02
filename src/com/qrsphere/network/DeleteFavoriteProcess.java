package com.qrsphere.network;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.qrsphere.database.Qrcode;
import com.qrsphere.widget.MyLog;

public class DeleteFavoriteProcess extends AsyncNetworkingProcess{

	
	String API = "UpdateHistoryCategory";

	public DeleteFavoriteProcess(Context context, Handler handler) {
		super(handler, SuccessCode.DELETE_FAVORITE_SUCCESS, context,
				"Deleting from Favorite...");

	}

	@Override
	protected String postData(String qcRawdata) {
		String str = null;

		try {
			Qrcode qc = new Qrcode(qcRawdata);
			JSONObject json = new JSONObject();
			json.put("QrScanHistoryID", qc.getQrcodeJSONData()
					.getQrScanHistoryID());
			json.put("CategoryName", "");

			String data = json.toString();
			str = super.httpPost(API, data);

		} catch (Exception e) {

			MyLog.i(e.getMessage());
		}
		return str;
	}
}
