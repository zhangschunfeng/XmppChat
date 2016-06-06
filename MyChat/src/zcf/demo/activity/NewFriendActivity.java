package zcf.demo.activity;

import java.util.ArrayList;
import java.util.List;

import zcf.demo.adapter.NewFriendAdapter;
import zcf.demo.bean.Friends;
import zcf.demo.db.FriendsOpenHelper.FriTable;
import zcf.demo.db.UserOpenHelper.UserTable;
import zcf.demo.provider.FriendProvider;
import zcf.demo.provider.UserProvider;
import zcf.demo.utils.FriendUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NewFriendActivity extends Activity implements OnClickListener{
	private ImageView back;
	private ListView listview;
	private List<Friends> friendsPool;
	private CursorAdapter cusorAdapter;
	private NewFriendAdapter newAdapter;
	private static final int FRESS=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friends_add);
		initView();
		//setData();
		initData();
		
	}
	
	private void initData() {
		friendsPool=new ArrayList<Friends>();
		newAdapter=new NewFriendAdapter(NewFriendActivity.this,friendsPool);
		listview.setAdapter(newAdapter);	
		new Thread(new Runnable()
		{

			@Override
			public void run() {
				Cursor cursor=NewFriendActivity.this.getContentResolver().query(FriendProvider.URI_FRIEND, null, null, null, null);
				Friends friends=null;
				while(cursor.moveToNext())
				{
					String account=cursor.getString(cursor.getColumnIndex(FriTable.COUNT));
					String name=cursor.getString(cursor.getColumnIndex(FriTable.NAME));
					String receiver=cursor.getString(cursor.getColumnIndex(FriTable.RECEIVER));
					friends=new Friends(account,name,receiver);
					friendsPool.add(friends);
					mHandler.sendEmptyMessage(FRESS);
				}
				
			}}).start();
			
	}
	private Handler mHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what)
			{
			case FRESS:
				newAdapter.notifyDataSetChanged();
				
			}
		}
	};
	private void setCuradapter(Cursor cursor)
	{
		 cusorAdapter=new CursorAdapter(NewFriendActivity.this,cursor)
		{

			@Override
			public void bindView(View view, Context context, Cursor c) {
				TextView name=(TextView) view.findViewById(R.id.name);
				Button btn_add=(Button) view.findViewById(R.id.btn_add);
				name.setText(c.getString(c.getColumnIndex(FriTable.NAME)));
				String reiver=c.getString(c.getColumnIndex(FriTable.RECEIVER));
				String account=c.getString(c.getColumnIndex(FriTable.COUNT));
				if(reiver.equalsIgnoreCase("yes"))
				{
					btn_add.setBackgroundResource(R.drawable.abc_list_selector_background_transition_holo_dark);
				}
				btn_add.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//
							
					}
				});
			}

			@Override
			public View newView(Context context, Cursor c, ViewGroup viewGroup) {
			     View view=LayoutInflater.from(context).inflate(R.layout.item_receive_addfriends,viewGroup);
				return view;
			}
			
		};
	
	}
	private void initView() {
		back=(ImageView) this.findViewById(R.id.back);
		listview=(ListView) this.findViewById(R.id.friends_add);
		back.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.back:
			finish();
			break;
		}
		
	}
	
}
