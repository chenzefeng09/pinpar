package com.ipinpar.app.network.api;

import org.json.JSONObject;

import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class BaseJsonRequest extends JsonObjectRequest{

	public BaseJsonRequest(int method, String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, "http://api.ipinpar.com/pinpaV2/"+url, jsonRequest, listener, errorListener);
		// TODO Auto-generated constructor stub
	}
	
	public BaseJsonRequest(int method, String url, JSONObject jsonRequest,
			Listener<JSONObject> listener) {
		this(method, url, jsonRequest, listener, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				if (error instanceof TimeoutError) {
//					Toast
				}
				else if (error instanceof NetworkError) {
					
				}
				else if (error instanceof NoConnectionError) {
					
				}
				else if (error instanceof ParseError) {
					
				}
				else if (error instanceof ServerError) {
					
				}
			}
		});
	}
	
}
