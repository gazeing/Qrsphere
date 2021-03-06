package com.qrsphere;

import com.qrsphere.widget.MyLog;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
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
		TextView tv = (TextView) findViewById(R.id.tvtitle);
		//Drawable back = getResources(). getDrawable(R.drawable.banner_about);
		tv.setBackgroundResource(R.drawable.banner_about);
		
		Button btn_back = (Button) findViewById(R.id.btn_title_left);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AboutActivity.this.finish();
			}
		});
		
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
			//tv2.setText("Privacy Policy");
			String htmlLinkText = "<a href=\"http://www.qrsphere.com\"><u>Privacy Policy</u></a>";  
			tv2.setText(Html.fromHtml(htmlLinkText));  
			tv2.setMovementMethod(LinkMovementMethod.getInstance());
			
//			tv2.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//		    		StartBrowser sb = new StartBrowser("www.qrsphere.com",AboutActivity.this);
//		    		sb.startBrowse();
//				}
//			});
		}
		
		tv3=(TextView) findViewById(R.id.textView3_about);
		if (tv3 != null){
			//tv3.setText("Terms of Use");
			String htmlLinkText = "<a href=\"http://www.qrsphere.com\"><u>Terms of Use</u></a>";  
			tv3.setText(Html.fromHtml(htmlLinkText));  
			tv3.setMovementMethod(LinkMovementMethod.getInstance());
		
		}
		tv4=(TextView) findViewById(R.id.textView4_about);
		if (tv4 != null){
			//tv4.setText("info@identibank.com");
			String htmlLinkText = "<a href=\"mailto:info@identibank.com\"><u>info@identibank.com</u></a>";  
			tv4.setText(Html.fromHtml(htmlLinkText));  
			tv4.setMovementMethod(LinkMovementMethod.getInstance());
		}
		tv5=(TextView) findViewById(R.id.textView5_about);
		if (tv5 != null){
			//tv5.setText("www.identibank.com");
			String htmlLinkText = "<a href=\"http://www.qrsphere.com\"><u>www.identibank.com</u></a>";  
			tv5.setText(Html.fromHtml(htmlLinkText));  
			tv5.setMovementMethod(LinkMovementMethod.getInstance());
			
		}
		
	}

	
	
}
