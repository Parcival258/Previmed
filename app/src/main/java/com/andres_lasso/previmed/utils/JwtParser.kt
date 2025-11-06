package com.andres_lasso.previmed.utils

import android.util.Base64
import org.json.JSONObject

object JwtParser {

    /**
     * Extrae un claim específico del payload de un JWT
     * @param token Token JWT completo
     * @param claim Nombre del claim a extraer (ej: "id_usuario", "email", "exp")
     * @return Valor del claim o null si no existe
     */
    fun getPayloadClaim(token: String, claim: String): String? {
        return try {
            val parts = token.split('.')
            if (parts.size != 3) {
                return null
            }

            // El payload es la segunda parte del JWT
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP))
            val json = JSONObject(payload)

            json.optString(claim, null)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Extrae todo el payload del JWT como JSONObject
     * @param token Token JWT completo
     * @return JSONObject con todo el payload o null si hay error
     */
    fun getPayload(token: String): JSONObject? {
        return try {
            val parts = token.split('.')
            if (parts.size != 3) {
                return null
            }

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP))
            JSONObject(payload)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Verifica si el token ha expirado
     * @param token Token JWT completo
     * @return true si ha expirado, false si aún es válido
     */
    fun isTokenExpired(token: String): Boolean {
        return try {
            val exp = getPayloadClaim(token, "exp")?.toLongOrNull() ?: return true
            val currentTime = System.currentTimeMillis() / 1000
            exp < currentTime
        } catch (e: Exception) {
            true
        }
    }
}