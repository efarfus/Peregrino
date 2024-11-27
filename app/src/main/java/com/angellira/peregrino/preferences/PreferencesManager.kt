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

    var totalLitros: String?
        get() = sharedPreferences.getString("totalLitros", null)
        set(value) {
            sharedPreferences.edit().putString("totalLitros", value).apply()
        }

    var litrosRestantes: String?
        get() = sharedPreferences.getString("litrosRestantes", null)
        set(value) {
            sharedPreferences.edit().putString("litrosRestantes", value).apply()
        }

    var mediaFinal: String?
        get() = sharedPreferences.getString("mediaFinal", null)
        set(value) {
            sharedPreferences.edit().putString("mediaFinal", value).apply()
        }

    var idCarroSelected: String?
        get() = sharedPreferences.getString("idCarroSelected", null)
        set(value) {
            sharedPreferences.edit().putString("idCarroSelected", value).apply()
        }

    var idCarroSelectedDelete: String?
        get() = sharedPreferences.getString("idCarroSelectedDelete", null)
        set(value) {
            sharedPreferences.edit().putString("idCarroSelectedDelete", value).apply()
        }
}
