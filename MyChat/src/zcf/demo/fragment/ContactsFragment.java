package zcf.demo.fragment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;



import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import zcf.demo.activity.NewFriendActivity;
import zcf.demo.activity.SearchFriendsActivity;
import zcf.demo.activity.MainActivity;
import zcf.demo.activity.R;
import zcf.demo.activity.SetFriendsInfoActivity;
import zcf.demo.adapter.UserFriendAdapter;
import zcf.demo.bean.User;
import zcf.demo.db.UserOpenHelper.UserTable;
import zcf.demo.provider.UserProvider;
import zcf.demo.service.ImChatService;
import zcf.demo.utils.PinyinComparator;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;


public class ContactsFragment extends Fragment  implements OnItemClickListener ,OnClickListener{
	private ListView listView;
	private List<User> userData,fixData;
	private UserFriendAdapter adapter;
	private LinearLayout layout_new;//新朋友
	private LinearLayout layout_near;//附近的人
	public LayoutInflater mInflater;
	private PinyinComparator pinyinComparator;
	public static final int REFRESH=1;
	/**
	 * 汉字转换成拼音的类
	 */
	public ContactsFragment()
	{
		
		
	}
	private Handler mHandler=new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch(msg.what)
			{
			case REFRESH:
				fixData=setFixData(userData);
				adapter.notifyDataSetChanged();
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//setData();
		super.onCreate(savedInstanceState);
		
	
	}
	void setData()
	{
		userData=new ArrayList<User>();
		fixData=new ArrayList<User>();
		adapter=new UserFriendAdapter(this.getActivity(),fixData);
		new Thread(new Runnable()
		{
			@Override
			public void run() {
				Cursor cursor=getContext().getContentResolver().query(UserProvider.URI_GROUP, null, null,null, null);
				User user=null;
				while(cursor.moveToNext())
				{
					String acount=cursor.getString(cursor.getColumnIndex(UserTable.ACOUNT));
					String nikeName=cursor.getString(cursor.getColumnIndex(UserTable.NIKENAME));
					String sortLetter=cursor.getString(cursor.getColumnIndex(UserTable.SORTLETTER));
					user=new User(acount,nikeName,sortLetter);
					userData.add(user);
					mHandler.sendEmptyMessage(REFRESH);
				}
				
			}}).start();
		
	}
	private List<User> setFixData(List<User> users) {
		User user;
		String sortStr;;
		String pinyin;
		
		pinyinComparator=new PinyinComparator();
		for(int i=0;i<users.size();i++)
		{
			 user= users.get(i);
			 sortStr=user.getSortLetter();
			 if(sortStr!=null)
			 {
				 pinyin=PinyinHelper.convertToPinyinString(sortStr, "#", PinyinFormat.WITHOUT_TONE);
				 pinyin=pinyin.substring(0, 1).toUpperCase();
				 if (pinyin.matches("[A-Z]")) {
						user.setSortLetter(pinyin.toUpperCase());} 
				 else {
						user.setSortLetter("#");
						}
				 
			 }
			 else
			 {
				 user.setSortLetter("#");
			 }
			 fixData.add(user);
		}
		Collections.sort(fixData,pinyinComparator);
		userData.clear();
		return fixData;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setData();
		initData();
		layout_new.setOnClickListener(this);
		super.onActivityCreated(savedInstanceState);
		
	}
	
	private void initData() {

		listView.setAdapter(adapter);
	    listView.setOnItemClickListener(this);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =inflater.inflate(R.layout.fragment_contacts,container, false);
		listView=(ListView) view.findViewById(R.id.list_friends);
		View new_frineds=LayoutInflater.from(getActivity()).inflate(R.layout.include_new_friend, null);
		layout_new=(LinearLayout) new_frineds.findViewById(R.id.layout_new);
		layout_near=(LinearLayout)new_frineds.findViewById(R.id.layout_near);
		listView.addHeaderView(new_frineds);
		return view;
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		User user = (User) adapter.getItem(position-1);
		//先进入好友的详细资料页面
		Intent intent =new Intent(getActivity(),SetFriendsInfoActivity.class);
		intent.putExtra("from", "other");
		intent.putExtra("user", user);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.layout_new:
			Intent intent=new Intent(this.getActivity(),NewFriendActivity.class);
			startActivity(intent);
			break;
		}
		
	}

}
