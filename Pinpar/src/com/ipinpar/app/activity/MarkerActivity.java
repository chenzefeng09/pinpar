package com.ipinpar.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;

public class MarkerActivity extends PPBaseActivity{

	private MarkerOptions markerOption;
	private Button markerButton;// 获取屏幕内所有marker的button
	private AMap aMap;
	private MapView mapView;
	private String latitude,longitude;
	private double dLatitude,dLongitude;
	private LatLng latlng;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marker_map);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
		aMap = mapView.getMap();
		
		latitude = getIntent().getStringExtra("latitude");
		longitude = getIntent().getStringExtra("longitude");
		
		dLatitude = Double.parseDouble(latitude);
		dLongitude = Double.parseDouble(longitude);
		
		latlng = new LatLng(dLatitude, dLongitude);
		
		markerOption = new MarkerOptions();
		markerOption.position(latlng);
		
		
		aMap.addMarker(markerOption);
		
	}
	
	
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
	
}
