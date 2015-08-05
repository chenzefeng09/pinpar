package com.ipinpar.app.network.api;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ipinpar.app.util.MD5Util;

public class PhotoMultipartRequest<T> extends Request<T> {
	private static final String PROTOCOL ="10007";

private static final String FILE_PART_NAME = "file";

private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
private final Response.Listener<T> mListener;
private final File mImageFile;
protected Map<String, String> headers;

public PhotoMultipartRequest(long uid,String uname,String signiture,String email,String qq,String weiixn,String sex,String hobbys,
		Listener<T> listener, File imageFile) throws UnsupportedEncodingException{
    super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s",
			PROTOCOL,
			uid,
			URLEncoder.encode(URLEncoder.encode(uname, "utf-8"), "utf-8"),
			URLEncoder.encode(URLEncoder.encode(signiture, "utf-8"), "utf-8"),
			email,
			qq,
			weiixn,
			sex,
			URLEncoder.encode(URLEncoder.encode(hobbys, "utf-8"), "utf-8"),
			MD5Util.MD5(PROTOCOL+uid+"pinpa")
			), null);

    mListener = listener;
    mImageFile = imageFile;
    if (imageFile == null) {
		return;
	}
    buildMultipartEntity();
}

@Override
public Map<String, String> getHeaders() throws AuthFailureError {
    Map<String, String> headers = super.getHeaders();

    if (headers == null
            || headers.equals(Collections.emptyMap())) {
        headers = new HashMap<String, String>();
    }

    headers.put("Accept", "application/json");

    return headers;
}

private void buildMultipartEntity(){
    mBuilder.addBinaryBody(FILE_PART_NAME, mImageFile, ContentType.create("image/jpeg"), mImageFile.getName());
    mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    mBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
}

@Override
public String getBodyContentType(){
    String contentTypeHeader = mBuilder.build().getContentType().getValue();
    return contentTypeHeader;
}

@Override
public byte[] getBody() throws AuthFailureError{
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
        mBuilder.build().writeTo(bos);
    } catch (IOException e) {
        VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
    }

    return bos.toByteArray();
}

@Override
protected Response<T> parseNetworkResponse(NetworkResponse response) {
    T result = null;
    return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
}

@Override
protected void deliverResponse(T response) {
    mListener.onResponse(response);
}
}