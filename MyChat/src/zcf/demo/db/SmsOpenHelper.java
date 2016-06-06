package zcf.demo.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class SmsOpenHelper extends SQLiteOpenHelper{
	public final  static String DB="sms.db";
	public final static  String TABLE="t_sms";
	public final static int VERSION=1;
	public String sql="create  table "+  TABLE +"(_id integer primary key autoincrement , "
			 				+SmsTable.FROM_ACCOUNT + " TEXT,"
			 				+SmsTable.TO_ACCOUNT + " TEXT,"
			 				+SmsTable.BODY + " TEXT," 
			 				+SmsTable.STATUS + " TEXT,"
			 				+SmsTable.TYPE + " TEXT," 
			 				+SmsTable.TIME + " TEXT," 
			 				+SmsTable.SESSION_ACCOUNT + " TEXT ,"
			 				+SmsTable.READER + " TEXT );";
	public class SmsTable implements BaseColumns {
	        /**
	         * from_account;//发送者
	         * to_account//接收者
	         * body//消息内容
	         * status//发送状态
	         * type//消息类型
	         * time//发送时间
	         * session_account//会话id-->最近你和哪些人聊天了-->
	         * <p/>
	         * //"我"  发送消息  给   "美女1" 对于我来讲  这个会话和谁的会话 ---->"美女1"
	         * //"美女1"  发送消息  给   "我" 对于我来讲  这个会话和谁的会话 ---->"美女1"
	         */
	        public static final String FROM_ACCOUNT = "from_account";
	        public static final String TO_ACCOUNT = "to_account";
	        public static final String BODY = "body";
	        public static final String STATUS = "status";
	        public static final String TYPE = "type";
	        public static final String TIME = "time";
	        public static final String SESSION_ACCOUNT = "session_account";
	        public static final String READER="reader";
	 }
	public SmsOpenHelper(Context context) {
		super(context, DB, null, VERSION);
		
	}
	
	public SmsOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB, factory, VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql);
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String upsql="drop table if exists"+TABLE;
		db.execSQL(upsql);
		this.onCreate(db);
	}
}
