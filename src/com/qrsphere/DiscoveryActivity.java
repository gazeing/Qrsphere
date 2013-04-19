package com.qrsphere;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak" })
public class DiscoveryActivity extends Activity {

	WebView wv;
	ProgressDialog pd;
	Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.discovery);
        init();//
       
        handler=new Handler(){
        	public void handleMessage(Message msg)
    	    {//
    	      if (!Thread.currentThread().isInterrupted())
    	      {
    	        switch (msg.what)
    	        {
    	        case 0:
    	        	pd.show();//      	
    	        	break;
    	        case 1:
    	        	pd.hide();//
    	        	break;
    	        }
    	      }
    	      super.handleMessage(msg);
    	    }
        };
        loadurl(wv,"http://www.google.com");
    }
	
	   public void init(){//
	    	wv=(WebView)findViewById(R.id.webview);
	        wv.getSettings().setJavaScriptEnabled(true);//
	        wv.setScrollBarStyle(0);//
	        wv.setWebViewClient(new WebViewClient(){   
	            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
	            	loadurl(view,url);//
	                return true;   
	            }//
	 
	        });
	        wv.setWebChromeClient(new WebChromeClient(){
	        	public void onProgressChanged(WebView view,int progress){//
	             	if(progress==100){
	            		handler.sendEmptyMessage(1);//
	            	}   
	                super.onProgressChanged(view, progress);   
	            }   
	        });
	 
	    	pd=new ProgressDialog(DiscoveryActivity.this);
	        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pd.setMessage("Loading...");
	    }
	   
	   public boolean onKeyDown(int keyCode, KeyEvent event) {//
	        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {   
	            wv.goBack();   
	            return true;   
	        }else if(keyCode == KeyEvent.KEYCODE_BACK){
	        	ConfirmExit();//
	        	return true; 
	        }   
	        return super.onKeyDown(keyCode, event);   
	    }
	    public void ConfirmExit(){//
	    	AlertDialog.Builder ad=new AlertDialog.Builder(DiscoveryActivity.this);
	    	ad.setTitle("quit");
	    	ad.setMessage("quit?");
	    	ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {//
				@Override
				public void onClick(DialogInterface dialog, int i) {
					// TODO Auto-generated method stub
					DiscoveryActivity.this.finish();//
	 
				}
			});
	    	ad.setNegativeButton("No",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int i) {
					//
				}
			});
	    	ad.show();//
	    }
	    public void loadurl(final WebView view,final String url){
	    	new Thread(){
	        	public void run(){
	        		handler.sendEmptyMessage(0);
	        		view.loadUrl(url);//
	        	}
	        }.start();
	    }

	
}
