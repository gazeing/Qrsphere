package com.qrsphere.network;

import org.json.JSONException;
import org.json.JSONObject;

import com.qrsphere.database.Qrcode;
import com.qrsphere.userinfo.CollectPhoneInformation;
import com.qrsphere.widget.MyLog;
import com.qrsphere.widget.ScanDetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class SendDetailProcess extends NetworkingProcess{

	public SendDetailProcess(ProgressDialog pd, Handler handler) {
		super(pd, handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getResult(Context context,Qrcode qc) {
		// TODO Auto-generated method stub
		String str = null;
		try {
			JSONObject jsonRequest = new JSONObject();
			String url = qc.getQrcodeJSONData().getUrl();
			if (parseGUID(url).length()>0)
				jsonRequest.put("QrCodeGUID", parseGUID(url));
			jsonRequest.put("ScanContent", url);
			JSONObject json = new JSONObject();
			json.put("RequestContent", jsonRequest);
			json.put("Latitude", qc.getQrcodeJSONData().getLatitude());
			json.put("Longitude", qc.getQrcodeJSONData().getLongitude());
			CollectPhoneInformation cl = new CollectPhoneInformation(context);
			json.put("DeviceModel", cl.getDeviceName());
			json.put("DateTime", ScanDetail.TransferServerTimeFormat(
										qc.getQrcodeJSONData().getTimeStamp()));
			String data = json.toString();
			SendDataToServer sd = new SendDataToServer
								("http://192.168.15.119/api/AddHistory");
			str = sd.doPost(data, "application/json");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			MyLog.i(e.getMessage());
		}
		return str;
	}

	public ProgressDialog sentToServer(Context context,
			Qrcode qc) {
		// TODO Auto-generated method stub
		return super.sentToServer(context, qc, SuccessCode.DETAIL_SENT_SUCCESS, "Sending to server...");
	}
	
	public String parseGUID(String originUrl){
		String result = "";
		if (originUrl != null){
			if (originUrl.startsWith("http://www.qrorb.com?" +
					"QrCodeGUID="))
				result=originUrl.substring(originUrl.indexOf("GUID=")+5);
		}
		
		return result;
	}

}
