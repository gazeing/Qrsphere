package com.qrsphere.database;

import org.json.JSONException;
import org.json.JSONObject;

public class QrcodeJSONData {
	
	String rawData;
	
	boolean isFavorite = false;
	String url = "www.qrsphere.com";
	String catalogue = "";
	
	
	public QrcodeJSONData(String rawData) {
		super();
		this.rawData = rawData;
		
		try {
			JSONObject json = new JSONObject(rawData);
			this.url = json.getString("URL");
			this.catalogue = json.getString("Catalogue");
			this.isFavorite = json.getBoolean("IsFavorite");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public String getRawData() {
		return rawData;
	}


	public boolean isFavorite() {
		return isFavorite;
	}


	public String getUrl() {
		return url;
	}


	public String getCatalogue() {
		return catalogue;
	}
	
	
	
	

}
