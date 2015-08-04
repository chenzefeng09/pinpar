package com.ipinpar.app.network.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class UpdatePartyExperienceRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="40004";

	public UpdatePartyExperienceRequest(String uid,String content,String imgs,String roleid, Listener<JSONObject> listener) 
			throws UnsupportedEncodingException {
		super(Method.POST,  String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s&e=%s",
				PROTOCOL,
				uid,
				URLEncoder.encode(URLEncoder.encode(content, "utf-8"), "utf-8"),
				imgs,
				roleid,
				MD5Util.MD5(PROTOCOL+uid+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
