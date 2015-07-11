package com.ipinpar.app.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Path.Op;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.HobbyEntity;
import com.ipinpar.app.entity.UserPersonalInfoEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.EditUserInfoRequest;
import com.ipinpar.app.network.api.GetUserInfoRequest;
import com.ipinpar.app.view.FlowLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserInfoEditActivity extends PPBaseActivity implements OnClickListener {
	
	private ImageView ib_left,iv_icon,iv_add_intrest;
	private TextView tv_name,tv_sex,tv_qianming;
	private FlowLayout fl_intrest;
	private Button btn_save;
	private DisplayImageOptions options;
	private String nickName;
	private int sex = 0;
	private String signature;
	private ArrayList<String> hobbys;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_edit_user);
		ib_left = (ImageView) findViewById(R.id.ib_left);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		iv_add_intrest = (ImageView) findViewById(R.id.iv_add_intrest);
		fl_intrest = (FlowLayout) findViewById(R.id.fl_intrest);
		
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_qianming = (TextView) findViewById(R.id.tv_qianming);
		btn_save = (Button) findViewById(R.id.btn_save);
		ib_left.setOnClickListener(this);
		iv_icon.setOnClickListener(this);
		iv_add_intrest.setOnClickListener(this);
		tv_name.setOnClickListener(this);
		tv_sex.setOnClickListener(this);
		tv_qianming.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		hobbys = new ArrayList<String>();
		options = new DisplayImageOptions.Builder().cacheOnDisk(false).build();
		showProgressDialog();
		GetUserInfoRequest request = new GetUserInfoRequest(UserManager.getInstance().getUserInfo().getUid()+"", new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				dissmissProgressDialog();
				try {
					if (response != null && response.getInt("result") == 1) {
						nickName = response.getString("username");
						sex = response.getInt("sex");
						signature = response.getString("signature");
							try {
								for(String string :response.getString("hobbys").split(",")){
									if (!TextUtils.isEmpty(string)) {
										hobbys.add(string);
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						tv_name.setText(nickName);
						tv_qianming.setText(signature);
						ImageLoader.getInstance().displayImage(response.getString("imgsrc"), iv_icon,options);
						if (sex == 1) {
							if (TextUtils.isEmpty(response.getString("imgsrc"))) {
								iv_icon.setImageResource(R.drawable.defaultavatarmale);
							}
							tv_sex.setText("男");
						}
						else {
							if (TextUtils.isEmpty(response.getString("imgsrc"))) {
								iv_icon.setImageResource(R.drawable.defaultavatarfemail);
							}
							tv_sex.setText("女");

						}
						for(String hobbyEntity :hobbys){
							TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.view_hobbys_textview, null);
							textView.setText(hobbyEntity);
							fl_intrest.addView(textView);
						}
					}
					else {
						finish();
						Toast.makeText(mContext, "获取用户信息失败，请重试", 1000).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		apiQueue.add(request);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_save:
			nickName = (String) tv_name.getText();
			signature = tv_qianming.getText().toString();
			EditUserInfoRequest request;
			try {
				StringBuilder hobbysStringBuilder = new StringBuilder() ;
				for(String string:hobbys){
					hobbysStringBuilder.append(string);
					hobbysStringBuilder.append(",");
				}
				request = new EditUserInfoRequest(UserManager.getInstance().getUserInfo().getUid(),
						nickName, signature, UserManager.getInstance().getUserInfo().getEmail(), 
						UserManager.getInstance().getUserInfo().getQq(),
						UserManager.getInstance().getUserInfo().getWeixin(), 
						sex+"", hobbysStringBuilder.toString(),new Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								// TODO Auto-generated method stub
								try {
									if (response.getInt("result") == 1) {
										Toast.makeText(mContext, "修改信息成功", 1000).show();
										finish();
									}
									else {
										Toast.makeText(mContext, "提交失败，请重试", 1000).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									Toast.makeText(mContext, "提交失败，请重试", 1000).show();
									e.printStackTrace();
								}
							}
						});
				apiQueue.add(request);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.ib_left:
			finish();
			break;
		case R.id.iv_icon:
			break;
		case R.id.iv_add_intrest:
			final EditText editText = new EditText(this);
			new AlertDialog.Builder(this).setTitle("请输入兴趣").setIcon(
				    android.R.drawable.ic_dialog_info).setView(
				    		editText).setPositiveButton("确定", new AlertDialog.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.view_hobbys_textview, null);
							textView.setText(editText.getText().toString().trim());
							hobbys.add(editText.getText().toString().trim());
							fl_intrest.addView(textView);
						}
					})
				    .setNegativeButton("取消", null).show();
			break;
		case R.id.tv_name:
			final EditText editText1 = new EditText(this);
			editText1.setText(tv_name.getText());
			new AlertDialog.Builder(this).setTitle("请输入姓名").setIcon(
				    android.R.drawable.ic_dialog_info).setView(
				    		editText1).setPositiveButton("确定", new AlertDialog.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							tv_name.setText(editText1.getText().toString());
						}
					})
				    .setNegativeButton("取消", null).show();
			break;
		case R.id.tv_sex:
			AlertDialog dialog = new AlertDialog.Builder(this).setTitle("复选框").setSingleChoiceItems(
				     new String[] { "男", "女" }, 0, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (which == 0) {
								sex = 1;
								tv_sex.setText("男");
							}
							else {
								sex = 0;
								tv_sex.setText("女");
							}
							dialog.dismiss();
						}
					}).create();
			dialog.show();
			break;
		case R.id.tv_qianming:
			final EditText editText2 = new EditText(this);
			editText2.setText(tv_qianming.getText());

			new AlertDialog.Builder(this).setTitle("请输入个性签名").setIcon(
				    android.R.drawable.ic_dialog_info).setView(
				    		editText2).setPositiveButton("确定", new AlertDialog.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							tv_qianming.setText(editText2.getText().toString());
						}
					})
				    .setNegativeButton("取消", null).show();
			break;

		default:
			break;
		}
	}

}
