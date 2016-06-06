package zcf.demo.adapter;

import java.text.SimpleDateFormat;
import java.util.List;
import zcf.demo.activity.R;
import zcf.demo.bean.ChatMessage;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatMessageAdapter extends BaseAdapter {

	private List<ChatMessage> chatData;
	private LayoutInflater flater;
	public ChatMessageAdapter(Context context,List<ChatMessage> chatData)
	{
		this.chatData=chatData;
		flater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
	
		return chatData.size();
	}

	@Override
	public Object getItem(int position) {
	
		return chatData.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatMessage message = chatData.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			//根据不同类型设置数据
			if (getItemViewType(position) == 0) {
				convertView = flater.inflate(R.layout.item_in_msg, parent,
						false);
				holder.date = (TextView) convertView.findViewById(R.id.in_time);
				holder.msg = (TextView) convertView.findViewById(R.id.in_msg);
				holder.user=(TextView)convertView.findViewById(R.id.user);
			} else {
				convertView = flater.inflate(R.layout.item_to_msg, parent,
						false);
				holder.date = (TextView) convertView.findViewById(R.id.to_time);
				holder.msg = (TextView) convertView.findViewById(R.id.to_msg);
				holder.user=(TextView)convertView.findViewById(R.id.user);

			}
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		//设置数据
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		holder.date.setText(df.format(message.getDate()));
		holder.msg.setText(message.getMessage());
        holder.user.setText(message.getName());
		return convertView;
	}
	@Override
	public int getItemViewType(int position) {
		ChatMessage message=chatData.get(position);
		if(message.getType()==ChatMessage.Type.INCOME)
			return 0;
		else
			return 1;
	}
	
	//获取View的中类
	@Override
	public int getViewTypeCount() {
	
		return 2;
	}
	public class ViewHolder
	{
		TextView user;
		TextView date;
		TextView msg;
	}
	
}
