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
	         * from_account;//������
	         * to_account//������
	         * body//��Ϣ����
	         * status//����״̬
	         * type//��Ϣ����
	         * time//����ʱ��
	         * session_account//�Ựid-->��������Щ��������-->
	         * <p/>
	         * //"��"  ������Ϣ  ��   "��Ů1" ����������  ����Ự��˭�ĻỰ ---->"��Ů1"
	         * //"��Ů1"  ������Ϣ  ��   "��" ����������  ����Ự��˭�ĻỰ ---->"��Ů1"
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
