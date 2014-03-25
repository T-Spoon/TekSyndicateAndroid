/*
 *	Tspoon | 2014
 */

package com.tspoon.teksyndicate.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Tspoon on 22/03/2014.
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String TAG = "DBHelper";
	private static final String DATABASE_NAME = "db.sqlite";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_VIDEOS = "Videos";
	public static final String VIDEOS_COLUMN_ID = "ID";
	public static final String VIDEOS_COLUMN_VIDEO_ID = "VideoID";
	public static final String VIDEOS_COLUMN_TITLE = "Title";
	public static final String VIDEOS_COLUMN_DATE = "DatePublished";
	public static final String VIDEOS_COLUMN_IMAGE = "ImageURL";

	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_VIDEOS + "(" + 
													VIDEOS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
													VIDEOS_COLUMN_VIDEO_ID + " TEXT NOT NULL, " + 
													VIDEOS_COLUMN_TITLE + " TEXT NOT NULL, " + 
													VIDEOS_COLUMN_IMAGE + " TEXT NOT NULL, " + 
													VIDEOS_COLUMN_DATE + " TEXT NOT NULL);";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrade DB " + oldVersion + " >> " + newVersion);
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
		//onCreate(db);
	}
}