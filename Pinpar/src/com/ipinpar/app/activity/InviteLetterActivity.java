package com.ipinpar.app.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.ipinpar.app.PPApplication;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.ActivityEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.AnserInviteLetterRequest;

public class InviteLetterActivity extends PPBaseActivity implements OnClickListener{
	private TextView tv_name,tv_chance,tv_time,tv_loc,tv_specification;
	private Button btn_giveup,btn_accepte;
	private ActivityEntity currActivity;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_invite_letter);
		btn_giveup = (Button) findViewById(R.id.btn_giveup);
		btn_accepte = (Button) findViewById(R.id.btn_accepte);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_chance = (TextView) findViewById(R.id.tv_chance);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_loc = (TextView) findViewById(R.id.tv_loc);
		tv_specification = (TextView) findViewById(R.id.activity_specification);
		btn_giveup.setOnClickListener(this);
		btn_accepte.setOnClickListener(this);

		currActivity = (ActivityEntity) getIntent().getSerializableExtra("activity");
		
		if (currActivity == null) {
			Toast.makeText(mContext, "获取邀请信内容失败，请重试", 1000).show();
		}
		else {
			tv_name.setText(Html.fromHtml(
					PPApplication.getInstance().getFormatString(
							R.string.activity_invite_letter_name,
							UserManager.getInstance().getUserInfo().getUsername())));
			tv_chance.setText(Html.fromHtml(
					PPApplication.getInstance().getFormatString(
							R.string.activity_invite_letter_chance,
							currActivity.getSname()+currActivity.getAcname())));
			tv_loc.setText(currActivity.getAddress2()
					+currActivity.getAddress3()
					+currActivity.getAddressdetail());
			
			
			
			long timeBegin = Long.parseLong(currActivity.getActivebegintime())*1000;
			long timeEnd = Long.parseLong(currActivity.getActiveendtime())*1000;
			
			tv_time.setText(DateFormat.format("yyyy.MM.dd kk:mm", timeBegin)+"~"+DateFormat.format("kk:mm", timeEnd));
			
			tv_specification.setText(Html.fromHtml(
					PPApplication.getInstance().getFormatString(
							R.string.activity_invite_letter_tips)));
		}
	}
	
	public static Intent getIntent2Me(Context context,ActivityEntity activity){
		Intent intent = new Intent(context, InviteLetterActivity.class);
		intent.putExtra("activity", activity);
		return intent;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_accepte:
			AnserInviteLetterRequest request = new AnserInviteLetterRequest(
					UserManager.getInstance().getUserInfo().getUid()+"",
					currActivity.getAcid()+"", 
					"1", 
					new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub
							try {
								if (response != null && response.getInt("result") == 1) {
									Toast.makeText(mContext, "接受成功！", 1000).show();
									finish();
								}
								else {
									Toast.makeText(mContext, "接受失败，请重试", 1000).show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
			apiQueue.add(request);
			break;
		case R.id.btn_giveup:
			AnserInviteLetterRequest request2 = new AnserInviteLetterRequest(
					UserManager.getInstance().getUserInfo().getUid()+"",
					currActivity.getAcid()+"", 
					"2", 
					new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub
							try {
								if (response != null && response.getInt("result") == 1) {
									Toast.makeText(mContext, "已拒绝", 1000).show();
									finish();
								}
								else {
									Toast.makeText(mContext, "拒绝失败，请重试", 1000).show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
			apiQueue.add(request2);
			break;

		default:
			break;
		}
		
	}

}
