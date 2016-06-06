package zcf.demo.fragment;
import zcf.demo.activity.LoginActivity;
import zcf.demo.activity.R;
import zcf.demo.activity.R.layout;
import zcf.demo.activity.SetMyInfoActivity;
import zcf.demo.db.UserOpenHelper;
import zcf.demo.provider.UserProvider;
import zcf.demo.service.ImChatService;
import zcf.demo.utils.SharePreferenceUtils;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingFragment extends Fragment implements OnClickListener{
	private String text;
	private  RelativeLayout layout_info,layout_blacklist,rl_switch_notification
		,rl_switch_voice,rl_switch_vibrate;
	private Button btn_logout;
	private ImageView iv_close_vibrate,iv_open_vibrate,iv_close_voice,iv_open_voice
		    ,iv_close_notification,iv_open_notification;
	private View view1,view2;
	public SettingFragment()
	{
	
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_set, container,false);
		initView(view);
		return view;
		
	}
	private void initView(View view) {
		layout_info=(RelativeLayout) view.findViewById(R.id.layout_info);
		layout_blacklist=(RelativeLayout) view.findViewById(R.id.layout_blacklist);
		rl_switch_notification=(RelativeLayout) view.findViewById(R.id.rl_switch_notification);
		rl_switch_voice=(RelativeLayout) view.findViewById(R.id.rl_switch_voice);
		rl_switch_vibrate=(RelativeLayout) view.findViewById(R.id.rl_switch_vibrate);
		btn_logout=(Button) view.findViewById(R.id.btn_logout);
		iv_close_vibrate=(ImageView) view.findViewById(R.id.iv_close_vibrate);
		iv_open_vibrate=(ImageView) view.findViewById(R.id.iv_open_vibrate);
		iv_close_voice=(ImageView) view.findViewById(R.id.iv_close_voice);
		iv_open_voice=(ImageView) view.findViewById(R.id.iv_open_voice);
		view1=view.findViewById(R.id.view1);
		view2=view.findViewById(R.id.view2);
		iv_close_notification=(ImageView) view.findViewById(R.id.iv_close_notification);
		iv_open_notification=(ImageView) view.findViewById(R.id.iv_open_notification);
		layout_info.setOnClickListener(this);
		layout_blacklist.setOnClickListener(this);
		rl_switch_notification.setOnClickListener(this);
		rl_switch_voice.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);
		btn_logout.setOnClickListener(this);
		iv_close_vibrate.setOnClickListener(this);
		iv_open_vibrate.setOnClickListener(this);
		iv_close_voice.setOnClickListener(this);
		iv_open_voice.setOnClickListener(this);
		iv_close_notification.setOnClickListener(this);
		iv_open_notification.setOnClickListener(this);
		iv_close_voice.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
	
		switch(v.getId())
		{
		case R.id.layout_blacklist:// 启动到黑名单页面
		//	startAnimActivity(new Intent(getActivity(),BlackListActivity.class));
			break;
		case R.id.layout_info:// 启动到个人资料页面
			Intent intent =new Intent(getActivity(),SetMyInfoActivity.class);
			intent.putExtra("from", "me");
			startActivity(intent);
			break;
		case R.id.btn_logout:
			SharePreferenceUtils.clearSharePre(getActivity());
			//删除数据
			getContext().getContentResolver().delete(UserProvider.URI_USER,null, null);
			ImChatService.disXppCon();
			//关闭service
			ImChatService.getInstaceService().stopSelf();
			getActivity().finish();
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;
		case R.id.rl_switch_notification:
			if(iv_open_notification.getVisibility() == View.VISIBLE) {
				iv_open_notification.setVisibility(View.INVISIBLE);
				iv_close_notification.setVisibility(View.VISIBLE);	
				rl_switch_vibrate.setVisibility(View.GONE);
				rl_switch_voice.setVisibility(View.GONE);
				view1.setVisibility(View.GONE);
				view2.setVisibility(View.GONE);
			} else {
				iv_open_notification.setVisibility(View.VISIBLE);
				iv_close_notification.setVisibility(View.INVISIBLE);
				rl_switch_vibrate.setVisibility(View.VISIBLE);
				rl_switch_voice.setVisibility(View.VISIBLE);
				view1.setVisibility(View.VISIBLE);
				view2.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.rl_switch_voice:
			if (iv_open_voice.getVisibility() == View.VISIBLE) {
				iv_open_voice.setVisibility(View.INVISIBLE);
				iv_close_voice.setVisibility(View.VISIBLE);
			} else {
				iv_open_voice.setVisibility(View.VISIBLE);
				iv_close_voice.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.rl_switch_vibrate:
			if (iv_open_vibrate.getVisibility() == View.VISIBLE) {
				iv_open_vibrate.setVisibility(View.INVISIBLE);
				iv_close_vibrate.setVisibility(View.VISIBLE);
			} else {
				iv_open_vibrate.setVisibility(View.VISIBLE);
				iv_close_vibrate.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.iv_open_vibrate:
			if (iv_open_vibrate.getVisibility() == View.VISIBLE) {
				iv_open_vibrate.setVisibility(View.INVISIBLE);
				iv_close_vibrate.setVisibility(View.VISIBLE);
				}
				break;
		case R.id.iv_close_vibrate:
			if (iv_close_vibrate.getVisibility() == View.VISIBLE) {
				iv_close_vibrate.setVisibility(View.INVISIBLE);
				iv_open_vibrate.setVisibility(View.VISIBLE);
				}
				break;
		case R.id.iv_open_voice:
			if (iv_open_voice.getVisibility() == View.VISIBLE) {
				iv_open_voice.setVisibility(View.INVISIBLE);
				iv_close_voice.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.iv_close_voice:
			if (iv_close_voice.getVisibility() == View.VISIBLE) {
				iv_close_voice.setVisibility(View.INVISIBLE);
				iv_open_voice.setVisibility(View.VISIBLE);
			}
			break;
		}
	}


}
