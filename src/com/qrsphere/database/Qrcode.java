package com.qrsphere.database;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.qrsphere.userinfo.CollectLocation;

public class Qrcode {
	String rawdata;
	long timestamp;
	String hashcode;
	
	
	public Qrcode(String rawdata) {
		super();
		this.rawdata = rawdata;
		this.timestamp = System.currentTimeMillis();
		this.hashcode = "";
		
		this.hashcode = SimpleSHA1.sha1Hash(rawdata);

	}
	public Qrcode(String url, Context context){
		super();
		this.rawdata = generateRawdataByUrl(url, context);
		this.timestamp = System.currentTimeMillis();
		this.hashcode = "";
		
		this.hashcode = SimpleSHA1.sha1Hash(rawdata);
	}
	String generateRawdataByUrl(String url, Context context){
		LocationListener ll = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
		};
		CollectLocation cl = new CollectLocation(context,ll);

	
			JSONObject json = new JSONObject();
			try {		
				json.put("Catalogue", "");
				json.put("URL", url);
				json.put("IsFavorite", false);
				json.put("Latitude", cl.getLatitude());
				json.put("Longitude", cl.getLongitude());
				json.put("TimeStamp", System.currentTimeMillis());
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			
		return json.toString();
	}
	
	public Qrcode(String rawdata, long timestamp, String hashcode) {
		super();
		this.rawdata = rawdata;
		this.timestamp = timestamp;
		this.hashcode = hashcode;
	}

	public String getRawdata() {
		return rawdata;
	}
	public long getTimeStamp() {
		return timestamp;
	}
	public String getHashcode() {
		return hashcode;
	}
	
	public QrcodeJSONData getQrcodeJSONData(){
		QrcodeJSONData q = new QrcodeJSONData(rawdata);
		return q;
	}
	
	

}
