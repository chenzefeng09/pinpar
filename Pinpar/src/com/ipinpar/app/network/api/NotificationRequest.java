package com.ipinpar.app.network.api;

import java.net.URLEncoder;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class NotificationRequest extends BaseJsonRequest{
	private static final String PROTOCOL ="20005";

	public NotificationRequest(int uid, int pageNum,int pageCount,
			Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&c=%s&pageNum=%s&pageCount=%s",
				PROTOCOL,
				uid,
				MD5Util.MD5(PROTOCOL+uid+"pinpa"),
				pageNum,
				pageCount
				), null, listener);
		// TODO Auto-generated constructor stub
	}
	
	

}
