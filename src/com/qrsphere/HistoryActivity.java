package com.qrsphere;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.qrsphere.database.Qrcode;
import com.qrsphere.database.QrcodeDataOperator;
import com.qrsphere.userinfo.CollectLocation;
import com.qrsphere.widget.ComboBox;
import com.qrsphere.widget.ScanDetail;
import com.qrsphere.widget.SeparatedListAdapter;
import com.qrsphere.widget.StartBrowser;





import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.PopupMenu;

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
	QrcodeDataOperator qdo;
	SeparatedListAdapter adapter;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		qdo = new QrcodeDataOperator(this);
		
		//for test
//		LocationListener ll = new LocationListener(){
//
//			@Override
//			public void onLocationChanged(Location location) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onProviderDisabled(String provider) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onProviderEnabled(String provider) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onStatusChanged(String provider, int status,
//					Bundle extras) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		};
//		CollectLocation cl = new CollectLocation(this,ll);
//		
//		for (int i = 0;i<20;i++){
//			JSONObject json = new JSONObject();
//			try {		
//				json.put("Catalogue", "test");
//				json.put("URL", "www.facebook.com");
//				json.put("IsFavorite", true);
//				json.put("Latitude", cl.getLatitude());
//				json.put("Longitude", cl.getLongitude());
//			} catch (JSONException e) {
//				
//				e.printStackTrace();
//			}
//			Qrcode q1 = new Qrcode(json.toString());
//			qdo.insert(q1);
//		}
//		
//		

		
		//test end
		

		setContentView(R.layout.history);
		lView = (ListView) findViewById(R.id.history_list);
		
		TextView tv = (TextView) findViewById(R.id.tvtitle);
		tv.setText("History");

		
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
	
    @SuppressLint("NewApi")
    //the function that responds user's click on list item, show the popup menu
	protected void showPopup(View v) {  
        PopupMenu popup = new PopupMenu(this, v);  
        MenuInflater inflater = popup.getMenuInflater();  
        inflater.inflate(R.menu.action_chose, popup.getMenu());  
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        	 
            @Override
            //the logic to deal with the menu selection
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                switch(item.getItemId()){
                case R.id.go_to_web:
                	showQPage();
                	break;
                case R.id.add_to_favor:
                	addToFavorite();
                	break;
                case R.id.scan_detail:

                	showScanDetails();
                	break;
                case R.id.send_out:
                	;
                	break;
                case R.id.delete_item:
                	deleteSelectedRecord();
                	break;
                default:
                	break;
                }
                return true;
            }
        });
        popup.show();  
    }

    public void showQPage(){
    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null){
    		Intent intent= new Intent("com.qrsphere.QPageActivity");
    		
    		startActivity(intent);
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

    	
    	ArrayAdapter<String> adp = getSuggestion();

    	InstructionsDialog("You have chose "+ qrDataGlobal +". " +
    			"\nPlease give a description of it. Or you can chose " +
    			"from the past list. ","Please input category",adp);
       	;
    }
    

    
	protected void InstructionsDialog(String text,String title,
							ArrayAdapter<String> list){

		  AlertDialog.Builder ad = new AlertDialog.Builder(this);
		  ad.setIcon(R.drawable.ic_launcher);
		  ad.setTitle(title);
		  LayoutInflater linf = LayoutInflater.from(this);
		  final View inflator = linf.inflate(R.layout.dialog, null);
		  ad.setView(inflator);

		  ad.setPositiveButton("OK", 
		    new android.content.DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog, int arg1) {
		      // OK, go back to Main menu
		    	 sendDataToFavorList();
		     }


		    }
		   );

		   ad.setOnCancelListener(new DialogInterface.OnCancelListener(){
		    public void onCancel(DialogInterface dialog) {
		     // OK, go back to Main menu   
		    }}
		   );
		   
			  TextView tv =(TextView)(inflator.findViewById(R.id.TextView01));
			  if (tv!=null)
				tv.setText(text);
			  ComboBox cb = (ComboBox) (inflator.findViewById(R.id.Combo01));
			  if (cb!= null)
				  cb.setSuggestionSource(list);

		  ad.show();

		 }
	
	private void sendDataToFavorList() {
		// TODO Auto-generated method stub
		
	}

	public void showScanDetails(){
//    	String text = "";
    	
   
    	
    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null)
    		ScanDetail.ScanDetailDialog(qc,this);
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
	
    public ArrayAdapter<String> getSuggestion(){
    	ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
    	int i = 4;
    	while(i-->0){

    		adp.add("Suggest"+i);
    	}
    	return adp;
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
