
package com.example.planets.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sw_planet";
	private static final int VERSION = 1;
	private static volatile SQLiteDatabaseHelper databaseHelper;
	private final AssetManager assets;
	private int userCount = 0;
	
	private Context context;

	private SQLiteDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		assets = context.getAssets();
		this.context = context;
	}

	public static synchronized SQLiteDatabaseHelper getDatabaseInstance(Context context) {
		if (databaseHelper == null) {
			databaseHelper = new SQLiteDatabaseHelper(context);
		}
		return databaseHelper;
	}


	


	@Override
	public void onCreate(SQLiteDatabase db) {
		BufferedReader bufferedReader = null;
		try {
			InputStream databaseStream = assets.open("database.sql");
			bufferedReader = new BufferedReader(new InputStreamReader(databaseStream));
			StringBuilder databaseDeclaration = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				databaseDeclaration.append(line.replace("\t", ""));
			}
			for (String sql : databaseDeclaration.toString().split(";")) {
				String trimmedQuery;
				if (!(trimmedQuery = sql.trim()).isEmpty()) {
					db.execSQL(trimmedQuery);
				}
			}
			db.execSQL("PRAGMA foreign_keys = ON");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		SQLiteDatabase writableDatabase = super.getWritableDatabase();
		writableDatabase.execSQL("PRAGMA foreign_keys = ON");
		++userCount;
		return writableDatabase;
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		++userCount;
		return super.getReadableDatabase();
	}

	@Override
	public synchronized void close() {
		if (--userCount == 0) {
			super.close();
		}
	}
}
