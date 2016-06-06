package zcf.demo.activity;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import zcf.demo.service.ImChatService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * 设置昵称和性别
 * 
 * @ClassName: SetNickAndSexActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-7 下午4:03:40
 */
public class UpdateInfoActivity extends Activity implements OnClickListener{

	EditText edit_nick;
	private ImageView back,updata;
	private XMPPConnection conn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set_updateinfo);
		initView();
	}

	private void initView() {
		edit_nick = (EditText) findViewById(R.id.edit_nick);
		back=(ImageView) findViewById(R.id.back);
		updata=(ImageView) findViewById(R.id.update);
		back.setOnClickListener(this);
		updata.setOnClickListener(this);
		
	}

	/** 修改资料
	  * updateInfo
	  * @Title: updateInfo
	  * @return void
	  * @throws
	  */
	private void updateInfo(String nick) {
		conn=ImChatService.getInstance();
		VCard vCard=new VCard();
		try {
			vCard.load(conn);
			vCard.setNickName(nick);
			vCard.save(conn);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId())
		{
			
		case R.id.back:
			intent=new Intent(UpdateInfoActivity.this,SetMyInfoActivity.class);
			finish();
			startActivity(intent);
			break;
		case R.id.update:
			if(edit_nick.getText().toString().equals(""))
			{
				Toast.makeText(UpdateInfoActivity.this, "请输出昵称", Toast.LENGTH_LONG).show();
			}
			else
			{
				updateInfo(edit_nick.getText().toString());
				Toast.makeText(UpdateInfoActivity.this, "修改成功", Toast.LENGTH_LONG).show();
				intent=new Intent(UpdateInfoActivity.this,SetMyInfoActivity.class);
				finish();
				startActivity(intent);
			}
			
		}
		
	}
}
