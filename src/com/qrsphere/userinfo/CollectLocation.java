package com.qrsphere.userinfo;

import java.util.List;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

public class CollectLocation /*extends AsyncTask<Context, String,String >*/{
	
	Location location = null;
	Context context;
	LocationListener locationListener;
	
public CollectLocation(Context context,LocationListener locationListener) {
		super();
		this.context = context;
		this.locationListener = locationListener;

		sentGPSLocationRequest();
	}

	public Location getLocation() {  
        LocationManager locMan = (LocationManager) context  
                .getSystemService(Context.LOCATION_SERVICE);  
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //String provider = locMan.getBestProvider(criteria, true);
        location = locMan  
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);  
        if (location == null) {  
            location = locMan  
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);  
        }  
        return location;  
    }  
//	
//	public String getLocationString(Context context) {
//		return getLocation(context).toString();
//	}

	public LocationManager getLocationManager()  
	{  
	    return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
	}  
	public String getLocationInfoString(Location loc) {
		Geocoder gc = new Geocoder(context);
		//Location cur= getLocation(context);
		Location cur= loc;
		List<Address> adds = null;
		
			
		try {
			if (cur!=null)
				adds = gc.getFromLocation(cur.getLatitude(), cur.getLongitude(), 1);
			else	
				adds = gc.getFromLocation(-33.79477,151.142753, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("location",e.toString());
		}
		if (adds==null)
			return null;
		if (adds.size()>0)
			return adds.get(0).toString();
		else return null;
	}
	
	public void sentGPSLocationRequest(){
		
			getLocationManager() .requestLocationUpdates("gps", 0, 0, locationListener);
		

	}

//	@Override
//	protected String doInBackground(Context... params) {
//		 	String addres="";  
//		    LocationManager locationManager = this.getLocationManager(params[0]);  
//		    Criteria criteria = new Criteria();  
//		      
//		    criteria.setAccuracy(Criteria.ACCURACY_FINE);    
//		    criteria.setAltitudeRequired(false);    
//		    criteria.setBearingRequired(false);    
//		    criteria.setCostAllowed(false);    
//		    // using power_low mode    
//		    criteria.setPowerRequirement(Criteria.POWER_LOW);    
//		    String provider =locationManager.getBestProvider(criteria, true);  
//		    Log.i("provider>>>>>>", provider);  
//		    
//
//		    //locationListener  
//		    LocationListener locationListener = new LocationListener()  
//		    {  
//		  
//		        @Override  
//		        public void onLocationChanged(Location location)  
//		        {  
//		        	CollectLocation.this.location=location;  
//		        }  
//		  
//		        @Override  
//		        public void onProviderDisabled(String provider)  
//		        {  
//		              
//		        }  
//		  
//		        @Override  
//		        public void onProviderEnabled(String provider)  
//		        {  
//		              
//		        }  
//		  
//
//				@Override
//				public void onStatusChanged(String provider, int status,
//						Bundle extras) {
//					// TODO Auto-generated method stub
//					
//				}  
//		          
//		    };  
//		    
//		    while(location==null)  
//		    {  
//		    location =locationManager.getLastKnownLocation(provider);  
//		    locationManager.requestLocationUpdates(provider, 1000, 10, locationListener);  
//		    }  
//		    Geocoder geo = new Geocoder(params[0],Locale.getDefault());  
//		    try  
//		    {  
//		        List<Address> address=geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);  
//		        if(address.size()>0)  
//		        {  
//		            addres=address.get(0).getAddressLine(0);  
//		        }  
//		    } catch (IOException e)  
//		    {  
//		        // TODO Auto-generated catch block  
//		        e.printStackTrace();  
//		    }  
//		    return addres;  
//		
//	}




}
