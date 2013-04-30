package com.qrsphere;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.zxing.BarcodeFormat;
import com.qrsphere.database.Qrcode;
import com.qrsphere.database.QrcodeList;
import com.qrsphere.login.LoginActivity;
import com.qrsphere.network.AddToFavoriteProcess;
import com.qrsphere.network.DeleteItemProcess;
import com.qrsphere.network.GetHistoryListProcess;
import com.qrsphere.network.QPageProcess;
import com.qrsphere.network.SuccessCode;
import com.qrsphere.scan.Contents;
import com.qrsphere.scan.Intents;
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
	 GetHistoryListProcess ghhGlobal = null;
	 DeleteItemProcess dipGlobal = null;
	 ProgressDialog pd;
	 
	 QrcodeList qrcodeListGlobal = null;
	 
	 @SuppressLint("HandlerLeak")
		private Handler handler = new Handler() {
		   
			public void handleMessage(Message msg) {
		      
		    	if(pd!=null&&pd.isShowing()){
		    		pd.dismiss();
		    		
		    		MyLog.i("Dialog","pd.dismiss(); pd.getProgress() = "+pd.getProgress());

		    	}
		        if (msg.what == SuccessCode.DETAIL_SENT_SUCCESS) {

		        }else if(msg.what == SuccessCode.GET_LIST_SUCCESS){
		        	qrcodeListGlobal.update();
		        	classfyListByDate();
		        	lView.invalidateViews();
		        	
		        }
		        else if(msg.what == SuccessCode.DELETE_SUCCESS){
		        	 Toast.makeText(getBaseContext(), "Success Deleted" , Toast.LENGTH_SHORT).show();
		        	 
		        	 if (qrcodeGlobal != null){
		        		 if (qrcodeGlobal.getQrcodeJSONData().getQrScanHistoryID()!=-1){
				        	 qrcodeListGlobal.deleteItem(qrcodeGlobal.getQrcodeJSONData().getQrScanHistoryID());
				        	 classfyListByDate();
				        	 lView.invalidateViews();
		        		 }else{
		        			 pd = ghhGlobal.sentToServer(HistoryActivity.this, null);
		        		 }
		        		}
		        	else{
		        		pd = ghhGlobal.sentToServer(HistoryActivity.this, null);
		        	}
		        		
		        }
		        else if (msg.what == SuccessCode.ERROR) {
		        

		        } else if (msg.what == SuccessCode.QPAGE_SUCCESS) {
		        	qpGlobal.startQPage(HistoryActivity.this);
		        	
		        }else if (msg.what == SuccessCode.ADD_TO_FAVORITE_SUCCESS) {
		        	pd = ghhGlobal.sentToServer(HistoryActivity.this, null);
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
		ghhGlobal = new GetHistoryListProcess(pd, handler);
		dipGlobal = new DeleteItemProcess(pd, handler);
		
		String listString = getIntent().getExtras().getString("ListString");
		if (listString != null)
			qrcodeListGlobal = new QrcodeList(this, ghhGlobal, pd, listString);
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
				jumpToFavorite();
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
		
		classfyListByDate();
		//sendListRequest();
	}
	




	protected void jumpToFavorite(){
		HistoryActivity.this.finish();
		Intent intent = new Intent(HistoryActivity.this,FavoriteActivity.class);
    	intent.putExtra("ListString",qrcodeListGlobal.getJSONArrayString());
    	startActivity(intent);
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

    	
          
    }
    
	protected void setPopupMenuAction(Qrcode q){
		//qrcodeGlobal = q;
		android.content.DialogInterface.OnClickListener onselect = new android.content.DialogInterface.OnClickListener() {  
			    @Override  
			    public void onClick(DialogInterface dialog, int which) {  
			         
			        switch (which) {  
			        case 0:  
			            showQPage();
			            break;  
			        case 1:  
			            share();
			            break;  
			        case 2:  
			            showScanDetails();
			            break;  
			        case 3:  
			            feedback();
			            break;  
			        case 4:  
			            deleteSelectedRecord();
			            break; 
			        case 5:  
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
    	
    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null){
    		pd = dipGlobal.sentToServer(HistoryActivity.this, qc);
    	}
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
		cbGlobal = af.show(this, qrDataGlobal, click, cancel,qrcodeListGlobal);
    }
    

    

	
	private void sendDataToFavorList() {
		if (cbGlobal!=null){
			String cata = cbGlobal.getText();

	    	Qrcode qc = qrcodeGlobal;
	    	if (qc!=null){
	    		if (qc.getQrcodeJSONData().isFavorite()){
	    			jumpToFavorite();// if the item has already been in favorite, just
	    								//jump to favorite page.
	    		}else{
	    			qc.addCatalogue(cata);
	    			this.pd =atpGlobal.sentToServer(this, qc);
	    		}
	    		
	    	}
		}
	}

	public void showScanDetails(){

    	
   
    	
    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null){
    		Intent intent = new Intent(HistoryActivity.this, ScanDetailActivity.class);
			Bundle b = new Bundle();
			b.putString("Qrcode",qc.getRawdata());
			intent.putExtras(b);
			startActivity(intent);
    	}

    	
    	
    }


	@Override
	protected void onResume() {
		
		super.onResume();


		
		lView.invalidateViews();
	}



	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	protected long getTodayMidnight(){

		Date today = new Date();
		int year = today.getYear();
		int month = today.getMonth();
		int day = today.getDate();
		
		Date mid = new Date(year,month,day);
		long longNow = mid.getTime();
		return longNow;
	}
	
	protected void classfyListByDate(){
		


		long longMid = getTodayMidnight();

		final long oneDay = 1000*60*60*24;
		hashlist1.clear();
		hashlist2.clear();
		hashlist3.clear();
		hashlist4.clear();

		

		List<Qrcode> qrs = getQrcodes();

		for (Qrcode q : qrs){

	        HashMap<String, Object> map = new HashMap<String, Object>();  
	        map.put("Qrcode",q);  
	        map.put("ItemTitle", q.getQrcodeJSONData().getUrl());  
	        map.put("ItemText", ScanDetail.TransferTimeFormat(
					ScanDetail.getLongFromServerTimeFormat(
							q.getQrcodeJSONData().getDateTime()))); 
	        
	        long longTime = ScanDetail.getLongFromServerTimeFormat(
	        						q.getQrcodeJSONData().getDateTime());
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
		List<Qrcode> list =null;
		if (qrcodeListGlobal != null){
			list = qrcodeListGlobal.getHistoryList();
		}
		return list;
	        
		
	}

	


}
