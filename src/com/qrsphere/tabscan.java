package com.qrsphere;

import java.util.ArrayList;
import java.util.List;

import com.qrsphere.database.Qrcode;
import com.qrsphere.database.QrcodeDataOperator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class tabscan extends Activity {

	private List<String> data = new ArrayList<String>();
	protected List<String> getData() {
		return data;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	@Override
	protected void onResume() {
		
		super.onResume();

		scanProcess();
	}
	@SuppressWarnings("deprecation")
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode ==0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
;
				
				QrcodeDataOperator qdo = ((MainActivity) this.getParent()).getQdo();
				qdo.insert(new Qrcode(contents));
				
			} else if (resultCode == RESULT_CANCELED) {
			// Handle cancel
				System.out.println(" scan error!");
		}
		((MainActivity) this.getParent()).getTabHost().setCurrentTabByTag("History");
	}
	}
	
	protected void scanProcess(){
		Intent intent =new Intent("com.google.zxing.client.android.SCAN");//start activity of barsanner to scan it 
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");//input param
		startActivityForResult(intent, 0);//start intent
	}
}

