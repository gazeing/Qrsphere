package com.qrsphere.widget;

import com.qrsphere.R;
import com.qrsphere.database.Qrcode;
import com.qrsphere.database.QrcodeList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AddToFavorite {

	public ComboBox show(Context context,String qrData,DialogInterface.OnClickListener click,
			DialogInterface.OnCancelListener cancel,QrcodeList list){
    	ArrayAdapter<String> adp = getSuggestion(context,list);

    	return InstructionsDialog(context,"You have chose "+ qrData +". " +
    			"\nPlease give a description of it. Or you can chose " +
    			"from the past list. ","Please input category",adp,
    			click,cancel);
       	
	}

	
    public ArrayAdapter<String> getSuggestion(Context context,QrcodeList list){
    	ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line);
    	adp.add("My Best Codes");
    	//add category to list, if it is not existed
    	for (Qrcode q:list.getFavoriteList()){
    		if (q.getQrcodeJSONData().getCatalogue() != null)
    			if (adp.getPosition(q.getQrcodeJSONData().getCatalogue())==-1) 
    				adp.add(q.getQrcodeJSONData().getCatalogue());  
    	}
    	return adp;
    }
	protected ComboBox InstructionsDialog(Context context,String text,String title,
			ArrayAdapter<String> list, DialogInterface.OnClickListener click,
			DialogInterface.OnCancelListener cancel){

		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setIcon(R.drawable.ic_launcher);
		ad.setTitle(title);
		LayoutInflater linf = LayoutInflater.from(context);
		final View inflator = linf.inflate(R.layout.dialog, null);
		ad.setView(inflator);
		
		ad.setPositiveButton("OK", click);
		
		ad.setOnCancelListener(cancel);
		
		TextView tv =(TextView)(inflator.findViewById(R.id.TextView01));
		if (tv!=null)
		tv.setText(text);
		ComboBox cb = (ComboBox) (inflator.findViewById(R.id.Combo01));
		if (cb!= null)
		  cb.setSuggestionSource(list);
		
		ad.show();
		
		return cb;
	}
}
