package zcf.demo.view;

import zcf.demo.activity.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ContactListView extends ListView {
	private Context context;
	private LayoutInflater inflater;
	public ContactListView(Context context) {
		
		super(context);
		addview(context);
		
	}
	public ContactListView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs);
		addview(context);
		
	}

	public ContactListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		addview(context);
		
	}
	
	public ContactListView(Context context, Context context2,
			LayoutInflater inflater) {
		super(context);
		addview(context);
	}
	public void addview(Context context)
	{
		inflater=LayoutInflater.from(context);
		RelativeLayout headView = (RelativeLayout) inflater.inflate(R.layout.include_new_friend, null);
		this.addHeaderView(headView);
	}
}
