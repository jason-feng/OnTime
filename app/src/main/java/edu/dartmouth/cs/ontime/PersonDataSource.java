package edu.dartmouth.cs.ontime;

public class PersonDataSource {

//    // Database fields
//    private SQLiteDatabase database;
//    private EventDbHelper dbHelper;
//
//    private static final String TAG = "ExerciseEntryDataSource.java";
//
//    private String[] allColumns = {
//            EventDbHelper.ROW_ID,
//            EventDbHelper.COLUMN_INPUT_TYPE,
//            EventDbHelper.COLUMN_ACTIVITY_TYPE,
//            EventDbHelper.COLUMN_DATE_TIME,
//            EventDbHelper.COLUMN_DURATION,
//            EventDbHelper.COLUMN_DISTANCE,
//            EventDbHelper.COLUMN_AVG_PACE,
//            EventDbHelper.COLUMN_AVG_SPEED,
//            EventDbHelper.COLUMN_CALORIES,
//            EventDbHelper.COLUMN_CLIMB,
//            EventDbHelper.COLUMN_HEART_RATE,
//            EventDbHelper.COLUMN_COMMENT,
//            EventDbHelper.COLUMN_PRIVACY,
//            EventDbHelper.COLUMN_GPS_DATA};
//
//
//    public PersonDataSource(Context context) {
//        dbHelper = new EventDbHelper(context);
//    }
//
//    public void open() throws SQLException {
//        database = dbHelper.getWritableDatabase();
//    }
//
//    public void close() {
//        dbHelper.close();
//    }
//
//    // Insert a item given each column value
//    public long insertEntry(ExerciseEntry entry) {
//        Log.d(TAG, "insertEntry()");
//        Log.d(TAG, "input type: " + entry.getmInputType());
//        Log.d(TAG, "activity type of entry: " + entry.getmActivityType());
//        Log.d(TAG, "duration of entry: " + Integer.toString(entry.getmDuration()));
//        Log.d(TAG, "datetime of entry " + entry.getmDateTime().toString());
//
//        long success;
//        ContentValues values = new ContentValues();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MMM dd yyyy");
//
//        values.put(EventDbHelper.COLUMN_INPUT_TYPE, entry.getmInputType());
//        values.put(EventDbHelper.COLUMN_ACTIVITY_TYPE, entry.getmActivityType());
//        values.put(EventDbHelper.COLUMN_DATE_TIME, dateFormat.format(entry.getmDateTime().getTime()));
//        values.put(EventDbHelper.COLUMN_DURATION, entry.getmDuration());
//        values.put(EventDbHelper.COLUMN_DISTANCE, entry.getmDistance());
//        values.put(EventDbHelper.COLUMN_AVG_PACE, entry.getmAvgPace());
//        values.put(EventDbHelper.COLUMN_AVG_SPEED, entry.getmCurSpeed());
//        values.put(EventDbHelper.COLUMN_CALORIES, entry.getmCalorie());
//        values.put(EventDbHelper.COLUMN_CLIMB, entry.getmClimb());
//        values.put(EventDbHelper.COLUMN_HEART_RATE, entry.getmHeartRate());
//        values.put(EventDbHelper.COLUMN_COMMENT, entry.getmComment());
//        values.put(EventDbHelper.COLUMN_PRIVACY, entry.getPrivacy());
//        if (entry.getmLocationList() != null) {
//            values.put(EventDbHelper.COLUMN_GPS_DATA, entry.getLocationByteArray());
//        }
//
//        success = database.insert(EventDbHelper.DATABASE_TABLE, null, values);
//        EventDbHelper.eventNumber++;
//        return success;
//    }
//
//    // Remove an entry by giving its index
//    public void removeEntry(long rowIndex) {
//        Log.d(TAG, "removeEntry()");
//        database.delete(EventDbHelper.DATABASE_TABLE,
//                EventDbHelper.ROW_ID + "=" + rowIndex, null);
//    }
//
//    // Query a specific entry by its index.
//    public ExerciseEntry fetchEntryByIndex(long rowId) {
//        Log.d(TAG, "fetchEntryByIndex()");
//        ExerciseEntry entry;
//        String where = EventDbHelper.ROW_ID + " = " + rowId;
//        Cursor cursor = database.query(EventDbHelper.DATABASE_TABLE, allColumns,where,
//            null, null, null, null);
//
//        if (!cursor.moveToFirst()) {
//            Log.e("System.out", "cursor is empty");
//        }
//        else {
//            Log.d(TAG, "moveToPosition row id: " + Long.toString(rowId));
//            return cursorToEntry(cursor);
//        }
//        cursor.close();
//        return null;
//    }
//
//    // Query the entire table, return all rows
//    public ArrayList<ExerciseEntry> fetchEntries() {
//        ArrayList<ExerciseEntry> list = new ArrayList<ExerciseEntry>();
//        Cursor cursor = database.query(EventDbHelper.DATABASE_TABLE, allColumns, null, null,
//                null, null, null);
//
//        if (cursor == null) {
//            return null;
//        }
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            ExerciseEntry entry = cursorToEntry(cursor);
//            list.add(entry);
//            cursor.moveToNext();
//        }
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
//        return list;
//    }
//
//    private ExerciseEntry cursorToEntry(Cursor cursor) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MMM dd yyyy");
//        ExerciseEntry entry = new ExerciseEntry();
//        entry = new ExerciseEntry();
//        entry.setId(cursor.getLong(0));
//        entry.setmInputType(cursor.getInt(1));
//        entry.setmActivityType(cursor.getInt(2));
//        try {
//            Calendar cal = new GregorianCalendar();
//            cal.setTime(dateFormat.parse(cursor.getString(3)));
//            entry.setmDateTime(cal);
//        }
//        catch (ParseException exception) {
//            System.out.println("Date format error");
//        }
//        entry.setmDuration(cursor.getInt(4));
//        entry.setmDistance(cursor.getInt(5));
//        entry.setmAvgPace(cursor.getDouble(6));
//        entry.setmAvgSpeed(cursor.getDouble(7));
//        entry.setmCalorie(cursor.getInt(8));
//        entry.setmClimb(cursor.getDouble(9));
//        entry.setmHeartRate(cursor.getInt(10));
//        entry.setmComment(cursor.getString(11));
//        entry.setPrivacy(cursor.getInt(12));
//        if (entry.getmInputType() != StartFragment.MANUAL_ENTRY) {
//            entry.setLocationListFromByteArray(cursor.getBlob(13));
//        }
//        return entry;
//    }
//
//    private void deleteDatabase() {
//        database.execSQL("DROP TABLE IF EXISTS " + EventDbHelper.DATABASE_TABLE);
//    }
}