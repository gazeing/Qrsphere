package com.qrsphere.login;

import org.json.JSONException;
import org.json.JSONObject;



import com.qrsphere.MainViewActivity;
import com.qrsphere.R;
import com.qrsphere.database.QrcodeDataOperator;
import com.qrsphere.network.SuccessCode;
import com.qrsphere.widget.ScanDetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity {
	
	Button btLater;
	Button btLogin;
	EditText etName,etAccount, etPassword;
	boolean isLoginOK = false;
	CheckBox checkbox;
	
	String nameGlobal = "";
	
	private ProgressDialog pd;
	Thread thread = null;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
	    public void handleMessage(Message msg) {
	        pd.dismiss();
	        if (msg.what == 11) {
				if (isLoginOK){
					LoginActivity.this.finish();
					Intent intent = new Intent(LoginActivity.this,MainViewActivity.class);
					Bundle b = new Bundle();
					b.putBoolean("IsOnline", true);
					String name = etName.getText().toString();
					b.putString("username", name);
					intent.putExtras(b);
					startActivity(intent);
				}
				else{
					btLogin.setClickable(true);
					Toast.makeText(getBaseContext(), "Account dosen't exist or password is incorrect.", Toast.LENGTH_SHORT).show();
				}
	        	
	        } else if (msg.what == SuccessCode.ERROR) {
	        	btLogin.setClickable(true);
	        	Toast.makeText(getBaseContext(), "Network error!", Toast.LENGTH_SHORT).show();
	        } else {
	        	
	            showNetworkAlert(LoginActivity.this);
	        }
	    }
	};
	
	
	@Override
	protected void onResume() {
		btLogin.setClickable(true);
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		

		checkbox = (CheckBox) findViewById(R.id.checkBox1);
		etName = (EditText) findViewById(R.id.nameEdit);
		etAccount = (EditText) findViewById(R.id.accountEdit);
		etPassword= (EditText) findViewById(R.id.passwordEdit);
		
		QrcodeDataOperator qdo = new QrcodeDataOperator(this);
		String macAddress = new com.qrsphere.userinfo
							.CollectPhoneInformation(getApplication()).getMacAddress();
		String info = qdo.withdrawUserInfo(macAddress);
		if (info != null){
			if (info.length() > 0){
				JSONObject json;
				try {
					json = new JSONObject(info);
					if (json!=null){
					String name = json.getString("Name");
					if (name.length() > 0){
						etName.setText(name);
						nameGlobal = name;
					}
					String account = json.getString("Account");
					if (account.length() > 0)
						etAccount.setText(account);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		btLater = (Button) this.findViewById(R.id.btn_later);
		btLater.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//startActivity(new Intent("com.example.clienttest.login.RegisterActivity"));
				LoginAuth.setAuth(null);
				
				Intent intent = new Intent(LoginActivity.this,MainViewActivity.class);
				Bundle b = new Bundle();
				b.putBoolean("IsOnline", false);
				
				b.putString("username", "Offline");
				intent.putExtras(b);
				startActivity(intent);
				LoginActivity.this.finish();
				
			}
		});
		

		
		
		btLogin = (Button) this.findViewById(R.id.btn_login);
		btLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			try {	
				String name = etName.getText().toString();
				String account = etAccount.getText().toString();
				String password = etPassword.getText().toString();
				
				JSONObject json = new JSONObject();  
                json.put("Name", name);  
                json.put("Account", account);  
                json.put("Password", password); 
                
          

				
			    if (isOnline(LoginActivity.this)) {
			    	btLogin.setClickable(false);//avoid user click twice or more
			        pd = ProgressDialog.show(LoginActivity.this, "", "Login to server...", true,
			                false);

			                    LoginProcess lp = new LoginProcess();
			    				isLoginOK = lp.Login(json,ScanDetail.buildUserInfo(LoginActivity.this),handler);
			    				Log.i("LoginPostResult",isLoginOK+"");
			    				
			    				checkLoginTickBox(json);
			    				

			    } else {
			        showNetworkAlert(LoginActivity.this);
			    }

			}
			catch(Exception e)
			{
				Log.i("LoginPost",e.toString());
			}
				
			}
		});
	}

	public static Boolean isOnline(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	public static void showNetworkAlert(Context context) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder.setMessage("Please check your internet connection.")
	            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    dialog.dismiss();
	                }
	            });

	    builder.setTitle("Connection Problem");
	    builder.show();

	}

	protected boolean checkLoginTickBox(JSONObject loginInfo){
		QrcodeDataOperator qdo = new QrcodeDataOperator(this);
		String mac = new com.qrsphere.userinfo.CollectPhoneInformation(getApplication()).getMacAddress();
		if (checkbox.isChecked()){
			//QrcodeDataOperator qdo = new QrcodeDataOperator(this);
			
			qdo.storeUserInfo(mac,loginInfo.toString());
		}
		else{
			try {
				String name = loginInfo.getString("Name");
				String account = loginInfo.getString("Account");
				
				JSONObject json = new JSONObject();  
                json.put("Name", name);  
                json.put("Account", account);
                json.put("Password", "");
    			//QrcodeDataOperator qdo = new QrcodeDataOperator(this);
    	    	qdo.storeUserInfo(mac,json.toString());
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return true;
	}

}
