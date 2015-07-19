package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class MyActivityListRequest extends BaseJsonRequest{
	private static final String PROTOCOL ="30014";
	

	public MyActivityListRequest(String uid,String type,String pageNum,String pageCount,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&pageNum=%s&pageCount=%s",
						PROTOCOL,
						uid,
						type,
						MD5Util.MD5(PROTOCOL+uid+type+"pinpa"),
						pageNum,
						pageCount
						),
				null, listener);
	}
	
	public MyActivityListRequest(String uid,String type,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s",
						PROTOCOL,
						uid,
						type,
						MD5Util.MD5(PROTOCOL+uid+type+"pinpa")
						),
				null, listener);
	}

}
