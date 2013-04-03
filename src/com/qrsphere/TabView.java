package com.qrsphere;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
class TabView extends LinearLayout {  
    ImageView imageView ; 
    


	@SuppressWarnings("deprecation")
	public TabView(Context c, int drawable, int drawableselec,String tabtag) {  
	    super(c);  
	    imageView = new ImageView(c);  
	    StateListDrawable listDrawable = new StateListDrawable();  
	    listDrawable.addState(SELECTED_STATE_SET, this.getResources()  
	            .getDrawable(drawableselec));  
	    listDrawable.addState(ENABLED_STATE_SET, this.getResources()  
	            .getDrawable(drawable));  
	    imageView.setImageDrawable(listDrawable);  
	    imageView.setBackgroundColor(Color.TRANSPARENT);
	    //imageView.setGravity(Gravity.BOTTOM);
	    //this.setBackgroundColor(Color.GRAY);
	    
	    StateListDrawable listDrawableView = new StateListDrawable();  
	    listDrawableView.addState(SELECTED_STATE_SET, this.getResources()  
	            .getDrawable(R.drawable.background_orange_bond));  
	    listDrawableView.addState(ENABLED_STATE_SET, this.getResources()  
	            .getDrawable(R.drawable.background_black));  
	    this.setBackgroundDrawable(listDrawableView);
	    
	    this.setOrientation(LinearLayout.VERTICAL); 
	    setGravity(Gravity.BOTTOM);  
	    addView(imageView); 
	    TextView tv = new TextView(c);
	    tv.setGravity(Gravity.CENTER_HORIZONTAL);
	    tv.setText(tabtag);
	    tv.setTextSize(14);
	    tv.setTextColor(Color.WHITE);
	    addView(tv);
	    }  

	 
}