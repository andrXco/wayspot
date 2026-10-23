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

/** Máximo de identificadores permitido por consulta de perfiles en Firestore. */
object UserRelationshipRules {
    const val MAX_USER_IDS_PER_QUERY = 30
}
