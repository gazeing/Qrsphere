package com.qrsphere;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import com.qrsphere.database.Qrcode;
import com.qrsphere.database.QrcodeJSONData;
import com.qrsphere.login.LoginActivity;
import com.qrsphere.network.QPageProcess;
import com.qrsphere.network.SuccessCode;
import com.qrsphere.userinfo.CollectLocation;
import com.qrsphere.widget.MyLog;
import com.qrsphere.widget.ScanDetail;
import com.qrsphere.widget.SeparatedListAdapter;
import com.qrsphere.widget.StartBrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;

@SuppressLint("HandlerLeak")
public class FavoriteActivity extends Activity {
	private List<String> data = new ArrayList<String>();
	ListView lView;
	protected List<String> getData() {
		return data;
	}
//	protected String getDataElement(int index){
//		return data.get(index);
//	}
	String qrDataGlobal = "";
	Qrcode qrcodeGlobal = null;
	
	
//	QrcodeDataOperator qdo;
//	
//	private QrcodeDataOperator getQdo() {
//		return qdo;
//	}
	
	ArrayList<SeparatedList> lists = new ArrayList<SeparatedList>();
	

	
	SeparatedListAdapter adapter;
	
	 QPageProcess qpGlobal = null;
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
		        	qpGlobal.startQPage(FavoriteActivity.this);
		        	
		        }else {
		            LoginActivity.showNetworkAlert(FavoriteActivity.this);
		        }
		    }
		};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//qdo = new QrcodeDataOperator(this);
		
		qpGlobal = new QPageProcess(pd, handler);
		
		
		

		setContentView(R.layout.favorite);
		lView = (ListView) findViewById(R.id.favorite_list);
		
		TextView tv = (TextView) findViewById(R.id.tvtitle);
		tv.setText("favorite");




		
		

		SeparatedList sl = new SeparatedList("My Best Codes");
		lists.add(sl);
		
		//for test
//		int i = 3;
//		while(i-->0){
//			JSONObject json = new JSONObject();
//			try {		
//				json.put("Catalogue", "test");
//				json.put("URL", "test");
//				json.put("IsFavorite", true);
//			} catch (JSONException e) {
//				
//				e.printStackTrace();
//			}
//			Qrcode q1 = new Qrcode(json.toString());
//	
//	;
//			sl.addItem(q1.getQrcodeJSONData().getUrl(), TransferTimeFormat(q1.getTimeStamp()));
//		}
		//test end
		

		adapter = new SeparatedListAdapter(this); 
		
		addSections(adapter);
        
		
		
		lView.setAdapter(adapter);
		lView.setBackgroundResource(R.drawable.background);
		lView.setOnItemClickListener(new OnItemClickListener() {  
	      	  
            @SuppressWarnings("unchecked")
			@Override  
            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {  
                

            	showPopup(childView);
               	qrDataGlobal = (String) ((HashMap<String, Object>)adapter.getItem(position)).get("ItemTitle");
            	qrcodeGlobal =  (Qrcode) ((HashMap<String, Object>)adapter.getItem(position)).get("Qrcode");
            	
            }  
        });
	}
	@SuppressLint("NewApi")
	protected void showPopup(View childView) {
		
		PopupMenu popup = new PopupMenu(this, childView);  
        MenuInflater inflater = popup.getMenuInflater();  
        inflater.inflate(R.menu.favor_chose, popup.getMenu());  
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        	 
            @Override
            //the logic to deal with the menu selection
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                switch(item.getItemId()){
                case R.id.go_to_web:
                	showQPage();
                	break;
                	
                case R.id.feedback:

                	;
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
	private void showQPage() {
				// TODO Auto-generated method stub
    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null){
    		this.pd = qpGlobal.sentToServer(FavoriteActivity.this, qc, SuccessCode.QPAGE_SUCCESS, "Generating Qpage...");;
    	}		
	}
    public void goToTheWeb(){
    	
    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null){
    		StartBrowser sb = new StartBrowser(qc.getQrcodeJSONData().getUrl(),this);
    		sb.startBrowse();
    	}
    }

    public void deleteSelectedRecord(){
//    	QrcodeDataOperator qdo = getQdo();
//    	Qrcode qc = qdo.queryFromFavor(qrDataGlobal);
//    	if (qc!=null){
//    		qdo.deleteFromFavorite(qrDataGlobal);
//    		onResume();
//    	}
    }

    @SuppressLint("SimpleDateFormat")
	public void showScanDetails(){

    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null)
    		ScanDetail.ScanDetailDialog(qc,this);
    }
    
