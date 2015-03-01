package edu.dartmouth.cs.ontime;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDbHelper extends SQLiteOpenHelper {

	public static final String DATABASE_TABLE = "events";
	public static final String ROW_ID = "_id";
    public static final String COLUMN_DATE_TIME = "date_time";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_PEOPLE = "people";

	private static final String DATABASE_NAME = "events.db";
	private static final int DATABASE_VERSION = 1;

    public static int eventNumber = 0;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + "("
            + ROW_ID + " integer primary key autoincrement, "
            + COLUMN_DATE_TIME + " STRING NOT NULL, "
            + COLUMN_TITLE + " STRING NOT NULL, "
            + COLUMN_LATITUDE + " DOUBLE NOT NULL, "
            + COLUMN_LONGITUDE + " DOUBLE NOT NULL, "
            + COLUMN_PEOPLE + " BLOB NOT NULL);";

	public EventDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
    }

    @Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
}