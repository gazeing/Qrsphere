package com.qrsphere;

import com.qrsphere.database.QrcodeJSONData;
import com.qrsphere.widget.ScanDetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FeedbackActivity extends Activity {

	TextView tv1,tv2,tv3 = null;
	Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.feedback);
		init();

	}
	@SuppressLint("NewApi")
	private void init() {
		// TODO Auto-generated method stub
		tv3= (TextView) findViewById(R.id.tvtitle);
		if(tv3 != null){
			Drawable back = getResources(). getDrawable(R.drawable.banner_feedback);
			tv3.setBackground(back);
			}
		
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
		
		Button btn_back = (Button) findViewById(R.id.btn_title_left);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FeedbackActivity.this.finish();
			}
		});
		
		btn = (Button) findViewById(R.id.btn_submit);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	

}
