package zcf.demo.adapter;

import java.util.List;

import zcf.demo.activity.R;
import zcf.demo.bean.Friends;
import zcf.demo.utils.FriendUtils;
import zcf.demo.utils.XmppUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class NewFriendAdapter extends BaseAdapter{
    private List<Friends> list;
    private Context context;
    private LayoutInflater inflater;
	public NewFriendAdapter(Context context,List<Friends> list)
	{
		this.context=context;
		this.list=list;
		inflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
	
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if(convertView==null)
		{
			convertView=inflater.inflate(R.layout.item_receive_addfriends,null);
			viewHolder=new ViewHolder();
			viewHolder.name=(TextView) convertView.findViewById(R.id.name);
			viewHolder.but=(Button) convertView.findViewById(R.id.btn_add);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		final String account=list.get(position).getAccount();
		final String name=list.get(position).getName();
		String receiver=list.get(position).getIsReceiver();
		viewHolder.name.setText(name);
		if(receiver.equalsIgnoreCase("no"))
		{
			viewHolder.but.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					XmppUtils.agreeAdd(account);
					XmppUtils.sendAdd(account);
					viewHolder.but.setBackgroundColor(R.drawable.abc_ab_share_pack_holo_dark);
					Friends friends=new Friends(account,name,"yes");
					FriendUtils.updateFriend(context, friends);
				}
			});
			
		}
		else
		{
			viewHolder.but.setBackgroundColor(R.drawable.abc_ab_share_pack_holo_dark);
			 
		}
		return convertView;
	}

	private class ViewHolder
	{
		TextView name;
		Button but;
	}
	
}
