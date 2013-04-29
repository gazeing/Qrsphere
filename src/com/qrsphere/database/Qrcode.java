package com.qrsphere.database;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import com.qrsphere.widget.MyLog;
import com.qrsphere.widget.ScanDetail;

public class Qrcode {
	String rawdata;
	
	String hashcode;
	
	
	public Qrcode(String rawdata) {
		super();
		this.rawdata = rawdata;
		
		
		
		this.hashcode = SimpleSHA1.sha1Hash(rawdata);

	}
	public Qrcode(String url, Context context){
		super();
		this.rawdata = generateRawdataByUrl(url, context);
		
		
		
		this.hashcode = SimpleSHA1.sha1Hash(rawdata);
	}
	String generateRawdataByUrl(String url, Context context){


	
			JSONObject json = new JSONObject();
			try {		
				json.put("CategoryName", "");
				json.put("ScanContent", url);
				json.put("IsFav", false);
				ScanDetail.buildUserInfo(context);
				
			} catch (JSONException e) {
				
				MyLog.i(e.getMessage());
			}
			
		return json.toString();
	}
	
//	public Qrcode(String rawdata, String hashcode) {
//		super();
//		this.rawdata = rawdata;
//		
//		this.hashcode = hashcode;
//	}
	
	public Qrcode addCatalogue(String cata){
		try {
			JSONObject json = new JSONObject(rawdata);
			String url = json.getString("ScanContent");
			String catalogue = json.getString("CategoryName");
			boolean isFavorite = json.getBoolean("IsFav");
			double latitude = json.getDouble("Latitude");
			double longitude = json.getDouble("Longitude");
			String dateTime = json.getString("ScanDateTime");
			catalogue = cata;
			
			JSONObject jsonnew = new JSONObject();
			
			jsonnew.put("CategoryName", catalogue);
			jsonnew.put("ScanContent", url);
			jsonnew.put("IsFav", isFavorite);
			jsonnew.put("Latitude", latitude);
			jsonnew.put("Longitude", longitude);
			jsonnew.put("ScanDateTime", dateTime);
			
			this.rawdata = jsonnew.toString();
			
		} catch (JSONException e) {
			
			MyLog.i(e.getMessage());
		}
		
		return this;
	}

	public String getRawdata() {
		return rawdata;
	}

	public String getHashcode() {
		return hashcode;
	}
	
	public QrcodeJSONData getQrcodeJSONData(){
		QrcodeJSONData q = new QrcodeJSONData(rawdata);
		return q;
	}
	
	

}
