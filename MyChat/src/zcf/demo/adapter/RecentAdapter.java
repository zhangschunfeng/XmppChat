package zcf.demo.adapter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import zcf.demo.activity.R;
import zcf.demo.bean.ImMessage;
import zcf.demo.bean.RecentInfo;
import zcf.demo.db.SmsOpenHelper;
import zcf.demo.service.GetInfoService;
import zcf.demo.utils.CommonUtils;
import zcf.demo.view.EmoticonsTextView;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RecentAdapter extends BaseAdapter {
	private Context context;
	private List<RecentInfo> mListInfo;
	private LayoutInflater mInflater;
	private SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public RecentAdapter(Context context ,List<RecentInfo> ListInfo)
	{ 
		this.context=context;
		this.mListInfo=ListInfo;
		mInflater=LayoutInflater.from(context);
		
	}
	@Override
	public int getCount() {
	
		return mListInfo.size();
	}
	
	@Override
	public Object getItem(int position) {

		return mListInfo.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null)
		{ 
			 viewHolder=new ViewHolder();
			 convertView=mInflater.inflate(R.layout.item_conversation,null);
			 viewHolder.tv_recent_name=(TextView)convertView.findViewById(R.id.tv_recent_name);
			 viewHolder.tv_recent_msg=(EmoticonsTextView)convertView.findViewById(R.id.tv_recent_msg);
			 viewHolder.tv_recent_time=(TextView) convertView.findViewById(R.id.tv_recent_time);
			 viewHolder.tv_recent_unread=(TextView)convertView.findViewById(R.id.tv_recent_unread);
			
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder=(ViewHolder) convertView.getTag();
			
		}
		 String body =mListInfo.get(position).getContent();
		 String account = mListInfo.get(position).getName();
		 String time=mListInfo.get(position).getTime();
		 //new Date(Long.parseLong(time))
		 time=simpleDateformat.format(new Date(Long.parseLong(time)));
		 //异步操作类获得根据用户数据
		 new MyAsynTask(viewHolder.tv_recent_name,account).execute(account);
		 viewHolder.tv_recent_msg.setText(body);
		 viewHolder.tv_recent_time.setText(time);
		 viewHolder.tv_recent_unread.setText("5");
		return convertView;
	}


   class ViewHolder
   {
	   TextView tv_recent_name;
	   EmoticonsTextView tv_recent_msg;
	   TextView tv_recent_time;
	   TextView tv_recent_unread;
   }
   class MyAsynTask extends AsyncTask<String,Void, String>
	{
	   private  String account;
	   private  TextView textView;
	   MyAsynTask(TextView textView,String account)
	   {
		   this.textView=textView;
		   this.account=account;
	   }
	   @Override
	protected void onPostExecute(String result) {
	
		super.onPostExecute(result);
		textView.setText(result);
	}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result=GetInfoService.getUserNikeName(params[0]);
			if(result==null)
			{
				return account;
			}
			else
			{
				return result;
			}
		}
		
	}


}
