package com.map;

import java.util.List;

import com.camera.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.session.Sessions;


import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class GoogleMaps extends MapActivity {
	
	private int longitude;
	private int latitude;
	private String locationName;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
       // latitude =  Integer.parseInt(getIntent().getStringExtra("latitude"));
        //longitude = Integer.parseInt(getIntent().getStringExtra("longitude"));
        //locationName = getIntent().getExtras().get("search_item").toString();
        
        latitude = (int)(((Sessions)getApplication()).getLatitude()*1e6);
        longitude = (int)(((Sessions)getApplication()).getLongitude()*1e6);
        locationName = ((Sessions)getApplication()).getSearch_item();
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        MapController mapController = mapView.getController();
        mapController.setZoom(14); 
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.location);
        HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
        
        GeoPoint point = new GeoPoint(latitude,longitude);
        OverlayItem overlayitem = new OverlayItem(point, locationName, locationName);
        
        mapController.setCenter(point);
        
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}