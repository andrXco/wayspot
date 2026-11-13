package com.example.wayspot.data.model.review

/**
 * Define una única ventana de 24 horas para la consulta y el mapa.
 * El límite inferior es exclusivo; una reseña desaparece al cumplir 24 horas.
 * Los documentos sin fecha y las fechas futuras no son reseñas recientes.
 */
object RecentReviewWindow {
    const val DURATION_MILLIS = 24L * 60L * 60L * 1000L

    fun cutoffMillis(nowMillis: Long): Long = nowMillis - DURATION_MILLIS

    fun contains(createdAtMillis: Long?, nowMillis: Long): Boolean =
        createdAtMillis != null &&
            createdAtMillis > cutoffMillis(nowMillis) &&
            createdAtMillis <= nowMillis

    /** Un review deja de ser visible al cumplir exactamente 24 horas. */
    fun expiresAtMillis(createdAtMillis: Long): Long =
        createdAtMillis + DURATION_MILLIS
}
