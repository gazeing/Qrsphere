package com.qrsphere.network;

import com.qrsphere.database.Qrcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class SendDetailProcess extends NetworkingProcess{

	public SendDetailProcess(ProgressDialog pd, Handler handler) {
		super(pd, handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	public ProgressDialog sentToServer(Context context,
			Qrcode qc) {
		// TODO Auto-generated method stub
		return super.sentToServer(context, qc, SuccessCode.DETAIL_SENT_SUCCESS, "Sending to server...");
	}

}
