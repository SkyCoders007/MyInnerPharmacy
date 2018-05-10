package com.mxi.myinnerpharmacy.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * Created by parth on 17/5/16.
 */
public class SQLiteTD {

    Context mcon;
    private static String dbname = "MIP.db";

    static String db_path = Environment.getExternalStorageDirectory()
            .toString() + "/" + dbname;

    SQLiteDatabase db;
    public static final String KEY_ROWID = "id";

    public SQLiteTD(Context con) {
        // TODO Auto-generated constructor stub
        mcon = con;

        db = mcon.openOrCreateDatabase(db_path, Context.MODE_PRIVATE, null);

        // Database Table for store all HeartRate list
        db.execSQL("CREATE TABLE IF NOT EXISTS HeartRate(id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " date VARCHAR,heart_rate VARCHAR); ");

        //========== LOGIN ==============================================
        db.execSQL("CREATE TABLE IF NOT EXISTS Login(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "emailId VARCHAR, password VARCHAR,rememberMe VARCHAR); ");

    }

    public void inseartHeartRateData(String date, String heart_rate) {

        String query = "INSERT INTO HeartRate(date,heart_rate)VALUES ('"

                + date + "','"
                + heart_rate + "')";

        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("Error Search", e.getMessage());
        }
    }

    public void inseartLogin(String emailId, String password, String rememberMe) {

        String query = "INSERT INTO Login(emailId,password,rememberMe)VALUES ('"

                + emailId + "','"
                + password + "','"
                + rememberMe + "')";

        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("Error Search", e.getMessage());
        }
    }

    public Cursor getLogin() {
        Cursor cur = null;
        try {
            String query = "SELECT  * FROM  Login";
            cur = db.rawQuery(query, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("Error: getSearch ", e.getMessage());
        }

        return cur;
    }

    public Cursor getHeartrate() {
        Cursor cur = null;
        try {
            String query = "SELECT  * FROM  HeartRate";
            cur = db.rawQuery(query, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("Error: getSearch ", e.getMessage());
        }

        return cur;
    }



}
