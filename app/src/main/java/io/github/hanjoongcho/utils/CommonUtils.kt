package io.github.hanjoongcho.utils

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by CHO HANJOONG on 2017-11-20.
 */

class CommonUtils {

    companion object {

        fun loadStringPreference(context: Context, key: String, defaultValue: String): String {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(key, defaultValue)
        }

        fun saveStringPreference(context: Context, key: String, value: String) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val edit = preferences.edit()
            edit.putString(key, value)
            edit.apply()
        }

        fun loadLongPreference(context: Context, key: String, defaultValue: Long): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(key, defaultValue)
        }

        fun saveLongPreference(context: Context, key: String, value: Long) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val edit = preferences.edit()
            edit.putLong(key, value)
            edit.apply()
        }
    }
}