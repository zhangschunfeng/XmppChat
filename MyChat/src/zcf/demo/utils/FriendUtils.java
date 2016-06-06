package zcf.demo.utils;

import android.content.ContentValues;
import android.content.Context;
import zcf.demo.bean.Friends;
import zcf.demo.db.FriendsOpenHelper.FriTable;
import zcf.demo.provider.FriendProvider;

public class FriendUtils {

	public static void saveFriend(Context context,Friends friends)	
	{
		ContentValues values=new ContentValues();
		values.put(FriTable.COUNT, friends.getAccount());
		values.put(FriTable.NAME, friends.getName());
		values.put(FriTable.RECEIVER, friends.getIsReceiver());
	    context.getContentResolver().insert(FriendProvider.URI_FRIEND, values);
	}
	
	public static void updateFriend(Context context,Friends friends)
	{
		ContentValues values=new ContentValues();
		values.put(FriTable.COUNT, friends.getAccount());
		values.put(FriTable.NAME, friends.getName());
		values.put(FriTable.RECEIVER, friends.getIsReceiver());
		context.getContentResolver().update(FriendProvider.URI_FRIEND, values,null,null);
	}
}
