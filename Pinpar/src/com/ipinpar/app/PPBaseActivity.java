package com.ipinpar.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class PPBaseActivity extends FragmentActivity {
	public final String TAG = this.getClass().getSimpleName();
	public RequestQueue apiQueue;
	public Context mContext;
	private ProgressDialog wattingDialog;

	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mContext = this;
		wattingDialog = new ProgressDialog(mContext);
		apiQueue = Volley.newRequestQueue(this);
		apiQueue.start();
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dissmissProgressDialog();
		apiQueue.cancelAll(TAG);
		apiQueue.stop();
	}

}
