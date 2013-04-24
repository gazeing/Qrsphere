package com.qrsphere;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.BarcodeFormat;
import com.qrsphere.database.Qrcode;
import com.qrsphere.login.LoginActivity;
import com.qrsphere.network.AddToFavoriteProcess;
import com.qrsphere.network.QPageProcess;
import com.qrsphere.network.SuccessCode;
import com.qrsphere.scan.Contents;
import com.qrsphere.scan.Intents;
import com.qrsphere.userinfo.CollectLocation;
import com.qrsphere.widget.AddToFavorite;
import com.qrsphere.widget.ComboBox;
import com.qrsphere.widget.MyLog;
import com.qrsphere.widget.ScanDetail;
import com.qrsphere.widget.SeparatedListAdapter;
import com.qrsphere.widget.StartBrowser;





import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("HandlerLeak")
public class HistoryActivity extends Activity {


	//ArrayList<HashMap<String, String>> hashlist = new ArrayList<HashMap<String, String>>(); 
	//for test
	ArrayList<HashMap<String, Object>> hashlist1 = new ArrayList<HashMap<String, Object>>(); 
	ArrayList<HashMap<String, Object>> hashlist2 = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> hashlist3 = new ArrayList<HashMap<String, Object>>(); 
	ArrayList<HashMap<String, Object>> hashlist4 = new ArrayList<HashMap<String, Object>>(); 
//	protected String getDataElement(int index){
//		return hashlist.get(index).get("ItemTitle");
//	}
	private ListView lView;
	int positionGlobal = 0;
	String qrDataGlobal = "";
	Qrcode qrcodeGlobal = null;
	//QrcodeDataOperator qdo;
	SeparatedListAdapter adapter;
	ComboBox  cbGlobal = null;
	
	 QPageProcess qpGlobal = null;
	 AddToFavoriteProcess atpGlobal= null;
	 ProgressDialog pd;
	 @SuppressLint("HandlerLeak")
		private Handler handler = new Handler() {
		   
			public void handleMessage(Message msg) {
		      
		    	if(pd!=null&&pd.isShowing()){
		    		pd.dismiss();
		    		
		    		MyLog.i("Dialog","pd.dismiss(); pd.getProgress() = "+pd.getProgress());

		    	}
		        if (msg.what == SuccessCode.DETAIL_SENT_SUCCESS) {

		        } else if (msg.what == SuccessCode.ERROR) {

		        } else if (msg.what == SuccessCode.QPAGE_SUCCESS) {
		        	qpGlobal.startQPage(HistoryActivity.this);
		        	
		        }else if (msg.what == SuccessCode.ADD_TO_FAVORITE_SUCCESS) {
		        	
		        }else {
		            LoginActivity.showNetworkAlert(HistoryActivity.this);
		        }
		    }
		};


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//qdo = new QrcodeDataOperator(this);
		
		qpGlobal = new QPageProcess(pd, handler);
		atpGlobal = new AddToFavoriteProcess(pd, handler);
		

		setContentView(R.layout.history);
		lView = (ListView) findViewById(R.id.history_list);
		
		TextView tv = (TextView) findViewById(R.id.tvtitle);
		//Drawable back = getResources(). getDrawable(R.drawable.banner_history);
		tv.setBackgroundResource(R.drawable.banner_history);
		
