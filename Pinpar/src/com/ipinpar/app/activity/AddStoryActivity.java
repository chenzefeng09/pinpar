package com.ipinpar.app.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;

public class AddStoryActivity extends PPBaseActivity {

	private ImageView iv_add_img;
	private EditText et_add_text;
	private Button btn_save;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_edit_diary);
		iv_add_img  = (ImageView) findViewById(R.id.iv_add_img);
		et_add_text = (EditText) findViewById(R.id.et_add_text);
		btn_save = (Button) findViewById(R.id.btn_save);
	}
}
