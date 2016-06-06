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
	 * @param name ���ݿ���
	 * @param factory �α깤��
	 * @param version ���ݿ�汾
	 */
	public MyDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB, factory, VERSION);
		
	}

	///�����ݿ��һ�δ���������
	@Override
	public void onCreate(SQLiteDatabase db) {
		//������
		db.execSQL(sql);

	}
	
	//�����ݿ�汾�����ı�ʱ����
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String upsql="drop table if exitst"+TABLE;
		db.execSQL(upsql);
		this.onCreate(db);

	}

}
