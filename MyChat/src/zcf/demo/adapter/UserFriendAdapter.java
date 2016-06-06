package zcf.demo.adapter;

import java.util.List;

import zcf.demo.activity.R;
import zcf.demo.bean.User;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;



/** �����б�
  * @ClassName: UserFriendAdapter
  * @Description: TODO
  * @author smile
  * @date 2014-6-12 ����3:03:40
  */

public class UserFriendAdapter extends BaseAdapter //implements SectionIndexer 
{
	private Context ct;
	private List<User> data;

	public UserFriendAdapter(Context ct, List<User> datas) {
		this.ct = ct;
		this.data = datas;
	}

	/** ��ListView���ݷ����仯ʱ,���ô˷���������ListView
	  * @Title: updateListView
	  * @Description: TODO
	  * @param @param list 
	  * @return void
	  * @throws
	  */
	public void updateListView(List<User> list) {
		this.data = list;
		notifyDataSetChanged();
	}

	public void remove(User user){
		this.data.remove(user);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(ct).inflate(
					R.layout.item_user_friend, null);
			viewHolder = new ViewHolder();
			viewHolder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.tv_friend_name);
			viewHolder.avatar = (ImageView) convertView
					.findViewById(R.id.img_friend_avatar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		User friend = data.get(position);
		final String count = friend.getCount();
		final String nickName = friend.getNickName();

		
	    viewHolder.avatar.setImageDrawable(ct.getResources().getDrawable(R.drawable.head));
		viewHolder.name.setText(nickName);

		// ����position��ȡ���������ĸ��Char asciiֵ
		int section = getSectionForPosition(position);
//		// �����ǰλ�õ��ڸ÷�������ĸ��Char��λ�� ������Ϊ�ǵ�һ�γ���
		if (position == getPositionForSection(section)) {
			viewHolder.alpha.setVisibility(View.VISIBLE);
			//viewHolder.alpha.setText(friend.getSortLetters());
	} else {
			viewHolder.alpha.setVisibility(View.GONE);
		}

		return convertView;
	}

	static class ViewHolder {
		TextView alpha;// ����ĸ��ʾ
		ImageView avatar;
		TextView name;
	}
	public int getSectionForPosition(int position) {
		return data.get(position).getSortLetter().charAt(0);
	}
	/**
	 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
	 */
	@SuppressLint("DefaultLocale")
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = data.get(i).getSortLetter();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section){
				return i;
			}
		}

		return -1;
	}

}