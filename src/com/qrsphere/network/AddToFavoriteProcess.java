package com.qrsphere.network;

import org.json.JSONObject;

import com.qrsphere.database.Qrcode;
import com.qrsphere.widget.MyLog;
import android.content.Context;
import android.os.Handler;

public class AddToFavoriteProcess extends AsyncNetworkingProcess {

	String API = "UpdateHistoryCategory";

	public AddToFavoriteProcess(Context context, Handler handler) {
		super(handler, SuccessCode.ADD_TO_FAVORITE_SUCCESS, context,
				"Sending to Favorite...");

	}

	@Override
	protected String postData(String qcRawdata) {
		String str = null;

		try {
			Qrcode qc = new Qrcode(qcRawdata);
			JSONObject json = new JSONObject();
			json.put("QrScanHistoryID", qc.getQrcodeJSONData()
					.getQrScanHistoryID());
			json.put("CategoryName", qc.getQrcodeJSONData().getCatalogue());

			String data = json.toString();
			str = super.httpPost(API, data);

		} catch (Exception e) {

			MyLog.i(e.getMessage());
		}
		return str;
	}

}
