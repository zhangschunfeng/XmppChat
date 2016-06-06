package zcf.demo.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import zcf.demo.activity.R;
import zcf.demo.bean.User;
import zcf.demo.service.ImChatService;
import zcf.demo.utils.CommonUtils;
import zcf.demo.utils.SharePreferenceUtils;
import zcf.demo.utils.XmppUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddFriendAdapter extends BaseAdapter  {

	private LayoutInflater inflater;
	private List<User> userList;
	private Context context;
	public AddFriendAdapter(Context context,List<User> list)
	{
		this.context=context;
		inflater=LayoutInflater.from(context);
		userList=list;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	     ViewHolder viewHolder;
		if(convertView==null)
		{   viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_addfriends,null);
			viewHolder.name=(TextView) convertView.findViewById(R.id.name);
			viewHolder.add_but=(Button) convertView.findViewById(R.id.btn_add);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder=(ViewHolder) convertView.getTag();
			
		}
		viewHolder.name.setText(userList.get(position).getCount());
		final String friendname=userList.get(position).getCount();
		final String username=SharePreferenceUtils.getSharePreStr(context, "username");
		viewHolder.add_but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//方法一：有问题，只能在刚登陆时才能接受的
				Presence subscription=new Presence(Presence.Type.subscribe); 
				subscription.setTo(friendname+"@"+ImChatService.getInstance().getServiceName());
				ImChatService.getInstance().sendPacket(subscription);
				Toast.makeText(context, "已发送好友请求",Toast.LENGTH_LONG).show();
				//方法二
//				Roster roster=ImChatService.getInstance().getRoster();
//				XmppUtils.addUser(roster,friendname+"@121.42.52.227",friendname);
//				//注意消息的协议格式 =》接收者卍发送者卍消息类型卍消息内容卍发送时间
//				String message=friendname+CommonUtils.SPLIT+username+CommonUtils.SPLIT+CommonUtils.MSG_TYPE_ADD_FRIEND+CommonUtils.SPLIT+"你好"+CommonUtils.SPLIT+new SimpleDateFormat("MM-dd HH:mm").format(new Date());
//				try {
//					XmppUtils.sendMessage(ImChatService.getInstance(), message,friendname);
//					Toast.makeText(context, "已发送添加好友请求", Toast.LENGTH_LONG).show();
//			} catch (XMPPException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				
			}
		});
		
		return convertView;
	}

	private class ViewHolder
	{
		TextView name;
		Button add_but;
	}

	
}
