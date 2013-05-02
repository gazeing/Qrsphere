package com.qrsphere.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.qrsphere.R;
import com.qrsphere.database.QrcodeDataOperator;
import com.qrsphere.network.SuccessCode;
import com.qrsphere.widget.MyLog;
import com.qrsphere.widget.ScanDetail;

@SuppressLint("HandlerLeak")
public class SplashScreen extends Activity {

	protected boolean _active = true;
    protected int _splashTime = 3000;//TODO: consider its length when published
    
   // TextView tv;
    String strIntent;
    boolean isLoginOK = false;
    String info;
    ProgressBar pd;
    LoginProcess lp;
    private Handler handler = new Handler() {
 	   
		public void handleMessage(Message msg) {
	      
	    	if(pd!=null){
	    		pd.setVisibility(View.GONE);
	    		
	    		//MyLog.i("Dialog","pd.dismiss(); pd.getProgress() = "+pd.getProgress());

	    	}
	    	isLoginOK = judgeAccountInfo(info);
	        if (msg.what == SuccessCode.LOGIN_SUCCESS) {
	        	


	        } else if (msg.what == SuccessCode.ERROR) {

	        } else {
	            LoginActivity.showNetworkAlert(SplashScreen.this);
	        }
            finish();
            // start mainActivity
			Intent intent = new Intent(new Intent(getIntentStr()));
			Bundle b = new Bundle();
			b.putBoolean("IsOnline", isLoginOK);
			b.putString("username", getUsernameFromInfo(info));
			intent.putExtras(b);

			startActivity(intent);
	    }
		
	};
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash);

        pd = (ProgressBar) findViewById(R.id.progressBar1);
        lp = new LoginProcess(SplashScreen.this, handler);
    	final QrcodeDataOperator qdo = new QrcodeDataOperator(this);
    	String mac = new com.qrsphere.userinfo.CollectPhoneInformation(getApplication()).getMacAddress();
    	info = qdo.withdrawUserInfo(mac);


                try {
                	pd.setVisibility(View.VISIBLE);
                    int waited = 0;
                    while(_active && (waited < _splashTime)) {
                        Thread.sleep(100);
                        if(_active) {
                            waited += 100;
                        }
                    
                	
               }
                    JSONObject json = ScanDetail.buildUserInfo(SplashScreen.this);
                    JSONObject jsonInfo = new JSONObject(info);
                    json.put("Name", jsonInfo.get("Name"));
                    json.put("Account", jsonInfo.get("Account"));
                    json.put("Password", jsonInfo.get("Password"));
                   //isLoginOK = judgeAccountInfo(info);
                    lp.Login(json.toString(),false);
                   
                	   
                } catch(Exception e) {
                    
                	MyLog.i(e.getMessage());
                } 
  }
 
  
  @Override
  public boolean onTouchEvent(MotionEvent event) {
      if (event.getAction() == MotionEvent.ACTION_DOWN) {
          _active = false;
      }
      return true;
  }
  
  public String getIntentStr(){
	  
  	if(!isLoginOK)
		strIntent =  "com.qrsphere.login.LoginActivity";
	else
		strIntent = "com.qrsphere.MainViewActivity";
  	
  	return strIntent;
  }
  
@SuppressWarnings("unused")
public static String getUsernameFromInfo(String jsonStr){
	
	 try {

			if (jsonStr.length() == 0)
				return null;
			else{
				JSONObject json = new JSONObject(jsonStr);
				if (json!=null){
					String name = json.getString("Name");
					return name;
					}
				else
						return null;
			}
		} catch (JSONException e) {
			
			MyLog.i(e.getMessage());
			return null;
		}
}
  @SuppressWarnings("unused")
public boolean judgeAccountInfo(String jsonStr){
	  try {

		if (jsonStr.length() == 0)
			return false;
		else{
			JSONObject json = new JSONObject(jsonStr);
			if (json!=null){
				String password = json.getString("Password");
				if (password.length()>0){
					//LoginProcess lp = new LoginProcess();
					if (lp.judgeLoginResponse())
						return true;
					else{
						
						return false;
					}
				}
				else{
					
					return false;
				}
			}
			else
				return false;
		}
	} catch (JSONException e) {
		
		MyLog.i(e.getMessage());
	}
	  
	
	  return false;
  }
 
  
}

