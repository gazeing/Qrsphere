package com.qrsphere;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import com.qrsphere.database.Qrcode;
import com.qrsphere.database.QrcodeDataOperator;
import com.qrsphere.database.QrcodeJSONData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
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

public class FavoriteActivity extends Activity {
	private List<String> data = new ArrayList<String>();
	ListView lView;
	protected List<String> getData() {
		return data;
	}
	protected String getDataElement(int index){
		return data.get(index);
	}
	String qrDataGlobal = "";
	
	
	QrcodeDataOperator qdo;
	
	private QrcodeDataOperator getQdo() {
		return qdo;
	}
	
	ArrayList<SeparatedList> lists = new ArrayList<SeparatedList>();
	

	
	SeparatedListAdapter adapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		qdo = new QrcodeDataOperator(this);
		
		
		
		
		

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
	      	  
            @Override  
            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {  
                

            	showPopup(childView);
              	qrDataGlobal = getDataElement(position);
            	
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
                	goToTheWeb();
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
    	Qrcode qc = qdo.queryFromFavor(qrDataGlobal);
    	if (qc!=null){
    		StartBrowser sb = new StartBrowser(qc.getRawdata(),this);
    		sb.startBrowse();
    	}
    }

    public void deleteSelectedRecord(){
    	QrcodeDataOperator qdo = getQdo();
    	Qrcode qc = qdo.queryFromFavor(qrDataGlobal);
    	if (qc!=null){
    		qdo.deleteFromFavorite(qrDataGlobal);
    		onResume();
    	}
    }

    @SuppressLint("SimpleDateFormat")
	public void showScanDetails(){
    	String text = "";
    	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
   
    	QrcodeDataOperator qdo = getQdo();
    	Qrcode qc = qdo.queryFromFavor(qrDataGlobal);
    	if (qc!=null)
    		text = "Rawdata: \n"+qc.getRawdata()+"\nHashcode: \n"+qc.getHashcode()+"\nTime: \n"+sdf.format(new Date(qc.getTimeStamp()));
    	Dialog dialog = new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher)
    				.setView(new ScanDetail(this,text))
    				.setTitle("Scan Details").show();
    	dialog.show();
    }
	protected void onResume() {
		
		super.onResume();

		classfyListByCatalogue();
		lView.invalidateViews();
	}
	

    @SuppressLint("SimpleDateFormat")
	public String TransferTimeFormat(long time){
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

	protected List<Qrcode> getQrcodeList(){
		List<Qrcode> qrs = new ArrayList<Qrcode>();
		for (int i =0; i<5;i++){
			JSONObject json = new JSONObject();
			try {		
				json.put("Catalogue", "test");
				json.put("URL", "test");
				json.put("IsFavorite", true);
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			Qrcode q1 = new Qrcode(json.toString());
			qrs.add(q1);
		}
		for (int i =0; i<5;i++){
			JSONObject json = new JSONObject();
			try {		
				json.put("Catalogue", "test2");
				json.put("URL", "test");
				json.put("IsFavorite", true);
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			Qrcode q1 = new Qrcode(json.toString());
			qrs.add(q1);
		}

		return qrs;
	}
	

	protected void classfyListByCatalogue(){
		
		
		List<Qrcode> qrs = getQrcodeList();
		
		for (Qrcode q : qrs){
			QrcodeJSONData qJson = q.getQrcodeJSONData();
			
			String catalogue = qJson.getCatalogue();
			
			boolean isfound = false; //it is the flag show if the catalog exists now.
			for (SeparatedList sl:lists){
				if (catalogue.compareTo(sl.getCatalogue())==0){// if exist add the item to the list.
					sl.addItem(q.getQrcodeJSONData().getUrl(), TransferTimeFormat(q.getTimeStamp()));
					isfound = true;
				}

			}
			if (isfound == false){// is not exist create a new list and add item to the list.
				SeparatedList newsl = new SeparatedList(catalogue);
				newsl.addItem(q.getQrcodeJSONData().getUrl(), TransferTimeFormat(q.getTimeStamp()));
				lists.add(newsl);
			}
			

		}
		addSections(adapter);
		lView.setAdapter(adapter);
	}
}

class SeparatedList{
	ArrayList<HashMap<String,String>> list;
	String catalogue = "";
	
	
	
	public SeparatedList(String catalogue) {
		super();
		list = new ArrayList<HashMap<String,String>>();
		this.catalogue = catalogue;
	}

	public void addItem(String title,String time)
	{				
		HashMap<String, String> map = new HashMap<String, String>();  
		map.put("ItemTitle", title);  
		map.put("Time", time);
		list.add(map);
	}

	public ArrayList<HashMap<String, String>> getList() {
		return list;
	}

	public String getCatalogue() {
		return catalogue;
	}
	
}