		Button btn_back = (Button) findViewById(R.id.btn_title_left);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HistoryActivity.this.finish();
			}
		});
		
		Button btn_favorite = (Button) findViewById(R.id.btn_title_right);
		//back = getResources(). getDrawable(R.drawable.icon_favorite);
		btn_favorite.setBackgroundResource(R.drawable.icon_favorite);
		btn_favorite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HistoryActivity.this.finish();
				startActivity(new Intent("com.qrsphere.FavoriteActivity"));
			}
		});

		
		SimpleAdapter simpAdp1 = new SimpleAdapter(this,
						hashlist1,R.layout.listitem,
		                new String[] {"ItemTitle", "ItemText"},   
		                new int[] {R.id.ItemTitle,R.id.ItemText});
		SimpleAdapter simpAdp2 = new SimpleAdapter(this,
						hashlist2,R.layout.listitem,
		                new String[] {"ItemTitle", "ItemText"},   
		                new int[] {R.id.ItemTitle,R.id.ItemText});
		SimpleAdapter simpAdp3 = new SimpleAdapter(this,
						hashlist3,R.layout.listitem,
		                new String[] {"ItemTitle", "ItemText"},   
		                new int[] {R.id.ItemTitle,R.id.ItemText});
		SimpleAdapter simpAdp4 = new SimpleAdapter(this,
						hashlist4,R.layout.listitem,
		                new String[] {"ItemTitle", "ItemText"},   
		                new int[] {R.id.ItemTitle,R.id.ItemText});


		

		
		
		
		
		adapter = new SeparatedListAdapter(this);  
        adapter.addSection("Today", simpAdp1); 
        adapter.addSection("Past week", simpAdp2); 
        adapter.addSection("Past month", simpAdp3);
        adapter.addSection("Month ago", simpAdp4);
		
		lView.setAdapter(adapter);
		lView.setBackgroundResource(R.drawable.background);


		
		lView.setOnItemClickListener(new OnItemClickListener() {  
	      	  
            @SuppressWarnings("unchecked")
			@Override  
            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {  
                

            	
            	positionGlobal = (int) adapter.getItemId(position);
            	qrDataGlobal = (String) ((HashMap<String, Object>)adapter.getItem(position)).get("ItemTitle");
            	qrcodeGlobal =  (Qrcode) ((HashMap<String, Object>)adapter.getItem(position)).get("Qrcode");
            	showPopup(childView);
            }  
        }); 
	}
	

    //the function that responds user's click on list item, show the popup menu
	protected void showPopup(View v) {  
//    	if (CheckVersion.CheckVersion11()){
//	    	PopupMenu popup = new PopupMenu(this, v);  
//	        MenuInflater inflater = popup.getMenuInflater();  
//	        inflater.inflate(R.menu.action_chose, popup.getMenu());  
//	        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//	        	 
//	            @Override
//	            //the logic to deal with the menu selection
//	            public boolean onMenuItemClick(MenuItem item) {
//	                Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
//	                switch(item.getItemId()){
//	                case R.id.go_to_web:
//	                	showQPage();
//	                	break;
//	                case R.id.add_to_favor:
//	                	addToFavorite();
//	                	break;
//	                case R.id.scan_detail:
//	                	showScanDetails();
//	                	break;
//	                case R.id.send_out:
//	                	share();
//	                	break;
//	                case R.id.feedback:
//	                	feedback();
//	                	break;
//	                case R.id.delete_item:
//	                	deleteSelectedRecord();
//	                	break;
//	                default:
//	                	break;
//	                }
//	                return true;
//	            }
//	        });
//	        popup.show();	
//    	}else{
		if (qrcodeGlobal!=null)
			setPopupMenuAction(qrcodeGlobal);    		
//    	}
    	
          
    }
    
	protected void setPopupMenuAction(Qrcode q){
		qrcodeGlobal = q;
		android.content.DialogInterface.OnClickListener onselect = new android.content.DialogInterface.OnClickListener() {  
			    @Override  
			    public void onClick(DialogInterface dialog, int which) {  
			         
			        switch (which) {  
			        case 0:  
			            Toast.makeText(HistoryActivity.this, "0",Toast.LENGTH_SHORT).show(); 
			            showQPage();
			            break;  
			        case 1:  
			            Toast.makeText(HistoryActivity.this, "1",Toast.LENGTH_SHORT).show();
			            share();
			            break;  
			        case 2:  
			            Toast.makeText(HistoryActivity.this, "2",Toast.LENGTH_SHORT).show(); 
			            showScanDetails();
			            break;  
			        case 3:  
			            Toast.makeText(HistoryActivity.this, "3",Toast.LENGTH_SHORT).show();
			            feedback();
			            break;  
			        case 4:  
			            Toast.makeText(HistoryActivity.this, "4",Toast.LENGTH_SHORT).show();
			            deleteSelectedRecord();
			            break; 
			        case 5:  
			            Toast.makeText(HistoryActivity.this, "5",Toast.LENGTH_SHORT).show();
			            addToFavorite();
			            break; 
			        default:
			        	break;
			            	
			        }  
 
			    }

			   };  
		if (q!=null){
	
			showPopup(q.getQrcodeJSONData().getUrl(),onselect);
	
		}
	}
	protected void showPopup(String str,android.content.DialogInterface.OnClickListener oc) {  
		
		String[] choices={this.getString(R.string.qpage) ,
				this.getString(R.string.send_out),
				this.getString(R.string.scan_detail),
				this.getString(R.string.feedback),
				this.getString(R.string.delete_item),
				this.getString(R.string.add_to_favor)};  

		
		AlertDialog dialog = new AlertDialog.Builder(HistoryActivity.this)  
            .setIcon(R.drawable.qrcode_icon)  
            .setTitle(str)  
            .setItems(choices, (android.content.DialogInterface.OnClickListener) oc).create();  
		dialog.show();  

    }
    
	protected void share() {
    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null){
				Intent intent = new Intent(new Intent(HistoryActivity.this,ShareActivity.class));
				Bundle b = new Bundle();
				b.putString("rawdata", qc.getRawdata());
				intent.putExtras(b);
			    intent.setAction(Intents.Encode.ACTION);
			    intent.putExtra(Intents.Encode.DATA, qc.getQrcodeJSONData().getUrl());
			    intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
			    intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE);
				startActivity(intent);
    	}
		
	}

	protected void feedback() {
	
    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null){
				Intent intent = new Intent(new Intent(HistoryActivity.this,FeedbackActivity.class));
				Bundle b = new Bundle();
				b.putString("rawdata", qc.getRawdata());
				intent.putExtras(b);
				startActivity(intent);
    	}
	}

    public void showQPage(){
    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null){
    		this.pd = qpGlobal.sentToServer(HistoryActivity.this, qc);
    	}
    }
    
    public void goToTheWeb(){
    	
    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null){
    		StartBrowser sb = new StartBrowser(qc.getRawdata(),this);
    		sb.startBrowse();
    	}
    }

    public void deleteSelectedRecord(){
    	
//    	Qrcode qc = qrcodeGlobal;
//    	if (qc!=null){
//    		qdo.delete(qrDataGlobal);
//    		onResume();
//    	}
    }

	public void addToFavorite(){

		DialogInterface.OnClickListener click =new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int arg1) {
			// OK, go back to Main menu
			 sendDataToFavorList();
			}
			
		
		};
		
		DialogInterface.OnCancelListener cancel = new DialogInterface.OnCancelListener(){
			public void onCancel(DialogInterface dialog) {
			// OK, go back to Main menu   
				}
			};
			
		AddToFavorite af = new AddToFavorite();
		cbGlobal = af.show(this, qrDataGlobal, click, cancel);
    }
    

    
