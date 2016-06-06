package zcf.demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class FriendsOpenHelper extends SQLiteOpenHelper {

	public static String DB="friends.db";
	public static String TABLE="friends";
	public static int VERSION=1;
	public String sql="create table "+ TABLE+ " ( _id integer primary key autoincrement , "
				+FriTable.COUNT+ " text ,"
				+FriTable.NAME+" text ,"
				+FriTable.RECEIVER+" text );";
	public class FriTable implements BaseColumns
	{
		public static final String COUNT="count";
		public static final String NAME="name";
		public static final String RECEIVER ="receiver";
	}
	public FriendsOpenHelper(Context context)
	{
		super(context, DB,null, VERSION);
	}
	public FriendsOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, DB,factory, VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql);
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String delete="drop table if exists "+TABLE;
		db.execSQL(delete);
		onCreate(db);
		
	}

}
