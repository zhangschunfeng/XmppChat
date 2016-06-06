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
	//首先把你需要匹配Uri路径全部给注册上，如下：
    //常量UriMatcher.NO_MATCH表示不匹配任何路径的返回码(-1)。
	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// 添加匹配规则
		//   uriMatcher.addURI(主机, path, 返回码);//添加需要匹配uri，如果匹配就会返回匹配码
		//就可以使用uriMatcher.match(uri)方法对输入的Uri进行匹配，如果匹配就返回匹配码，
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
			// 插入之后对于的id
			long id = mHelper.getWritableDatabase().insert(SmsOpenHelper.TABLE, "", values);
			if (id > 0) {
				System.out.println("--------------SmsProvider insertSuccess--------------");
				//
				// ContentUris：用于获取Uri路径后面的ID部分，它有两个比较实用的方法：
				//        withAppendedId(uri, id)用于为路径加上ID部分
				//       parseId(uri)方法用于从路径中获取ID部分
				uri = ContentUris.withAppendedId(uri, id);
				// 发送数据改变的信号
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
			// 具体删除了几条数据
			deleteCount = mHelper.getWritableDatabase().delete(SmsOpenHelper.TABLE, selection, selectionArgs);
			if (deleteCount > 0) {
				System.out.println("--------------SmsProvider deleteSuccess--------------");
				// 发送数据改变的信号
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
			// 更新了几条数据
			updateCount = mHelper.getWritableDatabase().update(SmsOpenHelper.TABLE, values, selection, selectionArgs);
			if (updateCount > 0) {
				System.out.println("--------------SmsProvider updateSuccess--------------");
				// 发送数据改变的信号
				getContext().getContentResolver().notifyChange(SmsProvider.URI_SMS, null);
			}
			break;

		default:
			break;
		}
		return updateCount;
	}

}
