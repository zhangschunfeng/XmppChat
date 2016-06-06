package zcf.demo.activity;

import zcf.demo.bean.User;
import zcf.demo.service.ImChatService;
import zcf.demo.utils.XmppUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SetFriendsInfoActivity extends Activity implements OnClickListener{
	private TextView tv_set_nick,tv_set_name;
	private Button btn_add_friend,btn_chat,btn_back;
	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set_info);
		initView();
		initData();
		
	}

	private void initData() {
		Intent recv=this.getIntent();
		user=(User) recv.getSerializableExtra("user");
		tv_set_nick.setText(user.getNickName());
		tv_set_name.setText(user.getCount());
		//;
		
	}

	private void initView() {
		tv_set_nick=(TextView) this.findViewById(R.id.tv_set_nick);
		tv_set_name=(TextView) this.findViewById(R.id.tv_set_name);
		btn_add_friend=(Button) this.findViewById(R.id.btn_add_friend);
		btn_back=(Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		btn_chat=(Button) this.findViewById(R.id.btn_chat);
		btn_chat.setOnClickListener(this);
		btn_add_friend.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.btn_back:
			XmppUtils.removeUser(ImChatService.getInstance().getRoster(), user.getCount());
			Toast.makeText(this, "ºÃÓÑÒÑÉ¾³ý",Toast.LENGTH_LONG).show();
			Intent intent1=new Intent(SetFriendsInfoActivity.this,MainActivity.class);
			startActivity(intent1);
			finish();
			break;
		case R.id.btn_add_friend:
			break;
		case R.id.btn_chat:
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra("user", user);
			startActivity(intent);
			finish();
			break;
		}
	}
}
