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
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ipinpar.app.network.api.DreamShowDetailRequest;
import com.ipinpar.app.network.api.ExperienceDiaryRequest;
import com.ipinpar.app.network.api.PartyExperienceDetailRequest;
import com.ipinpar.app.network.api.PublishCommentRequest;
import com.ipinpar.app.network.api.ReplyCommentRequest;
import com.ipinpar.app.network.api.StatementCommentListRequest;
import com.ipinpar.app.network.api.StatementDetailRequest;
import com.ipinpar.app.view.CircularImageView;
import com.ipinpar.app.widget.PartyAgreeDialog;
import com.ipinpar.app.widget.PullToRefreshListView;
import com.ipinpar.app.widget.PullToRefreshListView.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommentDetailActivity extends PPBaseActivity {

	private TextView name, time, content, support, comment;
	private ImageView userImage, iv_statement_support,iv_img;
	private AcStatementEntity currStatement;
	private ArrayList<CommentEntity> comments = new ArrayList<CommentEntity>();
	private CommentDetailAdapter adapter;
	private PullToRefreshListView lv_infolist;
	private EditText et_input;
	private Button btn_add_new;
	private View RL_support,RL_comment;
	private View view_1;

	private boolean isreply;
	private int reply_commentid;
	private int reply_to_uid;
	private String replyPrefix;
	private int fromid;
	private String fromidtype;
	private String nameString, contentString, peer_uidString;
	private long timeLong;
	private int agreecount, commentcount;
	private String imgurl;
	private int experienceingd;
	private PartyAgreeDialog partyAgreeDialog;
	private View RL_creative,RL_funny,RL_practical,RL_confused;
	private int authorid;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_comments_list);
		fromid = getIntent().getIntExtra("fromid", 0);
