package com.qrsphere;



import java.util.ArrayList;
import java.util.HashMap;

import com.qrsphere.scan.Contents;
import com.qrsphere.scan.Intents;
import com.google.zxing.BarcodeFormat;
import com.qrsphere.database.Qrcode;
import com.qrsphere.login.LoginActivity;
import com.qrsphere.network.AddToFavoriteProcess;
import com.qrsphere.network.QPageProcess;
import com.qrsphere.network.SendDetailProcess;
import com.qrsphere.network.SuccessCode;
import com.qrsphere.widget.AddToFavorite;
import com.qrsphere.widget.ComboBox;
import com.qrsphere.widget.MyLog;
import com.qrsphere.widget.StartBrowser;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MainViewActivity extends Activity{

    private int[] mImages={  
            R.drawable.scan_icon,  
            R.drawable.history_icon,  
            R.drawable.fav_icon,  
            R.drawable.gear_icon,
            R.drawable.discover_icon,
            R.drawable.about_icon,
            
            


  
    }; 
    
    private String[] mTexts ={
    		"Scan",
    		"History",
    		"Favorite",
    		"More",
    		"Discover",
    		"About",
    		
    		

    		
    };
    
	private ProgressDialog pd;
    GridView mGridView;
    Button bt_ad;
    Context context;
    boolean isOnline = false;
    
    boolean isFromPopupMenu = false;
    Qrcode qrcodeGlobal =  null;
    QPageProcess qpGlobal = null;
    SendDetailProcess sdqGlobal = null;
    AddToFavoriteProcess atpGlobal= null;
    
    ComboBox  cbGlobal = null;
    
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
	        	qpGlobal.startQPage(MainViewActivity.this);
	        	
	        }else if (msg.what == SuccessCode.ADD_TO_FAVORITE_SUCCESS) {
	        	
	        }else {
	            LoginActivity.showNetworkAlert(MainViewActivity.this);
	        }
	    }
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.mainview); 
      
        
       
        Init();
	}
	
	private void Init(){
		context = this;
		qpGlobal = new QPageProcess(pd, handler);
		sdqGlobal = new SendDetailProcess(pd, handler);
		atpGlobal = new AddToFavoriteProcess(pd, handler);
		
		Bundle b= getIntent().getExtras();
		if (b!=null){
			isOnline = b.getBoolean("IsOnline"); 
			String str = b.getString("username");
			TextView tvTitle = (TextView) findViewById(R.id.tvmaintitle);
			tvTitle.setText(str);
		}
		
		mGridView=(GridView) findViewById(R.id.gridview);  
        //  
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();  
          
        for (int i = 0; i < mImages.length; i++) {  
            HashMap<String, Object> map = new HashMap<String, Object>();  
            map.put("ItemImage", mImages[i]);// 
            map.put("ItemText", mTexts[i]);// 
            lstImageItem.add(map);  
        }  
        //
        SimpleAdapter simple = new SimpleAdapter(this, lstImageItem,  
                R.layout.gridviewitem,  
                new String[] { "ItemImage" }, new int[] {  
                        R.id.ItemImage});  
        mGridView.setAdapter(simple);  
        //  
        mGridView.setOnItemClickListener(new GridView.OnItemClickListener(){  
  
            @Override  
            public void onItemClick(AdapterView<?> parent, View view,  
                    int position, long id) {  
//                Toast toast=Toast.makeText(getApplicationContext(),
//                		"you chose"+(position+1)+"#picture", Toast.LENGTH_SHORT);  
//                toast.setGravity(Gravity.BOTTOM, 0, 0);  
//                toast.show();  
                
                if (position == 0){
    				int nRet = 0;
    				
    				startActivityForResult(new Intent("com.qrsphere.scan.CaptureActivity"),nRet);
               }
                else if (position == 3){
	                
                	startActivity(new Intent(MainViewActivity.this,SettingsActivity.class));
                }
                else if (position == 5){
                	
                	startActivity(new Intent(MainViewActivity.this,AboutActivity.class));
                }
                else if (isOnline){
                
	                if (position == 1){
	                	 startActivity(new Intent("com.qrsphere.HistoryActivity"));
	                }else if (position == 2){
	                	 startActivity(new Intent("com.qrsphere.FavoriteActivity"));
	                }

	                else if (position == 4){
	                	startActivity(new Intent("com.qrsphere.DiscoveryActivity"));
	                }

                }
                else{
                	MainViewActivity.this.finish();
                	startActivity(new Intent("com.qrsphere.login.LoginActivity"));
                	Toast toast=Toast.makeText(getApplicationContext(),
                    		"Sorry, to use this function, you have to login.", Toast.LENGTH_SHORT);  
                    toast.setGravity(Gravity.BOTTOM, 0, 0);  
                    toast.show();  
                }
            }  
              
        });  
        
        bt_ad = (Button) findViewById(R.id.btn_ad_bar);
        bt_ad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
	    		StartBrowser sb = new StartBrowser("www.qrsphere.com",context);
	    		sb.startBrowse();
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	public void sentScanDetailToServer(Qrcode q){
		
		if (q!=null){
			this.pd = sdqGlobal.sentToServer(this, q);
		}

	}
	protected void setPopupMenuAction(Qrcode q){
		qrcodeGlobal = q;
		android.content.DialogInterface.OnClickListener onselect = new android.content.DialogInterface.OnClickListener() {  
			    @Override  
			    public void onClick(DialogInterface dialog, int which) {  
			         
			        switch (which) {  
			        case 0:  
			            Toast.makeText(MainViewActivity.this, "0",Toast.LENGTH_SHORT).show(); 
			            showQPage();
			            break;  
			        case 1:  
			            Toast.makeText(MainViewActivity.this, "1",Toast.LENGTH_SHORT).show();
			            sendOutQrcode();
			            break;  
			        case 2:  
			            Toast.makeText(MainViewActivity.this, "2",Toast.LENGTH_SHORT).show(); 
			            showScanDetails();
			            break;  
			        case 3:  
			            Toast.makeText(MainViewActivity.this, "3",Toast.LENGTH_SHORT).show();
			            feedback();
			            break;  
			        case 4:  
			            Toast.makeText(MainViewActivity.this, "4",Toast.LENGTH_SHORT).show();
			            addToFavorite();
			            break; 
			        default:
			        	break;
			            	
			    }  
			        isFromPopupMenu = true;   
			    }

			   };  
		if (q!=null){
	
			showPopup(q.getQrcodeJSONData().getUrl(),onselect);
	
		}
	}
	private void addToFavorite() {
		
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
		cbGlobal = af.show(this, qrcodeGlobal.getQrcodeJSONData().getUrl(),
				click, cancel);
		
	}
	private void sendDataToFavorList() {
		
		if (cbGlobal!=null){
			String cata = cbGlobal.getText();
			qrcodeGlobal.addCatalogue(cata);
	    	Qrcode qc = qrcodeGlobal;
	    	if (qc!=null){
	    		this.pd =atpGlobal.sentToServer(this, qc);
	    	}
		}
	}

	private void feedback() {
		
    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null){
				Intent intent = new Intent(new Intent(MainViewActivity.this,FeedbackActivity.class));
				Bundle b = new Bundle();
				b.putString("rawdata", qc.getRawdata());
				intent.putExtras(b);
				startActivity(intent);
    	}
	}

	private void showScanDetails() {
		
    	if (qrcodeGlobal!=null){

    		Intent intent = new Intent(MainViewActivity.this, ScanDetailActivity.class);
    		Bundle b = new Bundle();
    		b.putString("Qrcode",qrcodeGlobal.getRawdata());
    		intent.putExtras(b);
    		startActivity(intent);
    	}
	}

	private void sendOutQrcode() {
		
		
    	Qrcode qc = qrcodeGlobal;
    	if (qc!=null){
				Intent intent = new Intent(new Intent(MainViewActivity.this,ShareActivity.class));
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

	private void showQPage() {
		
		
		this.pd = qpGlobal.sentToServer(context, qrcodeGlobal);
		 MyLog.i("Dialog","ServerProcessDialog.sentToServer(this, handler, qrcodeGlobal, pd, 22, ");
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode ==0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("Url");

                Toast toast=Toast.makeText(getApplicationContext(),
                		"you scan: "+contents, Toast.LENGTH_SHORT); 
                toast.setGravity(Gravity.BOTTOM, 0, 0);  
                toast.show(); 

                //TODO be aware of the risk that reading the previous q
				Qrcode qc = new Qrcode(contents, this);
				if(isOnline){
					sentScanDetailToServer(qc);
				}
				else{
					
				}
				setPopupMenuAction(qc);


				
			} else if (resultCode == RESULT_CANCELED) {
			// Handle cancel
				System.out.println(" scan error!");
		}
		
	}
	}
	
	
	@Override
	protected void onResume() {
		
		super.onResume();

		if(pd!=null){
    		pd.dismiss();
    		MyLog.i("dialog","    		pd.dismiss();");
		}
		if (isFromPopupMenu){
			setPopupMenuAction(qrcodeGlobal);
			isFromPopupMenu = false;
		}
			
			
	}
	


	
	protected void showPopup(String str,android.content.DialogInterface.OnClickListener oc) {  
		
		String[] choicesOnline={this.getString(R.string.qpage) ,
				this.getString(R.string.send_out),
				this.getString(R.string.scan_detail),
				this.getString(R.string.feedback),
				this.getString(R.string.add_to_favor)};  
		String[] choicesOffline={this.getString(R.string.qpage) ,
				this.getString(R.string.send_out),
				this.getString(R.string.scan_detail)};  
		String[] choices = isOnline?choicesOnline:choicesOffline;
		
		AlertDialog dialog = new AlertDialog.Builder(MainViewActivity.this)  
            .setIcon(R.drawable.qrcode_icon)  
            .setTitle(str)  
            .setItems(choices, (android.content.DialogInterface.OnClickListener) oc).create();  
		dialog.show();  

    }
	

	


}
