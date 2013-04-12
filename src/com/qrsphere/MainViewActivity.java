package com.qrsphere;



import java.util.ArrayList;
import java.util.HashMap;

import com.qrsphere.database.Qrcode;
import com.qrsphere.login.LoginActivity;
import com.qrsphere.widget.MyLog;
import com.qrsphere.widget.ScanDetail;
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
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MainViewActivity extends Activity{

    private int[] mImages={  
            R.drawable.scan_icon,  
            R.drawable.history_icon,  
            R.drawable.fav_icon,  
            R.drawable.gear_icon, 
            


  
    }; 
    
    private String[] mTexts ={
    		"Scan",
    		"History",
    		"Favorite",
    		"More",
    		

    		
    };
    
	private ProgressDialog pd;
    GridView mGridView;
    Button bt_ad;
    Context context;
    boolean isOnline = false;
    
    boolean isFromPopupMenu = false;
    Qrcode qrcodeGlobal =  null;
    
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
	   // @SuppressWarnings("deprecation")
		public void handleMessage(Message msg) {
	        //pd.dismiss();
	    	//removeDialog(pd.);
	    	if(pd!=null&&pd.isShowing()){
	    		pd.dismiss();
	    		//removeDialog(888);
	    		MyLog.i("Dialog","pd.dismiss(); pd.getProgress() = "+pd.getProgress());

	    	}
	        if (msg.what == 11) {
//				if (isLoginOK){
//					LoginActivity.this.finish();
//					startActivity(new Intent("com.qrsphere.MainViewActivity"));
//				}
//				else{
//					Toast.makeText(getBaseContext(), "Account dosen't exist or password is incorrect.", Toast.LENGTH_SHORT).show();
//				}
//	        	if (isOnline)
//	        		startActivity(new Intent("com.qrsphere.HistoryActivity"));
	        } else if (msg.what == 1) {

	        } else if (msg.what == 22) {
	        	if (qrcodeGlobal!=null){
	        		Intent intent= new Intent("com.qrsphere.QPageActivity");
	        		
	        		startActivity(intent);
	        		MyLog.i("Dialog","startActivity(intent);");
	        	}

	        }else {
	            LoginActivity.showNetworkAlert(MainViewActivity.this);
	        }
	    }
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.mainview); 
      
        
       
        Init();
	}
	
	private void Init(){
		context = this;
		
		Bundle b= getIntent().getExtras();
		if (b!=null)
			isOnline = b.getBoolean("IsOnline"); 
		
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
                new String[] { "ItemImage", "ItemText" }, new int[] {  
                        R.id.ItemImage, R.id.ItemText });  
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
                else if (isOnline){
                
	                if (position == 1){
	                	 startActivity(new Intent("com.qrsphere.HistoryActivity"));
	                }else if (position == 2){
	                	 startActivity(new Intent("com.qrsphere.FavoriteActivity"));
	                }
	                else if (position == 3){
	                
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
				// TODO Auto-generated method stub
	    		StartBrowser sb = new StartBrowser("www.qrsphere.com",context);
	    		sb.startBrowse();
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	public void sentScanDetailToServer(Qrcode q){
		
		 if (LoginActivity.isOnline(this)) {
		        pd = ProgressDialog.show(this, "", "Sending to server...", true,
		                false);
		        
		        MyLog.i("Dialog","pd = ProgressDialog.show(this, \"\", \"Sending to server...\", true,false);");
		        new Thread(new Runnable() {

		            @Override
		            public void run() {
		                try {

		    				
		    				Thread.sleep(5000);
		    				
		    				//strResponse = res;

		                    handler.sendEmptyMessage(11);
		                } catch (Exception e) {
		                    System.out.println("In Cache :");
		                    handler.sendEmptyMessage(1);
		                }
		            }
		        }).start();
		    } else {
		    	LoginActivity.showNetworkAlert(this);
		    }
			
//			if (res.compareTo("No result!")!=0){
//				finish();
//				startActivity(new Intent("com.example.clienttest.MainActivity"));
//			}
//			else{
//				Toast.makeText(getBaseContext(), "Wrong password.", Toast.LENGTH_SHORT).show();
//			}

//			finishActivity(getParent());
			//getParent().finish();
		
	}
	protected void setPopupMenuAction(Qrcode q){
		qrcodeGlobal = q;
		android.content.DialogInterface.OnClickListener onselect = new android.content.DialogInterface.OnClickListener() {  
			    @Override  
			    public void onClick(DialogInterface dialog, int which) {  
			        // TODO Auto-generated method stub  
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
		// TODO Auto-generated method stub
		
	}

	private void feedback() {
		// TODO Auto-generated method stub
		
	}

	private void showScanDetails() {
		// TODO Auto-generated method stub
    	if (qrcodeGlobal!=null){
    		DialogInterface.OnClickListener click =new android.content.DialogInterface.OnClickListener() {
   		     public void onClick(DialogInterface dialog, int arg1) {
   		      // go back to Main menu
   				setPopupMenuAction(qrcodeGlobal);
   				isFromPopupMenu = false;
   		     }
    		};
    		
    		DialogInterface.OnCancelListener cancel =new DialogInterface.OnCancelListener(){
     	  		    public void onCancel(DialogInterface dialog) {
     	  		     // go back to Main menu  
     	  				setPopupMenuAction(qrcodeGlobal);
     	  				isFromPopupMenu = false;
     	  		    }};
    		
    		ScanDetail.ScanDetailDialog(qrcodeGlobal,MainViewActivity.this,click,cancel);

    	}
	}

	private void sendOutQrcode() {
		// TODO Auto-generated method stub
		
	}

	private void showQPage() {
		// TODO Auto-generated method stub
		sentToServer(qrcodeGlobal, 22, "Generating Qpage...");
		 MyLog.i("Dialog","ServerProcessDialog.sentToServer(this, handler, qrcodeGlobal, pd, 22, ");
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode ==0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("Url");
//				Bitmap barcode = intent.getParcelableExtra("Bmp");
//				tv.setText(contents);
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

//				if (barcode != null)
//				{
//				        int width = barcode.getWidth();
//				        int height = barcode.getHeight();
////				        int newWidth = 200;
////				        int newHeight = 200;
////
////				        // calculate the scale - in this case = 0.4f
////				        float scaleWidth = ((float) newWidth) / width;
////				        float scaleHeight = ((float) newHeight) / height;
//
//				        // createa matrix for the manipulation
//				        Matrix matrix = new Matrix();
//				        // resize the bit map
//				        matrix.postScale(width, height);
//				        // rotate the Bitmap
//				        matrix.postRotate(180);
//				        //mOptions.inSampleSize = 2;
//				        // recreate the new Bitmap
//				        Bitmap resizedBitmap = Bitmap.createBitmap(barcode, 0, 0,
//				                          width/2, height/2, matrix, true);
//
//				        // make a Drawable from Bitmap to allow to set the BitMap
//				        // to the ImageView, ImageButton or what ever
//				        //@SuppressWarnings("deprecation")
//						//BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
//				        im.setImageBitmap(resizedBitmap);
//				}
			
//				QrcodeDataOperator qdo = ((MainActivity) this.getParent()).getQdo();
//				qdo.insert(new Qrcode(contents));
				
			} else if (resultCode == RESULT_CANCELED) {
			// Handle cancel
				System.out.println(" scan error!");
		}
		//((MainActivity) this.getParent()).getTabHost().setCurrentTabByTag("History");
	}
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//    	if(pd!=null&&pd.isShowing())
		if(pd!=null){
    		pd.dismiss();
    		MyLog.i("dialog","    		pd.dismiss();");
		}
		if (isFromPopupMenu){
			setPopupMenuAction(qrcodeGlobal);
			isFromPopupMenu = false;
		}
			
			
	}
	
	public void sentToServer (Qrcode qc,
			final int succussCode,
			String processText){

			if (LoginActivity.isOnline(context)) {
				pd = ProgressDialog.show(context, "", processText, true,
				false);
				//pd.setProgress(888);
				MyLog.i("Dialog"," pd = ProgressDialog.show(context,");
				new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
					
						Thread.sleep(1500);
						//TODO add server communication code here
					
					    handler.sendEmptyMessage(succussCode);
					    MyLog.i("Dialog","handler.sendEmptyMessage(succussCode);");
					} catch (Exception e) {
					    System.out.println("In Cache :");
					    handler.sendEmptyMessage(1);
					    MyLog.i("Dialog","handler.sendEmptyMessage(1);");
					}
				}
			}).start();
			// pd.dismiss();
			} else {
				
			}
}

	@SuppressLint("NewApi")
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
            .setIcon(R.drawable.scan_icon)  
            .setTitle(str)  
            .setItems(choices, (android.content.DialogInterface.OnClickListener) oc).create();  
		dialog.show();  
