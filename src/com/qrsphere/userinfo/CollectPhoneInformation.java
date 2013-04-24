package com.qrsphere.userinfo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

public class CollectPhoneInformation {

	Context mAppContext;
	public CollectPhoneInformation(Context mAppContext) {
		super();
		this.mAppContext = mAppContext;
	}

	public String getPhoneNum(){

//		 TelephonyManager tMgr =(TelephonyManager)mAppContext.getSystemService(Context.TELEPHONY_SERVICE);
		 TelephonyManager tMgr=(TelephonyManager)mAppContext.getSystemService(Context.TELEPHONY_SERVICE); 
		String mPhoneNumber = tMgr.getLine1Number();
		if (mPhoneNumber.length() ==0)
//			return "No available numner.";
			return tMgr.getDeviceId(); 
		else 
			return mPhoneNumber;
	}

	public String getMacAddress(){
		WifiManager wifiMan = (WifiManager) mAppContext.getSystemService(
                Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		String macAddr = wifiInf.getMacAddress();
		return macAddr;
	}
	
	public String getDeviceName() {
		  String manufacturer = Build.MANUFACTURER;
		  String model = Build.MODEL;
		  if (model.startsWith(manufacturer)) {
		    return capitalize(model);
		  } else {
		    return capitalize(manufacturer) + " " + model;
		  }
		}


		private String capitalize(String s) {
		  if (s == null || s.length() == 0) {
		    return "";
		  }
		  char first = s.charAt(0);
		  if (Character.isUpperCase(first)) {
		    return s;
		  } else {
		    return Character.toUpperCase(first) + s.substring(1);
		  }
		} 
}