//	protected void InstructionsDialog(String text,String title,
//							ArrayAdapter<String> list){
//
//		  AlertDialog.Builder ad = new AlertDialog.Builder(this);
//		  ad.setIcon(R.drawable.ic_launcher);
//		  ad.setTitle(title);
//		  LayoutInflater linf = LayoutInflater.from(this);
//		  final View inflator = linf.inflate(R.layout.dialog, null);
//		  ad.setView(inflator);
//
//		  ad.setPositiveButton("OK", 
//		    new android.content.DialogInterface.OnClickListener() {
//		     public void onClick(DialogInterface dialog, int arg1) {
//		      // OK, go back to Main menu
//		    	 sendDataToFavorList();
//		     }
//
//
//		    }
//		   );
//
//		   ad.setOnCancelListener(new DialogInterface.OnCancelListener(){
//		    public void onCancel(DialogInterface dialog) {
//		     // OK, go back to Main menu   
//		    }}
//		   );
//		   
//			  TextView tv =(TextView)(inflator.findViewById(R.id.TextView01));
//			  if (tv!=null)
//				tv.setText(text);
//			  ComboBox cb = (ComboBox) (inflator.findViewById(R.id.Combo01));
//			  if (cb!= null)
//				  cb.setSuggestionSource(list);
//
//		  ad.show();
//
//		 }
	
	private void sendDataToFavorList() {
		// TODO Auto-generated method stub
		if (cbGlobal!=null){
			String cata = cbGlobal.getText();
			qrcodeGlobal.addCatalogue(cata);
	    	Qrcode qc = qrcodeGlobal;
	    	if (qc!=null){
	    		this.pd =atpGlobal.sentToServer(this, qc);
	    	}
		}
	}

	public void showScanDetails(){
//    	String text = "";
    	
   
    	
    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null){
    		Intent intent = new Intent(HistoryActivity.this, ScanDetailActivity.class);
			Bundle b = new Bundle();
			b.putString("Qrcode",qc.getRawdata());
			intent.putExtras(b);
			startActivity(intent);
    	}
