package app.utilities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * MySQLLiteHelpter functions, with references to
 * http://www.vogella.com/articles/AndroidSQLite/article.html
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

	/**
	 * MYSQLite table information variables. This can be changed according to
	 * preferred variable names to be saved on the phone.
	 */
	private static final String DATABASE_NAME = "deals.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "deals";
	public static final String _ID = BaseColumns._ID;

	/**
	 * Database creation SQL statement
	 */
	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME
			+ "(" + "dealIndex INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "merchant TEXT NOT NULL, " + "title TEXT NOT NULL, "
			+ "content TEXT NOT NULL)";

	/**
	 * Constructor for MySQLiteHelper class.
	 * 
	 * @param context
	 *            Application context
	 */
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Create a new SQL table on the phone
	 * 
	 * @param database
	 *            The database associated with the new class object
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	/**
	 * Upon upgrade of SQLite, this function is executed.
	 * 
	 * @param db
	 *            Original SQLite database handle
	 * @param oldVersion
	 *            Old version number
	 * @param newVersion
	 *            New version number
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	/**
	 * Insert row into an already existing database.
	 * 
	 * @param dealIndex
	 *            Index of the deal to be stored
	 * @param merchant
	 *            Merchant name
	 * @param title
	 *            Title of the deal
	 * @param content
	 *            Detailed content of the deal
	 */
	public void insert(int dealIndex, String merchant, String title,
			String content) {

		// get current writable database
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("dealIndex", dealIndex);
		values.put("merchant", merchant);
		values.put("title", title);
		values.put("content", content);

		// insert row into the table
		db.insertOrThrow(TABLE_NAME, null, values);
	}

	/**
	 * Returns all query results from database
	 * 
	 * @param activity
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Cursor all(Activity activity) {
		String[] from = { "merchant", "title", "dealIndex" };
		String order = "merchant";

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, from, null, null, null, null,
				order);
		activity.startManagingCursor(cursor);

		return cursor;
	}

	/**
	 * Retrieve the deal content associated with the dealIndex
	 * 
	 * @param activity
	 *            Current activity
	 * @param dealIndex
	 *            Index of the deal content to be retrieved
	 * @return Cursor to the row containg the specified dealIndex
	 */
	public Cursor dealContent(Activity activity, int dealIndex) {
		String[] from = { "merchant", "title", "content" };
		String where = "dealIndex = ?";
		String[] selection = { String.valueOf(dealIndex) };

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, // table
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