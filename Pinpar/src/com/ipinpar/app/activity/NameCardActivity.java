package com.ipinpar.app.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.db.dao.FriendDao;
import com.ipinpar.app.entity.FriendEntity;
import com.ipinpar.app.entity.UserEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.AddFriendRequest;
import com.ipinpar.app.network.api.GetUserInfoRequest;
import com.ipinpar.app.view.FlowLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NameCardActivity extends PPBaseActivity implements OnClickListener{
	private TextView tv_uname,tv_qianming,tv_signatur_content;
	private ImageView iv_sex,iv_icon;
	private FlowLayout fl_intrest;
	private Button btn_add_friend;
	private FriendEntity friendEntity;
	private UserEntity currUser;
	private ArrayList<String> hobbys;
	private int peer_uid;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		peer_uid = getIntent().getIntExtra("uid", 0);
		if (peer_uid == 0) {
			Toast.makeText(mContext, "获取名片信息失败，请重试", 1000).show();
			finish();
		}
		setContentView(R.layout.activity_namecard);
		tv_uname = (TextView) findViewById(R.id.tv_uname);
		tv_qianming = (TextView) findViewById(R.id.tv_qianming);
		tv_signatur_content = (TextView) findViewById(R.id.tv_signatur_content);
		btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
		iv_sex = (ImageView) findViewById(R.id.iv_sex);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		fl_intrest = (FlowLayout) findViewById(R.id.fl_intrest);
		btn_add_friend.setOnClickListener(this);
		hobbys = new ArrayList<String>();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (UserManager.getInstance().isLogin()) {
			
			GetUserInfoRequest request = new GetUserInfoRequest(UserManager.getInstance().getUserInfo().getUid()+"", new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					dissmissProgressDialog();
					try {
						if (response != null && response.getInt("result") == 1) {
							fl_intrest.removeAllViews();
							UserEntity userEntity = new UserEntity();
							userEntity.setUid(response.getInt("uid"));
							userEntity.setUsername(response.getString("username"));
							userEntity.setSex(response.getInt("sex"));
							userEntity.setSignature(response.getString("signature"));
							userEntity.setImgsrc(response.getString("imgsrc"));
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
							ImageLoader.getInstance().displayImage(response.getString("imgsrc"), iv_icon);
							for(String hobbyEntity :hobbys){
								TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.view_hobbys_textview, null);
								textView.setText(hobbyEntity);
								fl_intrest.addView(textView);
							}
							currUser = userEntity;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						ImageLoader.getInstance().displayImage(UserManager.getInstance().getUserInfo().getImgsrc(), iv_icon);
						if (UserManager.getInstance().getUserInfo().getSex() == 1) {
							iv_sex.setImageResource(R.drawable.log_maleselected);
							if (TextUtils.isEmpty(UserManager.getInstance().getUserInfo().getImgsrc())) {
								iv_icon.setImageResource(R.drawable.defaultavatarmale);
							}
						}
						else {
							iv_sex.setImageResource(R.drawable.log_femailselected);
							if (TextUtils.isEmpty(UserManager.getInstance().getUserInfo().getImgsrc())) {
								iv_icon.setImageResource(R.drawable.defaultavatarfemail);
							}
						}
						if (TextUtils.isEmpty(UserManager.getInstance().getUserInfo().getSignature())) {
							tv_qianming.setText("没有留下任何文字");
						}
						else {
							tv_qianming.setText(UserManager.getInstance().getUserInfo().getSignature());
						}
						tv_uname.setText(UserManager.getInstance().getUserInfo().getUsername());
						friendEntity = FriendDao.getInstance().queryUser(peer_uid);
						if (friendEntity == null) {
							btn_add_friend.setText("添加好友");
						}
						else {
							btn_add_friend.setText("发消息");
						}
				}
			});
			apiQueue.add(request);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_friend:
			if (friendEntity == null) {
				final EditText editText = new EditText(this);
				new AlertDialog.Builder(this).setTitle("验证信息").setView(
					    		editText).setPositiveButton("发送", new AlertDialog.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
//								TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.view_hobbys_textview, null);
//								textView.setText(editText.getText().toString().trim());
//								hobbys.add(editText.getText().toString().trim());
//								fl_intrest.addView(textView);
								AddFriendRequest request = new AddFriendRequest(UserManager.getInstance().getUserInfo().getUid(),
										peer_uid, "", editText.getText().toString().trim(), new Listener<JSONObject>() {

											@Override
											public void onResponse(
													JSONObject response) {
												try {
													if (response != null && response.getInt("result") == 1) {
														Toast.makeText(mContext, "添加好友成功", 1000).show();
														FriendEntity friendEntity = new FriendEntity();
														friendEntity.setImgsrc(currUser.getImgsrc());
														friendEntity.setUid(currUser.getUid());
														friendEntity.setUsername(currUser.getUsername());
														FriendDao.getInstance().insertUser(friendEntity);
														btn_add_friend.setText("发消息");
													}
													else {
														Toast.makeText(mContext, "添加好友失败，请重试", 1000).show();
													}
												} catch (JSONException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												
											}
										});
								apiQueue.add(request);
							}
						})
					    .setNegativeButton("取消", null).show();
				
			}
			else {
				
			}
			break;

		default:
			break;
		}
	}
	
	public static Intent getIntent2Me(Context context,int viewed_uid){
		Intent intent = new Intent(context, NameCardActivity.class);
		intent.putExtra("uid", viewed_uid);
		return intent;
	}

}