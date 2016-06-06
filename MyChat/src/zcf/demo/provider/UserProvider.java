package zcf.demo.provider;

import zcf.demo.db.FriendsOpenHelper;
import zcf.demo.db.SmsOpenHelper;
import zcf.demo.db.UserOpenHelper;
import zcf.demo.db.UserOpenHelper.UserTable;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class UserProvider extends ContentProvider {
	public static final String AUTHORITY=UserProvider.class.getCanonicalName();
	public static UriMatcher mUriMatch;
	public static final  int USER_SEARCH=1;
	public static final  int USER_GROUP=2;
	public static Uri URI_GROUP=Uri.parse("content://"+AUTHORITY+"/group");
	public static Uri URI_USER=Uri.parse("content://"+AUTHORITY+"/user");
	private UserOpenHelper mHelper;
	static 
	{
		mUriMatch=new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatch.addURI(AUTHORITY, "/user", USER_SEARCH);
		mUriMatch.addURI(AUTHORITY, "/group",USER_GROUP);
	}
	@Override
	public boolean onCreate() {
		mHelper=new UserOpenHelper(getContext());
		if(mHelper!=null)
		{
			return true;
		}
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor=null;
		switch(mUriMatch.match(uri))
		{
	       case USER_SEARCH:
					
				cursor=mHelper.getReadableDatabase().query(UserOpenHelper.TABLE, projection, selection, selectionArgs,
						null, null, sortOrder);
				break;
	       case USER_GROUP:
	    	   cursor=mHelper.getReadableDatabase().rawQuery("select * from "+UserOpenHelper.TABLE +" group by "+UserTable.NIKENAME, null);
			default:
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
		
		switch(mUriMatch.match(uri))
		{
	       case USER_SEARCH:
				long id=mHelper.getWritableDatabase().insert(UserOpenHelper.TABLE, null, values);
				if(id>0)
				{
					uri=ContentUris.withAppendedId(uri, id);
					getContext().getContentResolver().notifyChange(URI_USER, null);
				}
				
				break;
	    	   
			default:
				break;
		}
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int deleteCount=0;
		switch(mUriMatch.match(uri))
		{
	       case USER_SEARCH:
	    	   	deleteCount=mHelper.getWritableDatabase().delete(UserOpenHelper.TABLE,selection,selectionArgs);
				if(deleteCount>0)
				{
					getContext().getContentResolver().notifyChange(URI_USER, null);
				}
				
				break;
			default:
				break;
		}
		return deleteCount;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int updateCount=0;
		switch(mUriMatch.match(uri))
		{
	       case USER_SEARCH:
	    	   updateCount=mHelper.getWritableDatabase().update(UserOpenHelper.TABLE, values, selection, selectionArgs);
				if(updateCount>0)
				{
					getContext().getContentResolver().notifyChange(URI_USER, null);
				}
				
				break;
			default:
				break;
		}
		return updateCount;
	}
	
}
