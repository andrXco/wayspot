package com.example.wayspot.data.model

/** Reglas compartidas para validar y preparar el texto de un comentario. */
object ReviewCommentRules {
    fun normalizeDraft(value: String): String = value.trim()

    fun canPublish(value: String): Boolean = normalizeDraft(value).isNotEmpty()
}
