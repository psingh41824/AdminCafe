package com.swi.admincafe.api.sharePrefrence

import android.content.Context
import android.content.SharedPreferences

class SharePrefs {

    constructor(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE, 0)
    }

    companion object {
        private lateinit var instance: SharePrefs;
        private var sharedPreferences: SharedPreferences? = null

        val TOKEN = "Token"
        val PREFERENCE = "retro"
        var IS_LOGIN = "is_login"
        var TOKEN_NAME = "token_name"
        var TOKEN_PASSWORD = "token_password"



        fun getInstance(context: Context): SharePrefs {
            if (sharedPreferences == null) {
                instance = SharePrefs(context)
            }
            return instance
        }
    }



    fun putString(key: String?, value: String) {
        sharedPreferences!!.edit().putString(key, value).apply()
    }

    fun getString(key: String?): String? {
        return sharedPreferences!!.getString(key, "")
    }

    fun upDate(key: String?, value: String) {
        sharedPreferences!!.edit().putString(key, value).apply()
    }

    fun delete(key: String?) {
        sharedPreferences!!.edit().remove(key).apply()
    }


    fun putInt(key: String?, value: Int?) {
        sharedPreferences!!.edit().putInt(key, value!!).apply()
    }

    fun putBoolean(key: String?, value: Boolean?) {
        sharedPreferences!!.edit().putBoolean(key, value!!).apply()
    }

    fun getBoolean(key: String?): Boolean? {
        return sharedPreferences!!.getBoolean(key, false)
    }

    fun getInt(key: String?): Int {
        return sharedPreferences!!.getInt(key, 0)
    }

    fun clearSharePrefs() {
        sharedPreferences!!.edit().clear().apply()
    }

    fun getStringSharedPreferences(context: Context, name: String?): String? {
        val settings = context.getSharedPreferences(PREFERENCE, 0)
        return settings.getString(name, "")
    }

}