package xyz.z3ro.colorenhance.utility

import android.content.Context
import android.preference.PreferenceManager

object PreferenceHelper {

    fun getBoolean(context: Context?, key: String, defaultValue: Boolean = false) =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(key, defaultValue)

    fun putBoolean(context: Context?, key: String, value: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).apply()
    }

    fun getInt(context: Context?, key: String, defaultValue: Int) =
        PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defaultValue)

    fun putInt(context: Context?, key: String, value: Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).apply()
    }

    fun getString(context: Context?, key: String, defaultValue: String? = null): String? =
        PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue)

    fun putString(context: Context?, key: String, value: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply()
    }
}