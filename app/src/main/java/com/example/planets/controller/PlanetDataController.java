package com.example.planets.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.planets.db.DbHandler;
import com.example.planets.db.SQLiteDatabaseHelper;
import com.example.planets.model.Planet;

import java.util.ArrayList;

public class PlanetDataController {


    public static void savePlanetsToDb(ArrayList<Planet> planets, Context context) {

        String insertSQL = "replace into tbl_planet_data(planet_ref_id, p_name, p_climate, p_orbital_period, p_gravity, p_img_path) values(?,?,?,?,?,?)";

        SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
        SQLiteDatabase database = databaseInstance.getWritableDatabase();
        try {
            database.execSQL("delete from tbl_planet_data");
            SQLiteStatement sqlStatement = database.compileStatement(insertSQL);

            database.beginTransaction();
            for (Planet se_planet : planets) {
                DbHandler.performExecuteInsert(sqlStatement, new Object[]{
                        se_planet.getP_id(),
                        se_planet.getName(),
                        se_planet.getClimate(),
                        se_planet.getOrbital_period(),
                        se_planet.getGravity(),
                        se_planet.getImg_url()
                });

            }
            database.setTransactionSuccessful();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            database.endTransaction();
            databaseInstance.close();
        }
    }


    public static ArrayList<Planet> getPlanets(Context context) {
        String selectSql = " select planet_ref_id, p_name, p_climate, p_orbital_period, p_gravity, p_img_path from tbl_planet_data ORDER BY planet_ref_id ASC";

        SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
        SQLiteDatabase database = databaseInstance.getWritableDatabase();

        Cursor dataCursor = DbHandler.performRawQuery(database, selectSql, null);

        ArrayList<Planet> planets = new ArrayList<Planet>();

        for (dataCursor.moveToFirst(); !dataCursor.isAfterLast(); dataCursor.moveToNext()) {

            int p_ref_id  = dataCursor.getInt(0);
            String p_name = dataCursor.getString(1);
            String p_climate = dataCursor.getString(2);
            String p_orbital_peri = dataCursor.getString(3);
            String gravity = dataCursor.getString(4);
            String p_img_path = dataCursor.getString(5);


            planets.add(new Planet( p_ref_id, p_name, p_climate,p_orbital_peri,gravity,p_img_path));
        }
        dataCursor.close();
        return planets;
    }

}
