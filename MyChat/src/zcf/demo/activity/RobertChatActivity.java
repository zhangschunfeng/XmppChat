package zcf.demo.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zcf.demo.adapter.ChatMessageAdapter;
import zcf.demo.bean.ChatMessage;
import zcf.demo.bean.ChatMessage.Type;
import zcf.demo.service.ImChatService;
import zcf.demo.utils.HttpUtils;
import zcf.demo.utils.SharePreferenceUtils;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class RobertChatActivity extends Activity implements OnClickListener{
	private ListView listview;
	private ChatMessageAdapter adapter;
	private Button sendBut;
	private EditText input_send_msg;
	private List<ChatMessage> chatData;
	private String username;
	private MyHandler handler = new MyHandler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_robert);
		initView();
		initData();
		
	}
	private void initData() {
		username=SharePreferenceUtils.getSharePreStr(this,"username");
		chatData=new ArrayList<ChatMessage>();
		adapter=new ChatMessageAdapter(this,chatData);
		listview.setAdapter(adapter);
		
		
	}
	private void initView() {
	listview=(ListView) this.findViewById(R.id.chat_msg_listview);
	sendBut=(Button) this.findViewById(R.id.send_msg_but);
	input_send_msg=(EditText) this.findViewById(R.id.send_msg_input);
	sendBut.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		final String msg = input_send_msg.getText().toString();
		if (TextUtils.isEmpty(msg)) {
			Toast.makeText(this, "消息不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		ChatMessage toMessage = new ChatMessage();
		toMessage.setName(username);
		toMessage.setDate(new Date());
		toMessage.setMessage(msg);
		toMessage.setType(Type.OUTCOME);
		chatData.add(toMessage);
		adapter.notifyDataSetChanged();
		listview.setSelection(chatData.size() - 1);
		input_send_msg.setText("");
		new Thread() {
			@Override
			public void run() {
				ChatMessage fromMessage = HttpUtils.getChatMessage(msg);
				Message message = new Message();
				message.obj = fromMessage;
				handler.sendMessage(message);
			}

		}.start();

	}
	private class MyHandler extends Handler
	{
		
		@Override
		public void handleMessage(Message msg) {
			
			ChatMessage recvMessage=(ChatMessage)msg.obj;
			chatData.add(recvMessage);
			adapter.notifyDataSetChanged();
			listview.setSelection(chatData.size()-1);
			
		}
	}

	
}
