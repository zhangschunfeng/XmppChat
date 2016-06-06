package zcf.demo.activity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import zcf.demo.adapter.ChatAdapter;
import zcf.demo.adapter.EmoViewPagerAdapter;
import zcf.demo.adapter.EmoteAdapter;
import zcf.demo.bean.FaceText;
import zcf.demo.bean.ImMessage;
import zcf.demo.bean.User;
import zcf.demo.db.SmsOpenHelper;
import zcf.demo.provider.SmsProvider;

import zcf.demo.utils.FaceTextUtils;
import zcf.demo.view.EmoticonsEditText;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import zcf.demo.service.*;

public class ChatActivity1 extends Activity implements OnClickListener,OnTouchListener{
	private TextView  chat_title,tv_voice_tips;
	private ImageView iv_record;
	private Button btn_chat_add,btn_chat_emo,btn_speak,btn_chat_voice,btn_chat_keyboard
	,btn_chat_send;
	private EmoticonsEditText emotionEditText;
	private LinearLayout layout_add,layout_more,layout_emo;
	private RelativeLayout layout_record;
	private ViewPager pager_emo;
    private List<FaceText> emos;

    private Chat currentChat = null;
	private List<ImMessage> message_pool = null;
	protected String to;// 聊天人
	private User user;
	private ChatAdapter chatAdapter;
	private MyMessageListener myMessageListener;
	private ListView mListView;
	private ChatManager chatManager;
	private Map<String, Chat> mChatMap	= new HashMap<>();
	public static final int  HANDLER_TO=1,HANDLER_FROM=2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		registerContentObserver();
		initView();
		initData();
		initChat();
		initemotionPager();
		
