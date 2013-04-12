package com.qrsphere.widget;

import android.util.Log;

public class MyLog {
	static boolean isLogging = true;
	public static void i(String tag,String test){
		if(isLogging){
			Log.i(tag,test);
		}
	}
	public static void i(String test){
		if(isLogging){
			Log.i("Qrsphere",test);
		}
	}

}
