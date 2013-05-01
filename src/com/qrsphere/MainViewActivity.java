package com.qrsphere;



import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.qrsphere.scan.Contents;
import com.qrsphere.scan.Intents;
import com.google.zxing.BarcodeFormat;
import com.qrsphere.database.Qrcode;
import com.qrsphere.database.QrcodeList;
import com.qrsphere.login.LoginActivity;
import com.qrsphere.login.LoginAuth;
import com.qrsphere.network.AddToFavorite;
import com.qrsphere.network.GetHistoryList;
import com.qrsphere.network.QPageProcess;
import com.qrsphere.network.SendDetail;
import com.qrsphere.network.SuccessCode;

import com.qrsphere.widget.AddToFavoriteDialog;
import com.qrsphere.widget.ComboBox;
import com.qrsphere.widget.GroupAdapter;
import com.qrsphere.widget.MyLog;
import com.qrsphere.widget.StartBrowser;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
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
    
    PopupWindow popupWindow;
    private ListView lv_group;
    private View view;
    private List<String> groups;
    boolean isFromPopupMenu = false;//to identify if the page jumping is from popup menu
	// if true, when back to main page, menu will be shown again
    
	private ProgressDialog pd;
    GridView mGridView;
    Button bt_ad;
    Context context;
    boolean isOnline = false;
    

    Qrcode qrcodeGlobal =  null;
    QPageProcess qpGlobal = null;
    SendDetail sdqGlobal = null;
    AddToFavorite atpGlobal= null;
    GetHistoryList ghhGlobal = null;
    
    ComboBox  cbGlobal = null;
    
    int historyIDGlobal = 0;
    
    QrcodeList qrcodeList = null;
    
	public QrcodeList getQrcodeList() {
		return qrcodeList;
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
	   
		public void handleMessage(Message msg) {
	      
	    	if(pd!=null&&pd.isShowing()){
	    		pd.dismiss();
	    		
	    		MyLog.i("Dialog","pd.dismiss(); pd.getProgress() = "+pd.getProgress());

	    	}
	        if (msg.what == SuccessCode.DETAIL_SENT_SUCCESS) {
	        	pd = qrcodeList.sendListRequest();
	        	if (sdqGlobal.getStrJSON() != null){
    					try {
    						JSONObject json = new JSONObject(sdqGlobal.getStrJSON());
    						if (json != null){
    						historyIDGlobal = json.getInt("ResponseContent");

    						}
						} catch (Exception e) {
							MyLog.i(e.getMessage());
						}
	        	}
	        }else if(msg.what == SuccessCode.GET_LIST_SUCCESS){
	        	qrcodeList.update();
	        	setPopupMenuAction();
	        }
	        
	        else if (msg.what == SuccessCode.ERROR) {
	        	Toast.makeText(MainViewActivity.this, "Network error!",Toast.LENGTH_SHORT).show(); 
	        } else if (msg.what == SuccessCode.QPAGE_SUCCESS) {
	        	qpGlobal.startQPage(MainViewActivity.this);
	        	
	        }else if (msg.what == SuccessCode.ADD_TO_FAVORITE_SUCCESS) {
	    		pd = ghhGlobal.postData(new Qrcode(""));
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
		qpGlobal = new QPageProcess(pd,  handler);
		sdqGlobal = new SendDetail(MainViewActivity.this, handler);
		atpGlobal = new AddToFavorite(MainViewActivity.this,handler);
		ghhGlobal = new GetHistoryList(MainViewActivity.this, handler);
		
		qrcodeList = new QrcodeList(this, ghhGlobal, pd);
		
		Bundle b= getIntent().getExtras();
		if (b!=null){
			isOnline = b.getBoolean("IsOnline"); 
			String str = b.getString("username");
			TextView tvTitle = (TextView) findViewById(R.id.tvmaintitle);
			tvTitle.setText(str);
		}
		if (isOnline)
			pd = qrcodeList.sendListRequest();
		
		
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
	                	Intent intent = new Intent(MainViewActivity.this,HistoryActivity.class);
	                	intent.putExtra("ListString",qrcodeList.getJSONArrayString());
	                	startActivity(intent);
	                }else if (position == 2){
	                	Intent intent = new Intent(MainViewActivity.this,FavoriteActivity.class);
	                	intent.putExtra("ListString",qrcodeList.getJSONArrayString());
	                	startActivity(intent);
	                }
	                else if (position == 4){
	                	startActivity(new Intent(MainViewActivity.this,DiscoveryActivity.class));
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
        
        //click on the logo of titlebar will open the browser and go to website.
        bt_ad = (Button) findViewById(R.id.btn_ad_bar);
        bt_ad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
	    		StartBrowser sb = new StartBrowser("www.qrsphere.com",context);
	    		sb.startBrowse();
			}
		});
        
        
        Button bt_portrait = (Button) findViewById(R.id.btn_main_title_right);
        bt_portrait.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showWindow(v);
				
			}
		});
	}
	@SuppressWarnings("deprecation")
	private void showWindow(View parent) {

		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
			view = layoutInflater.inflate(R.layout.group_list, null);
	
			lv_group = (ListView) view.findViewById(R.id.lvGroup);
			// add data
			groups = new ArrayList<String>();
			groups.add("Change Portrait");
			groups.add("logout");
	
	
			GroupAdapter groupAdapter = new GroupAdapter(this, groups);
			lv_group.setAdapter(groupAdapter);
			//create
			popupWindow = new PopupWindow(view,400,400);
		}

		// focus
		popupWindow.setFocusable(true);
		// allow outside touch to dismiss
		popupWindow.setOutsideTouchable(true);

		// 
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// set position
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
		- popupWindow.getWidth() / 2;
		MyLog.i("coder", "xPos:" + xPos);

		popupWindow.showAsDropDown(parent, xPos, 0);

		lv_group.setOnItemClickListener(new OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
	
				if (position ==1){
					LoginAuth.setAuth(null);
					MainViewActivity.this.finish();
					MainViewActivity.this
						.startActivity(new Intent
								(MainViewActivity.this,LoginActivity.class));
				}
					

	
				if (popupWindow != null) {
				popupWindow.dismiss();
				}
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
			
			pd= sdqGlobal.postData(q);
		}

	}
	protected void setPopupMenuAction(){
		//qrcodeGlobal = q;
		android.content.DialogInterface.OnClickListener onselect = new android.content.DialogInterface.OnClickListener() {  
			    @Override  
			    public void onClick(DialogInterface dialog, int which) {  
			         
			        switch (which) {  
			        case 0:  
			           
			        	isFromPopupMenu = true;   
			        	showQPage();
			            break;  
			        case 1:  
			          
			        	isFromPopupMenu = true;   
			        	sendOutQrcode();
			            break;  
			        case 2:  
			         
			        	isFromPopupMenu = true;   
			        	showScanDetails();
			            break;  
			        case 3:  
			          
			        	isFromPopupMenu = true;   
			        	feedback();
			            break;  
			        case 4:  
			         
			            addToFavorite();
			            break; 
			        default:
			        	break;
			            	
			    }  
			        
			    }

			   };  
		if (qrcodeGlobal!=null){
	
			showPopup(qrcodeGlobal.getQrcodeJSONData().getUrl(),onselect);
	
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
			
		AddToFavoriteDialog af = new AddToFavoriteDialog();
		cbGlobal = af.show(this, qrcodeGlobal.getQrcodeJSONData().getUrl(),
				click, cancel,qrcodeList);
		
	}
	private void sendDataToFavorList() {
		
		if (cbGlobal!=null){
			String cata = cbGlobal.getText();
			
	    	Qrcode qc = qrcodeGlobal;
	    	if (qc!=null){
	    		qc.addCatalogue(cata);
	    		qc.addHistoryId(historyIDGlobal);
	    		
	    		this.pd = atpGlobal.postData(qc);
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
					sentScanDetailToServer(qc);
				}
				qrcodeGlobal = qc;


				
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
			setPopupMenuAction();
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
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// when user cancel the window, do not pop it when resume
				isFromPopupMenu = false;
			}
		});
		dialog.show();  

    }
	

	public Object fetch(String address) throws MalformedURLException,
    IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }  

    private Drawable ImageOperations( String url) {
        try {
            InputStream is = (InputStream)fetch(url);
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

	@SuppressWarnings("deprecation")
	void showUserPortrait(String url){
		if (url != null){
			Drawable d = ImageOperations(url);
			Button iv = (Button) findViewById(R.id.btn_main_title_right);
			if(d != null)
				iv.setBackgroundDrawable(d);
		}
		
	}
	


}
