package com.qrsphere;



import com.qrsphere.database.QrcodeDataOperator;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;



@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	protected QrcodeDataOperator qdo;
    protected QrcodeDataOperator getQdo() {
		return qdo;
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_main); 
      
        
       
        Init();
 
    }  
    private void Init()
    {
    	qdo = new QrcodeDataOperator(this);
        TabHost tabHost=getTabHost();  
        
        TabView view = null;  
          
          
        view = new TabView(this, R.drawable.history_icon, R.drawable.history_icon,"History");   
        //view.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.backgn));  
              
        TabSpec historySpec=tabHost.newTabSpec("History");  
        historySpec.setIndicator(view);  
        Intent historyIntent = new Intent(this, HistoryActivity.class);  
        historySpec.setContent(historyIntent);  
          
        view = new TabView(this, R.drawable.gear_icon, R.drawable.gear_icon,"More");  
        //view.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.backgn));  
          
        TabSpec moreSpec=tabHost.newTabSpec("More");  
        moreSpec.setIndicator(view);  
        Intent moreIntent = new Intent(this,MoreActivity.class);  
        moreSpec.setContent(moreIntent);  
          
          
        view = new TabView(this, R.drawable.fav_icon, R.drawable.fav_icon,"Favorite");    
        //view.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.backgn));  
          
        TabSpec favoriteSpec = tabHost.newTabSpec("Favorite");  
        favoriteSpec.setIndicator(view);  
        Intent favoriteIntent = new Intent(this, FavoriteActivity.class);  
        favoriteSpec.setContent(favoriteIntent); 
        
        view = new TabView(this, R.drawable.scan_icon, R.drawable.scan_icon,"Scan");    
        //view.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.backgn));  
          
        TabSpec scanSpec = tabHost.newTabSpec("Scan");  
        scanSpec.setIndicator(view);  
        Intent scanIntent = new Intent(this, ScanActivity.class);  
        scanSpec.setContent(scanIntent);
          
  
        tabHost.addTab(scanSpec);           
        tabHost.addTab(historySpec);  
        tabHost.addTab(favoriteSpec);  
        tabHost.addTab(moreSpec);
       
        tabHost.setCurrentTabByTag("Scan"); 
        //tabHost.setCurrentTab(0); 
    }
	public void setCurrentTab(int index){
		TabHost tabHost= getTabHost();
		if (index< tabHost.getTabWidget().getTabCount())
			tabHost.setCurrentTab(index);
		else
			;//tabHost.setCurrentTab(0);
	}

}
 
