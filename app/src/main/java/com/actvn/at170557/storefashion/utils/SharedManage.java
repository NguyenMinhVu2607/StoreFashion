package com.actvn.at170557.storefashion.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedManage {

    private static final String PREF_NAME = "com.actvn.at170557.storefashion.PREFERENCES"; // Tên file SharedPreferences
    private static final String KEY_FIRST_APP_OPEN = "isFirstAppOpen"; // Khóa cho biến isFirstAppOpen
    private static SharedManage instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SharedManage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Lấy instance của SharedManage (theo Singleton pattern)
    public static synchronized SharedManage getInstance(Context context) {
        if (instance == null) {
            instance = new SharedManage(context);
        }
        return instance;
    }

    // Lưu giá trị boolean isFirstAppOpen
    public void setFirstAppOpen(boolean isFirstAppOpen) {
        editor.putBoolean(KEY_FIRST_APP_OPEN, isFirstAppOpen);
        editor.apply(); // Lưu thay đổi
    }

    // Lấy giá trị boolean isFirstAppOpen
    public boolean isFirstAppOpen() {
        return sharedPreferences.getBoolean(KEY_FIRST_APP_OPEN, true); // Mặc định là true nếu chưa được set
    }

    // Các phương thức khác để quản lý SharedPreferences
    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void setFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public void setLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    // Xóa giá trị theo key
    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }

    // Xóa toàn bộ dữ liệu trong SharedPreferences
    public void clear() {
        editor.clear();
        editor.apply();
    }
}
