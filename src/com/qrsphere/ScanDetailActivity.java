package com.qrsphere;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qrsphere.database.QrcodeJSONData;
import com.qrsphere.userinfo.CollectAddress;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.TextView;

public class ScanDetailActivity extends FragmentActivity {

	private GoogleMap mMap;
	LatLng ll =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.scandetailview);
        init();
        setUpMapIfNeeded();
        
	}
	
    private void init() {
		// TODO Auto-generated method stub
    	
		
		TextView tv=null;
		
		Bundle b = getIntent().getExtras();
		String rawdata = b.getString("Qrcode");
		if(rawdata != null){
			QrcodeJSONData q = new QrcodeJSONData(rawdata);
			if(q != null){
				tv = (TextView) findViewById(R.id.DetailContentText);
				if (tv != null)
					tv.setText(q.getUrl());
				tv = (TextView) findViewById(R.id.DetailWhenText);
				if (tv != null)
					tv.setText(q.getDateTime());
				ll = new LatLng(q.getLatitude(),q.getLongitude());
				
				
				String location = CollectAddress.getLocationInfoString(this,q.getLatitude(),q.getLongitude());
				tv = (TextView) findViewById(R.id.DetailWhereText);
				if (tv != null)
					tv.setText(location);
			}
		}

	}

	private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    
    private void setUpMap() {
        // We will provide our own zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        if (ll == null)
        	ll = new LatLng(-33.87365, 151.20689);
        // Show Sydney
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
    
        mMap.addMarker(new MarkerOptions().position(ll).title("Scan Position"));
    }

	
	
	
}
