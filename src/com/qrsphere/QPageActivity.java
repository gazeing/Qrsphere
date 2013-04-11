package com.qrsphere;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.qrsphere.widget.ScreenSolution;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QPageActivity extends Activity {
	
	// the margin between diffrent textviews: 12dp
	int nMarginTop = 0;

	Button btn_reminder, btn_about, btn_submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.qpage);
		nMarginTop =12 * (ScreenSolution.getDpi(this) / 160);
		setupButtons();
		updatePage(getQPageInfo());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	void setupButtons(){
		btn_reminder = (Button) findViewById(R.id.btn_reminder);
		btn_reminder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Toast.makeText(getBaseContext(), "You clicked: btn_reminder", Toast.LENGTH_SHORT).show();
			}
		});
		
		
		btn_about = (Button) findViewById(R.id.btn_about);
		btn_about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Toast.makeText(getBaseContext(), "You clicked: btn_about", Toast.LENGTH_SHORT).show();	
			}
		});
		
		
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "You clicked: btn_submit", Toast.LENGTH_SHORT).show();		
			}
		});
	}
	
	public JSONObject getQPageInfo(){
		JSONObject json = null;
	//for test	
		json = new JSONObject();
		try {		
			json.put("Brand", "Brand X");
			json.put("Product", "X product");
			json.put("Id", "12345678901234567890");
			
			json.put("Owner", "Lisa Smith");
			json.put("Tel", "0425555555");
			json.put("Email", "lis@xxxxx.com");
			json.put("Manufecturer", "Brand X");

		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	//test end
		
		return json;
		
	}
	public Map<String,String> getQpageContent(JSONObject json){
		Map<String,String> map = new HashMap<String, String>();

		map.put("Tel", "0425555555");
		map.put("Email", "lis@xxxxx.com");
		map.put("Manufecturer", "Brand X");
		
		return map;
	}
	public void updatePage(JSONObject json){
		if (json==null)
			return;
		String str = null;
		TextView tv = null;
		try {
			//set the title
			str = json.getString("Brand");
			tv = (TextView) findViewById(R.id.textView1);
			if ((tv!=null)&&(str!=null))
				tv.setText(str);
			
			str = json.getString("Product");
			tv = (TextView) findViewById(R.id.textView2);
			if ((tv!=null)&&(str!=null))
				tv.setText(str);
			
			str = json.getString("Id");
			tv = (TextView) findViewById(R.id.textView3);
			if ((tv!=null)&&(str!=null))
				tv.setText("Id: "+str);
			//set the owner info as the basic textview
			str = json.getString("Owner");
			tv = (TextView) findViewById(R.id.textView4);
			if ((tv!=null)&&(str!=null))
				tv.setText("Owner: "+str);
			
			//set other info textview following the owner info
			int nId =R.id.textView4;
			int nInit = 1;
			TextView tvadd = null;
			Map<String,String> map = getQpageContent(json);
			for (Map.Entry<String,String> entry : map.entrySet()) {
				str = entry.getKey() + ":  " + entry.getValue();
				RelativeLayout rl = (RelativeLayout) findViewById(R.id.qpageinfo);
				tvadd = new TextView(this);
				tvadd.setText(str);
				tvadd.setId(++nInit);
				RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				relativeParams.addRule(RelativeLayout.ALIGN_LEFT,R.id.textView4);
				relativeParams.addRule(RelativeLayout.BELOW,nId);
				relativeParams.topMargin = nMarginTop;
				nId = nInit;
				rl.addView(tvadd, relativeParams);
				}
			

			

			//set the contact textview following the last textview above.
			RelativeLayout.LayoutParams relativeParams1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			relativeParams1.addRule(RelativeLayout.ALIGN_LEFT,R.id.textView4);
			relativeParams1.addRule(RelativeLayout.BELOW,nId);
			relativeParams1.topMargin = nMarginTop*3;
			tvadd = (TextView) findViewById(R.id.textView8);
			tvadd.setLayoutParams(relativeParams1);
			tvadd.setText("Contect Owner");
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ImageView im = (ImageView) findViewById(R.id.QpimageView1);
		Bitmap bm = getLogoImage(json); 
		if (bm!=null)
			im.setImageBitmap(bm);
		
		im = (ImageView) findViewById(R.id.QpimageView2);
		bm = getQrLogoImage(json); 
		if (bm!=null)
			im.setImageBitmap(bm);
	}
	
	protected Bitmap getLogoImage(JSONObject json){
		json.toString();
		Drawable  dw = getResources().getDrawable(R.drawable.q_logo);
		Bitmap bitmap = ((BitmapDrawable)dw).getBitmap(); 
		
		return bitmap;
	}
	
	protected Bitmap getQrLogoImage(JSONObject json){
		json.toString();
		Drawable  dw = getResources().getDrawable(R.drawable.qr_logo);
		Bitmap bitmap = ((BitmapDrawable)dw).getBitmap(); 
		
		return bitmap;
	}

	
}
