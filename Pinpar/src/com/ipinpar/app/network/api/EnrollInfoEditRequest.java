package com.ipinpar.app.network.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class EnrollInfoEditRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="30007";

	public EnrollInfoEditRequest(int uid,String name,String adresstitle,String idnumber,String phone,int sex,String unit,
			String address_detail,int infoid,
			Listener<JSONObject> listener) throws UnsupportedEncodingException {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s",
				PROTOCOL,
				uid,
				URLEncoder.encode(URLEncoder.encode(name, "utf-8"), "utf-8"),
				URLEncoder.encode(URLEncoder.encode(adresstitle, "utf-8"), "utf-8"),
				idnumber,
				phone,
				sex,
				URLEncoder.encode(URLEncoder.encode(unit, "utf-8"), "utf-8"),
				MD5Util.MD5(PROTOCOL+uid+infoid+"pinpa"),
				infoid,
				URLEncoder.encode(URLEncoder.encode(address_detail, "utf-8"), "utf-8")
				), null, listener);
		// TODO Auto-generated constructor stub
	}
}
