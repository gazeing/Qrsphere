package com.qrsphere.database;

import org.json.JSONException;
import org.json.JSONObject;

import com.qrsphere.widget.MyLog;

public class QrcodeJSONData {
	
	String rawData;
	
	boolean isFavorite = false;
	String url = "www.qrsphere.com";
	String catalogue = "";
	double longitude;
	double latitude;
	String dateTime;
	int qrScanHistoryID;
	
	
	public String getDateTime() {
		try {
			JSONObject json = new JSONObject(rawData);
			this.dateTime = json.getString("ScanDateTime");
		} catch (JSONException e) {
			MyLog.i(e.getMessage());
		}
		return dateTime;
	}


	public int getQrScanHistoryID() {
		try {
			JSONObject json = new JSONObject(rawData);
			this.qrScanHistoryID = json.getInt("QrScanHistoryID");
		} catch (JSONException e) {
			MyLog.i(e.getMessage());
		}
		return qrScanHistoryID;
	}


	public QrcodeJSONData(String rawData) {
		super();
		this.rawData = rawData;
		
	}


	public String getRawData() {
		
		return rawData;
	}


	public boolean isFavorite() {
		try {
			JSONObject json = new JSONObject(rawData);
			this.isFavorite = json.getBoolean("IsFav");
		} catch (JSONException e) {
			MyLog.i(e.getMessage());
		}
		return isFavorite;
	}


	public String getUrl() {
		try {
			JSONObject json = new JSONObject(rawData);
			this.url = json.getString("ScanContent");
		} catch (JSONException e) {
			MyLog.i(e.getMessage());
		}
		return url;
	}


	public String getCatalogue() {
		try {
			JSONObject json = new JSONObject(rawData);
			this.catalogue = json.getString("CategoryName");
		} catch (JSONException e) {
			MyLog.i(e.getMessage());
		}
		return catalogue;
	}


	public double getLongitude() {
		try {
			JSONObject json = new JSONObject(rawData);
			this.longitude = json.getDouble("Longitude");
		} catch (JSONException e) {
			MyLog.i(e.getMessage());
		}
		return longitude;
	}


	public double getLatitude() {
		try {
			JSONObject json = new JSONObject(rawData);
			this.latitude = json.getDouble("Latitude");
		} catch (JSONException e) {
			MyLog.i(e.getMessage());
		}
		return latitude;
	}
	
	
	
	

}
