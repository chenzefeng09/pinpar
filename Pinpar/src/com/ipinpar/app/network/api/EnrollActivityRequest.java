package com.ipinpar.app.network.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class EnrollActivityRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="30009";


	public EnrollActivityRequest(int acid, int uid,String declaration,int infoid, 
			Listener<JSONObject> listener) throws UnsupportedEncodingException {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s&e=%s",
				PROTOCOL,
				acid,
				uid,
				URLEncoder.encode(URLEncoder.encode(declaration, "utf-8"), "utf-8"),
				infoid,
				MD5Util.MD5(PROTOCOL+acid+uid+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
