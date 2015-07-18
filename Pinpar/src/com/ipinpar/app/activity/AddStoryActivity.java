package com.ipinpar.app.activity;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.UploadActivityImgRequest;
import com.ipinpar.app.service.ForegroundService;
import com.ipinpar.app.util.BitmapUtil;
import com.ipinpar.app.util.TakePictureUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AddStoryActivity extends PPBaseActivity {

	private ImageView iv_add_img;
	private EditText et_add_text;
	private Button btn_save;
	private File imgFile;
	private String uploadUrl;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_edit_diary);
		iv_add_img  = (ImageView) findViewById(R.id.iv_add_img);
		et_add_text = (EditText) findViewById(R.id.et_add_text);
		btn_save = (Button) findViewById(R.id.btn_save);
		iv_add_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(AddStoryActivity.this)
				.setTitle("请选择图片")
				.setItems(new String[] { "拍照", "相册" },
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (which == 0) {
									startService(new Intent(mContext,
											ForegroundService.class));
									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									intent.putExtra(
											MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(TakePictureUtil.getPhotoFile(mContext)));
									startActivityForResult(intent,
											TakePictureUtil.REQUEST_TAKE_PHOTO);
								} else {
									Intent intent = new Intent(Intent.ACTION_PICK,
											android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
											intent.setType("image/*");
											startActivityForResult(intent, TakePictureUtil.REQUEST_GALLERY);
								}
							}
						}).show();
			}
		});
		
		btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent data = new Intent();
				data.putExtra("img", uploadUrl);
				data.putExtra("text", et_add_text.getText().toString());
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case TakePictureUtil.REQUEST_TAKE_PHOTO: // 拍照来的图片
			if (arg1 == RESULT_CANCELED) {
				return;
			}
		if (arg1 == RESULT_CANCELED) {
			return;
		}
		imgFile = BitmapUtil.compressFile(TakePictureUtil.photoPath);
		showProgressDialog();
		UploadActivityImgRequest request = new UploadActivityImgRequest(UserManager
				.getInstance().getUserInfo().getUid(),imgFile,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(final JSONObject response) {
						// TODO Auto-generated method stub
						runOnUiThread(new Runnable() {
							public void run() {
								dissmissProgressDialog();
								try {
									if (response.getInt("result") == 1) {
										ImageLoader.getInstance().displayImage("file://"+imgFile.getAbsolutePath(), iv_add_img);
										uploadUrl = response.getString("imgsrc");
									}
									else {
										Toast.makeText(mContext, "上传图片失败，请重试", 1000).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						Log.d("response", response.toString());
					}
				}, null);
		break;
		
		case TakePictureUtil.REQUEST_GALLERY:// 相册来的图片
			// 请求相册
			if (arg1 == RESULT_CANCELED) {
				return;
			}
			if (arg2 == null) {
				return;
			}
			Uri uri = arg2.getData();
			if (uri == null) {
				return;
			}
			String str = uri.getScheme();
			if (str == null) {
				return;
			}
			if (str.equalsIgnoreCase("file")) {
				String path = uri.getPath();
				imgFile = BitmapUtil.compressFile(path);

			} else if (str.equals("content")) {
				String fileStr = TakePictureUtil.getImageFilePathName(uri,mContext );
				if (fileStr != null) {
					imgFile = BitmapUtil.compressFile(fileStr);
				}
			}
			showProgressDialog();
			new UploadActivityImgRequest(UserManager
					.getInstance().getUserInfo().getUid(),imgFile,
					new Listener<JSONObject>() {

						@Override
						public void onResponse(final JSONObject response) {
							// TODO Auto-generated method stub
							runOnUiThread(new Runnable() {
								public void run() {
									dissmissProgressDialog();
									try {
										if (response.getInt("result") == 1) {
											ImageLoader.getInstance().displayImage("file://"+imgFile.getAbsolutePath(), iv_add_img);
											uploadUrl = response.getString("imgsrc");

										}
										else {
											Toast.makeText(mContext, "上传头像失败，请重试", 1000).show();

										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
							Log.d("response", response.toString());
						}
					}, null);
		}
	}
}
