package zcf.demo.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import zcf.demo.activity.Activity_location;
import zcf.demo.activity.R;
import zcf.demo.bean.ImMessage;
import zcf.demo.view.EmoticonsTextView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {
	///搞了很长时间的问题在于baseAdapter中getItemViewType中必须从返回值文0-----count-1，即初始值从0开始
	public final static int TXT_TO=0;
	public final static int TXT_FROM=1;
	public final static int POI_TO=2;
	public final static int POI_FROM=3;
	public final static int IMG_TO=5;
	public final static int IMG_FROM=6;
	public final static String TYPE_TXT="text";
	public final static String TYPE_POI="location";
	private Context context;
	private List<ImMessage> mListMessage;
	private LayoutInflater mInflater;
	private SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private ListView listView;
	public ChatAdapter(Context context ,List<ImMessage> ListMessage,ListView listView )
	{ 
		this.context=context;
		this.listView=listView;
		this.mListMessage=ListMessage;
		mInflater=LayoutInflater.from(context);
		
	}
	@Override
	public int getCount() {

		return mListMessage.size();
	}
	
	public void refreshList(List<ImMessage> items) {
		this.mListMessage= items;
		this.notifyDataSetChanged();
		listView.setSelection(items.size() - 1);
	}
	
	
	@Override
	public Object getItem(int position) {

		return mListMessage.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		ImMessage message=mListMessage.get(position);
		int type=0;
		switch(message.getType())
		{
		case TXT_TO:
				type=0;
				break;
		case TXT_FROM:
				type=1;
				break;
		case POI_TO:
				type=2;
				break;
		case POI_FROM:
				type=3;
				break;
		default:
				type=0;
				break;	
		}
		return type;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			//发送字符的view
			 if(getItemViewType(position)==TXT_TO||getItemViewType(position)==TXT_FROM)
			 {
				 	ViewHolder viewHolder;
				 	if(convertView==null)
					{ 
				 		viewHolder=new ViewHolder();
					 	if(getItemViewType(position)==TXT_TO)
						{
							convertView=mInflater.inflate(R.layout.item_chat_sent_message,null);
							viewHolder.iv_avatar=(ImageView) convertView.findViewById(R.id.iv_avatar);
							viewHolder.tv_message=(EmoticonsTextView) convertView.findViewById(R.id.tv_message);
							viewHolder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
						}
						else if(getItemViewType(position)==TXT_FROM)
						{
							convertView=mInflater.inflate(R.layout.item_chat_received_message,null);
							viewHolder.iv_avatar=(ImageView) convertView.findViewById(R.id.iv_avatar);
							viewHolder.tv_message=(EmoticonsTextView) convertView.findViewById(R.id.tv_message);
							viewHolder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
						}
					 	convertView.setTag(viewHolder);
				 	
					}
					else
					{
						viewHolder=(ViewHolder) convertView.getTag();
						
					}
					viewHolder.tv_message.setText(mListMessage.get(position).getContent());
					viewHolder.tv_time.setText(simpleDateformat.format(mListMessage.get(position).getTime()));

			 }
			
			else if(getItemViewType(position)==POI_TO||getItemViewType(position)==POI_FROM)
			{
				ViewHolder1 viewHolder1;
				if(convertView==null)
				{
					viewHolder1=new ViewHolder1();
					if(getItemViewType(position)==POI_TO)
					{
						convertView=mInflater.inflate(R.layout.item_chat_sent_location,null);
						viewHolder1.iv_avatar=(ImageView) convertView.findViewById(R.id.iv_avatar);
						viewHolder1.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
						viewHolder1.tv_location=(TextView) convertView.findViewById(R.id.tv_location);
						
					}
					else if(getItemViewType(position)==POI_FROM)
					{
						convertView=mInflater.inflate(R.layout.item_chat_received_location,null);
						viewHolder1.iv_avatar=(ImageView) convertView.findViewById(R.id.iv_avatar);
						viewHolder1.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
						viewHolder1.tv_location=(TextView) convertView.findViewById(R.id.tv_location);
						
					}
					convertView.setTag(viewHolder1);
				}
				else
				{
					viewHolder1=(ViewHolder1) convertView.getTag();
				}
				viewHolder1.tv_time.setText(simpleDateformat.format(mListMessage.get(position).getTime()));
				String str[]=mListMessage.get(position).getContent().split("@");
				viewHolder1.tv_location.setText(mListMessage.get(position).getContent());
				
				//final String longitude=str[2];
				//final String latitude=str[1];
				viewHolder1.tv_location.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(context,Activity_location.class);
						//intent.putExtra("latitude", latitude);
						//intent.putExtra("longitude", longitude);
						context.startActivity(intent);
						
					}
				});
			}
			
			 return convertView;
	}

    @Override
    public int getViewTypeCount() {
    	// TODO Auto-generated method stub
    	return 4;
    }
    @Override
    public void notifyDataSetChanged() {
    	super.notifyDataSetChanged();
    }
    
   class ViewHolder
   {
	   TextView tv_time;
	   EmoticonsTextView tv_message;
	   ImageView iv_avatar;
   }
   class ViewHolder1
   {
	   TextView tv_time;
	   ImageView iv_avatar;
	   TextView tv_location;
	   
   }
}
