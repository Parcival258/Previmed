package com.andres_lasso.previmed.utils

import android.content.Context
import android.content.SharedPreferences
import java.text.Normalizer

object PreferenceHelper {

    private const val PREF_NAME = "previmed_prefs"
    private const val TOKEN_KEY = "jwt"
    private const val ROLE_KEY = "user_role"
    private const val ID_PACIENTE_KEY = "id_paciente"
    private const val TELEFONO_KEY = "telefono"

    private fun prefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        prefs(context).edit().putString(TOKEN_KEY, token).apply()
    }
    fun getToken(context: Context): String? = prefs(context).getString(TOKEN_KEY, null)
    fun hasToken(context: Context): Boolean = !getToken(context).isNullOrBlank()
    fun clearToken(context: Context) { prefs(context).edit().remove(TOKEN_KEY).apply() }

    private fun normalizeRole(role: String?): String {
        if (role.isNullOrBlank()) return ""
        val normalized = Normalizer.normalize(role, Normalizer.Form.NFD)
        return normalized.replace("\\p{Mn}+".toRegex(), "").lowercase()
    }
    fun saveRole(context: Context, role: String) {
        prefs(context).edit().putString(ROLE_KEY, normalizeRole(role)).apply()
    }
    fun getRole(context: Context): String? = normalizeRole(prefs(context).getString(ROLE_KEY, null))
    fun clearRole(context: Context) { prefs(context).edit().remove(ROLE_KEY).apply() }

    fun saveIdPaciente(context: Context, idPaciente: String) {
        prefs(context).edit().putString(ID_PACIENTE_KEY, idPaciente).apply()
    }

    fun getIdPaciente(context: Context): String? {
        return prefs(context).getString(ID_PACIENTE_KEY, null)
    }

    fun clearIdPaciente(context: Context) { prefs(context).edit().remove(ID_PACIENTE_KEY).apply() }

    fun saveTelefono(context: Context, telefono: String) = prefs(context).edit().putString(TELEFONO_KEY, telefono).apply()
    fun getTelefono(context: Context): String? = prefs(context).getString(TELEFONO_KEY, null)
    fun clearTelefono(context: Context) = prefs(context).edit().remove(TELEFONO_KEY).apply()

    fun clearSession(context: Context) {
        prefs(context).edit().clear().apply()
    }
}
