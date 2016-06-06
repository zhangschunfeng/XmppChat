package zcf.demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

	public static final String DB="zcf.db";
	public static final String TABLE="person";
	private String sql="create table  "+TABLE+"( "+"id  integer primary key autoincrement ,"
											   	+"name varchar(50)	 ,"
											   	+"age  integer"+")";
	public static int VERSION=1;
	/**
	 * 
	 * @param context 
	 * @param name 数据库名
	 * @param factory 游标工厂
	 * @param version 数据库版本
	 */
	public MyDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB, factory, VERSION);
		
	}

	///当数据库第一次创建被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建表
		db.execSQL(sql);

	}
	
	//当数据库版本发生改变时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String upsql="drop table if exitst"+TABLE;
		db.execSQL(upsql);
		this.onCreate(db);

	}

}
