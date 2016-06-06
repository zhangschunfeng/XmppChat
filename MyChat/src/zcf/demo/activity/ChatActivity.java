package zcf.demo.activity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jivesoftware.smack.packet.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.location.LocationClientOption.LocationMode;

import zcf.demo.adapter.ChatAdapter;
import zcf.demo.adapter.EmoViewPagerAdapter;
import zcf.demo.adapter.EmoteAdapter;
import zcf.demo.bean.FaceText;
import zcf.demo.bean.ImMessage;
import zcf.demo.bean.User;
import zcf.demo.db.SmsOpenHelper;
import zcf.demo.provider.SmsProvider;
import zcf.demo.service.ImChatService;
import zcf.demo.service.LocationService;
import zcf.demo.utils.CommonUtils;
import zcf.demo.utils.FaceTextUtils;
import zcf.demo.utils.SharePreferenceUtils;
import zcf.demo.view.EmoticonsEditText;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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
import android.widget.Toast;

public class ChatActivity extends Activity implements OnClickListener,OnTouchListener{
	private TextView  chat_title,tv_voice_tips,tv_location;
	private ImageView iv_record;
	private Button btn_chat_add,btn_chat_emo,btn_speak,btn_chat_voice,btn_chat_keyboard
	,btn_chat_send;
	private EmoticonsEditText emotionEditText;
	private LinearLayout layout_add,layout_more,layout_emo;
	private RelativeLayout layout_record;
	private ViewPager pager_emo;
    private List<FaceText> emos;
	public  List<ImMessage> message_pool = null;
	protected String to;// 聊天人
	private User user;
	private ChatAdapter chatAdapter;
	private ListView mListView;	

	public static final int  HANDLER_TO=1,HANDLER_FROM=2;
	private MyBroadCast myBroadCast;
	private ImChatService imService;
	private String locationStr="";
	private LocationClient mLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		regisBroadCast();
		initService();
		initView();
		initData();
		initemotionPager();
	}
	
	//注册广播
	 void regisBroadCast()
	 {
		IntentFilter filter=new IntentFilter();
		filter.addAction(ImChatService.REFRESH);
		filter.addAction(ImChatService.ADD);
		myBroadCast=new MyBroadCast();
		this.registerReceiver(myBroadCast, filter);

	}
	 @Override
	protected void onStop() {
		
		super.onStop();
		mLocationClient.stop();
	}

	//绑定Service
	 private void initService() {
		 	Intent intent=new Intent(ChatActivity.this,ImChatService.class);
		 	this.bindService(intent, mMyServiceConnection, BIND_AUTO_CREATE);
		 	mLocationClient = new LocationClient(getApplicationContext());
		 	initLocation();
		 	mLocationClient.registerLocationListener( myListener );    //注册监听函数
			
			mLocationClient.start();// 定位SDK
		}
	 BDLocationListener myListener=new BDLocationListener()
     {
			@Override
			public void onReceiveLocation(BDLocation location) {
				locationStr=location.getAddrStr();
				
			}
     };
	 private void initLocation() {
		  	LocationClientOption option = new LocationClientOption();
		    option.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
	        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
	        option.setScanSpan(3000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
	        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
	        option.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
	        option.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
	        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
	        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死   
	        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
	        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
	        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集

	        mLocationClient.setLocOption(option);
	        
	        
	      
	}
	 @Override
	protected void onDestroy()
	 {
			// 解绑服务
			if (mMyServiceConnection != null) {
				unbindService(mMyServiceConnection);
			}
			this.unregisterReceiver(myBroadCast);
		super.onDestroy();
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
		final EmoteAdapter gridAdapter = new EmoteAdapter(ChatActivity.this,
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
				Cursor cursor =  getContentResolver().query(SmsProvider.URI_SMSREAD,//
						null,null,
						new String[]{ImChatService.filterAccount(user.getCount()),"no" },// where条件的参数
						null// 根据时间升序排序
						);
				ImMessage message;
				while (cursor.moveToNext()) {
					String msg = cursor.getString(cursor
							.getColumnIndex(SmsOpenHelper.SmsTable.BODY));
							
					String time = cursor.getString(cursor
							.getColumnIndex(SmsOpenHelper.SmsTable.TIME));
					int type=ChatAdapter.TXT_FROM;
					if(msg.contains(CommonUtils.SPLIT))
					{   
						type=ChatAdapter.POI_FROM;
						msg=msg.substring(0, msg.indexOf(CommonUtils.SPLIT));
					}
					else if(cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.
							FROM_ACCOUNT)).equalsIgnoreCase(user.getCount()))
					{
						type=ChatAdapter.TXT_TO;
					}
					ContentValues values=new ContentValues();
					//更改数据，为已经读了
					values.put(SmsOpenHelper.SmsTable.READER, "yes");
					getContentResolver().update(SmsProvider.URI_SMS, values, null, null);
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
		tv_location=(TextView) this.findViewById(R.id.tv_location);
		tv_location.setOnClickListener(this);
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
						//saveMessage(user.getCount(), message);
						handler.sendEmptyMessage(1);
						imService.sendMessage(message);
						
						
				}
				}).start();
			break;	
		case R.id.tv_location:
			//Toast.makeText(this,locationStr , Toast.LENGTH_LONG).show();
			new Thread(new Runnable()
			{
				@Override
				public void run() {
					Message mess=new Message();
					mess.setFrom(ImChatService.mCurAccout);
					mess.setTo(user.getCount());
					if(locationStr.equals(""))
					{
						locationStr="定位没有结果";
					}
					mess.setBody(locationStr+CommonUtils.SPLIT);
					ImMessage imess=new ImMessage(ChatAdapter.POI_TO,locationStr,new Date());
					message_pool.add(imess);
					handler.sendEmptyMessage(2);
					imService.sendMessage(mess);
				}
				}).start();
			break;
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

		
	
		
	//广播
	private class MyBroadCast extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(ImChatService.ADD))
				{
					ImMessage imMessage=(ImMessage) intent.getSerializableExtra("message");
					message_pool.add(imMessage);
					chatAdapter.notifyDataSetChanged();
					mListView.setSelection(message_pool.size()-1);
				}
				else if(intent.getAction().equals(ImChatService.REFRESH))
				{
					chatAdapter.refreshList(message_pool);
				}
			
		}
		
	}
	/*=============== 定义父子线程通信 ===============*/
	MyHandler handler=new MyHandler();
	class MyHandler extends Handler
	{
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.what)
			{
			case 1:
				emotionEditText.setText("");
				chatAdapter.refreshList(message_pool);
				break;
			case 2:
				chatAdapter.refreshList(message_pool);
			}
			super.handleMessage(msg);
		}
	}
	/*=============== 定义ServiceConnection调用服务里面的方法 ===============*/

	MyServiceConnection	mMyServiceConnection	= new MyServiceConnection();

	class MyServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			System.out.println("--------------onServiceConnected--------------");
			ImChatService.MyBinder binder = (ImChatService.MyBinder) service;
			imService = binder.getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			System.out.println("--------------onServiceDisconnected--------------");

		}
	}
	
	
	/*****
	 * @see copy funtion to you project
	 * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 *
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (null != location && location.getLocType() != BDLocation.TypeServerError) 
			{
//				sb.append("\nlatitude : ");
//				sb.append(location.getLatitude());
//				sb.append("\nlontitude : ");
//				sb.append(location.getLongitude());
				//locationStr=location.getAddrStr()+"@"+31.83+"@"+117.25;
				locationStr=location.getLongitude()+location.getAddrStr();
				

			}
		}

	};
}
