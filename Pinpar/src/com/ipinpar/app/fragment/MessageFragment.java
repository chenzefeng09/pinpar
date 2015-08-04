package com.ipinpar.app.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.TextMessageBody;
import com.ipinpar.app.Constant;
import com.ipinpar.app.PPBaseFragment;
import com.ipinpar.app.R;
import com.ipinpar.app.activity.ChatActivity;
import com.ipinpar.app.activity.CommentsListActivity;
import com.ipinpar.app.activity.LoginActivity;
import com.ipinpar.app.activity.MainActivity;
import com.ipinpar.app.activity.NotificationListActivity;
import com.ipinpar.app.activity.SupportListActivity;
import com.ipinpar.app.adapter.MessageAdapter;
import com.ipinpar.app.entity.NotificationEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.GetUserInfoRequest;
import com.ipinpar.app.network.api.NotificationRequest;
import com.ipinpar.app.util.PreferenceUtils;
import com.ipinpar.app.util.SmileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class MessageFragment extends PPBaseFragment implements OnClickListener {
	private View rl_notification, rl_support, rl_comment;
	private ImageView ci_comment, ci_suppport, ci_notification;
	private TextView tv_newcomment, tv_newsupport, tv_newsup;
	private ArrayList<NotificationEntity> notifications = new ArrayList<NotificationEntity>();
	private ListView lv_message;
	private MessageAdapter adapter;
	private ArrayList<EMConversation> conversations  = new ArrayList<EMConversation>();
	private int unreadNotification;
	
	//处理聊天新消息
	private NewMessageBroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver();
	private IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
	private EMEventListener messageEnventListener = new EMEventListener() {
		
		@Override
		public void onEvent(EMNotifierEvent event) {

			switch (event.getEvent()) {	
			case EventNewMessage: // 接收新消息
			{
			EMMessage message = (EMMessage) event.getData();
			if (!EMChatManager.getInstance().areAllConversationsLoaded()) {
				EMChatManager.getInstance().asyncLoadAllConversations(new EMCallBack() {
					@Override
					public void onSuccess() {
						// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
						updateMessageList();
					}
					
					@Override
					public void onProgress(int arg0, String arg1) {
						// TODO Auto-generated method stub
						updateMessageList();
					}
					
					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						updateMessageList();
					}
				});
			}
			else {
				updateMessageList();
			}
				break;
			}
			case EventDeliveryAck:{//接收已发送回执
				
				break;
			}
			
			case EventNewCMDMessage:{//接收透传消息
				
				break;
			}
			
			case EventReadAck:{//接收已读回执
				
				break;
			}
			case EventOfflineMessage: {//接收离线消息
				List<EMMessage> messages = (List<EMMessage>) event.getData();
				if (!EMChatManager.getInstance().areAllConversationsLoaded()) {
					EMChatManager.getInstance().asyncLoadAllConversations(new EMCallBack() {
						@Override
						public void onSuccess() {
							// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
							updateMessageList();
						}
						
						@Override
						public void onProgress(int arg0, String arg1) {
							// TODO Auto-generated method stub
							updateMessageList();
						}
						
						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							updateMessageList();
						}
					});
				}
				else {
					updateMessageList();
				}
				break;
			}

			case EventConversationListChanged: {//通知会话列表通知event注册（在某些特殊情况，SDK去删除会话的时候会收到回调监听）
			    
			    break;
			}
			
			default:
				break;
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_message, null);
		initView(view);
		initChat();
		return view;
	}

	private void initChat() {
//		intentFilter.setPriority(Integer.MAX_VALUE);
//		getActivity().registerReceiver(msgReceiver, intentFilter);
		
		EMChatManager.getInstance().registerEventListener(messageEnventListener);
	}
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			// 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
			String msgId = intent.getStringExtra("msgid");
			//发送方
			String username = intent.getStringExtra("from");
			Toast.makeText(mContext, "收到新消息from:"+username, 1000).show();
			if (!EMChatManager.getInstance().areAllConversationsLoaded()) {
				EMChatManager.getInstance().asyncLoadAllConversations(new EMCallBack() {
					@Override
					public void onSuccess() {
						// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
						updateMessageList();
					}
					
					@Override
					public void onProgress(int arg0, String arg1) {
						// TODO Auto-generated method stub
						updateMessageList();
					}
					
					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						updateMessageList();
					}
				});
			}
			else {
				updateMessageList();
			}
			 // 注销广播
			abortBroadcast();
		}
	}
	
	public void refreshUnread(){
		refreshNotification();
		updateMessageList();
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			refreshNotification();
		}
	}
	
	private void updateMessageList(){
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (adapter == null) {
					Hashtable<String, EMConversation> maps = EMChatManager
							.getInstance().getAllConversations();
					conversations.clear();;
					for (Entry<String, EMConversation> entry : maps.entrySet()) {
						conversations.add(entry.getValue());
					}
					adapter = new MessageAdapter(conversations);
					lv_message.setAdapter(adapter);
				} else {
					Hashtable<String, EMConversation> maps = EMChatManager
							.getInstance().getAllConversations();
					conversations.clear();;
					for (Entry<String, EMConversation> entry : maps.entrySet()) {
						conversations.add(entry.getValue());
					}
					adapter.notifyDataSetChanged();
				}
				MainActivity mainActivity = (MainActivity) getActivity();
				mainActivity.setUnreadCount(EMChatManager.getInstance().getUnreadMsgsCount()+unreadNotification);
			}
		});
		
	}

	private void initView(View view) {
		rl_notification = view.findViewById(R.id.rl_notification);
		rl_support = view.findViewById(R.id.rl_support);
		rl_comment = view.findViewById(R.id.rl_comment);
		tv_newcomment = (TextView) view.findViewById(R.id.tv_newcomment);
		tv_newsupport = (TextView) view.findViewById(R.id.tv_newsupport);
		tv_newsup = (TextView) view.findViewById(R.id.tv_newsup);
		lv_message = (ListView) view.findViewById(R.id.lv_message);
		rl_comment.setOnClickListener(this);
		rl_notification.setOnClickListener(this);
		rl_support.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	    MobclickAgent.onPageStart("PinparMessageFragment"); //统计页面
	    refreshNotification();
	}
	
	private void refreshNotification(){
		if (UserManager.getInstance().isLogin()) {
			lv_message.setVisibility(View.VISIBLE);
			
			NotificationRequest request = new NotificationRequest(UserManager
					.getInstance().getUserInfo().getUid(), 1, 100,
					new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub
							try {
								if (response != null
										&& response.getInt("result") == 1) {
									notifications.clear();
									int new_total = Integer.parseInt(response
											.getString("newtotal"));
									int total = Integer.parseInt(response
											.getString("total"));
									JSONArray jsonArray = response
											.getJSONArray("data");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject = (JSONObject) jsonArray
												.get(i);
										NotificationEntity notificationEntity = new NotificationEntity();
										notificationEntity.setAuthor(jsonObject
												.getString("author"));
										notificationEntity
												.setAuthorid(jsonObject
														.getInt("authorid"));
										notificationEntity
												.setDateline(jsonObject
														.getLong("dateline"));
										notificationEntity
												.setFrom_id(jsonObject
														.getInt("from_id"));
										notificationEntity.setFrom_idtype(jsonObject
												.getString("from_idtype"));
										notificationEntity
												.setFrom_num(jsonObject
														.getInt("from_num"));
										notificationEntity.setId(jsonObject
												.getInt("id"));
										notificationEntity.setIs_new(jsonObject
												.getBoolean("new"));
										notificationEntity.setStatus(jsonObject
												.getInt("status"));
										notificationEntity.setType(jsonObject
												.getString("type"));
										notificationEntity.setUid(jsonObject
												.getInt("uid"));
										notifications.add(notificationEntity);
									}
									int commentcount = 0;
									int supportcount = 0;
									int notificationcount = 0;
									for (NotificationEntity notificationEntity : notifications) {
										if ("agree".equals(notificationEntity
												.getType())) {
											if (notificationEntity.getIs_new()) {
												supportcount++;
											}
										} else if ("comment"
												.equals(notificationEntity
														.getType())) {
											if (notificationEntity.getIs_new()) {
												commentcount++;
											}
										} else if ("friend"
												.equals(notificationEntity
														.getType())) {
											if (notificationEntity.getIs_new()) {
												notificationcount++;
											}

										} else if ("invite"
												.equals(notificationEntity
														.getType())) {
											if (notificationEntity.getIs_new()) {
												notificationcount++;
											}
										}
										else if ("invite".equals(notificationEntity.getType())) {
											if (notificationEntity.getIs_new()) {
												notificationcount++;
											}											}
									}
									
									tv_newcomment.setText(commentcount+"个新评论");
									tv_newsupport.setText(supportcount+"个新支持");
									tv_newsup.setText(notificationcount+"个新通知");
									if (supportcount == 0) {
										tv_newsupport.setVisibility(View.GONE);
									}
									else {
										tv_newsupport.setVisibility(View.VISIBLE);
									}
									if (commentcount == 0) {
										tv_newcomment.setVisibility(View.GONE);
									}
									else {
										tv_newcomment.setVisibility(View.VISIBLE);

									}
									if (notificationcount == 0) {
										tv_newsup.setVisibility(View.GONE);
									}
									else {
										tv_newsup.setVisibility(View.VISIBLE);
									}
									unreadNotification = notificationcount+supportcount+commentcount;
									MainActivity mainActivity = (MainActivity) getActivity();
									mainActivity.setUnreadCount(EMChatManager.getInstance().getUnreadMsgsCount()+unreadNotification);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
			apiQueue.add(request);
			if (EMChatManager.getInstance().isConnected() 
					&&!EMChatManager.getInstance().areAllConversationsLoaded()) {
				EMChatManager.getInstance().asyncLoadAllConversations(new EMCallBack() {
					
					@Override
					public void onSuccess() {
						// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								updateMessageList();
							}
						});
					}
					
					@Override
					public void onProgress(int arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			}
			else {
				updateMessageList();
			}
		}
		else {
			lv_message.setVisibility(View.GONE);
			tv_newsupport.setText("");
			tv_newcomment.setText("");
			tv_newsup.setText("");
			MainActivity mainActivity = (MainActivity) getActivity();
			mainActivity.setUnreadCount(0);
		}
	}

	private class MessageAdapter extends BaseAdapter {

		private ArrayList<EMConversation> conversations;
		private ViewHolder holder;

		public MessageAdapter(ArrayList<EMConversation> conversations) {
			this.conversations = conversations;
		}

		@Override
		public int getCount() {
			return conversations == null ? 0 : conversations.size();
		}

		@Override
		public Object getItem(int position) {
			return conversations == null ? null : conversations.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final EMConversation conversation = conversations.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_conversion, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				holder.tv_lastmsg = (TextView) convertView
						.findViewById(R.id.tv_lastmsg);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_unread = (TextView) convertView
						.findViewById(R.id.tv_unread);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (conversation.getUnreadMsgCount() == 0) {
				holder.tv_unread.setVisibility(View.GONE);
			} else {
				holder.tv_unread.setVisibility(View.VISIBLE);
				holder.tv_unread.setText(conversation.getUnreadMsgCount() + "");
			}
			if (conversation.getLastMessage().getType() == Type.TXT) {
				TextMessageBody messageBody = (TextMessageBody)conversation.getLastMessage().getBody();
				holder.tv_lastmsg.setText(SmileUtils.getSmiledText(mContext, messageBody.getMessage()));
			}
			else if (conversation.getLastMessage().getType() == Type.IMAGE) {
				holder.tv_lastmsg.setText("[图片]");
			}
			else if (conversation.getLastMessage().getType() == Type.VOICE) {
				holder.tv_lastmsg.setText("[语音]");
			}
			holder.tv_time.setText(formatTime(conversation.getLastMessage()
					.getMsgTime()));
			ImageLoader.getInstance().displayImage(Constant.URL_GET_USERIMAGE+conversation.getUserName(), holder.iv_icon);
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, ChatActivity.class);
					intent.putExtra("peer_name", PreferenceUtils.getPrefString(mContext,
							"usernamekey"+conversation.getUserName(), ""));
					intent.putExtra("userId", conversation.getUserName());
					startActivity(intent);
					
				}
			});
			if (PreferenceUtils.hasKey(mContext, "usernamekey"+conversation.getUserName())) {
				holder.tv_name.setText(PreferenceUtils.getPrefString(mContext,
						"usernamekey"+conversation.getUserName(), conversation.getUserName()+""));
			}
			else {
				GetUserInfoRequest request = new GetUserInfoRequest(
						conversation.getUserName(), new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response != null && response.getInt("result") == 1) {
								PreferenceUtils.setPrefString(mContext, "usernamekey"+conversation.getUserName(), response.getString("username"));
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
			return convertView;
		}

		private String formatTime(long dateline) {
			Date datemsg = new Date(dateline *1000);
			Date datenow = new Date(System.currentTimeMillis());
			Calendar calendarmsg = Calendar.getInstance();
			calendarmsg.setTime(datemsg);
			Calendar calendarnow = Calendar.getInstance();
			calendarnow.setTime(datenow);
			if (calendarnow.get(Calendar.DAY_OF_YEAR) - calendarmsg.get(Calendar.DAY_OF_YEAR) == 1) {
				return "昨天";
			}
			else if (calendarnow.get(Calendar.DAY_OF_YEAR) - calendarmsg.get(Calendar.DAY_OF_YEAR) >= 1) {
				SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
				return format.format(datemsg);
			}
			else {
				SimpleDateFormat format = new SimpleDateFormat("hh:mm");
				return format.format(datemsg);
			}

		}

		private class ViewHolder {
			public TextView tv_unread, tv_name, tv_lastmsg, tv_time;
			public ImageView iv_icon;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_notification:
			if (UserManager.getInstance().isLogin()) {
				startActivity(new Intent(mContext,
						NotificationListActivity.class));
			} else {
				startActivity(new Intent(mContext, LoginActivity.class));
			}
			break;
		case R.id.rl_support:
			if (UserManager.getInstance().isLogin()) {
				startActivity(new Intent(mContext, SupportListActivity.class));
			} else {
				startActivity(new Intent(mContext, LoginActivity.class));
			}
			break;
		case R.id.rl_comment:
			if (UserManager.getInstance().isLogin()) {
				startActivity(new Intent(mContext, CommentsListActivity.class));
			} else {
				startActivity(new Intent(mContext, LoginActivity.class));
			}
			break;

		default:
			break;
		}
	}
	

	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("PinparActivityListFragment"); 
//	    getActivity().unregisterReceiver(msgReceiver);
	}
}
