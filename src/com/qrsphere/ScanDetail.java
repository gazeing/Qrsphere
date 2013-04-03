package com.qrsphere;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class ScanDetail extends LinearLayout {
	TextView tvTitle;
	TextView tvScanInfo;

	public ScanDetail(Context context, String text) {
		super(context);
		// TODO Auto-generated constructor stub
		//setContentView(R.layout.activity_main); 
		this.setOrientation(LinearLayout.VERTICAL);
		tvTitle = new TextView(context);
		tvScanInfo = new TextView(context);
		tvTitle.setText("Information: \n");
		tvTitle.setTextSize(24);
		tvScanInfo.setText(text);
		tvScanInfo.setTextSize(20);
		this.addView(tvTitle);
		this.addView(tvScanInfo);
	}
	public void SetScanDetail(String text)
	{
		tvScanInfo.setText(text);
	}

}
