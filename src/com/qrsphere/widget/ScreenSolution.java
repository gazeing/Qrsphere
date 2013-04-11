package com.qrsphere.widget;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenSolution {

	public static int getDpi(Context context){
		
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int densityDpi = dm.densityDpi;
		
		return densityDpi;
	}
}
