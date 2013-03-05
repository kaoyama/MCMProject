package app.utilities;
// http://www.vogella.com/articles/AndroidSQLite/article.html

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_COMMENTS = "deals";
	public static final String COLUMN_ID = "dealIndex";
	public static final String COLUMN_COMMENT = "deal";

	private static final String DATABASE_NAME = "deals.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement	
	private static final String DATABASE_CREATE = "delimiter $$ " +
			" CREATE TABLE `deals` (`dealIndex` int(11) NOT NULL auto_increment, " + 
			" `merchant` varchar(255) NOT NULL default '', " + 
			" `title` varchar(255) default NULL, " + 
			" `content` varchar(255) default NULL, " + 
			" `minAge` int(3) default NULL, " + 
			" `maxAge` int(3) default NULL, " + 
			" `sendTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP, " +
			" `targetGender` varchar(1) default NULL, " + 
			" `student` tinyint(1) default NULL, " + 
			" `targetLat` decimal(5,0) default NULL, " + 
			"`targetLon` decimal(5,0) default NULL," +
			" `expDate` timestamp NOT NULL default '0000-00-00 00:00:00', " +
			" `accepted` varchar(45) character set latin1 collate latin1_bin NOT NULL default '0', " + 
			" `enabled` varchar(45) character set latin1 collate latin1_bin NOT NULL default '0', " +
			" PRIMARY KEY  (`dealIndex`), " + 
			" KEY `merchant` (`merchant`) " + 
			") ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1$$";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
		onCreate(db);
	}

} 