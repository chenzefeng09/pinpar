package com.ipinpar.app.network.api;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class ActivityStatementListRequest extends BaseJsonRequest{
	private static final String PROTOCOL ="30010";
	
	public ActivityStatementListRequest(String uid,String acid,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s",
						PROTOCOL,
						uid,
						acid,
						MD5Util.MD5(PROTOCOL+uid+acid+"pinpa")
						),
				null, listener);
		
		Log.d("OngoingActivityStatementListRequest:","http://api.ipinpar.com/pinpaV2/"
				+String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s",
						PROTOCOL,
						uid,
						acid,
						MD5Util.MD5(PROTOCOL+uid+acid+"pinpa")
						));
	}
	
	public ActivityStatementListRequest(String uid,String acid,
			String pageNum,String pageCount,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s",
						PROTOCOL,
						uid,
						acid,
						MD5Util.MD5(PROTOCOL+uid+acid+"pinpa"),
						pageNum,
						pageCount
						),
				null, listener);
	}

}
