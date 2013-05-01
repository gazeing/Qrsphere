package com.qrsphere.network;

import org.json.JSONObject;

import com.qrsphere.database.Qrcode;
import com.qrsphere.widget.MyLog;
import android.content.Context;
import android.os.Handler;

//public class SendDetailProcess extends NetworkingProcess{
//
//	public SendDetailProcess(ProgressDialog pd, Handler handler) {
//		super(pd, handler);
//		
//	}
//
//	@Override
//	protected String getResult(Context context,Qrcode qc) {
//		
//		String str = null;
//		try {
//			JSONObject json = new JSONObject();
//			String url = qc.getQrcodeJSONData().getUrl();
//			if (parseGUID(url).length()>0)
//				json.put("QrCodeGUID", parseGUID(url));
//			json.put("ScanContent", url);
//			
//			//json.put("RequestContent", jsonRequest);
//			json.put("Latitude", qc.getQrcodeJSONData().getLatitude());
//			json.put("Longitude", qc.getQrcodeJSONData().getLongitude());
//			CollectPhoneInformation cl = new CollectPhoneInformation(context);
//			json.put("DeviceModel", cl.getDeviceName());
//			json.put("ScanDateTime", ScanDetail.TransferServerTimeFormat(
//													System.currentTimeMillis()));
//			String data = json.toString();
//			SendDataToServer sd = new SendDataToServer
//								("http://192.168.15.119/api/AddHistory");
//			str = super.post(sd,data, "application/json");
//			if (str != null){
//				json = new JSONObject(str);
//				if (json != null){
//					str = json.getInt("ResponseContent")+"";
//				}
//				
//			}
//			
//		} catch (JSONException e) {
//			
//			MyLog.i(e.getMessage());
//		}
//
//		return str;
//	}
//
//	public ProgressDialog sentToServer(Context context,
//			Qrcode qc) {
//		
//		return super.sentToServer(context, qc, SuccessCode.DETAIL_SENT_SUCCESS, "Sending to server...");
//	}
//	
//	public String parseGUID(String originUrl){
//		String result = "";
//		if (originUrl != null){
//			if (originUrl.startsWith("http://www.qrorb.com?" +
//					"QrCodeGUID="))
//				result=originUrl.substring(originUrl.indexOf("GUID=")+5);
//		}
//		
//		return result;
//	}
//
//}

public class SendDetailProcess extends AsyncNetworkingProcess{

	String API = "AddHistory";
	public SendDetailProcess(Context context, Handler handler) {
		super(handler, SuccessCode.DETAIL_SENT_SUCCESS, context, "Sending to server...");
		
	}

	@Override
	protected String postData(String qcRawdata) {
		String str = null;
		try {
			Qrcode qc = new Qrcode(qcRawdata);
			JSONObject json = new JSONObject(qcRawdata);
			String url = qc.getQrcodeJSONData().getUrl();
			if (parseGUID(url).length()>0)
				json.put("QrCodeGUID", parseGUID(url));

			String data = json.toString();
			str= super.httpPost(API, data);

			
		} catch (Exception e) {
			
			MyLog.i(e.getMessage());
		}


		return str;
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
