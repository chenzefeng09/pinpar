package com.ipinpar.app.network.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class RegistRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="10002";


	public RegistRequest(String phone,String pwd,String nickName,String email,
			Listener<JSONObject> listener) throws UnsupportedEncodingException {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s&e=%s",
				PROTOCOL,
				phone,
				MD5Util.MD5(pwd),
				URLEncoder.encode(URLEncoder.encode(nickName,"utf-8"),"utf-8"),
				email,
				MD5Util.MD5(PROTOCOL+phone+MD5Util.MD5(pwd)+nickName+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
