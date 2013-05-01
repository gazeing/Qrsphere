package com.qrsphere.network;

import org.json.JSONObject;

import com.qrsphere.database.Qrcode;
import com.qrsphere.widget.MyLog;
import android.content.Context;
import android.os.Handler;

//public class AddToFavoriteProcess extends NetworkingProcess{
//
//	public AddToFavoriteProcess(ProgressDialog pd, Handler handler) {
//		super(pd, handler);
//		
//	}
//
//	@Override
//	protected String getResult(Context context,Qrcode qc) {
//		String str = null;
//		
//		try {
//			JSONObject json = new JSONObject();
//			json.put("QrScanHistoryID", qc.getQrcodeJSONData().getQrScanHistoryID());
//			json.put("CategoryName", qc.getQrcodeJSONData().getCatalogue());
////			String url = qc.getQrcodeJSONData().getUrl();
////			if (parseGUID(url).length()>0)
////				json.put("QrCodeGUID", parseGUID(url));
////			json.put("ScanContent", url);
////			
////			//json.put("RequestContent", jsonRequest);
////			json.put("Latitude", qc.getQrcodeJSONData().getLatitude());
////			json.put("Longitude", qc.getQrcodeJSONData().getLongitude());
////			CollectPhoneInformation cl = new CollectPhoneInformation(context);
////			json.put("DeviceModel", cl.getDeviceName());
////			json.put("ScanDateTime", ScanDetail.TransferServerTimeFormat(
////													System.currentTimeMillis()));
//			String data = json.toString();
//			SendDataToServer sd = new SendDataToServer
//								("http://192.168.15.119/api/AddHistoryFav");
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
//		
//		return super.sentToServer(context, qc, SuccessCode.ADD_TO_FAVORITE_SUCCESS,
//				"Sending to Favorite...");
//	}
//
//}

public class AddToFavoriteProcess extends AsyncNetworkingProcess{


	//final String processText="Sending to Favorite...";
	String API = "AddHistoryFav";
	

	public AddToFavoriteProcess(  Context context, Handler handler) {
		super( handler, SuccessCode.ADD_TO_FAVORITE_SUCCESS, context, "Sending to Favorite...");
		
	}



	@Override
	protected String postData(String qcRawdata) {
		String str = null;
		
		try {
			Qrcode qc = new Qrcode(qcRawdata);
			JSONObject json = new JSONObject();
			json.put("QrScanHistoryID", qc.getQrcodeJSONData().getQrScanHistoryID());
			json.put("CategoryName", qc.getQrcodeJSONData().getCatalogue());

			String data = json.toString();
			str= super.httpPost(API, data);

			
		} catch (Exception e) {
			
			MyLog.i(e.getMessage());
		}
		return str;
	}




	
	
}

