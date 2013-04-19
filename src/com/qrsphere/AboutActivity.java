package com.qrsphere;

import com.qrsphere.widget.MyLog;
import com.qrsphere.widget.StartBrowser;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class AboutActivity extends Activity {

	TextView tv1,tv2,tv3,tv4,tv5 =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.about);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		String app_ver ="0.9";
		try
		{
		    app_ver = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		}
		catch (Exception e)
		{
		    MyLog.i( e.getMessage());
		}
		tv1=(TextView) findViewById(R.id.textView1_about);
		if (tv1 != null)
			tv1.setText("Version: "+app_ver);

		tv2=(TextView) findViewById(R.id.textView2_about);
		if (tv2 != null){
			tv2.setText("Privacy Policy");
			
			tv2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
		    		StartBrowser sb = new StartBrowser("www.qrsphere.com",AboutActivity.this);
		    		sb.startBrowse();
				}
			});
		}
		
		tv3=(TextView) findViewById(R.id.textView3_about);
		if (tv3 != null)
			tv3.setText("Terms of Use");
		tv4=(TextView) findViewById(R.id.textView4_about);
		if (tv4 != null)
			tv4.setText("info@identibank.com");
		tv5=(TextView) findViewById(R.id.textView5_about);
		if (tv5 != null)
			tv5.setText("www.identibank.com");
		
	}

	
	
}
