package com.chatitzemoumin.londoncoffeeapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * PlacesSQLiteOpenHelper class creates database and related tables
 * 
 */
public class PlacesSQLiteOpenHelper extends SQLiteOpenHelper{

	public final static int VERSION = 1;
	public final static String DB_NAME = "findrestauranttapp.db";
	public final static String TABLE_LOCATION = "location";
	public final static String TABLE_PLACES = "places";
	public final static String PLACE_ID = "id";
	public final static String PLACE_NAME = "name";
	public final static String PLACE_LATITUDE = "platitude";
	public final static String PLACE_LONGITUDE = "longitude";
	public final static String PLACE_RATING = "rating";
	public final static String PLACE_VICINITY = "vicinity";
	public final static String PLACE_ICON = "icon";
	public final static String PLACE_PRICE_LEVEL = "pricelevel";
	public final static String PLACE_OPENNING_HOURS = "openninghours";
	public final static String PLACE_DISTANCE = "distance";
	public final static String LOCATION_ID = "id";
	public final static String LOCATION_LATITUDE = "latitude";
	public final static String LOCATION_LONGITUDE = "longitude";

	public PlacesSQLiteOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		dropAndCreate(db);	
	}

	protected void dropAndCreate(SQLiteDatabase db) {
		db.execSQL("drop table if exists " + TABLE_PLACES + ";");
		db.execSQL("drop table if exists " + TABLE_LOCATION + ";");
		createTables(db);
	}
	
	protected void createTables(SQLiteDatabase db) {
		db.execSQL(
				"create table " + TABLE_PLACES +" (" +
				PLACE_ID + " integer primary key autoincrement not null," +
				PLACE_NAME + " text," +
				PLACE_LATITUDE + " double," +
				PLACE_LONGITUDE + " double," +
				PLACE_RATING + " double," +
				PLACE_VICINITY + " text," +
				PLACE_ICON + " text," +
				PLACE_PRICE_LEVEL + " integer," +
				PLACE_OPENNING_HOURS + " text," +
				PLACE_DISTANCE + " double" +
				");"
			);
		
		db.execSQL(
				"create table " + TABLE_LOCATION +" (" +
				LOCATION_ID + " integer primary key autoincrement not null," +
				LOCATION_LATITUDE + " double," +
				LOCATION_LONGITUDE + " double" +
				");"
			);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("Find Restaurants", "onUpgrade");
	}
}

