package com.ipinpar.app.activity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipinpar.app.PPApplication;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.AcStatementEntity;
import com.ipinpar.app.entity.CommentEntity;
import com.ipinpar.app.entity.ReplyEntity;
import com.ipinpar.app.manager.AgreeManager;
import com.ipinpar.app.manager.AgreeManager.AgreeResultListener;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.PublishCommentRequest;
import com.ipinpar.app.network.api.ReplyCommentRequest;
import com.ipinpar.app.network.api.StatementCommentListRequest;
import com.ipinpar.app.network.api.StatementDetailRequest;
import com.ipinpar.app.view.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommentDetailActivity extends PPBaseActivity {

	private TextView name,time,content,support,comment;
	private ImageView userImage;
	private AcStatementEntity currStatement;
	private ArrayList<CommentEntity> comments = new ArrayList<CommentEntity>();
	private CommentDetailAdapter adapter;
	private ListView lv_infolist;
	private EditText et_input;
	private Button btn_add_new;
	
	private boolean isreply;
	private int reply_commentid;
	private int reply_to_uid;
	private String replyPrefix;
	private int currenrollid;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_comments_list);
		currenrollid =getIntent().getIntExtra("enrollid",0);
		userImage =(CircularImageView) findViewById(R.id.statement_image);
		name = (TextView) findViewById(R.id.statement_user_name);
		time = (TextView) findViewById(R.id.statement_time);
		content = (TextView) findViewById(R.id.statement_content);
		support = (TextView) findViewById(R.id.tv_statement_support_num);
		comment = (TextView) findViewById(R.id.tv_statement_comment_num);
		lv_infolist = (ListView) findViewById(R.id.lv_infolist);
		et_input = (EditText) findViewById(R.id.et_input);
		btn_add_new = (Button) findViewById(R.id.btn_add_new);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshStatement();
	}
	
	private void refreshStatement(){
		showProgressDialog();
		StatementDetailRequest request = new StatementDetailRequest(currenrollid, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				dissmissProgressDialog();
				try {
					if (response != null && response.getInt("result") == 1) {
						dissmissProgressDialog();
						Gson gson = new Gson();
						AcStatementEntity statementEntity = gson.fromJson(response.toString(), AcStatementEntity.class);
						currStatement = statementEntity;
						setupViews();
						refreshData();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		apiQueue.add(request);
	}
	
	private void refreshData(){
		showProgressDialog();
		StatementCommentListRequest request = new StatementCommentListRequest(
				currenrollid, "enrollid", new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						dissmissProgressDialog();
						try {
							if (response != null && response.getInt("result") == 1) {
								comments.clear();
								Gson gson = new Gson();
								Type token = new TypeToken<ArrayList<CommentEntity>>(){}.getType();
								comments.addAll((ArrayList<CommentEntity>) gson.fromJson(response.getJSONArray("comments").toString(), token));
								if (adapter == null) {
									adapter = new CommentDetailAdapter(comments);
									lv_infolist.setAdapter(adapter);
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
	
	private void setupViews(){
		ImageLoader.getInstance().displayImage(
				"http://api.ipinpar.com/pinpaV2/api.pinpa?protocol=10008&a="+currStatement.getUid(),
				userImage);
		name.setText(currStatement.getUsername());
		
		long timel = Long.parseLong(currStatement.getCreatetime())*1000;
		time.setText(DateFormat.format("yyyy/MM/dd    kk:mm", timel));
		content.setText(currStatement.getDeclaration());
		support.setText(currStatement.getAgreecount()+"");
		comment.setText(currStatement.getCommentcount()+"");
		support.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (UserManager.getInstance().isLogin()) {
					AgreeManager.getInstance().agree(currStatement.getEnrollid(),
							"enrollid",new AgreeResultListener() {
						
						@Override
						public void onAgreeResult(boolean agree) {
							if (agree) {
								support.setCompoundDrawables(getResources().getDrawable(R.drawable.ac_support), null, null, null);
							}
							else {
								support.setCompoundDrawables(getResources().getDrawable(R.drawable.ac_support), null, null, null);
							}
						}
					}, apiQueue);
				}
				else {
					startActivity(new Intent(mContext, LoginActivity.class));
				}
			}
		});
		btn_add_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!UserManager.getInstance().isLogin()) {
					startActivity(new Intent(mContext, LoginActivity.class));
					return;
				}
				String reply = et_input.getText().toString().trim();
				if (reply.length() == 0) {
					Toast.makeText(mContext, "请输入评论内容~", 1000).show();
					return;
				}
				showProgressDialog();
				if (isreply && reply.startsWith(replyPrefix)) {
					String replycontent = (String) reply.subSequence(replyPrefix.length(), reply.length());
					ReplyCommentRequest replyCommentRequest;
					try {
						replyCommentRequest = new ReplyCommentRequest(
								reply_commentid, replycontent,
								UserManager.getInstance().getUserInfo().getUid(),
								reply_to_uid, new Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										dissmissProgressDialog();
										try {
											if (response != null && response.getInt("result") == 1) {
												Toast.makeText(mContext, "回复评论成功", 1000).show();
												refreshData();
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
						apiQueue.add(replyCommentRequest);

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					PublishCommentRequest request;
					try {
						request = new PublishCommentRequest(
								currStatement.getEnrollid(), 
								"enrollid", UserManager.getInstance().getUserInfo().getUid(),
								reply, new Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										dissmissProgressDialog();
										try {
											if (response != null && response.getInt("result") == 1) {
												Toast.makeText(mContext, "评论成功", 1000).show();
												refreshData();
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
						apiQueue.add(request);

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
	}
	
	private class CommentDetailAdapter extends BaseAdapter{
		private ArrayList<CommentEntity> comments;
		private ViewHolder holder;

		public CommentDetailAdapter(ArrayList<CommentEntity> commentEntities) {
			comments = commentEntities;
		}
		@Override
		public int getCount() {
			return comments == null ? 0 :comments.size();
		}

		@Override
		public Object getItem(int position) {
			return comments == null ? null:comments.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final CommentEntity commentEntity = comments.get(position);
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.list_item_comment_detail, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				holder.tv_comment_content = (TextView) convertView.findViewById(R.id.tv_comment_content);
				holder.ll_content = (LinearLayout) convertView.findViewById(R.id.ll_content);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(commentEntity.getAuthorimg(), holder.iv_icon);
			holder.tv_name.setText(commentEntity.getAuthor());
			holder.tv_time.setText(DateFormat.format("yyyy.MM.dd kk:mm", commentEntity.getCommenttime() *1000));
			holder.tv_comment_content.setText(commentEntity.getComment());
			if (commentEntity.getReplys() != null && commentEntity.getReplys().size() != 0) {
				holder.ll_content.removeAllViews();
				for(ReplyEntity replyEntity:commentEntity.getReplys()){
					addReplyView(replyEntity,holder.ll_content);
				}
			}
			else {
				holder.ll_content.removeAllViews();
			}
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					isreply = true;
					reply_to_uid = commentEntity.getAuthorid();
					reply_commentid = commentEntity.getCommentid();
					replyPrefix = "回复"+commentEntity.getAuthor()+":";
					et_input.setText(replyPrefix);
				}
			});
			return convertView;
		}
		
		private void addReplyView(final ReplyEntity replyEntity,
				final LinearLayout ll_content) {
			View replyview = getLayoutInflater().inflate(R.layout.view_comment_reply, null);
			TextView tv_replier,tv_reply_to,tv_reply_time;
			tv_replier = (TextView) replyview.findViewById(R.id.tv_replier);
			tv_reply_time = (TextView) replyview.findViewById(R.id.tv_reply_time);
			tv_replier.setText(Html.fromHtml(PPApplication.getInstance().getFormatString(R.string.statements_commment_content,
					replyEntity.getFrom_username(),replyEntity.getTo_username(),replyEntity.getContent())));
			tv_reply_time.setText(DateFormat.format("yyyy.MM.dd kk:mm", replyEntity.getCreatetime() *1000));
			tv_replier.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					isreply = true;
					reply_to_uid = replyEntity.getFromid();
					reply_commentid = replyEntity.getCommentid();
					replyPrefix = "回复"+replyEntity.getFrom_username()+":";
					et_input.setText(replyPrefix);
				}
			});
			
			ll_content.addView(replyview);
		}

		private class ViewHolder {
			public ImageView iv_icon;
			public TextView tv_name,tv_time,tv_comment_content;
			public LinearLayout ll_content;
		}
		
	}
	
	public static Intent getIntent2Me(Context context,int enrollid){
		Intent intent = new Intent(context, CommentDetailActivity.class);
		intent.putExtra("enrollid", enrollid);
		return intent;
	}
}
