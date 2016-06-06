package zcf.demo.activity;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;


public class Activity_location extends Activity{
	private MapView mapView;
	private Intent intent;
	private BaiduMap mBaiduMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_look_location);
		initView();
		initData();
	}
	private void initData() {
		//intent=this.getIntent();
		//String longitude=intent.getStringExtra("longitude");
		//String latitude=intent.getStringExtra("latitude");
		//����Maker�����  
		LatLng point = new LatLng(31.83, 117.25); 
		//����Markerͼ��  
		BitmapDescriptor bitmap = BitmapDescriptorFactory  
		    .fromResource(R.drawable.icon_openmap_mark);  
		//����MarkerOption�������ڵ�ͼ�����Marker  
		OverlayOptions option = new MarkerOptions()  
		    .position(point)  
		    .icon(bitmap);  
		//�ڵ�ͼ�����Marker������ʾ  
		mBaiduMap.addOverlay(option);
	}
	private void initView() {
		mapView=(MapView) this.findViewById(R.id.mapView);
		mBaiduMap = mapView.getMap();  
		//������ͨͼ   
		mBaiduMap.setTrafficEnabled(true);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
	@Override
	protected void onResume() {
	
		super.onResume();
		mapView.onResume();
	}
	@Override
	protected void onPause() {
		
		super.onPause();
		mapView.onPause();
	}
}
