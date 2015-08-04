package com.ipinpar.app.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.ExperienceDiaryDetailEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.ExperienceDiaryRequest;
import com.ipinpar.app.network.api.UpdateExperienceDiaryRequest;
import com.ipinpar.app.network.api.UploadActivityImgRequest;
import com.ipinpar.app.service.ForegroundService;
import com.ipinpar.app.util.BitmapUtil;
import com.ipinpar.app.util.TakePictureUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ExperienceDiaryEditActivity extends PPBaseActivity implements OnClickListener{
	
	private static final int REQUEST_CODE_ADD_STORY = 1;
	private ListView lv_diary;
	private ImageView iv_icon,iv_title_bkg,iv_edit_name;
	private View ll_add;
	private TextView tv_name,tv_publish;
	private Button btn_add_pic;
	private ArrayList<ExperienceDiaryDetailEntity> experienceDetials;
	
	private DiaryDetailAdapter adapter;
	private int activityid,uid,sid;
	private File imgFile;
	private String uploadUrl;
	private String newtitle;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_experience_diary_edit);
		experienceDetials = new ArrayList<ExperienceDiaryDetailEntity>();
		int sid = getIntent().getIntExtra("sid", 0);
		uid = getIntent().getIntExtra("uid", 0);
		activityid = getIntent().getIntExtra("activityid", 0);
		showProgressDialog();
		if (sid != 0){
			ExperienceDiaryRequest request = new ExperienceDiaryRequest(sid, new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					dissmissProgressDialog();
					try {
						if (response != null && response.getInt("result") == 1) {
							experienceDetials.clear();
							ImageLoader.getInstance().displayImage(response.getString("img"), iv_title_bkg);
							ImageLoader.getInstance().displayImage("http://api.ipinpar.com/pinpaV2/api.pinpa?protocol=10008&a="+response.getInt("uid"), iv_icon);
							tv_name.setText(response.getString("title"));
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<ExperienceDiaryDetailEntity>>(){}.getType();
							experienceDetials.addAll((ArrayList<ExperienceDiaryDetailEntity>)gson.fromJson(response.getJSONArray("details").toString(), type));
							if (adapter == null) {
								adapter = new DiaryDetailAdapter(experienceDetials);
								lv_diary.setAdapter(adapter);
							}
							else {
								adapter.notifyDataSetChanged();
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			apiQueue.add(request);
		}
		else if (uid != 0 && activityid != 0) {
			ExperienceDiaryRequest request = new ExperienceDiaryRequest(uid,activityid, new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					dissmissProgressDialog();
					try {
						if (response != null && response.getInt("result") == 1) {
							experienceDetials.clear();
							ImageLoader.getInstance().displayImage(response.getString("img"), iv_title_bkg);
							ImageLoader.getInstance().displayImage("http://api.ipinpar.com/pinpaV2/api.pinpa?protocol=10008&a="+response.getInt("uid"), iv_icon);
							tv_name.setText(response.getString("title"));
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<ExperienceDiaryDetailEntity>>(){}.getType();
							experienceDetials.addAll((ArrayList<ExperienceDiaryDetailEntity>)gson.fromJson(response.getJSONArray("details").toString(), type));
							if (adapter == null) {
								adapter = new DiaryDetailAdapter(experienceDetials);
								lv_diary.setAdapter(adapter);
							}
							else {
								adapter.notifyDataSetChanged();
							}
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			apiQueue.add(request);
		}
		lv_diary = (ListView) findViewById(R.id.lv_diary);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		iv_title_bkg = (ImageView) findViewById(R.id.iv_title_bkg);
		tv_name = (TextView) findViewById(R.id.tv_name);
		btn_add_pic = (Button) findViewById(R.id.btn_add_pic);
		ll_add = findViewById(R.id.ll_add);
		iv_edit_name = (ImageView) findViewById(R.id.iv_edit_name);
		tv_publish = (TextView) findViewById(R.id.tv_publish);
		ll_add.setOnClickListener(this);
		iv_edit_name.setOnClickListener(this);
		tv_publish.setOnClickListener(this);
		btn_add_pic.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_add:
			Intent intent = new Intent(mContext, AddStoryActivity.class);
			startActivityForResult(intent, REQUEST_CODE_ADD_STORY);
			break;
			
		case R.id.iv_edit_name:
			final EditText editText = new EditText(this);
			new AlertDialog.Builder(this).setTitle("请输入新的标题").setView(editText)
					.setPositiveButton("确定", new AlertDialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							newtitle = editText.getText().toString().trim();
							tv_name.setText(newtitle);

						}
					}).setNegativeButton("取消", null).show();
			
			break;
			
		case R.id.btn_add_pic:
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
			
			break;
			
		case R.id.tv_publish:
			Gson gson = new Gson();
			showProgressDialog();
			UpdateExperienceDiaryRequest request = new UpdateExperienceDiaryRequest(
					UserManager.getInstance().getUserInfo().getUid(), activityid, newtitle, 
					uploadUrl,gson.toJson(experienceDetials) , new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							dissmissProgressDialog();
							try {
								if (response != null && response.getInt("result") == 1) {
									Toast.makeText(mContext, "编辑体验日记成功！", 1000).show();;
									startActivity(ExperienceDiaryActivity.getIntent2Me(mContext, activityid, uid));
									
								}
								else {
									Toast.makeText(mContext, "编辑体验失败，请重试", 1000).show();;

								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
						}
					});
			apiQueue.add(request);
			break;

		default:
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case REQUEST_CODE_ADD_STORY:
			if (arg1 != RESULT_CANCELED) {
				String img = arg2.getStringExtra("img");
				String text = arg2.getStringExtra("text");
				ExperienceDiaryDetailEntity entity = new ExperienceDiaryDetailEntity();
				entity.setContent(text);
				entity.setImg(img);
				entity.setSid(sid);
				experienceDetials.add(entity);
				if (adapter == null) {
					adapter = new DiaryDetailAdapter(experienceDetials);
					lv_diary.setAdapter(adapter);
				}
				else {
					adapter.notifyDataSetChanged();
				}
				
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
										ImageLoader.getInstance().displayImage("file://"+imgFile.getAbsolutePath(), iv_title_bkg);
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
											ImageLoader.getInstance().displayImage("file://"+imgFile.getAbsolutePath(), iv_title_bkg);
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

		default:
			break;
		}
	}
	
	
	
	
	private class DiaryDetailAdapter extends BaseAdapter{
		private ArrayList<ExperienceDiaryDetailEntity> details;
		private ViewHolder holder;
		public DiaryDetailAdapter(ArrayList<ExperienceDiaryDetailEntity> detailEntities) {
			// TODO Auto-generated constructor stub
			this.details = detailEntities;
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return details == null ? 0 :details.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return details == null ?null : details.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ExperienceDiaryDetailEntity detailEntity = details.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.list_item_experience_diary_edit, null);
				holder.iv_diary_img = (ImageView) convertView.findViewById(R.id.iv_diary_img);
				holder.tv_diary_txt = (TextView) convertView.findViewById(R.id.tv_diary_txt);
				holder.iv_del = (ImageView) convertView.findViewById(R.id.iv_del);

				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (TextUtils.isEmpty(detailEntity.getImg())) {
				holder.iv_diary_img.setVisibility(View.GONE);
			}
			else {
				holder.iv_diary_img.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(detailEntity.getImg(), holder.iv_diary_img);
			}
			holder.iv_del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(mContext).setMessage("确定删除该条吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							details.remove(detailEntity);
							notifyDataSetChanged();
						}
					}).setNegativeButton("取消", null).create().show();;
					
				}
			});
			holder.tv_diary_txt.setText(detailEntity.getContent());
			return convertView;
		}
		
		private class ViewHolder{
			public ImageView iv_diary_img;
			public TextView tv_diary_txt;
			public ImageView iv_del;
		}
		
	}
	
	public static Intent getIntent2Me(Context context,int sid){
		Intent intent = new Intent(context, ExperienceDiaryEditActivity.class);
		intent.putExtra("sid", sid);
		return intent;
	}
	
	public static Intent getIntent2Me(Context context,int activityid,int uid){
		Intent intent = new Intent(context, ExperienceDiaryEditActivity.class);
		intent.putExtra("uid", uid);
		intent.putExtra("activityid", activityid);
		return intent;
	}


}
