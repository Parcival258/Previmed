package com.andres_lasso.previmed.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {

    // Nombre del archivo donde se guardan las preferencias
    private const val PREF_NAME = "previmed_prefs"

    // Clave para almacenar el token
    private const val TOKEN_KEY = "jwt"

    /**
     * Guarda el token en SharedPreferences
     */
    fun saveToken(context: Context, token: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    /**
     * Obtiene el token guardado en SharedPreferences
     * Retorna null si no existe
     */
    fun getToken(context: Context): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(TOKEN_KEY, null)
    }

    /**
     * Elimina el token (se usa al cerrar sesión)
     */
    fun clearToken(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(TOKEN_KEY).apply()
    }

    /**
     * Verifica si hay un token guardado
     * Devuelve true si el usuario está logeado, false si no
     */
    fun hasToken(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.contains(TOKEN_KEY)
    }
}