//    private void ScanDetailDialog(Qrcode qc) {
//    	AlertDialog.Builder ad = new AlertDialog.Builder(this);
//		  ad.setIcon(R.drawable.ic_launcher);
//		  ad.setTitle("Scan Detail");
//		  LayoutInflater linf = LayoutInflater.from(this);
//		  final View inflator = linf.inflate(R.layout.scan_detail, null);
//		  ad.setView(inflator);
//
//		  ad.setPositiveButton("OK", 
//		    new android.content.DialogInterface.OnClickListener() {
//		     public void onClick(DialogInterface dialog, int arg1) {
//		      // OK, go back to Main menu
//		    	 //sendDataToFavorList();
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
//		   //fill the content to scan detail page
//			  TextView tv =(TextView)(inflator.findViewById(R.id.ScanDetailUrl));
//			  if (tv!=null)
//				tv.setText("URL: ");
//			  TextView tv1 =(TextView)(inflator.findViewById(R.id.ScanDetailUrlText));
//			  if (tv1!=null)
//				tv1.setText(qc.getQrcodeJSONData().getUrl());
//			  TextView tv2 =(TextView)(inflator.findViewById(R.id.ScanDetailTime));
//			  if (tv2!=null)
//				tv2.setText("Scan Time: ");
//			  TextView tv3 =(TextView)(inflator.findViewById(R.id.ScanDetailTimeText));
//			  if (tv3!=null)
//				tv3.setText(TransferTimeFormat(qc.getTimeStamp()));
//			  TextView tv4 =(TextView)(inflator.findViewById(R.id.ScanDetailLocation));
//			  if (tv4!=null)
//				tv4.setText("Location: ");
//			  TextView tv5 =(TextView)(inflator.findViewById(R.id.ScanDetailLocationText));
//			  if (tv5!=null)
//				tv5.setText(qc.getQrcodeJSONData().getLatitude()+", "+qc.getQrcodeJSONData().getLongitude());
//
//
//		  ad.show();
//		
//	}
	protected void onResume() {
		
		super.onResume();

		classfyListByCatalogue();
		lView.invalidateViews();
	}
	

    @SuppressLint("SimpleDateFormat")
	public static String TransferTimeFormat(long time){
    	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
    	return sdf.format(new Date(time));
    }
    

	protected SeparatedListAdapter addSections(SeparatedListAdapter adapter){
    	adapter.clear();

    	for (SeparatedList sl: lists){
    		SimpleAdapter simpAdp1 = new SimpleAdapter(this,
			sl.getList(),R.layout.listitem,
            new String[] {"ItemTitle", "Time"},   
            new int[] {R.id.ItemTitle,R.id.ItemText});
    		adapter.addSection(sl.getCatalogue(), simpAdp1);
    	}
		return adapter;
    }

	public List<Qrcode> generateTestQrcode(){
		List<Qrcode> qrs = new ArrayList<Qrcode>();
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
		for (int i =0; i<5;i++){
			JSONObject json = new JSONObject();
			try {		
				json.put("Catalogue", "test");
				json.put("URL", "www.facebook.com");
				json.put("IsFavorite", true);
				json.put("Latitude", cl.getLatitude());
				json.put("Longitude", cl.getLongitude());
			} catch (JSONException e) {
				
				MyLog.i(e.toString());
			}
			Qrcode q1 = new Qrcode(json.toString(),now-oneDay*i,"testHashCode");
			qrs.add(q1);
		}
		for (int i =0; i<5;i++){
			JSONObject json = new JSONObject();
			try {		
				json.put("Catalogue", "test2");
				json.put("URL", "www.hoopchina.com");
				json.put("IsFavorite", true);
				json.put("Latitude", cl.getLatitude());
				json.put("Longitude", cl.getLongitude());
			} catch (JSONException e) {
				
				MyLog.i(e.toString());
			}
			Qrcode q1 = new Qrcode(json.toString(),now-oneDay*i*32,"testHashCode");
			qrs.add(q1);
		}

		for (int i =0; i<5;i++){
			JSONObject json = new JSONObject();
			try {		
				json.put("Catalogue", "My Best Codes");
				json.put("URL", "www.weibo.com");
				json.put("IsFavorite", true);
				json.put("Latitude", cl.getLatitude());
				json.put("Longitude", cl.getLongitude());
			} catch (JSONException e) {
				
				MyLog.i(e.toString());
			}
			Qrcode q1 = new Qrcode(json.toString(),now-oneDay*i*12,"testHashCode");
			qrs.add(q1);
		}
		return qrs;
	}
	protected List<Qrcode> getQrcodeList(){
		
		return generateTestQrcode();
	}
	

	protected void classfyListByCatalogue(){
		
		
		List<Qrcode> qrs = getQrcodeList();
		
		for (Qrcode q : qrs){
			QrcodeJSONData qJson = q.getQrcodeJSONData();
			
			String catalogue = qJson.getCatalogue();
			
			boolean isfound = false; //it is the flag show if the catalog exists now.
			for (SeparatedList sl:lists){
				if (catalogue.compareTo(sl.getCatalogue())==0){// if exist add the item to the list.
					sl.addItem(q);
					isfound = true;
				}

			}
			if (isfound == false){// is not exist create a new list and add item to the list.
				SeparatedList newsl = new SeparatedList(catalogue);
				newsl.addItem(q);
				lists.add(newsl);
			}
			

		}
		addSections(adapter);
		lView.setAdapter(adapter);
	}
}

class SeparatedList{
	ArrayList<HashMap<String,Object>> list;
	String catalogue = "";
	
	
	
	public SeparatedList(String catalogue) {
		super();
		list = new ArrayList<HashMap<String,Object>>();
		this.catalogue = catalogue;
	}

	public void addItem(Qrcode q)
	{				
		HashMap<String, Object> map = new HashMap<String, Object>();  
        map.put("Qrcode",q);  
        map.put("ItemTitle", q.getQrcodeJSONData().getUrl());  
        map.put("Time", FavoriteActivity.TransferTimeFormat(q.getTimeStamp()));
		list.add(map);
	}

	public ArrayList<HashMap<String, Object>> getList() {
		return list;
	}

	public String getCatalogue() {
		return catalogue;
	}
	
}
