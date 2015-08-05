package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;

public class ActivityListRequest extends BaseJsonRequest{
	private static final String PROTOCOL ="30001";
	
	public ActivityListRequest(String status,String pageNum,String pageCount,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s&pageNum=%s&pageCount=%s",
						PROTOCOL,
						status,
						pageNum,
						pageCount
						),
				null, listener);
	}

	public ActivityListRequest(String status,String maxid,String pageNum,String pageCount,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s&c=%s&pageNum=%s&pageCount=%s",
						PROTOCOL,
						status,
						maxid,
						pageNum,
						pageCount
						),
				null, listener);
	}
	
	public ActivityListRequest(String uid,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s",
						PROTOCOL,
						uid
						),
				null, listener);
	}

}
