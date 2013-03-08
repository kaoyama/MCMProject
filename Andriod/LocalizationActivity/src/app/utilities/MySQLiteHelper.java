package app.utilities;
// http://www.vogella.com/articles/AndroidSQLite/article.html

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "deals.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "deals";
	public static final String _ID = BaseColumns._ID; 
	
	// Database creation sql statement	
	private static final String DATABASE_CREATE = 
			"CREATE TABLE " + TABLE_NAME + "(" + 
					"dealIndex INTEGER PRIMARY KEY AUTOINCREMENT," + 
					"merchant TEXT NOT NULL, " + 
					"title TEXT NOT NULL, " + 
					"content TEXT NOT NULL, " +
					"minAge INTEGER NOT NULL, " + 
					"maxAge INTEGER NOT NULL, " + 
					"sendTime TIMESTAMP NOT NULL , " + 
					"targetGender TEXT NOT NULL, " +
					"student INTEGER NOT NULL, " + 
					"targetLat INTEGER NOT NULL, " + 
					"targetLon INTEGER NOT NULL, " + 
					"expDate TIMESTAMP NOT NULL, " + 
					"accepted TEXT NOT NULL, " + 
					"enabled TEXT NOT NULL)"; 
					
			
			/*
			"CREATE TABLE " + TABLE_NAME + " (" + 
					  "dealIndex int(11) NOT NULL AUTOINCREMENT, " + 
					  "merchant varchar(255) NOT NULL default ''," + 
					  "title varchar(255) default NULL," +
					  "content varchar(255) default NULL," +
					  "minAge int(3) default NULL," +
					  "maxAge int(3) default NULL," +
					  "sendTime timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP," +
					  "targetGender varchar(1) default NULL," +
					  "student tinyint(1) default NULL," +
					  "targetLat decimal(5,0) default NULL," +
					  "targetLon decimal(5,0) default NULL," +
					  "expDate timestamp NOT NULL default '0000-00-00 00:00:00'," +
					  "accepted varchar(45) character set latin1 collate latin1_bin NOT NULL default '0'," +
					  "enabled varchar(45) character set latin1 collate latin1_bin NOT NULL default '0'," +
					  "PRIMARY KEY  (dealIndex)," +
					  "KEY merchant (merchant)" +
					") ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1";*/

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Create a new SQL table on the phone 
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	
	/**
	 * Insert a row into the deals database
	 * @param merchant
	 * @param title
	 * @param content
	 * @param minAge
	 * @param maxAge
	 * @param sendTime
	 * @param targetGender
	 * @param student
	 * @param targetLat
	 * @param targetLon
	 * @param expDate
	 * @param accepted
	 * @param enabled
	 */
	public void insert(String merchant, String title, String content, int minAge,
			int maxAge, String sendTime, String targetGender, Boolean student, 
			int targetLat, int targetLon, String expDate, Boolean accepted, 
			Boolean enabled) {
		
		SQLiteDatabase db = getWritableDatabase(); 
		
		ContentValues values = new ContentValues(); 
		values.put("merchant", merchant);
		values.put("title", title);
		values.put("content", content);
		values.put("minAge", minAge);
		values.put("maxAge", maxAge);
		values.put("sendTime", sendTime);
		values.put("targetGender", targetGender);
		values.put("student", student);
		values.put("targetLat", targetLat);
		values.put("targetLon", targetLon); 
		values.put("expDate", expDate);
		values.put("accepted", accepted);
		values.put("enabled", enabled); 
		
		db.insertOrThrow(TABLE_NAME, null, values);
		
	}
	
	/**
	 * Returns all query results from database
	 * @param activity
	 * @return
	 */
	public Cursor all(Activity activity) {
		String[] from = {"merchant", "title", "content", "targetLat", "targetLon"};
		String order = "merchant";
		
		SQLiteDatabase db = getReadableDatabase(); 
		Cursor cursor = db.query(TABLE_NAME, from, null, null, null, null, order);
		activity.startManagingCursor(cursor); 
		
		return cursor;
	}

} 