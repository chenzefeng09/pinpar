package com.ipinpar.app.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.UpdateExperienceDiaryRequest;
import com.ipinpar.app.network.api.UpdatePartyExperienceRequest;
import com.ipinpar.app.network.api.UploadActivityImgRequest;
import com.ipinpar.app.service.ForegroundService;
import com.ipinpar.app.util.BitmapUtil;
import com.ipinpar.app.util.TakePictureUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PartyExperienceEditActivity extends PPBaseActivity{

	private Context mContext;
	private Button btnBack;
	private EditText etPartyExperiText;
	private ImageView ivPartyExperiImage;
	private TextView tvPartyExperiCommit;
	
	private static final int REQUEST_CODE_ADD_IMAGE = 1;
	private File imgFile;
	private String uploadUrl="";
	
	private int roleId;
	private String contentText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_party_experiences_edit);
		mContext=this;
		
		roleId = getIntent().getIntExtra("RoleId",1);
		
		findView();
		
		
		setView();
		
	}
	
	public void findView(){
		btnBack = (Button) findViewById(R.id.btn_party_experiences_edit_back);
		tvPartyExperiCommit = (TextView) findViewById(R.id.tv_party_experience_commit);
		etPartyExperiText = (EditText) findViewById(R.id.et_party_experiences_text);
		ivPartyExperiImage = (ImageView) findViewById(R.id.iv_party_experiences_image);
		
	}
	
	
	public void setView(){
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		ivPartyExperiImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(mContext)
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
		tvPartyExperiCommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				contentText = etPartyExperiText.getText().toString().trim();
				if((contentText == null ||contentText.equals(""))
						&&(uploadUrl == null ||uploadUrl.equals(""))){
					Toast.makeText(mContext, "填写一下您的体验经历吧~", Toast.LENGTH_SHORT).show();
				}else{
					UpdatePartyExperienceRequest updatePartyExperienceRequest = null;
					try {
						updatePartyExperienceRequest = new UpdatePartyExperienceRequest(
								UserManager.getInstance().getUserInfo().getUid()+"", 
								contentText,
								uploadUrl,
								roleId+"", new Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										dissmissProgressDialog();
										try {
											if (response != null && response.getInt("result") == 1) {
												Toast.makeText(mContext, "发表成功！", 1000).show();
												finish();
											}
											else {
												Toast.makeText(mContext, "发表失败，请重试", 1000).show();
												finish();
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
										
									}
								});
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					apiQueue.add(updatePartyExperienceRequest);
				}
				
			}
		});
		
		
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case REQUEST_CODE_ADD_IMAGE:
			if (arg1 != RESULT_CANCELED) {
				String img = arg2.getStringExtra("img");
				String text = arg2.getStringExtra("text");
//				ExperienceDiaryDetailEntity entity = new ExperienceDiaryDetailEntity();
//				entity.setContent(text);
//				entity.setImg(img);
//				entity.setSid(sid);
//				experienceDetials.add(entity);
//				if (adapter == null) {
//					adapter = new DiaryDetailAdapter(experienceDetials);
//					lv_diary.setAdapter(adapter);
//				}
//				else {
//					adapter.notifyDataSetChanged();
//				}
				
			}
			
			break;
			
			
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
											ImageLoader.getInstance().displayImage("file://"+imgFile.getAbsolutePath(), ivPartyExperiImage);
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
											ImageLoader.getInstance().displayImage("file://"+imgFile.getAbsolutePath(), ivPartyExperiImage);
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

		default:
			break;
		}
	}
	
	
}
