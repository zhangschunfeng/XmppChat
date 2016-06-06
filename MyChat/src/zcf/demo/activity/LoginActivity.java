package zcf.demo.activity;

import zcf.demo.service.ImChatService;
import zcf.demo.utils.CommonUtils;
import zcf.demo.utils.SharePreferenceUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity implements OnClickListener {

	private EditText username,password;
	private Button bt_login;
	private TextView register;
	private ProgressDialog progressDialog;
	private LoginBroadCastReceiver loginReceiver;
        // ��Ӧ�Ķ˿ں�
	public static final String SERVICENAME = "win8.1-03062317";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		initView();
		initBroadCast();
		
		
	}
	private void initBroadCast() {
		loginReceiver=new  LoginBroadCastReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction(CommonUtils.isLoginSuccess);
		this.registerReceiver(loginReceiver, filter);
		
	}
	private void initView() {
	  username=(EditText) this.findViewById(R.id.et_username);
      password=(EditText) this.findViewById(R.id.et_password);
	  bt_login=(Button) this.findViewById(R.id.btn_login);
	  register= (TextView) this.findViewById(R.id.btn_register);
	  bt_login.setOnClickListener(this);
	  register.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.btn_login:
			boolean isNetAvaliable=CommonUtils.netAvaliable(this);
			if(!isNetAvaliable)
			{
				Toast.makeText(this, "���粻���ã���������", Toast.LENGTH_LONG).show();
				return;
			}
			login();
			break;
		case R.id.btn_register:
			 Intent intent = new Intent(LoginActivity.this, RegistertActivity.class);
             startActivity(intent);
			break;
		}
	}
	private void login() {
		final String name=username.getText().toString().trim();
		final String word=password.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this, "�û�������Ϊ��"
					, Toast.LENGTH_LONG).show();
			return;
		}

		if (TextUtils.isEmpty(word)) {
			Toast.makeText(this, "���벻��Ϊ��", Toast.LENGTH_LONG).show();
			return;
		}
		SharePreferenceUtils.putSharePre(LoginActivity.this, "username", name);
		SharePreferenceUtils.putSharePre(LoginActivity.this, "password", word);
	    progressDialog=new ProgressDialog(this);
		progressDialog.setTitle("���ڵ�½...���Ե�");
		//������loading��ʱ������㴥����Ļ�������򣬾ͻ������progressDialog��ʧ��Ȼ����ܳ��ֱ������⣬���£�
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		Intent intent=new Intent(LoginActivity.this,ImChatService.class);
		startService(intent);

	}
	@Override
	protected void onDestroy() {
		this.unregisterReceiver(loginReceiver);
		super.onDestroy();
	}
	private class LoginBroadCastReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equalsIgnoreCase(CommonUtils.isLoginSuccess))
			{
				if(progressDialog.isShowing()){
					progressDialog.dismiss();
				}
				boolean isLoginSuccess=intent.getBooleanExtra("isLoginSuccess", false);
				if(isLoginSuccess){//��¼�ɹ�
					Intent intent2=new Intent(LoginActivity.this,MainActivity.class);
					startActivity(intent2);
					finish();
				}
				else{
					Toast.makeText(LoginActivity.this,  "��¼ʧ�ܣ�������������Ƿ������Լ��û����������Ƿ���ȷ",Toast.LENGTH_LONG).show();;
				}
			}
		}
		
	}
}
