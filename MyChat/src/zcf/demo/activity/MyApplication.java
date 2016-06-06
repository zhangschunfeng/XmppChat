package zcf.demo.activity;



import com.baidu.mapapi.SDKInitializer;

import zcf.demo.service.LocationService;
import android.app.Application;


public class MyApplication extends Application {
	public static MyApplication mInstance;
	public static  LocationService locationService;
	@Override
	public void onCreate() {
		// 1.�����������ö���
		mInstance=this;  
		// locationService = new LocationService(getApplicationContext());
	     SDKInitializer.initialize(this); 
	}
	public static MyApplication getInstance() {
		return mInstance;
	}
}
