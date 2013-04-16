package com.qrsphere.widget;

import com.qrsphere.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AddToFavorite {

	public ComboBox show(Context context,String qrData,DialogInterface.OnClickListener click,
			DialogInterface.OnCancelListener cancel){
    	ArrayAdapter<String> adp = getSuggestion(context);

    	return InstructionsDialog(context,"You have chose "+ qrData +". " +
    			"\nPlease give a description of it. Or you can chose " +
    			"from the past list. ","Please input category",adp,
    			click,cancel);
       	
	}

	
    public ArrayAdapter<String> getSuggestion(Context context){
    	ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line);
    	int i = 4;
    	while(i-->0){

    		adp.add("Suggest"+i);
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
