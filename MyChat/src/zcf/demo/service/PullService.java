package zcf.demo.service;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.xmlpull.v1.XmlPullParser;

import zcf.demo.activity.ChatActivity;
import zcf.demo.activity.MainActivity;
import zcf.demo.activity.R;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class PullService extends Service{

	private XMPPConnection conn;
	private NotificationManager notiManager; 
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		
		 notiManager=(NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
	     conn=ImChatService.getInstance();
	     conn.addPacketListener(new PacketListener()
	     {
			@Override
			public void processPacket(Packet packet) {
				Message message=(Message) packet;
				if(message.getFrom().equalsIgnoreCase("127.0.0.1"))
				{
					String body=message.getBody();
					String from=message.getFrom();
//					Intent intent=new Intent();
//					intent.putExtra("from",from);
//					intent.putExtra("body", body);
//					intent.setAction("INTENT_NOTIFICATION");
//					PullService.this.sendBroadcast(intent);
					Notification.Builder mBuilder=new Notification.Builder(PullService.this);
					mBuilder.setContentText(body);
					mBuilder.setContentTitle(from);
					mBuilder.setSmallIcon(R.drawable.ic_launcher);
				//	mBuilder.setVibrate(pattern)
					mBuilder.setWhen(System.currentTimeMillis());
	                PendingIntent pendingIntent=PendingIntent.getActivity(PullService.this, 1, new Intent(PullService.this,MainActivity.class),1);
	                mBuilder.setContentIntent(pendingIntent);
	                Notification noti = mBuilder.getNotification();//获取一个Notification
	                noti.defaults =Notification.DEFAULT_SOUND;//设置为默认的声音
					notiManager.notify(1,noti );
				}
					

			}
	    	 
	     }, null);
		super.onCreate();
	}
//	void set()
//	{
//	
//			final NotificationManager notiManager=(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//			ConnectService.getInstance().addPacketListener(new PacketListener()
//		     {
//				@Override
//				public void processPacket(Packet packet) {
//					Message message=(Message) packet;
//					String body=message.getBody();
//					String from=message.getFrom();
//					Notification.Builder mBuilder=new Notification.Builder(ChatActivity.this);
//					mBuilder.setContentText(body);
//					mBuilder.setContentTitle(from);
//					mBuilder.setSmallIcon(R.drawable.ic_launcher);
//				//	mBuilder.setVibrate(pattern)
//					notiManager.notify(1, mBuilder.build());
//
//				}
//		    	 
//		     }, null);
//		
//	}
}
