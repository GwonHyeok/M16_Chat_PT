package com.hyeok.m16_chat_pt.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by GwonHyeok on 2014. 6. 12..
 */
public class PreferencesControl {
    public static String USER_DATA_PREF = "USER_DATA";
    public static String USER_NAME = "user_id";
    public static String USER_PWD = "user_pw";

    private Context mContext;

    private PreferencesControl(Context context) {
        this.mContext = context;
    }

    public static PreferencesControl instance;

    public synchronized static PreferencesControl getInstance(Context context) {

        if (instance == null) {
            instance = new PreferencesControl(context);
        }
        return instance;
    }

    public void put(String PREF_NAME, String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public void put(String PREF_NAME, String key, boolean value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public void put(String PREF_NAME, String key, int value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    public String getValue(String PREF_NAME, String key, String dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);

        try {
            return pref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }

    public int getValue(String PREF_NAME, String key, int dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);

        try {
            return pref.getInt(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }

    public Map getAll(String PREF_NAME) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return pref.getAll();
    }
    public boolean getValue(String PREF_NAME, String key, boolean dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);

        try {
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public void clear(String PREF_NAME, String key) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    public void clearAll(String PREF_NAME) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
