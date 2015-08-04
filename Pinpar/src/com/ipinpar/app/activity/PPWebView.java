package com.ipinpar.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;

public class PPWebView extends PPBaseActivity {
	private String url,title;
	private WebView wb_content;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_webview);
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		setTitleText(title);
		wb_content = (WebView) findViewById(R.id.wv_content);
		wb_content.getSettings().setJavaScriptEnabled(true);  
		wb_content.loadUrl(url); 
	}
	
	public static Intent getIntent2Me(Context context,String url,String title){
		Intent intent = new Intent(context, PPWebView.class);
		intent.putExtra("url", url);
		intent.putExtra("title", title);
		return intent;
	}

}
