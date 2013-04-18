package com.qrsphere.userinfo;

import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

public class CollectAddress {

	public static String getLocationInfoString(Context context,double latitude, double longitude) {
        context.getSystemService(Context.LOCATION_SERVICE);
		Geocoder gc = new Geocoder(context);
		//Location cur= getLocation(context);
		//Location cur= loc;
		List<Address> adds = null;
		
			
		try {
			if (latitude!=0&&longitude!=0)
				adds = gc.getFromLocation(latitude, longitude, 1);
			else	
				adds = gc.getFromLocation(-33.79477,151.142753, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("location",e.toString());
			return e.getMessage();
		}
		if (adds==null)
			return null;
		if (adds.size()>0)
			return adds.get(0).getLocality()+", "+adds.get(0).getCountryName()+", "+adds.get(0).getPostalCode();
		else return null;
	}
}