//    		text = "Rawdata: \n"+qc.getRawdata()+"\nHashcode: \n"+qc.getHashcode()+"\nTime: \n"+TransferTimeFormat(qc.getTimeStamp());
//    	Dialog dialog = new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher)
//    				.setView(new ScanDetail(this,text))
//    				.setTitle("Scan Details").show();
//    	dialog.show();
    	
    	
    }


	@Override
	protected void onResume() {
		
		super.onResume();

		classfyListByDate();
		lView.invalidateViews();
	}
	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	protected long getTodayMidnight(){
;
		Date today = new Date();
		int year = today.getYear();
		int month = today.getMonth();
		int day = today.getDate();
		
		Date mid = new Date(year,month,day);
		long longNow = mid.getTime();
		return longNow;
	}
	
	protected void classfyListByDate(){
		
		//QrcodeDataOperator qdo = getQdo();

		long longMid = getTodayMidnight();
		//Log.i("the middlenight is :",TransferTimeFormat(longMid));
		final long oneDay = 1000*60*60*24;
		hashlist1.clear();
		hashlist2.clear();
		hashlist3.clear();
		hashlist4.clear();

		
		//List<Qrcode> qrs = qdo.queryAll();
		List<Qrcode> qrs = getQrcodes();

		for (Qrcode q : qrs){

	        HashMap<String, Object> map = new HashMap<String, Object>();  
	        map.put("Qrcode",q);  
	        map.put("ItemTitle", q.getQrcodeJSONData().getUrl());  
	        map.put("ItemText", ScanDetail.TransferTimeFormat(q.getTimeStamp())); 
	        
	        long longTime = q.getTimeStamp();
	        if (((longTime-longMid)<oneDay)&&((longTime-longMid)>=0))
	        	hashlist1.add(map);
	        else if (((longMid-longTime)<(oneDay)*6)&&((longMid-longTime)>=0))
	        	hashlist2.add(map);
	        else if (((longMid-longTime)<(oneDay)*30)&&((longMid-longTime)>=(oneDay)*6))
	        	hashlist3.add(map);
	        else	if ((longMid-longTime)>=(oneDay)*30)
	        	hashlist4.add(map);
	        else
	        	hashlist1.add(map);
	       // hashlist.add(map);
		}
	}

	public List<Qrcode> getQrcodes(){
		return generateTestQrcode();
	}

	
	public List<Qrcode> generateTestQrcode()
	{
		List<Qrcode> qrs = new ArrayList<Qrcode>();
		//for test
		LocationListener ll = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
		};
		CollectLocation cl = new CollectLocation(this,ll);
		long now = System.currentTimeMillis();
		final long oneDay = 1000*60*60*24;
		for (int i = 0;i<20;i++){
			JSONObject json = new JSONObject();
			try {		
				json.put("Catalogue", "test");
				json.put("URL", "www.facebook.com");
				json.put("IsFavorite", true);
				json.put("Latitude", cl.getLatitude());
				json.put("Longitude", cl.getLongitude());
				json.put("TimeStamp",now-oneDay*i );
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			Qrcode q1 = new Qrcode(json.toString(),now-oneDay*i,"testHashCode");
			//qdo.insert(q1);
			qrs.add(q1);
		}
		
		

		
		//test end
		
		return qrs;
	}


}
