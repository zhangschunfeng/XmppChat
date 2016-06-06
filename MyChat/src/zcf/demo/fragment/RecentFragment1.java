package zcf.demo.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zcf.demo.activity.R;
import zcf.demo.adapter.RecentAdapter;
import zcf.demo.bean.ImMessage;
import zcf.demo.bean.RecentInfo;
import zcf.demo.db.SmsOpenHelper;
import zcf.demo.provider.SmsProvider;
import zcf.demo.service.ImChatService;
import zcf.demo.utils.CommonUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;

public class RecentFragment1  extends Fragment{

	private ListView recentList;
	private MyCursorAdapter cursorAdapter;
	private List<RecentInfo> list;
	private RecentAdapter adapter;
	private Context context;
	private Handler handler=new Handler();
	public RecentFragment1(Context context)
	{
		this.context=context;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		registerContentObserver();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_recent,container,false);
		recentList=(ListView) view.findViewById(R.id.list);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData();
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		unRegisterContentObserver();
	}
	//��ʼ������
	void initData()
	{
		 freshData();
	}
	//ˢ������
   private void freshData() {
	    
		if(cursorAdapter!=null)
		{
			cursorAdapter.getCursor().requery();
			Log.e("ssss", "adapterΪ��");
			return;
		}
		new Thread(new Runnable()
		{
					@Override
					public void run() {
							final Cursor c=getActivity().getContentResolver().query(SmsProvider.URI_SESSION, null, null, 	
									new String[] {ImChatService.mCurAccout, ImChatService.mCurAccout }, null);
							if (c.getCount() <= 0) {
								Log.e("ssss", "��ȡ�����ݼ�Ϊ��");
								return; }
							handler.post(new Runnable(){
								@Override
								public void run() {
									cursorAdapter=new MyCursorAdapter(getActivity(),c);
									recentList.setAdapter(cursorAdapter);	
									
								}
								
							});	
						}
					
					
			}).start();
		
	
	}

	
	//
	//ʵ�����ݽ����
	class MyCursorAdapter extends CursorAdapter
	{
		private Context context;
		private Cursor c;
		public MyCursorAdapter(Context context, Cursor c) {
			super(context, c);
			this.c=c;
			this.context=context;
			
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			 
			 TextView tv_recent_name=(TextView) view.findViewById(R.id.tv_recent_name);
			 TextView tv_recent_msg=(TextView)view.findViewById(R.id.tv_recent_msg);
			 TextView tv_recent_time=(TextView) view.findViewById(R.id.tv_recent_time);
			 TextView tv_recent_unread=(TextView)view.findViewById(R.id.tv_recent_unread);
			 String body = c.getString(c.getColumnIndex(SmsOpenHelper.SmsTable.BODY));
			 String acccount = c.getString(c.getColumnIndex(SmsOpenHelper.SmsTable.SESSION_ACCOUNT));
			 String time=c.getString(c.getColumnIndex(SmsOpenHelper.SmsTable.TIME));
			 time=CommonUtils.forMatTime(new Date(Long.parseLong(time)));
			 tv_recent_msg.setText(body);
			 tv_recent_time.setText(time);
			 tv_recent_name.setText(acccount);

				// acccount �����������¼��(sms)����û�б��������Ϣ,ֻ��(Contact��������)
					
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup ViewGroup) {
			View view = View.inflate(context, R.layout.item_conversation, null);
			return view;
		}
				
	}
	/*=============== �������ݿ��¼�ĸı� ===============*/

	MyContentObserver	mMyContentObserver	= new MyContentObserver(new Handler());

	/**
	 * ע�����
	 */
	public void registerContentObserver() {
		// content://xxxx/contact
		// content://xxxx/contact/i
		getActivity().getContentResolver().registerContentObserver(SmsProvider.URI_SMS, true, mMyContentObserver);
	}

	public void unRegisterContentObserver() {
		getActivity().getContentResolver().unregisterContentObserver(mMyContentObserver);
	}

	/**
	 * ��ע�����
	 */
	
	private class MyContentObserver extends ContentObserver
	{

		public MyContentObserver(Handler handler) {
			super(handler);
			
		}
		/**
		 * ������ݿ����ݸı������������յ�֪ͨ
		 */
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			// TODO Auto-generated method stub
			super.onChange(selfChange, uri);
			freshData();
		}
	
	}
	
}
