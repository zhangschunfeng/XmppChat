package zcf.demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class UserOpenHelper extends SQLiteOpenHelper {

	public static final String DB="use.db";
	public static final String TABLE="user";
	public static final int VERSION=1;
	public String sql="create table "+TABLE +" ( _id integer primary key autoincrement ,"
			+UserTable.ACOUNT +" text ,"
			+UserTable.NIKENAME +" text ,"
			+UserTable.SORTLETTER+ " text );";
	public UserOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB, factory, VERSION);
		
	}
	public UserOpenHelper(Context context)
	{
		super(context,DB,null,VERSION);
		
	}
	public  class UserTable implements BaseColumns
	{
		public static final String ACOUNT="acount";
		public static final String NIKENAME="nikename";
		public static final String SORTLETTER="sortLetter";
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql);
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String destortSql="drop table if exitst "+TABLE;
		db.execSQL(destortSql);
		onCreate(db);
		
	}
}
