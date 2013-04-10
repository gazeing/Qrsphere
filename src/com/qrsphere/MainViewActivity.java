package com.qrsphere;



import java.util.ArrayList;
import java.util.HashMap;

import com.qrsphere.database.Qrcode;
import com.qrsphere.login.LoginActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
    
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
	    public void handleMessage(Message msg) {
	        pd.dismiss();
	        if (msg.what == 11) {
//				if (isLoginOK){
//					LoginActivity.this.finish();
//					startActivity(new Intent("com.qrsphere.MainViewActivity"));
//				}
//				else{
//					Toast.makeText(getBaseContext(), "Account dosen't exist or password is incorrect.", Toast.LENGTH_SHORT).show();
//				}
	        	startActivity(new Intent("com.qrsphere.HistoryActivity"));
	        } else if (msg.what == 0) {

	        } else {
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
                Toast toast=Toast.makeText(getApplicationContext(),
                		"you chose"+(position+1)+"#picture", Toast.LENGTH_SHORT);  
                toast.setGravity(Gravity.BOTTOM, 0, 0);  
                toast.show();  
                
                if (position == 1){
                	 startActivity(new Intent("com.qrsphere.HistoryActivity"));
                }else if (position == 2){
                	 startActivity(new Intent("com.qrsphere.FavoriteActivity"));
                }
                else if (position == 0){
    				int nRet = 0;
    				
    				startActivityForResult(new Intent("com.qrsphere.scan.CaptureActivity"),nRet);
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
		        pd = ProgressDialog.show(this, "", "Send to server...", true,
		                false);
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

				Qrcode q = new Qrcode(contents, this);
				if (q!=null)
					sentScanDetailToServer(q);

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
	


}
