package com.barkat.barkatsevings.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager.getDefaultSharedPreferences

/**
 * Created by Sajid Ali Suthar.
 */
class PreferenceProvider(private val appContext: Context) {

    private val mSharedPreferences: SharedPreferences
        get() = getDefaultSharedPreferences(appContext)

    /**
     * Set a value for the key
     */
    fun setValue(key: String, value: String) {
        val editor = mSharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * Set a value for the key
     */
    fun setValue(key: String, value: Int) {
        val editor = mSharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    /**
     * Set a value for the key
     */
    fun setValue(key: String, value: Double) {
        setValue(key, value.toString())
    }

    /**
     * Set a value for the key
     */
    fun setValue(key: String, value: Long) {
        val editor = mSharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun setValue(key: String, value: Set<String>) {
        val editor = mSharedPreferences.edit()
        editor.putStringSet(key, value)
        editor.apply()
    }

    /**
     * Gets the value from the settings stored natively on the device.
     *
     * @param defaultValue Default value for the key, if one is not found.
     */
    fun getValue(key: String, defaultValue: String): String? {
        return mSharedPreferences.getString(key, defaultValue)
    }

    fun getIntValue(key: String, defaultValue: Int): Int {
        return mSharedPreferences.getInt(key, defaultValue)
    }

    fun getLongValue(key: String, defaultValue: Long): Long {
        return mSharedPreferences.getLong(key, defaultValue)
    }

    fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defaultValue)
    }

    fun getFloatValue(key: String, defaultValue: Float): Float {
        return mSharedPreferences.getFloat(key, defaultValue)
    }

    fun getStringSet(key: String, defaultValue: Set<String>): Set<String>? {
        return mSharedPreferences.getStringSet(key, defaultValue)
    }

    /**
     * Gets the value from the preferences stored natively on the device.
     *
     * @param defValue Default value for the key, if one is not found.
     */
    fun getValue(key: String, defValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defValue)
    }

    fun setValue(key: String, value: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    /**
     * Clear all the preferences store in this [SharedPreferences.Editor]
     */
    fun clear() {
        mSharedPreferences.edit().clear().apply()
    }

    /**
     * Removes preference entry for the given key.
     *
     * @param key Value for the key
     */
    fun removeValue(key: String) {
        mSharedPreferences.edit().remove(key).apply()
    }
}
