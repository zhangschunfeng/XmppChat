package zcf.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.text.format.DateFormat;

public class CommonUtils {
	public static String isStartService="isStartService";
	public static String isLoginSuccess="isLoginSuccess";
	public static String loginsuccess="loginsuccess";
	public static final String MSG_TYPE_ADD_FRIEND="msg_type_add_friend";//添加好友
	public static final String MSG_TYPE_ADD_FRIEND_SUCCESS="msg_type_add_friend_success";//同意添加好友
	
	public static final String SPLIT="卍";
	private static SimpleDateFormat formatTime=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static boolean netAvaliable(Context context)
	{
		ConnectivityManager cm=(ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo info=cm.getActiveNetworkInfo();
		if(info!=null)
		{
			return info.isAvailable();
		}
		else
		{
			return false;
		}
	
	}
	//format时间
	public static String forMatTime(Date date)
	{
		String time=formatTime.format(date);
		return time;
	}
}
