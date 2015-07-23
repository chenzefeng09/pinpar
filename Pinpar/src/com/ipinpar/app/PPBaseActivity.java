package com.ipinpar.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.umeng.analytics.MobclickAgent;


public class PPBaseActivity extends FragmentActivity{
	public final String TAG = this.getClass().getSimpleName();
	public RequestQueue apiQueue;
	public Context mContext;
	private ProgressDialog wattingDialog;
	private ImageView backview;
	private TextView tv_header_title;
	private OnClickListener clickBack;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mContext = this;
		wattingDialog = new ProgressDialog(mContext);
		apiQueue = Volley.newRequestQueue(this);
		apiQueue.start();
		clickBack = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		};
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (backview == null) {
			backview = (ImageView) findViewById(R.id.ib_left);
		}
		if (backview != null) {
			backview.setOnClickListener(clickBack);
		}
		MobclickAgent.onResume(mContext);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(mContext);
	}
	
	public void setTitleText(String title){
		if (tv_header_title == null) {
			tv_header_title = (TextView) findViewById(R.id.tv_header_title);
		}
		if (tv_header_title != null) {
			tv_header_title.setText(title);
		}
	}
	
	public void showProgressDialog(){
		if (!wattingDialog.isShowing()) {
			wattingDialog.show();
		}
	}
	public void dissmissProgressDialog(){
		if (wattingDialog.isShowing()) {
			wattingDialog.dismiss();
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		if (wattingDialog != null && wattingDialog.isShowing()) {
			wattingDialog.dismiss();
			finish();
		}
		super.onBackPressed();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dissmissProgressDialog();
		apiQueue.cancelAll(TAG);
		apiQueue.stop();
	}

}
