package com.example.valut.ui.local

import android.content.Context
import android.content.SharedPreferences

class Preferences(private val sharedPreferences: SharedPreferences) {
    fun setString(key: String?, string: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, string)
        editor.apply()
    }

    private val BEARER_HEADER = "Bearer0 "
    private val PREFS_ACCOUNT = "PREFS_ACCOUNT"
    private val KEY_TYPE_ONE = "KEY_TYPE_ONE"
    private val KEY_TOTAL_COIN = "KEY_TOTAL_COIN" // coin
    private val KEY_FIRST_INSTALL = "KEY_FIRST_INSTALL" // coin
    private val INT_ZERO = 0 // coin



    fun setValueTypeOne(value: String?) {
        sharedPreferences.edit().putString(KEY_TYPE_ONE, value).apply()
    }

    var firstInstall: Boolean
        get() = sharedPreferences.getBoolean(KEY_FIRST_INSTALL, false)
        set(value) {
            sharedPreferences.edit().putBoolean(KEY_FIRST_INSTALL, value).apply()
        }

    fun setValueCoin(value: Int) {
        sharedPreferences.edit().putInt(KEY_TOTAL_COIN, value).apply()
    }

    fun getValueCoin(): Int {
        return sharedPreferences.getInt(KEY_TOTAL_COIN, INT_ZERO)
    }

    fun setBoolean(key: String?, booleanValue: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, booleanValue)
        editor.apply()
    }

    fun setInt(key: String?, intValue: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, intValue)
        editor.apply()
    }

    fun setFloat(key: String?, floatValue: Float) {
        val editor = sharedPreferences.edit()
        editor.putFloat(key, floatValue)
        editor.apply()
    }

    fun setLong(key: String?, longValue: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, longValue)
        editor.apply()
    }

    fun getString(key: String?): String? {
        return sharedPreferences.getString(key, null)
    }

    fun getBoolean(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getInt(key: String?): Int {
        return sharedPreferences.getInt(key, -1)
    }

    fun getFloat(key: String?): Float {
        return sharedPreferences.getFloat(key, 0f)
    }

    fun getLong(key: String?): Long {
        return sharedPreferences.getLong(key, 0)
    }

    companion object {
        private const val PREFS_NAME = "share_prefs"
        private var INSTANCE: Preferences? = null
        @JvmStatic
        fun getInstance(context: Context): Preferences? {
            if (INSTANCE == null) {
                synchronized(Preferences::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Preferences(context.getSharedPreferences(PREFS_NAME, 0))
                    }
                }
            }
            return INSTANCE
        }
    }
}