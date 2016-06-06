package zcf.demo.activity;

import java.io.FileNotFoundException;
import java.net.URI;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import zcf.demo.bean.User;

import zcf.demo.service.ImChatService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SetMyInfoActivity extends Activity implements OnClickListener{
	private TextView tv_set_nick,tv_set_name;
	private  ImageView iv_set_avator,back;
	private Button btn_add_friend,btn_chat,btn_back;
	private User user;
	private XMPPConnection conn;
	private VCard vCard;
	private RelativeLayout layout_head,layout_nick;
	private int TAKE_PHOTO=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set_info);
		initView();
		initData();
		
	}

	private void initData() {
		tv_set_name.setText(ImChatService.mCurAccout);
		new Thread(new Runnable()
		{

			@Override
			public void run() {
				conn=ImChatService.getInstance();
				vCard=new VCard();
				try {
					vCard.load(conn);
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message message=new Message();
				message.obj=vCard.getNickName();
				message.what=1;
			   handler.sendMessage(message);
				
			}}
		).start();
				
	}

	private void initView() {
		tv_set_nick=(TextView) this.findViewById(R.id.tv_set_nick);
		tv_set_name=(TextView) this.findViewById(R.id.tv_set_name);
		btn_back=(Button) this.findViewById(R.id.btn_back);
		back=(ImageView) this.findViewById(R.id.back);
		back.setOnClickListener(this);
		btn_add_friend=(Button) this.findViewById(R.id.btn_add_friend);	
		layout_head=(RelativeLayout) this.findViewById(R.id.layout_head);
		layout_nick=(RelativeLayout) this.findViewById(R.id.layout_nick);
		iv_set_avator=(ImageView) findViewById(R.id.iv_set_avator);
		btn_chat=(Button) this.findViewById(R.id.btn_chat);
		btn_add_friend.setVisibility(View.GONE);
		btn_chat.setVisibility(View.GONE);
		btn_back.setVisibility(View.GONE);
		layout_nick.setOnClickListener(this);
		layout_head.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId())
		{
		case R.id.layout_head:
			intent =new Intent(Intent.ACTION_PICK);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
			startActivityForResult(intent, TAKE_PHOTO);
			
			break;
		case R.id.layout_nick:
			intent = new Intent(this, UpdateInfoActivity.class);
			finish();
			startActivity(intent);
			break;
		case R.id.back:
			finish();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode==TAKE_PHOTO)
		{
			
			Uri uri=intent.getData();
			float dw = iv_set_avator.getWidth();
			float dh = iv_set_avator.getHeight();			
			Bitmap bitmap = null;
			try {
				 bitmap=BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
			} 
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			iv_set_avator.setImageBitmap(bitmap);
		}
	}
	Handler handler=new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1)
			{
				tv_set_nick.setText((String)msg.obj);
				
			}
				
		};
	};
}
