package zcf.demo.provider;

import zcf.demo.db.SmsOpenHelper;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class SmsProvider extends ContentProvider {

	private static final String	AUTHORITIES	= SmsProvider.class.getCanonicalName();

	static UriMatcher			mUriMatcher;
	public static Uri			URI_SESSION	= Uri.parse("content://" + AUTHORITIES + "/session");
	public static Uri			URI_SMS		= Uri.parse("content://" + AUTHORITIES + "/sms");
	public static Uri			URI_SMSREAD		= Uri.parse("content://" + AUTHORITIES + "/smsread");
	private SmsOpenHelper		mHelper;
	public static final int		SMS			= 1;
	public static final int		SESSION		= 2;
	public static final int     SMSREAD   =3;
	//���Ȱ�����Ҫƥ��Uri·��ȫ����ע���ϣ����£�
    //����UriMatcher.NO_MATCH��ʾ��ƥ���κ�·���ķ�����(-1)��
	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// ���ƥ�����
		//   uriMatcher.addURI(����, path, ������);//�����Ҫƥ��uri�����ƥ��ͻ᷵��ƥ����
		//�Ϳ���ʹ��uriMatcher.match(uri)�����������Uri����ƥ�䣬���ƥ��ͷ���ƥ���룬
		mUriMatcher.addURI(AUTHORITIES, "/sms", SMS);
		mUriMatcher.addURI(AUTHORITIES, "/session", SESSION);
		mUriMatcher.addURI(AUTHORITIES, "/smsread", SMSREAD);
	}
	@Override
	public boolean onCreate() {
		mHelper = new SmsOpenHelper(getContext());
		if (mHelper != null) {
			return true;
		}
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		switch (mUriMatcher.match(uri)) {
		case SMS:
			cursor =
					mHelper.getReadableDatabase().query(SmsOpenHelper.TABLE, projection, selection, selectionArgs,
							null, null, sortOrder);
			System.out.println("--------------SmsProvider querySuccess--------------");
			break;
		case SMSREAD:
			SQLiteDatabase dbb = mHelper.getReadableDatabase();
			cursor =dbb.rawQuery("select * from " +SmsOpenHelper.TABLE +" where from_account= ? and reader = ? order by time asc",selectionArgs );
			break;
		case SESSION:
			SQLiteDatabase db = mHelper.getReadableDatabase();
			cursor = db.rawQuery("select * from "//
					+ "(select * from "+SmsOpenHelper.TABLE+" where from_account = ? or to_account = ? order by time asc)" //
					+ " group by session_account", selectionArgs);//
		default:
			break;
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (mUriMatcher.match(uri)) {
		case SMS:
			// ����֮����ڵ�id
			long id = mHelper.getWritableDatabase().insert(SmsOpenHelper.TABLE, "", values);
			if (id > 0) {
				System.out.println("--------------SmsProvider insertSuccess--------------");
				//
				// ContentUris�����ڻ�ȡUri·�������ID���֣����������Ƚ�ʵ�õķ�����
				//        withAppendedId(uri, id)����Ϊ·������ID����
				//       parseId(uri)�������ڴ�·���л�ȡID����
				uri = ContentUris.withAppendedId(uri, id);
				// �������ݸı���ź�
				getContext().getContentResolver().notifyChange(SmsProvider.URI_SMS, null);
			}
			break;

		default:
			break;
		}
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int deleteCount = 0;
		switch (mUriMatcher.match(uri)) {
		case SMS:
			// ����ɾ���˼�������
			deleteCount = mHelper.getWritableDatabase().delete(SmsOpenHelper.TABLE, selection, selectionArgs);
			if (deleteCount > 0) {
				System.out.println("--------------SmsProvider deleteSuccess--------------");
				// �������ݸı���ź�
				getContext().getContentResolver().notifyChange(SmsProvider.URI_SMS, null);
			}
			break;

		default:
			break;
		}
		return deleteCount;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,String[] selectionArgs) {
		int updateCount = 0;
		switch (mUriMatcher.match(uri)) {
		case SMS:
			// �����˼�������
			updateCount = mHelper.getWritableDatabase().update(SmsOpenHelper.TABLE, values, selection, selectionArgs);
			if (updateCount > 0) {
				System.out.println("--------------SmsProvider updateSuccess--------------");
				// �������ݸı���ź�
				getContext().getContentResolver().notifyChange(SmsProvider.URI_SMS, null);
			}
			break;

		default:
			break;
		}
		return updateCount;
	}

}
