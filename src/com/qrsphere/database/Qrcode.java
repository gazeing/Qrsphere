package com.qrsphere.database;

public class Qrcode {
	String rawdata;
	long timestamp;
	String hashcode;
	
	
	public Qrcode(String rawdata) {
		super();
		this.rawdata = rawdata;
		this.timestamp = System.currentTimeMillis();
		this.hashcode = "";
		
		this.hashcode = SimpleSHA1.sha1Hash(rawdata+timestamp);

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
	
	

}
