package zcf.demo.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import zcf.demo.activity.ChatActivity;
import zcf.demo.activity.FlashActivity;
import zcf.demo.activity.LoginActivity;
import zcf.demo.activity.MainActivity;
import zcf.demo.activity.NewFriendActivity;
import zcf.demo.activity.R;
import zcf.demo.adapter.ChatAdapter;
import zcf.demo.bean.Friends;
import zcf.demo.bean.ImMessage;
import zcf.demo.bean.RecentInfo;
import zcf.demo.bean.User;
import zcf.demo.db.SmsOpenHelper;
import zcf.demo.db.UserOpenHelper.UserTable;
import zcf.demo.provider.SmsProvider;
import zcf.demo.provider.UserProvider;
import zcf.demo.utils.CommonUtils;
import zcf.demo.utils.FriendUtils;
import zcf.demo.utils.SharePreferenceUtils;
import zcf.demo.utils.XmppUtils;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

public class ImChatService extends Service{
	//xppConnection参数
	public static final String HOST = "121.42.52.227";    // 主机ip
	public static final int PORT =5222;     
	public static XMPPConnection conn;
	//用户账号信息
	private String username;
	private String password;
	
	private ChatManager chatManager;
	public static String	mCurAccout;	
    private MediaPlayer mediaPlayer;
	//currentChat是参与者的jid
	private Chat currentChat;
	private Map chatList=new HashMap<String,Chat>();
	//
	//NotificationManager 
	private NotificationManager notiManager; 
	private Roster roster;
	private MyChatManagerListener chatManagerListener;
	private MyMessageListener myMessageListener;
	private static final int  MESS_HANDLER_TO=1,MESS_HANDLER_FROM=2, ADD_MESSAGE=3;
	public static  String REFRESH="refresh",ADD="add";
	public static String INTENT_NEW="intent_new",INTENT_CHAT="intent_chat";
	public static ImChatService mInstance=null;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new MyBinder();
	}
	public class MyBinder extends Binder {
		/**
		 * 返回service的实例
		 */
		public ImChatService getService() {
			return ImChatService.this;
		}
	}
	public static ImChatService getInstaceService()
	{
		return mInstance;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mInstance=this;
		notiManager=(NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		username=SharePreferenceUtils.getSharePreStr(ImChatService.this,"username");
		password=SharePreferenceUtils.getSharePreStr(ImChatService.this,"password");
		
		//Toast.makeText(this.getApplicationContext(), "为空"+username+password, Toast.LENGTH_SHORT).show();
		chatManagerListener=new MyChatManagerListener();
		myMessageListener=new MyMessageListener();
		mediaPlayer=MediaPlayer.create(this, R.raw.coming);       
		new Thread(new Runnable()
		{

			@Override
			public void run() {
				conn=ImChatService.getInstance();
				//登陆
				loginXMPP();
				//注册监听好友添加;
				setPresenceListener();
				setRosterListener();
				if(chatManager==null)
				{
					chatManager=conn.getChatManager();
					
				}
				chatManager.addChatListener(chatManagerListener);
			}
			
		}
		).start();
		
	}
	//获取好友信息的监听
	protected void setRosterListener() {
		roster=ImChatService.getInstance().getRoster();
		roster.addRosterListener(rosterListener);
		final Collection<RosterEntry> entries =roster.getEntries();
		for(RosterEntry entry:entries)
		{
			savaOrUpdateEntry(entry);
		}
		
		
	}
	//保存或更新数据
	private void savaOrUpdateEntry(RosterEntry entry) {
		ContentValues values=new ContentValues();
		String account = entry.getUser();

		// account = account.substring(0, account.indexOf("@")) + "@" + LoginActivity.SERVICENAME;

		// 处理昵称
		String nickname = entry.getName();
		if (nickname == null || "".equals(nickname)) {
			nickname = account.substring(0, account.indexOf("@"));// billy@itheima.com-->billy
		}
		values.put(UserTable.ACOUNT, account);
		values.put(UserTable.NIKENAME, nickname);
		values.put(UserTable.SORTLETTER, nickname);
		getContentResolver().insert(UserProvider.URI_USER, values);
		// 先update,后插入-->重点
		int updateCount=this.getContentResolver().update(UserProvider.URI_USER,
				values, UserTable.ACOUNT+" = ?",new String[]{account});
		if (updateCount <= 0) {// 没有更新到任何记录
			getContentResolver().insert(UserProvider.URI_USER, values);
		}
		
	}
	private RosterListener rosterListener=new RosterListener()
	{

		@Override
		public void entriesAdded(Collection<String> addresses) {
			for (String address : addresses) {
				RosterEntry entry = roster.getEntry(address);
				// 要么更新,要么插入
				savaOrUpdateEntry(entry);
			}
		}

		@Override
		public void entriesDeleted(Collection<String> addresses) {
			for (String account : addresses) {
				// 执行删除操作
				getContentResolver().delete(UserProvider.URI_USER,
						UserTable.ACOUNT + " = ?", new String[] { account });
			}
			
		}

		@Override
		public void entriesUpdated(Collection<String> addresses) {
			for (String address : addresses) {
				RosterEntry entry = roster.getEntry(address);
				// 要么更新,要么插入
				savaOrUpdateEntry(entry);
			}
			
		}

		@Override
		public void presenceChanged(Presence arg0) {
			//先不做处理
			
		}
		
	};
	private void setPresenceListener()
	{
		
		ImChatService.getInstance().addPacketListener(PresencePacketListenter, filter1);
	}
	 
 //好友申请数据包的监听
	 PacketFilter filter1 = new AndFilter(new PacketTypeFilter(  
             Presence.class)); 
   //数据包监听器
	private PacketListener PresencePacketListenter= new PacketListener()
	{

		@Override
		public void processPacket(Packet packet) {
                
				Presence presence=(Presence) packet;
				String from=presence.getFrom();
				if(filterAccounts(from).equalsIgnoreCase(SharePreferenceUtils.getSharePreStr(ImChatService.this, "username")))
				{
					return;
				}
				if (Roster.getDefaultSubscriptionMode().equals(SubscriptionMode.accept_all)) {
		              XmppUtils.agreeAdd(from);
		              
				}
				if(presence.getType().equals(Presence.Type.subscribe))
				{	
					Friends friend=new Friends();
					friend.setAccount(from);
					friend.setName(filterAccounts(from));
					friend.setIsReceiver("no");
					FriendUtils.saveFriend(ImChatService.this,friend);
					Notification.Builder builder=new Notification.Builder(ImChatService.this);
					builder.setContentTitle("好友申请");
					builder.setContentText(from+"请求添加好友");
					builder.setSmallIcon(R.drawable.ic_launcher);
					builder.setWhen(System.currentTimeMillis());
					Intent intent =new Intent(ImChatService.this,NewFriendActivity.class);
					intent.putExtra("from", from);
					PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
					builder.setContentIntent(pendingIntent);
					Notification notification=builder.getNotification();
					notification.defaults =Notification.DEFAULT_SOUND;//设置为默认的声音
					notiManager.notify(2, notification);
					
				}

			
		}
		
	};
	protected void loginXMPP() {
		try
		{
			
			conn.connect();
			conn.login(username, password);
			ImChatService.mCurAccout= username + "@121.42.52.227" ;
			if(conn.isAuthenticated())
			{
				sendLoginBroadcast(true);
				//只有成功的情况下保存LoginSuccess,然后在pflashActivity判断页面跳转
				SharePreferenceUtils.putSharePre(ImChatService.this,CommonUtils.loginsuccess,true);
				//添加xmpp连接监听
			}	
			else
			{
				
				sendLoginBroadcast(false);
				SharePreferenceUtils.putSharePre(ImChatService.this,CommonUtils.loginsuccess,false);
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			sendLoginBroadcast(false);
		}
		
		

	}

	@Override
	public void onDestroy() {
		try
		{
			if(conn!=null)
			{
				conn=null;
			}
			if(chatManager!=null)
			{
				chatManager=null;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		super.onDestroy();
	}

	private void sendLoginBroadcast(boolean isLoginSuccess) {
		Intent intent =new Intent(CommonUtils.isLoginSuccess);
		intent.putExtra("isLoginSuccess", isLoginSuccess);
		sendBroadcast(intent);
		
	}


	public static XMPPConnection getInstance()
	{
		if(conn==null)
		{
			ProviderManager pm = ProviderManager.getInstance();
			configure(pm);
			ConnectionConfiguration config = new ConnectionConfiguration(HOST, PORT);
			config.setSASLAuthenticationEnabled(false);// 不使用SASL验证，设置为false
			//config.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
			config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);// 明文传输  
			//config.setDebuggerEnabled(true);// 开启调试模式,方便我们查看具体发送的内容
			
			// 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友
			//Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
			conn=new XMPPConnection(config);
		}
		return conn;
	}
	//大概是说由于smack.providers 的文件不能载入asmack中导致的。所以必须我们自己手动载入一个。
	public static void configure(ProviderManager pm) {

		pm.addIQProvider("query", "jabber:iq:private",new PrivateDataManager.PrivateDataIQProvider());
		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",new RosterExchangeProvider());
		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",new MessageEventProvider());
		// Chat State
		pm.addExtensionProvider("active","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone","http://jabber.org/protocol/chatstates",new ChatStateExtension.Provider());
		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",new XHTMLExtensionProvider());
		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",new GroupChatInvitation.Provider());
		// Service Discovery # Items //解析房间列表
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",new DiscoverItemsProvider());
		// Service Discovery # Info //某一个房间的信息
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",new DiscoverInfoProvider());
		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",new MUCUserProvider());
		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",new MUCAdminProvider());
		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",new MUCOwnerProvider());
		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",new DelayInformationProvider());
		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		pm.addExtensionProvider("offline","http://jabber.org/protocol/offline",new OfflineMessageInfo.Provider());
		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup","http://www.jivesoftware.org/protocol/sharedgroup",new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses","http://jabber.org/protocol/address",new MultipleAddressesProvider());
		pm.addIQProvider("si", "http://jabber.org/protocol/si",new StreamInitiationProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",new BytestreamsProvider());
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
		pm.addIQProvider("command", "http://jabber.org/protocol/commands",new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired","http://jabber.org/protocol/commands",new AdHocCommandDataProvider.SessionExpiredError());

	}
	//
	public static void disXppCon()
	{
		if(conn!=null)
		{
			conn.disconnect();
		}
		//ImChatService.
		
	
	}
	class MyChatManagerListener implements ChatManagerListener
	{

		@Override
		public void chatCreated(Chat chat, boolean createdLocally) {
			//createdLocally表示是否由自己创建的chat
			//和自己聊天的参与者
			String participant=chat.getParticipant();
			participant = filterAccount(participant);
			if(!chatList.containsKey(participant))
			{
				chatList.put(participant, chat);
				chat.addMessageListener(myMessageListener);//message监听器
			}
				
			
		}
		
	}
	///handler 主子线程的操作
	 MyHandler handler=new MyHandler();
	 class MyHandler extends Handler
	 {
		 @Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			 Intent intent;
			switch(msg.what)
			{
			case MESS_HANDLER_FROM:
			    intent=new Intent();
			    intent.setAction(REFRESH);
				ImChatService.this.sendBroadcast(intent);
				break;
			case ADD_MESSAGE:
				intent=new Intent();
			    intent.setAction(ADD);
			    ImMessage imMessage=(ImMessage) msg.obj;
			    intent.putExtra("message", imMessage);
				ImChatService.this.sendBroadcast(intent);
			}
		}
	 }
	
	
	//Message接听器
	
	class MyMessageListener implements MessageListener
	{

		@Override
		public void processMessage(final Chat chat, Message message) {
			final Message msg=message;
			final String body=message.getBody();
			String fromAccount = message.getFrom();
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					if(currentChat==null)
					{
						saveMessage(msg.getFrom(), msg,"no");
						Notification.Builder mBuilder=new Notification.Builder(ImChatService.this);
						mBuilder.setContentTitle(msg.getFrom());
						mBuilder.setContentText(msg.getBody());
						mBuilder.setSmallIcon(R.drawable.ic_launcher);
						mBuilder.setWhen(System.currentTimeMillis());
						//传递user
						String name=msg.getFrom();
						String nikeName=name.substring(0,name.indexOf("@"));
						User user=new User(name,nikeName,nikeName);
						Intent intent=new Intent(ImChatService.this,ChatActivity.class);
						intent.putExtra("user", user);
						PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(), 1,intent,1);
					    mBuilder.setContentIntent(pendingIntent);
			            Notification noti = mBuilder.getNotification();//获取一个Notification
			            noti.defaults =Notification.DEFAULT_SOUND;//设置为默认的声音
			            notiManager.notify(1,noti);
					}
		
					else if(filterAccount(currentChat.getParticipant()).equalsIgnoreCase(filterAccount(msg.getFrom())))
					{
						ImMessage imMessage=null;
						if(body.contains(CommonUtils.SPLIT))
						{   
							imMessage = new ImMessage(ChatAdapter.POI_FROM,body.substring(0,body.indexOf(CommonUtils.SPLIT)), new Date()); 
							 android.os.Message mss = new android.os.Message();
							 mss.obj = imMessage;
							 mss.what = ADD_MESSAGE;
							 handler.sendMessage(mss);
							// handler.sendEmptyMessage(MESS_HANDLER_FROM);
							 //saveMessage(msg.getFrom(), msg,"yes");
						}
						
						else
						{
							 imMessage = new ImMessage(ChatAdapter.TXT_FROM,body, new Date());
							 android.os.Message mss = new android.os.Message();
							 mss.obj = imMessage;
							 mss.what = ADD_MESSAGE;
							 handler.sendMessage(mss);
							 handler.sendEmptyMessage(MESS_HANDLER_FROM);
							 saveMessage(msg.getFrom(), msg,"yes");
							
						}
						 
						
					}
					else
					{
						saveMessage(msg.getFrom(), msg,"no");
						Notification.Builder mBuilder=new Notification.Builder(ImChatService.this);
						mBuilder.setContentTitle(msg.getFrom());
						mBuilder.setContentText(msg.getBody());
						mBuilder.setSmallIcon(R.drawable.ic_launcher);
						mBuilder.setWhen(System.currentTimeMillis());
						//传递user
						String name=msg.getFrom();
						String nikeName=name.substring(0,name.indexOf("@"));
						User user=new User(name,nikeName,nikeName);
						Intent intent=new Intent(ImChatService.this,ChatActivity.class);
						intent.putExtra("user", user);
						PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(), 1,intent,1);
					    mBuilder.setContentIntent(pendingIntent);
			            Notification noti = mBuilder.getNotification();//获取一个Notification
			            noti.defaults =Notification.DEFAULT_SOUND;//设置为默认的声音
					    notiManager.notify(1,noti);
						
					}
                    
				}

			}).start();

		}
		
	}
	/**
	 * 发送消息
	 */
	public void sendMessage(final Message msg) {
		try {
			// 2.创建聊天对象
			// 我-->美女(jid)
			// chatManager.createChat("被发送对象jid",消息的监听者);
			// 判断chat对是否已经创建
			
			
			String toAccount = msg.getTo();
			if (chatList.containsKey(toAccount)) {
				currentChat = (Chat) chatList.get(toAccount);
			} else {
				currentChat= chatManager.createChat(toAccount, myMessageListener);
				chatList.put(toAccount, currentChat);
			}
			// 发送消息
			currentChat.sendMessage(msg);
			// 保存消息
			saveMessage(msg.getTo(), msg,"yes");
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存消息-->contentResolver-->contentProvider-->sqlite
	 *
	 * @param msg
	 */
	private void saveMessage(String sessionAccount, Message msg,String read) {
		ContentValues values = new ContentValues();
		// 我(from)--->小蜜(to) ===>小蜜
		// 小蜜(from)--->我(to)====>小蜜
		String from = filterAccount(msg.getFrom());
		String to = filterAccount(msg.getTo());
		
		values.put(SmsOpenHelper.SmsTable.FROM_ACCOUNT, from);
		values.put(SmsOpenHelper.SmsTable.TO_ACCOUNT, to);
		values.put(SmsOpenHelper.SmsTable.BODY, msg.getBody());
		values.put(SmsOpenHelper.SmsTable.STATUS, "offline");
		values.put(SmsOpenHelper.SmsTable.TYPE, msg.getType().name());
		values.put(SmsOpenHelper.SmsTable.TIME, System.currentTimeMillis());
		
		//sessionAccount 为baby@127.0.0.1 除去smack
		values.put(SmsOpenHelper.SmsTable.SESSION_ACCOUNT, filterAccount(sessionAccount));
		values.put(SmsOpenHelper.SmsTable.READER, read);
		getContentResolver().insert(SmsProvider.URI_SMS, values);
	}

	//
	
	public static String filterAccount(String accout) {
		if(accout.contains("/"))
		{
			return accout.substring(0, accout.indexOf("/")) ;
		}
		return accout;
	}
	public static String filterAccounts(String account)
	{
		if(account.contains("@"))
		{
			return account.substring(0, account.indexOf("@")) ;
		}
		return account;
	}
	private boolean checkExsit(String participant) {
		final Cursor c=ImChatService.this.getContentResolver().query(SmsProvider.URI_SESSION, null, null, 	
				new String[] {ImChatService.mCurAccout, ImChatService.mCurAccout }, null);
		while(c.moveToNext())
		{
		
			String account = c.getString(c.getColumnIndex(SmsOpenHelper.SmsTable.SESSION_ACCOUNT));
			if(account.equalsIgnoreCase(account))
			{
				return true;
			}
	
		}		
		return false;
	}

	void soundPlayer() {

		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.start();
	}
	
	
}
