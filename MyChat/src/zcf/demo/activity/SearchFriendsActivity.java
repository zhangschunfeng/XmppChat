package zcf.demo.activity;

import java.util.ArrayList;
import java.util.List;

import zcf.demo.adapter.AddFriendAdapter;
import zcf.demo.adapter.UserFriendAdapter;
import zcf.demo.bean.User;
import zcf.demo.service.ImChatService;
import zcf.demo.utils.XmppUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class SearchFriendsActivity extends Activity implements OnClickListener{

	private Button but_press;
	private EditText edit_nick;
	private ListView friends_list;
    private String I,YOU;
    private ProgressDialog progressDialog;
    private List<User> listUser;
    private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_searchfriends);
		initView();
		
	}
	void initView()
	{
		friends_list=(ListView) this.findViewById(R.id.friends_list);
		edit_nick=(EditText) this.findViewById(R.id.edit_nick);
		but_press=(Button) this.findViewById(R.id.but_press);
		back=(ImageView) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		but_press.setOnClickListener(this);
		progressDialog=new ProgressDialog(this);
		progressDialog.setTitle("正在查询...");
		
	}
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.but_press:
			if(TextUtils.isEmpty(edit_nick.getText().toString())){
				return;
			}
			searchUser();
			break;
		case R.id.back:
			finish();
			break;
			
		}
		
	}
	private void searchUser() {
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				listUser=XmppUtils.searchUsers(ImChatService.getInstance(), edit_nick.getText().toString());
				if(listUser.size()>0){
					mHandler.sendEmptyMessage(1);
				}else{
					mHandler.sendEmptyMessage(-1);
				}
			}
		}).start();
	}
	private Handler mHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
			switch (msg.what) {
			case 1:
				AddFriendAdapter ddFriendAdapter=new AddFriendAdapter(SearchFriendsActivity.this, listUser);
				friends_list.setAdapter(ddFriendAdapter);
				break;
			case -1:
				Toast.makeText(SearchFriendsActivity.this,"查询无结果",Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(SearchFriendsActivity.this,"申请已发送",Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
}
