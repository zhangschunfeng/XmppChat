package zcf.demo.adapter;

import java.util.List;

import zcf.demo.activity.R;
import zcf.demo.bean.FaceText;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class EmoteAdapter extends BaseAdapter {
     
	private Context mContext;
	private LayoutInflater mInflater;
	private List<FaceText> datas;
	public EmoteAdapter(Context context, List<FaceText> datas) {
		this.mContext=context;
		this.datas=datas;
		mInflater=LayoutInflater.from(context);
	}
 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_face_text, null);
			holder = new ViewHolder();
			holder.mIvImage = (ImageView) convertView
					.findViewById(R.id.v_face_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FaceText faceText = (FaceText) getItem(position);
		String key = faceText.text.substring(1);
		Drawable drawable =mContext.getResources().getDrawable(mContext.getResources().getIdentifier(key, "drawable", mContext.getPackageName()));
		holder.mIvImage.setImageDrawable(drawable);
		return convertView;
	}

	class ViewHolder {
		ImageView mIvImage;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
