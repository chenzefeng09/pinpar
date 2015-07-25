package com.ipinpar.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
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
	private TextView tvShopName;
	private String shopName;
	private LatLng latlng;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_marker_map);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
		aMap = mapView.getMap();
		tvShopName = (TextView) findViewById(R.id.tv_shopname_title);
		
		
//		aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
		latitude = getIntent().getStringExtra("latitude");
		longitude = getIntent().getStringExtra("longitude");
		shopName = getIntent().getStringExtra("shopname");
		
		dLatitude = Double.parseDouble(latitude);
		dLongitude = Double.parseDouble(longitude);
		
		latlng = new LatLng(dLatitude, dLongitude);
		
		aMap.moveCamera(CameraUpdateFactory.newCameraPosition( new CameraPosition(latlng,18,0,0)));
		
		markerOption = new MarkerOptions();
		markerOption.position(latlng);
		
		aMap.addMarker(markerOption);
		tvShopName.setText(shopName);
		
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
