package com.example.wayspot.data.model.user

/** Tipo de relación que se muestra en la lista de conexiones. */
enum class UserRelationshipListType {
    FOLLOWERS,
    FOLLOWING;

    companion object {
        fun fromRoute(value: String?): UserRelationshipListType? =
            values().firstOrNull { it.name.equals(value, ignoreCase = true) }
    }
}

/** Límites compartidos para cargar listas de relaciones en páginas. */
object UserRelationshipRules {
    const val PAGE_SIZE = 30L
}
