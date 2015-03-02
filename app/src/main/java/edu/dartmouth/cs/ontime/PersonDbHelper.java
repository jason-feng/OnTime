package edu.dartmouth.cs.ontime;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PersonDbHelper extends SQLiteOpenHelper {

	public static final String DATABASE_TABLE = "exercises";
	public static final String ROW_ID = "_id";
	public static final String COLUMN_INPUT_TYPE = "input_type";
    public static final String COLUMN_ACTIVITY_TYPE = "activity_type";
    public static final String COLUMN_DATE_TIME = "date_time";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_AVG_PACE = "avg_pace";
    public static final String COLUMN_AVG_SPEED = "speed";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_CLIMB = "climb";
    public static final String COLUMN_HEART_RATE = "heart_rate";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_PRIVACY = "privacy";
    public static final String COLUMN_GPS_DATA = "gps_data";

	private static final String DATABASE_NAME = "exercise_entries.db";
	private static final int DATABASE_VERSION = 1;

    public static int entryNumber = 0;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + "("
            + ROW_ID + " integer primary key autoincrement, "
            + COLUMN_INPUT_TYPE + " INTEGER NOT NULL, "
            + COLUMN_ACTIVITY_TYPE + " INTEGER NOT NULL, "
            + COLUMN_DATE_TIME + " STRING NOT NULL, "
            + COLUMN_DURATION + " INTEGER NOT NULL, "
            + COLUMN_DISTANCE + " FLOAT, "
            + COLUMN_AVG_PACE + " FLOAT, "
            + COLUMN_AVG_SPEED + " FLOAT,"
            + COLUMN_CALORIES + " INTEGER, "
            + COLUMN_CLIMB + " FLOAT, "
            + COLUMN_HEART_RATE + " INTEGER, "
            + COLUMN_COMMENT + " TEXT, "
            + COLUMN_PRIVACY + " INTEGER,"
            + COLUMN_GPS_DATA + " BLOB);";

	public PersonDbHelper(Context context) {
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