//		authorid = getIntent().getIntExtra("authorid", 0);
		fromidtype = getIntent().getStringExtra("fromidtype");
		String title = getIntent().getStringExtra("title");
		if (!TextUtils.isEmpty(title)) {
			setTitleText(title);
		}
		view_1 = getLayoutInflater().inflate(R.layout.statement_list_item, null);
		lv_infolist = (PullToRefreshListView) findViewById(R.id.lv_infolist);
		lv_infolist.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				
				if ("enrollid".equals(fromidtype)) {
					refreshStatement();
				} else if ("sid".equals(fromidtype)) {
					refreshExperienceDiary();
				}
				else if ("dreamid".equals(fromidtype)) {
					refreshDreamshow();
				}else if("experiencingid".equals(fromidtype)){
					refreshPartyExperience();
				}
			}
		});
		userImage = (CircularImageView) view_1.findViewById(R.id.statement_image);
		name = (TextView) view_1.findViewById(R.id.statement_user_name);
		time = (TextView) view_1.findViewById(R.id.statement_time);
		content = (TextView) view_1.findViewById(R.id.statement_content);
		support = (TextView) view_1.findViewById(R.id.tv_statement_support_num);
		comment = (TextView) view_1.findViewById(R.id.tv_statement_comment_num);
		iv_statement_support = (ImageView) view_1.findViewById(R.id.iv_statement_support);
		iv_img = (ImageView) view_1.findViewById(R.id.iv_img);
		RL_support = view_1.findViewById(R.id.RL_support);
		RL_comment = view_1.findViewById(R.id.RL_comment);
		
		lv_infolist.addHeaderView(view_1);
		
		et_input = (EditText) findViewById(R.id.et_input);
		btn_add_new = (Button) findViewById(R.id.btn_add_new);
		
		partyAgreeDialog = new PartyAgreeDialog(mContext,  
                R.layout.dialog_party_agree, R.style.PartyDialogTheme);  
		
		RL_creative = partyAgreeDialog.findViewById(R.id.RL_dialog_party_agree_creative);
		RL_funny = partyAgreeDialog.findViewById(R.id.RL_dialog_party_agree_funny);
		RL_practical = partyAgreeDialog.findViewById(R.id.RL_dialog_party_agree_practical);
		RL_confused = partyAgreeDialog.findViewById(R.id.RL_dialog_party_agree_confused);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if ("enrollid".equals(fromidtype)) {
			refreshStatement();
		} else if ("sid".equals(fromidtype)) {
			refreshExperienceDiary();
		}
		else if ("dreamid".equals(fromidtype)) {
			refreshDreamshow();
		}else if("experiencingid".equals(fromidtype)){
			refreshPartyExperience();
		}
	}

	private void refreshStatement() {
		showProgressDialog();
		StatementDetailRequest request = new StatementDetailRequest(fromid,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						dissmissProgressDialog();
						if (lv_infolist != null) {
							lv_infolist.onRefreshComplete();
						}
						try {
							if (response != null
									&& response.getInt("result") == 1) {
								dissmissProgressDialog();
								Gson gson = new Gson();
								AcStatementEntity statementEntity = gson
										.fromJson(response.toString(),
												AcStatementEntity.class);
								currStatement = statementEntity;
								nameString = currStatement.getUsername();
								timeLong = Long.parseLong(currStatement
										.getCreatetime());
								contentString = currStatement.getDeclaration();
								agreecount = currStatement.getAgreecount();
								commentcount = currStatement.getCommentcount();
								peer_uidString = currStatement.getUid() + "";
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

	private void refreshExperienceDiary() {
		ExperienceDiaryRequest request = new ExperienceDiaryRequest(fromid,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						dissmissProgressDialog();
						if (lv_infolist != null) {
							lv_infolist.onRefreshComplete();
						}
						try {
							if (response != null
									&& response.getInt("result") == 1) {
								contentString = response.getString("title");
								agreecount = response.getInt("agreecount");
								commentcount = response.getInt("commentcount");
								nameString = response.getString("username");
								peer_uidString = response.getInt("uid") + "";
								timeLong = response.getLong("createtime");
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
	
	private void refreshDreamshow(){
		showProgressDialog();
		DreamShowDetailRequest request = new DreamShowDetailRequest(fromid, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				dissmissProgressDialog();
				if (lv_infolist != null) {
					lv_infolist.onRefreshComplete();
				}
				try {
					if (response != null && response.getInt("result") == 1) {
						Log.e("dreamshow comment", response.toString());
						contentString = response.getString("title");
						agreecount = response.getInt("agreecount");
						commentcount = response.getInt("commentcount");
						nameString = response.getString("username");
						peer_uidString = response.getInt("uid") + "";
						timeLong = response.getLong("createtime");
						imgurl = response.getString("author_img");
						setupViews();
						refreshData();
						if (TextUtils.isEmpty(response.getString("author_img"))) {
							
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
	
	private void refreshPartyExperience(){
		showProgressDialog();
		PartyExperienceDetailRequest request = new PartyExperienceDetailRequest(fromid, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				dissmissProgressDialog();
				if (lv_infolist != null) {
					lv_infolist.onRefreshComplete();
				}
				try {
					if (response != null && response.getInt("result") == 1) {
						Log.e("partyexperience comment", response.toString());
						contentString = response.getString("content");
						agreecount = response.getInt("agreecount");
						commentcount = response.getInt("commentcount");
						nameString = response.getString("username");
						peer_uidString = response.getInt("authorid") + "";
						authorid = response.getInt("authorid");
						timeLong = response.getLong("createtime");
						imgurl = response.getString("img");
//						setupViews();
						setupPartyExperienceViews();
						refreshData();
						if (TextUtils.isEmpty(response.getString("author_img"))) {
							
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

	private void refreshData() {
		showProgressDialog();
		StatementCommentListRequest request = new StatementCommentListRequest(
				fromid, fromidtype, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						dissmissProgressDialog();
						try {
							if (response != null
									&& response.getInt("result") == 1) {
								comments.clear();
								Gson gson = new Gson();
								Type token = new TypeToken<ArrayList<CommentEntity>>() {
								}.getType();
								comments.addAll((ArrayList<CommentEntity>) gson
										.fromJson(
												response.getJSONArray(
														"comments").toString(),
												token));
								if (adapter == null) {
									adapter = new CommentDetailAdapter(comments);
									lv_infolist.setAdapter(adapter);
								} else {
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

	private void setupViews() {
		ImageLoader.getInstance().displayImage(
				"http://api.ipinpar.com/pinpaV2/api.pinpa?protocol=10008&a="
						+ peer_uidString, userImage);
		name.setText(nameString);

		long timel = timeLong * 1000;
		time.setText(DateFormat.format("yyyy/MM/dd    kk:mm", timel));
		content.setText(contentString);
		support.setText(agreecount + "");
		comment.setText(commentcount + "");
		if (!TextUtils.isEmpty(imgurl)) {
			ImageLoader.getInstance().displayImage(imgurl, iv_img);
		}
		else {
			iv_img.setVisibility(View.GONE);
		}
		view_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isreply = false;
				reply_to_uid = 0;
				replyPrefix = "";
				et_input.setText("");
			}
		});
		RL_support.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (UserManager.getInstance().isLogin()) {
					if (AgreeManager.getInstance().isAgreed(fromid, fromidtype)) {
						AgreeManager.getInstance().agree(fromid, fromidtype,
								new AgreeResultListener() {

									@Override
									public void onAgreeResult(boolean agree) {
										if (!agree) {
											support.setText((--agreecount) + "");
											iv_statement_support
													.setImageResource(R.drawable.ac_support);
										}
									}
								}, apiQueue);
					} else {
						AgreeManager.getInstance().agree(fromid, fromidtype,
								new AgreeResultListener() {

									@Override
									public void onAgreeResult(boolean agree) {
										if (agree) {
											support.setText((++agreecount) + "");
											iv_statement_support
													.setImageResource(R.drawable.enroll_fist);
										}
									}
								}, apiQueue);
					}
				} else {
					mContext.startActivity(new Intent(mContext,
							LoginActivity.class));
				}
			}
		});
		RL_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isreply = false;
				reply_to_uid = 0;
				replyPrefix = "";
				et_input.setText("");
			}
		});
		if (UserManager.getInstance().isLogin()) {
			if (AgreeManager.getInstance().isAgreed(fromid, fromidtype)) {
				iv_statement_support.setImageResource(R.drawable.enroll_fist);
			}
		}
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
					String replycontent = (String) reply.subSequence(
							replyPrefix.length(), reply.length());
					ReplyCommentRequest replyCommentRequest;
					try {
						replyCommentRequest = new ReplyCommentRequest(
								reply_commentid, replycontent, UserManager
										.getInstance().getUserInfo().getUid(),
								reply_to_uid, new Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										dissmissProgressDialog();
										try {
											if (response != null
													&& response
															.getInt("result") == 1) {
												Toast.makeText(mContext,
														"回复评论成功", 1000).show();
												InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
												imm.toggleSoftInput(
														0,
														InputMethodManager.HIDE_NOT_ALWAYS);
												et_input.setText("");
												agreecount++;
												setupViews();
												refreshData();
											} else {
												Toast.makeText(mContext,
														"发送失败", 1000).show();
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
				} else if(isreply){
					ReplyCommentRequest replyCommentRequest2;
					try {
						replyCommentRequest2 = new ReplyCommentRequest(
								reply_commentid, reply, UserManager
										.getInstance().getUserInfo().getUid(),
								reply_to_uid, new Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										dissmissProgressDialog();
										try {
											if (response != null
													&& response
															.getInt("result") == 1) {
												Toast.makeText(mContext,
														"回复评论成功", 1000).show();
												InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
												imm.toggleSoftInput(
														0,
														InputMethodManager.HIDE_NOT_ALWAYS);
												et_input.setText("");
												refreshData();
											} else {
												Toast.makeText(mContext,
														"发送失败", 1000).show();
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
						apiQueue.add(replyCommentRequest2);

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					PublishCommentRequest request;
					try {
						request = new PublishCommentRequest(fromid, fromidtype,
								UserManager.getInstance().getUserInfo()
										.getUid(), reply,
								new Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										dissmissProgressDialog();
										try {
											if (response != null
													&& response
															.getInt("result") == 1) {
												InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
												imm.toggleSoftInput(
														0,
														InputMethodManager.HIDE_NOT_ALWAYS);
												et_input.setText("");
												Toast.makeText(mContext,
														"评论成功", 1000).show();
												refreshData();
												
												//刷新评论
//												refreshStatement();
//												commentcount++;
//												comment.setText(commentcount + "");
												
												if ("enrollid".equals(fromidtype)) {
													refreshStatement();
												} else if ("sid".equals(fromidtype)) {
													refreshExperienceDiary();
												}
												
											} else {
												Toast.makeText(mContext,
														"发送失败", 1000).show();

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
	
	private void setupPartyExperienceViews() {
		ImageLoader.getInstance().displayImage(
				"http://api.ipinpar.com/pinpaV2/api.pinpa?protocol=10008&a="
						+ peer_uidString, userImage);
		name.setText(nameString);

		long timel = timeLong * 1000;
		time.setText(DateFormat.format("yyyy/MM/dd    kk:mm", timel));
		content.setText(contentString);
		support.setText(agreecount + "");
		comment.setText(commentcount + "");
		if (!TextUtils.isEmpty(imgurl)) {
			ImageLoader.getInstance().displayImage(imgurl, iv_img);
		}
		view_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isreply = false;
				reply_to_uid = 0;
				replyPrefix = "";
				et_input.setText("");
			}
		});
		RL_support.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (UserManager.getInstance().isLogin()) {
					//已经点过赞
					if (AgreeManager.getInstance().isAgreed(fromid, "experiencingid")) {
						Toast.makeText(mContext, "请不要重复点赞哦～", 1000).show();
					}
					else {
						partyAgreeDialog.show();
					}
				}
				else {
					mContext.startActivity(new Intent(mContext, LoginActivity.class));
				}
				
			}
		});
		RL_creative.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				partyAgreeDialog.dismiss();
				if (UserManager.getInstance().isLogin()) {
					if (!AgreeManager.getInstance().isAgreed(fromid, "experiencingid")) {
						AgreeManager.getInstance().partyAgree(
								fromid, 
								"experiencingid",
								"1",
								new AgreeResultListener() {

									@Override
									public void onAgreeResult(boolean agree) {
										if (agree) {
											support.setText((++agreecount) + "");
											iv_statement_support
													.setImageResource(R.drawable.enroll_fist);
											Toast.makeText(mContext, "创意值 ＋1", 1000).show();
										}
									}
								}, apiQueue);
					} else {
						iv_statement_support
						.setImageResource(R.drawable.enroll_fist);
						Toast.makeText(mContext, "亲已经点赞了哦～4－1", 1000).show();
					}
				} else {
					mContext.startActivity(new Intent(mContext,
							LoginActivity.class));
				}
				
			}
		});
		RL_funny.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				partyAgreeDialog.dismiss();
				
				if (UserManager.getInstance().isLogin()) {
					if (!AgreeManager.getInstance().isAgreed(fromid, "experiencingid")) {
						AgreeManager.getInstance().partyAgree(
								fromid, 
								"experiencingid",
								"2",
								new AgreeResultListener() {

									@Override
									public void onAgreeResult(boolean agree) {
										if (agree) {
											support.setText((++agreecount) + "");
											iv_statement_support
													.setImageResource(R.drawable.enroll_fist);
											Toast.makeText(mContext, "搞笑值 ＋1", 1000).show();
										}
									}
								}, apiQueue);
					} else {
						iv_statement_support
						.setImageResource(R.drawable.enroll_fist);
						Toast.makeText(mContext, "亲已经点赞了哦～4－2", 1000).show();
					}
				} else {
					mContext.startActivity(new Intent(mContext,
							LoginActivity.class));
				}
			}
		});
		RL_practical.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				partyAgreeDialog.dismiss();
				
				if (UserManager.getInstance().isLogin()) {
					if (!AgreeManager.getInstance().isAgreed(fromid, "experiencingid")) {
						AgreeManager.getInstance().partyAgree(
								fromid, 
								"experiencingid",
								"3",
								new AgreeResultListener() {

									@Override
									public void onAgreeResult(boolean agree) {
										if (agree) {
											support.setText((++agreecount) + "");
											iv_statement_support
													.setImageResource(R.drawable.enroll_fist);
											Toast.makeText(mContext, "实用值 ＋1", 1000).show();
										}
									}
								}, apiQueue);
					} else {
						iv_statement_support
						.setImageResource(R.drawable.enroll_fist);
						Toast.makeText(mContext, "亲已经点赞了哦～4－3", 1000).show();
					}
				} else {
					mContext.startActivity(new Intent(mContext,
							LoginActivity.class));
				}
			}
		});
		RL_confused.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				partyAgreeDialog.dismiss();
				
				if (UserManager.getInstance().isLogin()) {
					if (!AgreeManager.getInstance().isAgreed(fromid, "experiencingid")) {
						AgreeManager.getInstance().partyAgree(
								fromid, 
								"experiencingid",
								"4",
								new AgreeResultListener() {

									@Override
									public void onAgreeResult(boolean agree) {
										if (agree) {
											support.setText((++agreecount) + "");
											iv_statement_support
													.setImageResource(R.drawable.enroll_fist);
											Toast.makeText(mContext, "太囧值 ＋1", 1000).show();
										}
									}
								}, apiQueue);
					} else {
						iv_statement_support
						.setImageResource(R.drawable.enroll_fist);
						Toast.makeText(mContext, "亲已经点赞了哦～4－4", 1000).show();
					}
				} else {
					mContext.startActivity(new Intent(mContext,
							LoginActivity.class));
				}
			}
		});
		RL_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isreply = false;
				reply_to_uid = 0;
				replyPrefix = "";
				et_input.setText("");
			}
		});
		if (UserManager.getInstance().isLogin()) {
			if (AgreeManager.getInstance().isAgreed(fromid, fromidtype)) {
				iv_statement_support.setImageResource(R.drawable.enroll_fist);
			}
		}
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
					String replycontent = (String) reply.subSequence(
							replyPrefix.length(), reply.length());
					ReplyCommentRequest replyCommentRequest;
					try {
						replyCommentRequest = new ReplyCommentRequest(
								reply_commentid, replycontent, UserManager
										.getInstance().getUserInfo().getUid(),
								reply_to_uid, new Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										dissmissProgressDialog();
										try {
											if (response != null
													&& response
															.getInt("result") == 1) {
												Toast.makeText(mContext,
														"回复评论成功", 1000).show();
												InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
												imm.toggleSoftInput(
														0,
														InputMethodManager.HIDE_NOT_ALWAYS);
												et_input.setText("");
												refreshData();
											} else {
												Toast.makeText(mContext,
														"发送失败", 1000).show();
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
				} else if(isreply){
					ReplyCommentRequest replyCommentRequest2;
					try {
						replyCommentRequest2 = new ReplyCommentRequest(
								reply_commentid, reply, UserManager
										.getInstance().getUserInfo().getUid(),
								reply_to_uid, new Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										dissmissProgressDialog();
										try {
											if (response != null
													&& response
															.getInt("result") == 1) {
												Toast.makeText(mContext,
														"回复评论成功", 1000).show();
												InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
												imm.toggleSoftInput(
														0,
														InputMethodManager.HIDE_NOT_ALWAYS);
												et_input.setText("");
												refreshData();
											} else {
												Toast.makeText(mContext,
														"发送失败", 1000).show();
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
						apiQueue.add(replyCommentRequest2);

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					PublishCommentRequest request;
					try {
						request = new PublishCommentRequest(fromid, fromidtype,
								UserManager.getInstance().getUserInfo()
										.getUid(), reply,
								new Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										dissmissProgressDialog();
										try {
											if (response != null
													&& response
															.getInt("result") == 1) {
												InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
												imm.toggleSoftInput(
														0,
														InputMethodManager.HIDE_NOT_ALWAYS);
												et_input.setText("");
												Toast.makeText(mContext,
														"评论成功", 1000).show();
												refreshData();
												
												//刷新评论
//												refreshStatement();
//												commentcount++;
//												comment.setText(commentcount + "");
												
												if ("enrollid".equals(fromidtype)) {
													refreshStatement();
												} else if ("sid".equals(fromidtype)) {
													refreshExperienceDiary();
												}
												
											} else {
												Toast.makeText(mContext,
														"发送失败", 1000).show();

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

	private class CommentDetailAdapter extends BaseAdapter {
		private ArrayList<CommentEntity> comments;
		private ViewHolder holder;

		public CommentDetailAdapter(ArrayList<CommentEntity> commentEntities) {
			comments = commentEntities;
		}

		@Override
		public int getCount() {
			return comments == null ? 0 : comments.size();
		}

		@Override
		public Object getItem(int position) {
			return comments == null ? null : comments.get(position);
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
				convertView = getLayoutInflater().inflate(
						R.layout.list_item_comment_detail, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.tv_comment_content = (TextView) convertView
						.findViewById(R.id.tv_comment_content);
				holder.ll_content = (LinearLayout) convertView
						.findViewById(R.id.ll_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(
					commentEntity.getAuthorimg(), holder.iv_icon);
			holder.tv_name.setText(commentEntity.getAuthor());
			holder.tv_time.setText(DateFormat.format("yyyy.MM.dd kk:mm",
					commentEntity.getCommenttime() * 1000));
			holder.tv_comment_content.setText(commentEntity.getComment());
			if (commentEntity.getReplys() != null
					&& commentEntity.getReplys().size() != 0) {
				holder.ll_content.removeAllViews();
				for (ReplyEntity replyEntity : commentEntity.getReplys()) {
					addReplyView(replyEntity, holder.ll_content);
				}
			} else {
				holder.ll_content.removeAllViews();
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					isreply = true;
					reply_to_uid = commentEntity.getAuthorid();
					reply_commentid = commentEntity.getCommentid();
					replyPrefix = "回复" + commentEntity.getAuthor() + ":";
					et_input.setText(replyPrefix);
				}
			});
			holder.iv_icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!UserManager.getInstance().isLogin() || commentEntity.getAuthorid() != UserManager.getInstance().getUserInfo().getUid()) {
						startActivity(NameCardActivity.getIntent2Me(mContext,
								commentEntity.getAuthorid()));
					}
					
				}
			});
			return convertView;
		}

		private void addReplyView(final ReplyEntity replyEntity,
				final LinearLayout ll_content) {
			View replyview = getLayoutInflater().inflate(
					R.layout.view_comment_reply, null);
			TextView tv_replier, tv_reply_to, tv_reply_time;
			tv_replier = (TextView) replyview.findViewById(R.id.tv_replier);
			tv_reply_time = (TextView) replyview
					.findViewById(R.id.tv_reply_time);
			tv_replier.setText(Html.fromHtml(PPApplication.getInstance()
					.getFormatString(R.string.statements_commment_content,
							replyEntity.getFrom_username(),
							replyEntity.getTo_username(),
							replyEntity.getContent())));
			tv_reply_time.setText(DateFormat.format("yyyy.MM.dd kk:mm",
					replyEntity.getCreatetime() * 1000));
			tv_replier.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					isreply = true;
					reply_to_uid = replyEntity.getFromid();
					reply_commentid = replyEntity.getCommentid();
					replyPrefix = "回复" + replyEntity.getFrom_username() + ":";
					et_input.setText(replyPrefix);
				}
			});

			ll_content.addView(replyview);
		}

		private class ViewHolder {
			public ImageView iv_icon;
			public TextView tv_name, tv_time, tv_comment_content;
			public LinearLayout ll_content;
		}

	}

	public static Intent getIntent2Me(Context context, int fromid,
			String fromidtype) {
		Intent intent = new Intent(context, CommentDetailActivity.class);
		intent.putExtra("fromid", fromid);
		intent.putExtra("fromidtype", fromidtype);

		return intent;
	}
	
	public static Intent getIntent2Me(Context context, int fromid,int authorid,
			String fromidtype) {
		Intent intent = new Intent(context, CommentDetailActivity.class);
		intent.putExtra("fromid", fromid);
		intent.putExtra("authorid", authorid);
		intent.putExtra("fromidtype", fromidtype);

		return intent;
	}
	
	public static Intent getIntent2Me(Context context, int fromid,
			String fromidtype,String title) {
		Intent intent = new Intent(context, CommentDetailActivity.class);
		intent.putExtra("fromid", fromid);
		intent.putExtra("fromidtype", fromidtype);
		
		intent.putExtra("title", title);
		return intent;
	}
}
