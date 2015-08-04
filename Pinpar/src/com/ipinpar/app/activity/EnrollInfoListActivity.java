package com.ipinpar.app.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.db.dao.EnrollInfoDao;
import com.ipinpar.app.entity.EnrollInfoEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.EnrollInfoDelRequest;
import com.ipinpar.app.network.api.EnrollInfoSetDefaultRequest;
import com.ipinpar.app.network.api.GetEnrollInfoListRequest;

public class EnrollInfoListActivity extends PPBaseActivity {
	
	private ListView lv_infolist;
	private TextView tv_header_right;
	private Button btn_add_new;
	private ArrayList<EnrollInfoEntity> infos = new ArrayList<EnrollInfoEntity>();
	private EnrollInfoListAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_enrollinfo_list);

		lv_infolist = (ListView) findViewById(R.id.lv_infolist);
		tv_header_right = (TextView) findViewById(R.id.tv_header_right);
		btn_add_new = (Button) findViewById(R.id.btn_add_new);
		btn_add_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, EnrollUserinfo.class));
			}
		});
		tv_header_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (adapter != null ) {
					adapter.setEditmode(!adapter.isEditmode());
					adapter.notifyDataSetChanged();
					if (adapter.isEditmode()) {
						tv_header_right.setText("完成");
					}
					else {
						tv_header_right.setText("管理");
					}
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		GetEnrollInfoListRequest request  = new GetEnrollInfoListRequest(
				UserManager.getInstance().getUserInfo().getUid(), 
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response != null && response.getInt("result") == 1) {
								infos.clear();
								Gson gson = new Gson();
								Type type = new TypeToken<ArrayList<EnrollInfoEntity>>(){}.getType();
								infos.addAll((Collection<? extends EnrollInfoEntity>) gson.fromJson(response.getJSONArray("infos").toString(),
										type));
								EnrollInfoDao.getInstance().insertEnrollInfos(infos);
								if (adapter ==null) {
									adapter = new EnrollInfoListAdapter(infos);
									lv_infolist.setAdapter(adapter);
									lv_infolist.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> arg0, View arg1,
												int arg2, long arg3) {
											Intent daIntent = new Intent();
											daIntent.putExtra("selected_info", infos.get(arg2));
											setResult(RESULT_OK, daIntent);
											finish();
										}
									});
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
	
	private class EnrollInfoListAdapter extends BaseAdapter{
		private ArrayList<EnrollInfoEntity> infos;
		private ViewHolder holder;
		private EnrollInfoEntity default_info;
		private boolean editmode;
		
		public EnrollInfoListAdapter(ArrayList<EnrollInfoEntity> entities) {
			this.infos = entities;
		}
		
		public void setEditmode(boolean editmode) {
			this.editmode = editmode;
		}
		public boolean isEditmode(){
			return editmode;
		}
		

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos ==null ? 0:infos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return infos == null ? null:infos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final EnrollInfoEntity enInfoEntity = infos.get(position);
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.list_item_enroll_info, null);
				holder = new ViewHolder();
				holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
				holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.iv_del = (ImageView) convertView.findViewById(R.id.iv_del);
				holder.cb_default = (CheckBox) convertView.findViewById(R.id.cb_default);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (editmode) {
				holder.iv_del.setVisibility(View.VISIBLE);
			}
			else {
				holder.iv_del.setVisibility(View.GONE);

			}
			if (enInfoEntity.getIsdefault() == 1) {
				default_info = enInfoEntity;
				holder.cb_default.setButtonDrawable(R.drawable.enroll_checkbox_pressed);
				holder.cb_default.setText("默认");
			}
			else {
				holder.cb_default.setButtonDrawable(R.drawable.enroll_checkbox_normal);
				holder.cb_default.setText("");
			}
			holder.tv_phone.setText(enInfoEntity.getPhone());
			holder.tv_address.setText(enInfoEntity.getAddress());
			holder.tv_name.setText(enInfoEntity.getName());
			holder.cb_default.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (enInfoEntity.getIsdefault() == 1) {
						return;
					}
					if (isChecked) {
						showProgressDialog();
						EnrollInfoSetDefaultRequest request = new EnrollInfoSetDefaultRequest(
								UserManager.getInstance().getUserInfo().getUid(),
								enInfoEntity.getInfoid(), new Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										dissmissProgressDialog();
										try {
											if (response != null && response.getInt("result") == 1) {
												enInfoEntity.setIsdefault(1);
												default_info.setIsdefault(0);
												EnrollInfoDao.getInstance().insertEnrollInfo(enInfoEntity);
												EnrollInfoDao.getInstance().insertEnrollInfo(default_info);
												notifyDataSetChanged();
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
						apiQueue.add(request);
					}
				}
			});
			holder.iv_del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(mContext)
					.setMessage("确定要删除该条联系方式吗？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							showProgressDialog();
							EnrollInfoDelRequest request = new EnrollInfoDelRequest(
									UserManager.getInstance().getUserInfo().getUid(), 
									enInfoEntity.getInfoid(), new Listener<JSONObject>() {

										@Override
										public void onResponse(
												JSONObject response) {
											dissmissProgressDialog();
											try {
												if (response != null && response.getInt("result") == 1) {
													EnrollInfoDao.getInstance().removeInfo(enInfoEntity.getInfoid());
													Toast.makeText(mContext, "删除成功", 1000).show();
													infos.remove(enInfoEntity);
													notifyDataSetChanged();
												}
												else {
													Toast.makeText(mContext, "删除失败，请重试", 1000).show();
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
					.create().show();
				}
			});
			return convertView;
		}
		
		private class ViewHolder{
			public TextView tv_name,tv_phone,tv_address;
			public ImageView iv_del;
			public CheckBox cb_default;
		}
		
	}
}
