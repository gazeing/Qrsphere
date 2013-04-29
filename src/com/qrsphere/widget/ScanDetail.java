package com.qrsphere.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;


import com.qrsphere.R;
import com.qrsphere.database.Qrcode;
import com.qrsphere.userinfo.CollectLocation;
import com.qrsphere.userinfo.CollectPhoneInformation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class ScanDetail{
    public  static void ScanDetailDialog(Qrcode qc, Context context) {
    	AlertDialog.Builder ad = new AlertDialog.Builder(context);
		  ad.setIcon(R.drawable.ic_launcher);
		  ad.setTitle("Scan Detail");
		  LayoutInflater linf = LayoutInflater.from(context);
		  final View inflator = linf.inflate(R.layout.scan_detail, null);
		  ad.setView(inflator);

		  ad.setPositiveButton("OK", 
		    new android.content.DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog, int arg1) {
		      // OK, go back to Main menu
		    	// sendDataToFavorList();
		    	 
		     }


		    }
		   );

		   ad.setOnCancelListener(new DialogInterface.OnCancelListener(){
		    public void onCancel(DialogInterface dialog) {
		     // OK, go back to Main menu   
		    }}
		   );
		   
		   //fill the content to scan detail page
			  TextView tv =(TextView)(inflator.findViewById(R.id.ScanDetailUrl));
			  if (tv!=null)
				tv.setText("URL: ");
			  TextView tv1 =(TextView)(inflator.findViewById(R.id.ScanDetailUrlText));
			  if (tv1!=null)
				tv1.setText(qc.getQrcodeJSONData().getUrl());
			  TextView tv2 =(TextView)(inflator.findViewById(R.id.ScanDetailTime));
			  if (tv2!=null)
				tv2.setText("Scan Time: ");
			  TextView tv3 =(TextView)(inflator.findViewById(R.id.ScanDetailTimeText));
			  if (tv3!=null)
				tv3.setText(TransferTimeFormat(qc.getTimeStamp()));
			  TextView tv4 =(TextView)(inflator.findViewById(R.id.ScanDetailLocation));
			  if (tv4!=null)
				tv4.setText("Location: ");
			  TextView tv5 =(TextView)(inflator.findViewById(R.id.ScanDetailLocationText));
			  if (tv5!=null)
				tv5.setText(qc.getQrcodeJSONData().getLatitude()+", "+qc.getQrcodeJSONData().getLongitude());


		  ad.show();
		
	}
    public  static void ScanDetailDialog(Qrcode qc, Context context,
    		DialogInterface.OnClickListener click,DialogInterface.OnCancelListener cancel) {
    	AlertDialog.Builder ad = new AlertDialog.Builder(context);
		  ad.setIcon(R.drawable.ic_launcher);
		  ad.setTitle("Scan Detail");
		  LayoutInflater linf = LayoutInflater.from(context);
		  final View inflator = linf.inflate(R.layout.scan_detail, null);
		  ad.setView(inflator);

		  ad.setPositiveButton("OK", click );

		   ad.setOnCancelListener(cancel);
		   
		   //fill the content to scan detail page
			  TextView tv =(TextView)(inflator.findViewById(R.id.ScanDetailUrl));
			  if (tv!=null)
				tv.setText("URL: ");
			  TextView tv1 =(TextView)(inflator.findViewById(R.id.ScanDetailUrlText));
			  if (tv1!=null)
				tv1.setText(qc.getQrcodeJSONData().getUrl());
			  TextView tv2 =(TextView)(inflator.findViewById(R.id.ScanDetailTime));
			  if (tv2!=null)
				tv2.setText("Scan Time: ");
			  TextView tv3 =(TextView)(inflator.findViewById(R.id.ScanDetailTimeText));
			  if (tv3!=null)
				tv3.setText(TransferTimeFormat(qc.getTimeStamp()));
			  TextView tv4 =(TextView)(inflator.findViewById(R.id.ScanDetailLocation));
			  if (tv4!=null)
				tv4.setText("Location: ");
			  TextView tv5 =(TextView)(inflator.findViewById(R.id.ScanDetailLocationText));
			  if (tv5!=null)
				tv5.setText(qc.getQrcodeJSONData().getLatitude()+", "+qc.getQrcodeJSONData().getLongitude());


		  ad.show();
		
	}


	@SuppressLint("SimpleDateFormat")
	public static String TransferTimeFormat(long time){
    	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm aa");
    	return sdf.format(new Date(time));
    }
	@SuppressLint("SimpleDateFormat")
	public static String TransferServerTimeFormat(long time){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'000'Z");
    	String date = sdf.format(new Date(time));
    	date = date.substring(0, date.length()-2) + ":" + date.substring(date.length()-2); 
    	return date;

    }
	
	  public static JSONObject buildUserInfo(Context context){
	    	
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
				json.put("Latitude", cl.getLatitude());
				json.put("Longitude", cl.getLongitude());
				json.put("DeviceModel", new CollectPhoneInformation(context).getDeviceName());
				json.put("DateTime", ScanDetail.TransferTimeFormat(System.currentTimeMillis()));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		  
		  
		  return json;
	  }

}
