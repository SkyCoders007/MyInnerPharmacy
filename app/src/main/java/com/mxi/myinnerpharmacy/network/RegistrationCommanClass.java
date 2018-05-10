package com.mxi.myinnerpharmacy.network;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sonali on 12/1/17.
 */
public class RegistrationCommanClass {

    private Context _context;
    SharedPreferences pref;

    public RegistrationCommanClass(Context context) {
        this._context = context;

        pref = _context.getSharedPreferences("MIP",
                _context.MODE_PRIVATE);
    }

    public void savePrefString(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePrefBoolean(String key, Boolean value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String loadPrefString(String key) {
        // TODO Auto-generated method stub
        String strSaved = pref.getString(key, "");
        return strSaved;
    }

    public Boolean loadPrefBoolean(String key) {
        // TODO Auto-generated method stub
        boolean isbool = pref.getBoolean(key, false);
        return isbool;
    }

    public void logoutapp() {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
