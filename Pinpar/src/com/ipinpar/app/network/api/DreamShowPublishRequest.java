package com.ipinpar.app.network.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class DreamShowPublishRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="30017";

	public DreamShowPublishRequest(int uid, String title,String detail,String img,
			 Listener<JSONObject> listener) throws UnsupportedEncodingException {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s&e=%s",
				PROTOCOL,
				uid,
				URLEncoder.encode(URLEncoder.encode(title, "utf-8"), "utf-8"),
				URLEncoder.encode(URLEncoder.encode(detail, "utf-8"), "utf-8"),
				img,
				MD5Util.MD5(PROTOCOL+uid+"pinpa")
				), null, listener);
	}

}
