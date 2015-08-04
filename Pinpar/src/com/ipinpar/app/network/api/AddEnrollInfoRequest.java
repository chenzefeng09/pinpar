package com.ipinpar.app.network.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class AddEnrollInfoRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="30004";

	public AddEnrollInfoRequest(int uid, String name,String adress_p,String address_c,String address_d,String idnumber,String phone,int sex,String unit,
			String address_detail,Listener<JSONObject> listener) throws UnsupportedEncodingException {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c1=%s&c2=%s&c3=%s&d=%s&e=%s&f=%d&g=%s&h=%s&i=%s",
				PROTOCOL,
				uid,
				URLEncoder.encode(URLEncoder.encode(name, "utf-8"), "utf-8"),
				URLEncoder.encode(URLEncoder.encode(adress_p, "utf-8"), "utf-8"),
				URLEncoder.encode(URLEncoder.encode(address_c, "utf-8"), "utf-8"),
				URLEncoder.encode(URLEncoder.encode(address_d, "utf-8"), "utf-8"),
				idnumber,
				phone,
				sex,
				URLEncoder.encode(URLEncoder.encode(unit, "utf-8"), "utf-8"),
				MD5Util.MD5(PROTOCOL+uid+"pinpa"),
				URLEncoder.encode(URLEncoder.encode(address_detail, "utf-8"), "utf-8")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