//		FrameLayout titil_bar= (FrameLayout) findViewById(R.id.title_bar);
//        PopupMenu popup = new PopupMenu(this, titil_bar);  
//        MenuInflater inflater = popup.getMenuInflater(); 
//        int id = isOnline?R.menu.scan_online:R.menu.scan_offline;
//        inflater.inflate(id, popup.getMenu());
//        popup.
//        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//        	 
//            @Override
//            //the logic to deal with the menu selection
//            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                switch(item.getItemId()){
//                case R.id.go_to_web:
//                	showQPage();
//                	break;
//                case R.id.add_to_favor:
//                	addToFavorite();
//                	break;
//                case R.id.scan_detail:
//
//                	showScanDetails();
//                	break;
//                case R.id.send_out:
//                	;
//                	break;
//                	
//                case R.id.feedback:
//                	;
//                	break;
//
//                default:
//                	break;
//                }
//                return true;
//            }
//
//
//
//
//        });
//        popup.show();  
		
//	    LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
//	      									.getSystemService(LAYOUT_INFLATER_SERVICE);  
//	    View popupView = layoutInflater.inflate(R.layout.scanpopup, null);  
//	    final PopupWindow popupWindow = new PopupWindow(popupView, 
//	               									LayoutParams.WRAP_CONTENT,  
//	               									LayoutParams.WRAP_CONTENT);  
//	             
//	             Button btnDismiss = (Button)popupView.findViewById(R.id.scbutton1);
//	             btnDismiss.setOnClickListener(new Button.OnClickListener(){
//
//	     @Override
//	     public void onClick(View v) {
//	      // TODO Auto-generated method stub
//	      popupWindow.dismiss();
//	     }});
//	               
//	    popupWindow.showAsDropDown(v, -50, -30);
    }
	

	


}
