package zcf.demo.activity;

import zcf.demo.utils.CommonUtils;
import zcf.demo.utils.SharePreferenceUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ImageView;

public class FlashActivity  extends Activity{
	private ImageView splash;
	private Boolean isClear=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		splash=(ImageView) this.findViewById(R.id.im_splash);
		isClear=SharePreferenceUtils.getSharePreBoolean(FlashActivity.this, CommonUtils.loginsuccess);
		if(!isClear)
		{
			SharePreferenceUtils.clearSharePre(FlashActivity.this);
		}
		splash.postDelayed(new Runnable()
		{

			@Override
			public void run() {
				Intent intent=null;
				String username=SharePreferenceUtils.getSharePreStr(FlashActivity.this,"username");
				String password=SharePreferenceUtils.getSharePreStr(FlashActivity.this,"password");
				if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password))
				{
					intent=new Intent(FlashActivity.this,MainActivity.class);
					intent.putExtra(CommonUtils.isStartService, true);
					
				}
				else
				{
					intent=new Intent(FlashActivity.this,LoginActivity.class);
					
				}
				startActivity(intent);
				finish();
			}
			
		}, 3000);
		
		
	}
}
