package com.andres_lasso.previmed.utils

import android.content.Context
import android.content.SharedPreferences
import java.text.Normalizer

object PreferenceHelper {

    private const val PREF_NAME = "previmed_prefs"
    private const val TOKEN_KEY = "jwt"
    private const val ROLE_KEY = "user_role"

    private fun prefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // ================== TOKEN ==================
    fun saveToken(context: Context, token: String) {
        prefs(context).edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(context: Context): String? {
        return prefs(context).getString(TOKEN_KEY, null)
    }

    fun clearToken(context: Context) {
        prefs(context).edit().remove(TOKEN_KEY).apply()
    }

    fun hasToken(context: Context): Boolean {
        return !getToken(context).isNullOrBlank()
    }

    // ================== ROL ==================
    private fun normalizeRole(role: String?): String {
        if (role.isNullOrBlank()) return ""
        val normalized = Normalizer.normalize(role, Normalizer.Form.NFD)
        return normalized.replace("\\p{Mn}+".toRegex(), "").lowercase()
    }

    fun saveRole(context: Context, role: String) {
        val normalized = normalizeRole(role)
        prefs(context).edit().putString(ROLE_KEY, normalized).apply()
    }

    fun getRole(context: Context): String? {
        val rawRole = prefs(context).getString(ROLE_KEY, null)
        return normalizeRole(rawRole)
    }

    fun clearRole(context: Context) {
        prefs(context).edit().remove(ROLE_KEY).apply()
    }

    // ================== SESIÓN ==================
    fun clearSession(context: Context) {
        prefs(context).edit().clear().apply()
    }
}
