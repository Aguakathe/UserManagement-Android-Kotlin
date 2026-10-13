package com.example.parcialkotlinapp.api

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Response

// =====================================
// INTERPRETA LOS ERRORES QUE ENVÍA LA API .NET
//
// 1. Validaciones controladas del servicio:
//    HTTP 400 { "mensaje": "Ya existe un usuario con ese correo." }
//
// 2. Validaciones automáticas de [ApiController] (DataAnnotations):
//    HTTP 400 { "title": "...", "errors": { "Semestre": ["..."] } }
// =====================================
object ApiErrorParser {

        fun obtenerMensaje(response: Response<*>): String {

                // errorBody() solo se puede leer UNA vez
                val cuerpo = try {
                        response.errorBody()?.string()
                    } catch (e: Exception) {
                        null
                    }

                val mensajeApi = extraerMensaje(cuerpo)

                if (!mensajeApi.isNullOrBlank()) {
                        return mensajeApi
                    }

                // Si la API no envió un mensaje legible,
                // se usa uno según el código HTTP
                return when (response.code()) {
                        400 -> "Los datos enviados no son válidos."
                        401 -> "Correo o contraseña incorrectos."
                        404 -> "El recurso solicitado no existe."
                        in 500..599 -> "Error interno del servidor (${response.code()}). Intente más tarde."
                        else -> "Error inesperado (${response.code()})."
                    }
            }

        private fun extraerMensaje(cuerpo: String?): String? {

                if (cuerpo.isNullOrBlank()) {
                        return null
                    }

                return try {

                        val json: JsonObject =
                                JsonParser.parseString(cuerpo).asJsonObject

                        // Caso 1: { "mensaje": "..." }
                        leerTexto(json, "mensaje")

                            // Caso 2: { "errors": { "Campo": ["..."] } }
                            ?: json.getAsJsonObject("errors")
                                ?.entrySet()
                                ?.firstOrNull()
                                ?.value
                                        ?.asJsonArray
                                        ?.firstOrNull()
                                ?.asString

                                    // Caso 3: { "title": "..." }
                                    ?: leerTexto(json, "title")

                    } catch (e: Exception) {
                        // El cuerpo no era JSON
                        null
                    }
            }

        private fun leerTexto(json: JsonObject, campo: String): String? {

                val valor = json.get(campo)

                return if (valor != null && valor.isJsonPrimitive) {
                        valor.asString
                    } else {
                        null
                    }
            }
    }