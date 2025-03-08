package com.ftg2024.ftgcylinderapp.sharedprefs

import android.content.Context
import com.ftg2024.ftgcylinderapp.auth.model.LoginResponseData
import com.ftg2024.ftgcylinderapp.auth.model.UserData
import com.ftg2024.ftgcylinderapp.constants.ApiConstants
import com.ftg2024.ftgcylinderapp.constants.ApiConstants.AUTH_TOKEN
import com.ftg2024.ftgcylinderapp.constants.SharedPrefConstants
import com.ftg2024.ftgcylinderapp.constants.SharedPrefConstants.LOGIN_RESPONSE_PREF
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefManager @Inject constructor(@ApplicationContext context: Context) {
    private var tokenPref = context.getSharedPreferences(SharedPrefConstants.TOKEN_PREF, Context.MODE_PRIVATE)
    private var loginResponsePref = context.getSharedPreferences(SharedPrefConstants.LOGIN_RESPONSE_PREF, Context.MODE_PRIVATE)

    fun saveToken(token: String?) {
        val editor = tokenPref.edit()
        editor.putString(ApiConstants.AUTH_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return tokenPref.getString(AUTH_TOKEN, null)
    }

    fun setLoginResponseData(data: List<LoginResponseData>) {
        val gson = Gson()
        val dataListJson = gson.toJson(data)
        val editor = loginResponsePref.edit()
        editor.putString(LOGIN_RESPONSE_PREF, dataListJson)
        editor.apply()
    }

    fun getLoginResponseData() : List<UserData>? {
        val gson = Gson()
        val json = loginResponsePref.getString(LOGIN_RESPONSE_PREF, null)
        return if (json != null) {
            val type = object : TypeToken<List<LoginResponseData>>() {}.type
            gson.fromJson(json, type)
        } else {
            null
        }
    }
}