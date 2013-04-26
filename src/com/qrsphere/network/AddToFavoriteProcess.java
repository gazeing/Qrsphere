package com.qrsphere.network;

import com.qrsphere.database.Qrcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class AddToFavoriteProcess extends NetworkingProcess{

	public AddToFavoriteProcess(ProgressDialog pd, Handler handler) {
		super(pd, handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getResult(Context context,Qrcode qc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProgressDialog sentToServer(Context context, Qrcode qc) {
		// TODO Auto-generated method stub
		return super.sentToServer(context, qc, SuccessCode.ADD_TO_FAVORITE_SUCCESS,
				"Sending to Favorite...");
	}

}
