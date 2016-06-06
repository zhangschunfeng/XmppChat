package zcf.demo.activity;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;

import zcf.demo.service.ImChatService;
import zcf.demo.utils.CommonUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegistertActivity extends Activity implements OnClickListener{
	private EditText  et_username,et_password,et_repassword;
    private ImageView back;
    private Button bt_register;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		initView();
		
	}
	private void initView() {
		et_username=(EditText) this.findViewById(R.id.et_username);
		et_password=(EditText) this.findViewById(R.id.et_password);
		et_repassword=(EditText) this.findViewById(R.id.et_repassword);
		back=(ImageView) this.findViewById(R.id.back);
		bt_register=(Button) this.findViewById(R.id.btn_register);
		back.setOnClickListener(this);
		bt_register.setOnClickListener(this);
		
		
	}
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.back:
			Intent Backintent=new Intent(RegistertActivity.this,LoginActivity.class);
			startActivity(Backintent);
			break;
		case R.id.btn_register:
			boolean isNetCon=CommonUtils.netAvaliable(this);
			if(!isNetCon)
			{
				Toast.makeText(RegistertActivity.this, "����������", Toast.LENGTH_LONG).show();
				return;
			}
			register();
			
			break;
		}
	}
	private void register() {
		final String name=et_username.getText().toString().trim();
		final String password=et_password.getText().toString().trim();
		String repassword=et_repassword.getText().toString().trim();
		if(TextUtils.isEmpty(name))
		{
			Toast.makeText(RegistertActivity.this, "�û�������Ϊ��", Toast.LENGTH_LONG).show();
			return;
			
		}
		else if(TextUtils.isEmpty(password))
		{
			Toast.makeText(RegistertActivity.this, "���벻��Ϊ��", Toast.LENGTH_LONG).show();
			return;
			
		}
		else if(TextUtils.isEmpty(repassword))
		{
			Toast.makeText(RegistertActivity.this, "�ظ����벻��Ϊ��", Toast.LENGTH_LONG).show();
			return;
			
		}
		else if(!(password.equals(repassword)))
		{
			Toast.makeText(RegistertActivity.this, "�������벻ͬ", Toast.LENGTH_LONG).show();
			return;
			
		}
		ProgressDialog progressDialog=new ProgressDialog(this);
		progressDialog.setTitle("����ע��.....");
		//������loading��ʱ������㴥����Ļ�������򣬾ͻ������progressDialog��ʧ��Ȼ����ܳ��ֱ������⣬���£�
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		new Thread(new Runnable()
		{

			@Override
			public void run() {
	
				XMPPConnection conn =ImChatService.getInstance();
		        // ��ʼ����
				try {
					conn.connect();
					AccountManager accountManager =conn.getAccountManager();
					accountManager.createAccount(name, password);
					Toast.makeText(RegistertActivity.this, "��ϲ��ע��ɹ�",
			                   Toast.LENGTH_LONG).show();
					finish();
					Intent intent=new Intent(RegistertActivity.this,LoginActivity.class);
					startActivity(intent);
				} catch (XMPPException e) {
			
					e.printStackTrace();
					Toast.makeText(RegistertActivity.this, "����������ʧ��",
			                   Toast.LENGTH_LONG).show();
				}
				
			}
		 
	 }).start();
	 
      

			
	}
//		Registration reg=new Registration();
//		reg.setType(IQ.Type.SET);
//		reg.setTo(conn.getServiceName());
//		// ���addAttribute����Ϊ�գ������������������־��android�ֻ������İɣ���������
//		reg.setUsername(name);
//		reg.setPassword(password);
//		reg.addAttribute("android", "geolo_createUser_android");
//		PacketFilter filter = new AndFilter(new PacketIDFilter(reg
//                
//				.getPacketID()), new PacketTypeFilter(IQ.class));
//        PacketCollector collector = conn.createPacketCollector(filter);
//        conn.sendPacket(reg);
//        IQ result = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
//       collector.cancel();// ֹͣ����results���Ƿ�ɹ��Ľ����
//        if (result == null) {
//            Toast.makeText(RegistertActivity.this, "ע��ʧ��",
//                    Toast.LENGTH_SHORT).show();
//        }
//        else if(result.getType()==IQ.Type.ERROR)
//        {
//        	  Toast.makeText(getApplicationContext(), result.getError().toString(),
//                      Toast.LENGTH_SHORT).show();
//        }
//        else if (result.getType() == IQ.Type.RESULT) {
//            Toast.makeText(getApplicationContext(), "��ϲ��ע��ɹ�",
//                    Toast.LENGTH_SHORT).show();
//        }
//
//      
	
}