		//registerContentObserver();
	}

	private void initChat() {
		
		myMessageListener=new MyMessageListener();
		if(chatManager==null)
		{
			chatManager=ImChatService.getInstance().getChatManager();
		}
		currentChat =chatManager.createChat(user.getCount(), myMessageListener);		
						
		
	}
	

	// 接受到的message的事件监听接口
	private class MyMessageListener implements MessageListener
	{

		@Override
		public void processMessage(Chat chat, Message message) {
					final Message msg=message;
					final String body=message.getBody();
					new Thread(new Runnable()
							{
								@Override
								public void run() {	
									ImMessage imMessage=new ImMessage(ChatAdapter.TXT_FROM,body,new Date());
									message_pool.add(imMessage);
									handler.sendEmptyMessage(HANDLER_FROM);
		  						    saveMessage(msg.getFrom(), msg);
									
								}
								
							}).start();
				      
		}
		
	}
	
	/*
	 * 初始化表情字符
	 */
	private void initemotionPager() {
		emos = FaceTextUtils.faceTexts;
		List<View> views = new ArrayList<View>();
		for (int i = 0; i < 2; ++i) {
			views.add(getGridView(i));
		}
		pager_emo.setAdapter(new EmoViewPagerAdapter(views));
	}


	private View getGridView(int i) {
		View view = View.inflate(this, R.layout.include_emo_gridview, null);
		GridView gridview = (GridView) view.findViewById(R.id.gridview);
		List<FaceText> list = new ArrayList<FaceText>();
		if (i == 0) {
			list.addAll(emos.subList(0, 21));
		} else if (i == 1) {
			list.addAll(emos.subList(21, emos.size()));
		}
		final EmoteAdapter gridAdapter = new EmoteAdapter(ChatActivity1.this,
				list);
		gridview.setAdapter(gridAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				FaceText name = (FaceText) gridAdapter.getItem(position);
				String key = name.text.toString();
				try {
					if (emotionEditText != null && !TextUtils.isEmpty(key)) {
						int start = emotionEditText.getSelectionStart();
						CharSequence content = emotionEditText.getText()
								.insert(start, key);
						emotionEditText.setText(content);
						// 定位光标位置
						CharSequence info = emotionEditText.getText();
						if (info instanceof Spannable) {
							Spannable spanText = (Spannable) info;
							Selection.setSelection(spanText,
									start + key.length());
						}
					}
				} catch (Exception e) {

				}

			}
		});
		return view;
	}

	private void initData() {
		//获取user对象
		user=(User) this.getIntent().getSerializableExtra("user");
		chat_title.setText("正在和" + user.getNickName() + "聊天");
		message_pool = new ArrayList<ImMessage>();
		chatAdapter = new ChatAdapter(this, message_pool,mListView);
		mListView.setAdapter(chatAdapter);
		new Thread(new Runnable() {

			@Override
			public void run() {
				Cursor cursor =  getContentResolver().query(SmsProvider.URI_SMS,//
						null,
						"(from_account = ? and to_account=?)or(from_account = ? and to_account= ? )",// where条件
						new String[] { ImChatService.mCurAccout, user.getCount(), user.getCount(), ImChatService.mCurAccout },// where条件的参数
						SmsOpenHelper.SmsTable.TIME + "  ASC"// 根据时间升序排序
						);
				ImMessage message;
				while (cursor.moveToNext()) {
					String msg = cursor.getString(cursor
							.getColumnIndex(SmsOpenHelper.SmsTable.BODY));
							
					String time = cursor.getString(cursor
							.getColumnIndex(SmsOpenHelper.SmsTable.TIME));
					int type=ChatAdapter.TXT_TO;
					if(cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.
							FROM_ACCOUNT)).equalsIgnoreCase(user.getCount()))
					{
						type=ChatAdapter.TXT_FROM;
					}
					message = new ImMessage(type, msg,
							new Date(Long.parseLong(time)));
					message_pool.add(message);
					chatAdapter.notifyDataSetChanged();
					mListView.setSelection(message_pool.size() - 1);
			}

			}

		}).start();
		// // 根据输入框输入值的改变布局
		emotionEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					btn_chat_send.setVisibility(View.VISIBLE);
					btn_chat_keyboard.setVisibility(View.GONE);
					btn_chat_voice.setVisibility(View.GONE);
				} else {
					if (btn_chat_voice.getVisibility() != View.VISIBLE) {
						btn_chat_voice.setVisibility(View.VISIBLE);
						btn_chat_send.setVisibility(View.GONE);
						btn_chat_keyboard.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}


	private void initView() {
		chat_title=(TextView) this.findViewById(R.id.chat_title);
		//语音的view
		tv_voice_tips=(TextView) this.findViewById(R.id.tv_voice_tips);
		iv_record=(ImageView) this.findViewById(R.id.iv_record);
		//
		btn_chat_add=(Button) this.findViewById(R.id.btn_chat_add);
		btn_chat_emo=(Button) this.findViewById(R.id.btn_chat_emo);
		btn_speak=(Button) this.findViewById(R.id.btn_speak);
		btn_chat_voice=(Button) this.findViewById(R.id.btn_chat_voice);
		btn_chat_keyboard=(Button) this.findViewById(R.id.btn_chat_keyboard);
		btn_chat_send=(Button) this.findViewById(R.id.btn_chat_send);
		emotionEditText=(EmoticonsEditText) this.findViewById(R.id.edit_user_comment);
		layout_add=(LinearLayout) this.findViewById(R.id.layout_add);
		layout_more=(LinearLayout) this.findViewById(R.id.layout_more);
		layout_emo=(LinearLayout) this.findViewById(R.id.layout_emo);
		pager_emo=(ViewPager) this.findViewById(R.id.pager_emo);
		layout_record=(RelativeLayout) this.findViewById(R.id.layout_record);
		// listview
		mListView=(ListView) this.findViewById(R.id.mListView);
		btn_chat_add.setOnClickListener(this);
        btn_chat_emo.setOnClickListener(this);	
        btn_chat_voice.setOnClickListener(this);
        btn_speak.setOnTouchListener(this);
        btn_chat_send.setOnClickListener(this);
        btn_chat_keyboard.setOnClickListener(this);
        emotionEditText.setOnClickListener(this);
	}


	//
	void resetView()
	{
		btn_speak.setVisibility(View.GONE);
 		emotionEditText.setVisibility(View.VISIBLE);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.edit_user_comment:// 点击文本输入框
			 mListView.setSelection(mListView.getCount() - 1);
			if (layout_more.getVisibility() == View.VISIBLE) {
				layout_add.setVisibility(View.GONE);
				layout_emo.setVisibility(View.GONE);
				layout_more.setVisibility(View.GONE);
			}
			break;
		case R.id.btn_chat_add:
			if (layout_more.getVisibility() == View.GONE) {
				layout_more.setVisibility(View.VISIBLE);
				layout_add.setVisibility(View.VISIBLE);
				layout_emo.setVisibility(View.GONE);
				//hideSoftInputView();
			} 
			else {
				if (layout_emo.getVisibility() == View.VISIBLE) {
					layout_emo.setVisibility(View.GONE);
					layout_add.setVisibility(View.VISIBLE);
				} else {
					layout_more.setVisibility(View.GONE);
				}
			}
			break;
		case R.id.btn_chat_emo:// 点击笑脸图标
			if (layout_more.getVisibility() == View.GONE) {
				showEditState(true);
			} else {
				if (layout_add.getVisibility() == View.VISIBLE) {
					layout_add.setVisibility(View.GONE);
					layout_emo.setVisibility(View.VISIBLE);
				} else {
					layout_more.setVisibility(View.GONE);
				}
			}

			break;
		case R.id.btn_chat_voice:
			emotionEditText.setVisibility(View.GONE);
			layout_more.setVisibility(View.GONE);
			btn_chat_voice.setVisibility(View.GONE);
			btn_chat_keyboard.setVisibility(View.VISIBLE);
			btn_speak.setVisibility(View.VISIBLE);
			//hideSoftInputView();
			break;
	 case R.id.btn_chat_keyboard:// 键盘按钮，点击就弹出键盘并隐藏掉声音按钮
		 	emotionEditText.setVisibility(View.VISIBLE);
			layout_more.setVisibility(View.GONE);
			btn_chat_voice.setVisibility(View.VISIBLE);
			btn_chat_keyboard.setVisibility(View.GONE);
			btn_speak.setVisibility(View.GONE);
			break;
		case R.id.btn_chat_send:
		   	new Thread(new Runnable()
			{
				@Override
				public void run() {
					 	Message message=new Message();
						message.setFrom(ImChatService.mCurAccout);
						message.setTo(user.getCount());
						message.setBody(emotionEditText.getText().toString());
						ImMessage imMessage=new ImMessage(ChatAdapter.TXT_TO,emotionEditText.getText().toString(),new Date());
						message_pool.add(imMessage);
						saveMessage(user.getCount(), message);
						try {
							currentChat.sendMessage(message);
							handler.sendEmptyMessage(HANDLER_TO);
						} 
						catch (XMPPException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				}).start();
			break;		 
		}
	}
	MyHandler handler=new MyHandler();
	class MyHandler extends Handler
	{
		@Override
		public void handleMessage(android.os.Message msg) {

			super.handleMessage(msg);
			switch(msg.what)
			{
			case HANDLER_TO:
				emotionEditText.setText("");
				chatAdapter.refreshList(message_pool);
				break;
			case HANDLER_FROM:
				chatAdapter.refreshList(message_pool);
			}
		}
	}
	//语音案件的点击事件
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction())
		{
		case MotionEvent.ACTION_UP:
			layout_record.setVisibility(View.INVISIBLE);
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getY() < 0) {
				tv_voice_tips
						.setText(getString(R.string.voice_cancel_tips));
				tv_voice_tips.setTextColor(Color.RED);
			} else {
				tv_voice_tips.setText(getString(R.string.voice_up_tips));
				tv_voice_tips.setTextColor(Color.WHITE);
			}
			layout_record.setVisibility(View.VISIBLE);
			iv_record.setImageResource(R.drawable.chat_icon_voice4);
			break;
		case MotionEvent.ACTION_DOWN:
			layout_record.setVisibility(View.VISIBLE);
			iv_record.setImageResource(R.drawable.chat_icon_voice4);
			tv_voice_tips.setText(R.string.voice_up_tips);
			
			break;
		}
		return true;
	}

	
	/**
	 * 根据是否点击笑脸来显示文本输入框的状态
	 * 
	 * @Title: showEditState
	 * @Description: TODO
	 * @param @param isEmo: 用于区分文字和表情
	 * @return void
	 * @throws
	 */
	private void showEditState(boolean isEmo) {
		emotionEditText.setVisibility(View.VISIBLE);
		btn_chat_keyboard.setVisibility(View.GONE);
		btn_chat_voice.setVisibility(View.VISIBLE);
		btn_speak.setVisibility(View.GONE);
		emotionEditText.requestFocus();
		if (isEmo) {
			layout_more.setVisibility(View.VISIBLE);
			layout_more.setVisibility(View.VISIBLE);
			layout_emo.setVisibility(View.VISIBLE);
			layout_add.setVisibility(View.GONE);
			//hideSoftInputView();
		} else {
			layout_more.setVisibility(View.GONE);
			showSoftInputView();
		}
	}
	// 显示软键盘
		public void showSoftInputView() {
			if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
				if (getCurrentFocus() != null)
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.showSoftInput(emotionEditText, 0);
			}
		}

		
		/**
		 * 保存消息-->contentResolver-->contentProvider-->sqlite
		 *
		 * @param msg
		 */
		private void saveMessage(String sessionAccount, Message msg) {
			ContentValues values = new ContentValues();
			// 我(from)--->小蜜(to) ===>小蜜
			// 小蜜(from)--->我(to)====>小蜜
			//sessionAccount = filterAccount(sessionAccount);
			String from = msg.getFrom();
			from = filterAccount(from);
			String to = msg.getTo();
			to = filterAccount(to);
			values.put(SmsOpenHelper.SmsTable.FROM_ACCOUNT, from);
			values.put(SmsOpenHelper.SmsTable.TO_ACCOUNT, to);
			values.put(SmsOpenHelper.SmsTable.BODY, msg.getBody());
			values.put(SmsOpenHelper.SmsTable.STATUS, "offline");
			values.put(SmsOpenHelper.SmsTable.TYPE, msg.getType().name());
			values.put(SmsOpenHelper.SmsTable.TIME, System.currentTimeMillis());
			values.put(SmsOpenHelper.SmsTable.SESSION_ACCOUNT, sessionAccount);
			getContentResolver().insert(SmsProvider.URI_SMS, values);
		}

		private String filterAccount(String accout) {
			return accout.substring(0, accout.indexOf("@")) + "@" + LoginActivity.SERVICENAME;
		}
		
		/*=============== 使用contentObserver时刻监听记录的改变 ===============*/
		MyContentObserver	mMyContentObserver	= new MyContentObserver(new Handler());

		/**
		 * 注册监听
		 */
		public void registerContentObserver() {
			getContentResolver().registerContentObserver(SmsProvider.URI_SMS, true, mMyContentObserver);
		}

		/**
		 * 反注册监听
		 */
		public void unRegisterContentObserver() {
			getContentResolver().unregisterContentObserver(mMyContentObserver);
		}

		class MyContentObserver extends ContentObserver {

			public MyContentObserver(Handler handler) {
				super(handler);
			}

			/**
			 * 接收到数据记录的改变
			 */
			@Override
			public void onChange(boolean selfChange, Uri uri) {
				// 设置adapter或者notifyadapter
				chatAdapter.notifyDataSetChanged();
				mListView.setSelection(message_pool.size()-1);
				super.onChange(selfChange, uri);
			}
		}
}
