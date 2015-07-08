package com.ipinpar.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.view.FlowLayout;

public class UserInfoEditActivity extends PPBaseActivity implements OnClickListener {
	
	private ImageView ib_left,iv_icon,iv_add_intrest;
	private TextView tv_name,tv_sex,tv_qianming;
	private FlowLayout fl_intrest;
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
		
		ib_left.setOnClickListener(this);
		iv_icon.setOnClickListener(this);
		iv_add_intrest.setOnClickListener(this);
		tv_name.setOnClickListener(this);
		tv_sex.setOnClickListener(this);
		tv_qianming.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
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
							TextView textView = new TextView(mContext);
							textView.setText(editText.getText().toString());
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
								tv_sex.setText("男");
							}
							else {
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
