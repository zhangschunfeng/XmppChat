package zcf.demo.provider;

import zcf.demo.db.FriendsOpenHelper;
import zcf.demo.db.FriendsOpenHelper.FriTable;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class FriendProvider extends ContentProvider {

	public static final String AUTHORITIES	=FriendProvider.class.getCanonicalName();
	//java.lang.Class.getCanonicalName() 方法返回底层阶级Java语言规范中定义的标准名称
	public static UriMatcher UriMatch;
	public static Uri URI_FRIEND=Uri.parse("content://"+AUTHORITIES+"/friends");
	public static final int FRIEND=1;
	private FriendsOpenHelper friendsOpenHelper;
	static
	{
		UriMatch=new UriMatcher(UriMatcher.NO_MATCH);
		UriMatch.addURI(AUTHORITIES, "/friends", FRIEND);
	};
	
	
	@Override
	public boolean onCreate() {
		friendsOpenHelper=new FriendsOpenHelper(this.getContext());
		if(friendsOpenHelper!=null)
			return true;
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor=null;
		switch(UriMatch.match(uri))
		{
		case FRIEND:
			cursor=friendsOpenHelper.getReadableDatabase().query(FriendsOpenHelper.TABLE, projection, selection, selectionArgs,
					null, null, sortOrder);
			
			break;	
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch(UriMatch.match(uri))
		{
		case FRIEND:
			long id=friendsOpenHelper.getWritableDatabase().insert(FriendsOpenHelper.TABLE,null, values);
			if (id > 0) {
				//
				// ContentUris：用于获取Uri路径后面的ID部分，它有两个比较实用的方法：
				//        withAppendedId(uri, id)用于为路径加上ID部分
				//       parseId(uri)方法用于从路径中获取ID部分
				uri = ContentUris.withAppendedId(uri, id);
				// 发送数据改变的信号
				getContext().getContentResolver().notifyChange(URI_FRIEND, null);
			}
			break;
		}
		
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int updateCount=0;
		switch(UriMatch.match(uri))
		{
		case FRIEND:
			long id=friendsOpenHelper.getWritableDatabase().update(FriendsOpenHelper.TABLE,values,selection, selectionArgs);
			if (updateCount > 0) {
				
				// 发送数据改变的信号
				getContext().getContentResolver().notifyChange(URI_FRIEND, null);
			}
			break;
		}
		return updateCount;
	}

}
