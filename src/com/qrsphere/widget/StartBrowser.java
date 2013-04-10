package com.qrsphere.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class StartBrowser {

	private String url;
	Context context;
	
	
    public void setUrl(String url) {
		this.url = url;
	}


	public StartBrowser(String url, Context context) {
		super();
		this.url = url;
		this.context = context;
	}


	public void startBrowse()
    {


    	if (!url.startsWith("http://") && !url.startsWith("https://"))
    	   url = "http://" + url;
    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    	context.startActivity(browserIntent);
    }
}
