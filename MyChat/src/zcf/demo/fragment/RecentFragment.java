package zcf.demo.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zcf.demo.activity.ChatActivity;
import zcf.demo.activity.R;
import zcf.demo.activity.RobertChatActivity;
import zcf.demo.adapter.RecentAdapter;
import zcf.demo.bean.ImMessage;
import zcf.demo.bean.RecentInfo;
import zcf.demo.bean.User;
import zcf.demo.db.SmsOpenHelper;
import zcf.demo.provider.SmsProvider;
import zcf.demo.service.GetInfoService;
import zcf.demo.service.ImChatService;
import zcf.demo.utils.SharePreferenceUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class RecentFragment  extends Fragment implements OnItemClickListener,OnClickListener{

	
	private ListView recentList;
	private List<RecentInfo> listInfo;
	private RecentAdapter adapter;
	private ReBroadCastReceiver receiver;
	private View convertation_robert;
	private LinearLayout chat_robert;
	public RecentFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//注册广播
		receiver=new ReBroadCastReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction(ImChatService.INTENT_CHAT);
		filter.addAction(ImChatService.INTENT_NEW);
		this.getActivity().registerReceiver(receiver, filter);
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_recent,container,false);
		recentList=(ListView) view.findViewById(R.id.list);
		convertation_robert=LayoutInflater.from(getActivity()).inflate(R.layout.convertation_robert, null);
		
		chat_robert=(LinearLayout) convertation_robert.findViewById(R.id.chat_robert);
		recentList.addFooterView(convertation_robert);
		recentList.setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		initData();
		chat_robert.setOnClickListener(this);
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		
	}
	//初始化数据
	void initData()
	{
		 freshData();
	}
	//刷新数据
   private void freshData() {
	    listInfo=new ArrayList<RecentInfo>();
	    adapter=new RecentAdapter(getActivity(),listInfo);
	    //获取查找的条件account的用户名
	    final String account=SharePreferenceUtils.getSharePreStr(getActivity(), "username")+"@121.42.52.227";
	    
		new Thread(new Runnable()
		{
					@Override
					public void run() {
							//
							final Cursor c=getActivity().getContentResolver().query(SmsProvider.URI_SESSION, null, null, 	
									new String[] {account, account }, null);
							if (c.getCount() <= 0) {
								Log.e("ssss", "获取的数据集为空");
								return; }
							RecentInfo info;
							while(c.moveToNext())
							{
								String body = c.getString(c.getColumnIndex(SmsOpenHelper.SmsTable.BODY));
								String account = c.getString(c.getColumnIndex(SmsOpenHelper.SmsTable.SESSION_ACCOUNT));
								String time=c.getString(c.getColumnIndex(SmsOpenHelper.SmsTable.TIME));
			                    info=new RecentInfo(account,time,body);
			                     listInfo.add(info);
			                     handler.sendEmptyMessage(1);
							}
							
						
						}
					
					
			}).start();
		 
	      recentList.setAdapter(adapter);
	}
 
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
			RecentInfo recentInfo=listInfo.get(position);
			String name=recentInfo.getName();
			String nikeName=recentInfo.getName().substring(0,name.indexOf("@"));
			User user=new User(name,nikeName,nikeName);
			Intent intent=new Intent(getActivity(),ChatActivity.class);
			intent.putExtra("user", user);
			startActivity(intent);
			
		
	}

	class ReBroadCastReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(ImChatService.INTENT_CHAT))
			{
				//String partcipant=intent.getStringExtra("participant");
				//for( listInfo)
				freshData();
				
			}
			if(intent.getAction().equals(ImChatService.INTENT_NEW))
			{
				freshData();
			}
			
		}
		
	}
	private Handler handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
		
			super.handleMessage(msg);
			switch(msg.what)
			{
			case 1:
				adapter.notifyDataSetChanged();
				break;
			}
		}
		
	};
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.chat_robert:
			Intent intent=new Intent(this.getActivity(),RobertChatActivity.class);
			startActivity(intent);
			break;
		}
		
	}
	
}
