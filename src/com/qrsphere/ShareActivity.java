package com.qrsphere;

import com.qrsphere.R;
import com.qrsphere.database.QrcodeJSONData;
import com.qrsphere.scan.QRCodeEncoder;
import com.qrsphere.widget.ScanDetail;
import com.google.zxing.WriterException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareActivity extends Activity{

	TextView tv1,tv2,tv3 = null;
	Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.share);
		init();
		
	}

	private void init() {
		tv3= (TextView) findViewById(R.id.tvtitle);
		if(tv3 != null)
			tv3.setText("Share");
		
		tv1 = (TextView) findViewById(R.id.ItemTitle);
		tv2 = (TextView) findViewById(R.id.ItemText);
		String data = getIntent().getExtras().getString("rawdata");
		if (data != null){
			QrcodeJSONData q = new QrcodeJSONData(data);
			if (tv1 != null)
				tv1.setText(q.getUrl());
			if (tv2 != null)
				tv2.setText(ScanDetail.TransferTimeFormat(q.getTimeStamp()));
			
		}
		
		btn = (Button) findViewById(R.id.btn_submit);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		initEncoding();
		
	}

	@SuppressWarnings("deprecation")
	private void initEncoding() {
		QRCodeEncoder qrCodeEncoder;
		String USE_VCARD_KEY = "USE_VCARD";

		
	    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
	    Display display = manager.getDefaultDisplay();
	    int width = display.getWidth();
	    int height = display.getHeight();
	    int smallerDimension = width < height ? width : height;
	    smallerDimension = smallerDimension * 5 / 8;
	    
	    Intent intent = getIntent();
	    if (intent == null) {
	      return;
	    }


	    try {
	      boolean useVCard = intent.getBooleanExtra(USE_VCARD_KEY, false);
	      qrCodeEncoder = new QRCodeEncoder(this, intent, smallerDimension, useVCard);
	      Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
	      if (bitmap == null) {
	        Log.w("test", "Could not encode barcode");
	        //showErrorMessage(R.string.msg_encode_contents_failed);
	        qrCodeEncoder = null;
	        return;
	      }
	      ImageView view = (ImageView) findViewById(R.id.image_share);
	      view.setImageBitmap(bitmap);


	    } catch (WriterException e) {
	      Log.w("test", "Could not encode barcode", e);
	      //showErrorMessage(R.string.msg_encode_contents_failed);
	      qrCodeEncoder = null;
	    }
		
		
	}

	
}
