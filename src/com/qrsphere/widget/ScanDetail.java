package com.qrsphere.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.qrsphere.R;
import com.qrsphere.database.Qrcode;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

	@SuppressLint("SimpleDateFormat")
	public static String TransferTimeFormat(long time){
    	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
    	return sdf.format(new Date(time));
    }

}
