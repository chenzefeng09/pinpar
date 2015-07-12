package com.ipinpar.app.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.JsonObject;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.EditUserInfoRequest;
import com.ipinpar.app.network.api.GetUserInfoRequest;
import com.ipinpar.app.network.api.PhotoMultipartRequest;
import com.ipinpar.app.service.ForegroundService;
import com.ipinpar.app.util.BitmapUtil;
import com.ipinpar.app.view.FlowLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserInfoEditActivity extends PPBaseActivity implements OnClickListener {
	
	private ImageView ib_left,iv_icon,iv_add_intrest;
	private TextView tv_name,tv_sex,tv_qianming;
	private FlowLayout fl_intrest;
	private RelativeLayout rl_signature;

	private Button btn_save;
	private DisplayImageOptions options;
	private String nickName;
	private int sex = 0;
	private String signature;
	private ArrayList<String> hobbys;
	private File imgFile;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_edit_user);
		ib_left = (ImageView) findViewById(R.id.ib_left);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		iv_add_intrest = (ImageView) findViewById(R.id.iv_add_intrest);
		fl_intrest = (FlowLayout) findViewById(R.id.fl_intrest);
		rl_signature = (RelativeLayout) findViewById(R.id.rl_signature);

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
		rl_signature.setOnClickListener(this);

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
	protected void onActivityResult(int requestCode, int arg1, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, arg1, data);
		stopService(new Intent(mContext, ForegroundService.class));
		switch (requestCode) {
		case REQUEST_TAKE_PHOTO: {// 拍照来的图片
			if (arg1 == RESULT_CANCELED) {
				return;
			}
			if (data == null) {
				return;
			}
			Bundle bundle = data.getExtras();
			if (bundle == null) {
				return;
			}
			Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
			imgFile = BitmapUtil.bitmapToFile(bitmap, getPhotoFilePath(mContext));
			break;
		}
		case REQUEST_GALLERY:// 相册来的图片
			// 请求相册
			if (arg1 == RESULT_CANCELED) {
				return;
			}
			if (data == null) {
				return;
			}
			Uri uri = data.getData();
			if(uri == null){
				return ;
			}
			String str = uri.getScheme();
			if(str == null){
				return;
			}
			if (str.equalsIgnoreCase("file")) {
				String path = uri.getPath();
				imgFile = BitmapUtil.compressFile(path);
				if (null != imgFile && imgFile.length() > 0) {
					return;
				} else {
					return;
				}

			}
		}
	}

	@Override
	public void onClick(View v) {	
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_save:
			nickName = (String) tv_name.getText();
			signature = tv_qianming.getText().toString();
			PhotoMultipartRequest<JsonObject> request;
			EditUserInfoRequest request2;
			try {
				StringBuilder hobbysStringBuilder = new StringBuilder() ;
				for(String string:hobbys){
					hobbysStringBuilder.append(string);
					hobbysStringBuilder.append(",");
				}
				if (imgFile != null && imgFile.length() >0) {
					request = new PhotoMultipartRequest(UserManager.getInstance().getUserInfo().getUid(),
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
							},imgFile);
					apiQueue.add(request);

				}
				else {
					request2 = new EditUserInfoRequest(UserManager.getInstance().getUserInfo().getUid(),
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
					apiQueue.add(request2);

				}
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.ib_left:
			finish();
			break;
		case R.id.iv_icon:
			new AlertDialog.Builder(this).setTitle("请选择图片").setItems(
				     new String[] { "拍照", "相册" }, new AlertDialog.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (which == 0) {
								startService(new Intent(mContext, ForegroundService.class));
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getPhotoFile(mContext)));
								startActivityForResult(intent, REQUEST_TAKE_PHOTO);
							}
							else {
								
							}
						}
					}).show();
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
		case R.id.rl_signature:
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
	

	public File getPhotoFile(Context context) {
		File file = new File(getPhotoFilePath(context));
		if(file.exists()){
			file.delete();
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	private String PHOTO_DIR;// 存储照片的位置
	public static final int REQUEST_TAKE_PHOTO = 11;// 照相
	public static final int REQUEST_GALLERY = 22;// 相册
	private String photoPath ;
	
	public String getPhotoFilePath(Context context) {
		if (TextUtils.isEmpty(PHOTO_DIR)) {
			createImgFolders(context);
		}
		 photoPath = PHOTO_DIR+"/"+getPhotoName();
		return photoPath;
	}
	
	/**
	 * 得到图片的名称
	 */
	public String getPhotoName() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssSSS");
		String str = sdf.format(date);
		Random ran = new Random();
		str = str + ran.nextInt(100) + ".jpg";
		return str;
	}
	
	private void createImgFolders(Context context) {
		File compressImgDir;
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			// 如果未加载SD卡，存放在内置卡中
			PHOTO_DIR = context.getCacheDir().getAbsolutePath()
					+ File.separator + "pics" + File.separator;
			compressImgDir = new File(PHOTO_DIR, "CompressPics");
		} else {
			PHOTO_DIR = Environment.getExternalStorageDirectory()
					+ "/DCIM/Camera";
			compressImgDir = new File(Environment
					.getExternalStorageDirectory().getAbsoluteFile().toString()
					+ "/ipinpar/pictures/CompressedPictures");
		}
		BitmapUtil.compressPicDir = compressImgDir;

		File photoDir = new File(PHOTO_DIR);
		if (!photoDir.exists()) {
			photoDir.mkdirs();
			photoDir = null;
		}
	}

}
