package com.ipinpar.app.network.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class AddFriendRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="20002";


	public AddFriendRequest(int uid, int fuid,String note,String bkname,
			 Listener<JSONObject> listener) throws UnsupportedEncodingException {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s&e=%s",
				PROTOCOL,
				uid,
				fuid,
				URLEncoder.encode(URLEncoder.encode(note, "utf-8"), "utf-8"),
				URLEncoder.encode(URLEncoder.encode(bkname, "utf-8"), "utf-8"),
				MD5Util.MD5(PROTOCOL+uid+fuid+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
