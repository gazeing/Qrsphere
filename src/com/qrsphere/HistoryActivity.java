package com.qrsphere;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.qrsphere.database.Qrcode;
import com.qrsphere.database.QrcodeDataOperator;





import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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


	ArrayList<HashMap<String, String>> hashlist = new ArrayList<HashMap<String, String>>(); 
	//for test
	ArrayList<HashMap<String, String>> hashlist1 = new ArrayList<HashMap<String, String>>(); 
	ArrayList<HashMap<String, String>> hashlist2 = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> hashlist3 = new ArrayList<HashMap<String, String>>(); 
	ArrayList<HashMap<String, String>> hashlist4 = new ArrayList<HashMap<String, String>>(); 
	protected String getDataElement(int index){
		return hashlist.get(index).get("ItemTitle");
	}
	private ListView lView;
	int positionGlobal = 0;
	String qrDataGlobal = "";
	QrcodeDataOperator qdo;
	
	private QrcodeDataOperator getQdo() {
		return qdo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		qdo = new QrcodeDataOperator(this);
		
		

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
		
		

		
		
		
		
		SeparatedListAdapter adapter = new SeparatedListAdapter(this);  
        adapter.addSection("Today", simpAdp1); 
        adapter.addSection("Past week", simpAdp2); 
        adapter.addSection("Past month", simpAdp3);
        adapter.addSection("Month ago", simpAdp4);
		
		lView.setAdapter(adapter);
		lView.setBackgroundResource(R.drawable.background);


		
		lView.setOnItemClickListener(new OnItemClickListener() {  
	      	  
            @Override  
            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {  
                

            	
            	positionGlobal = position;
            	qrDataGlobal = getDataElement(position);
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
                	goToTheWeb();
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

    
    public void goToTheWeb(){
    	QrcodeDataOperator qdo = getQdo();
    	Qrcode qc = qdo.query(qrDataGlobal);
    	if (qc!=null){
    		StartBrowser sb = new StartBrowser(qc.getRawdata(),this);
    		sb.startBrowse();
    	}
    }

    public void deleteSelectedRecord(){
    	QrcodeDataOperator qdo = getQdo();
    	Qrcode qc = qdo.query(qrDataGlobal);
    	if (qc!=null){
    		qdo.delete(qrDataGlobal);
    		onResume();
    	}
    }
    @SuppressWarnings("unused")
	public void addToFavorite(){

    	ComboBox cb= new ComboBox(this);
    	ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
    	int i = 4;
    	while(i-->0){

    		adp.add("Suggest"+i);
    	}

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
    	String text = "";
    	
   
    	QrcodeDataOperator qdo = getQdo();
    	Qrcode qc = qdo.query(qrDataGlobal);
    	if (qc!=null)
    		text = "Rawdata: \n"+qc.getRawdata()+"\nHashcode: \n"+qc.getHashcode()+"\nTime: \n"+TransferTimeFormat(qc.getTimeStamp());
    	Dialog dialog = new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher)
    				.setView(new ScanDetail(this,text))
    				.setTitle("Scan Details").show();
    	dialog.show();
    }
    @SuppressLint("SimpleDateFormat")
	public String TransferTimeFormat(long time){
    	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
    	return sdf.format(new Date(time));
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
		
		QrcodeDataOperator qdo = getQdo();

		long longMid = getTodayMidnight();
		//Log.i("the middlenight is :",TransferTimeFormat(longMid));
		final long oneDay = 1000*60*60*24;
		hashlist1.clear();
		hashlist2.clear();
		hashlist3.clear();
		hashlist4.clear();

		
		List<Qrcode> qrs = qdo.queryAll();
		for (Qrcode q : qrs){

	        HashMap<String, String> map = new HashMap<String, String>();  
	        map.put("ItemTitle", q.getRawdata());  
	        map.put("ItemText", TransferTimeFormat(q.getTimeStamp())); 
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
	        hashlist.add(map);
		}
	}


}
