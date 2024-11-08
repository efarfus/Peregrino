package com.angellira.reservafrotas.preferences

import android.content.Context

const val DB_NAME = "userPreferences"

class Preferences(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE)

    var isLogged: Boolean
        get() = sharedPreferences.getBoolean("isLogged", false)
        set(value) {
            sharedPreferences.edit().putBoolean("isLogged", value).apply()
        }

    var id: String?
        get() = sharedPreferences.getString("id", null)
        set(value) {
            sharedPreferences.edit().putString("id", value).apply()
        }

}
