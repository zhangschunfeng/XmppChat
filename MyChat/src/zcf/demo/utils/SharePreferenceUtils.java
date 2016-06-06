package zcf.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharePreferenceUtils {
	/**
	 * ��ͨ�ֶδ�ŵ�ַ
	 */
	public static String  XMPP="mychatxmpp";
	
	
	public static String getSharePreStr(Context mContext,String field){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(XMPP, 0);
		String s=sp.getString(field,"");//������ֶ�û��Ӧֵ����ȡ���ַ���0
		return s;
	}
	//ȡ��whichSp��field�ֶζ�Ӧ��int���͵�ֵ
	public static int getSharePreInt(Context mContext,String field){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(XMPP, 0);
		int i=sp.getInt(field,0);//������ֶ�û��Ӧֵ����ȡ��0
		return i;
	}
	
	//ȡ��whichSp��field�ֶζ�Ӧ��boolean���͵�ֵ
	public static boolean getSharePreBoolean(Context mContext,String field){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(XMPP, 0);
		boolean i=sp.getBoolean(field, false);//������ֶ�û��Ӧֵ����ȡ��0
		return i;
	}
	//����string���͵�value��whichSp�е�field�ֶ�
	public static void putSharePre(Context mContext,String field,String value){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(XMPP, 0);
		sp.edit().putString(field, value).commit();
	}
	//����int���͵�value��whichSp�е�field�ֶ�
	public static void putSharePre(Context mContext,String field,int value){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(XMPP, 0);
		sp.edit().putInt(field, value).commit();
	}
	
	//����boolean���͵�value��whichSp�е�field�ֶ�
	public static void putSharePre(Context mContext,String field,Boolean value){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(XMPP, 0);
		sp.edit().putBoolean(field, value).commit();
	}
	//��ձ��������
		public static void clearSharePre(Context mContext){
			try {
				SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(XMPP, 0);
				sp.edit().clear().commit();
			} catch (Exception e) {
			}
		}
		
}
