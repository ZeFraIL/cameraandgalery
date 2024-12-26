package zeev.fraiman.cameraandgalery;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "all_info.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "info_about_files";
    public static final String COLUMN_NAME_OF_FILE = "name_of_file";
    public static final String COLUMN_DATE_OF_FILE = "date_of_file";
    public static final String COLUMN_TIME_OF_FILE = "time_of_file";
    public static final String COLUMN_URI_OF_FILE = "uri_of_file";
    public static final String COLUMN_TYPE_OF_FILE = "type_of_file";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_OF_FILE + " TEXT," +
                    COLUMN_DATE_OF_FILE + " TEXT," +
                    COLUMN_TIME_OF_FILE + " TEXT," +
                    COLUMN_URI_OF_FILE + " TEXT," +
                    COLUMN_TYPE_OF_FILE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void insertFileInfo(String name, String date, String time, String uri, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_OF_FILE, name);
        values.put(COLUMN_DATE_OF_FILE, date);
        values.put(COLUMN_TIME_OF_FILE, time);
        values.put(COLUMN_URI_OF_FILE, uri);
        values.put(COLUMN_TYPE_OF_FILE, type);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
}