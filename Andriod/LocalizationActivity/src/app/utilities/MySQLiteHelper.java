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
	 * @param dealIndex
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
	public void insert(int dealIndex, String merchant, String title, String content, int minAge,
			int maxAge, String sendTime, String targetGender, Boolean student, 
			int targetLat, int targetLon, String expDate, Boolean accepted, 
			Boolean enabled) {
		
		SQLiteDatabase db = getWritableDatabase(); 
		
		ContentValues values = new ContentValues(); 
		values.put("dealIndex", dealIndex);
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
		String[] from = {"merchant", "title", "dealIndex"};
		String order = "merchant";
		
		SQLiteDatabase db = getReadableDatabase(); 
		Cursor cursor = db.query(TABLE_NAME, from, null, null, null, null, order);
		activity.startManagingCursor(cursor); 
		
		return cursor;
	}
	
	public Cursor dealContent(Activity activity, int dealIndex) {
		String[] from = {"merchant", "title", "content", "targetLat", "targetLon", "dealIndex"};
		String where = "dealIndex = ?";
		String[] selection = {String.valueOf(dealIndex)};
		
		SQLiteDatabase db = getReadableDatabase(); 
		Cursor cursor = db.query(
				TABLE_NAME, // table
				from, // columns
				where, // where selection 
				selection, // selectionArgs
				null, // group by 
				null, // having
				null // order
				);
		
		return cursor; 
	}

} 