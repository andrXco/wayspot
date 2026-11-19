package com.example.wayspot.data.model.user

/** Reúne las reglas de normalización y validez que comparte la edición de perfil. */
object EditProfileRules {
    const val MAX_BIO_LENGTH = 160
    const val MAX_NAME_LENGTH = 60

    private val emailPattern = Regex(
        pattern = "^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$"
    )

    fun filterUsername(username: String): String = username.filter { character ->
        character.isLetterOrDigit() || character == '_'
    }

    fun normalizeUsername(username: String): String = filterUsername(username)

    fun normalizeProfile(
        profile: UserProfile,
        username: String = profile.username,
        email: String = profile.email,
        bio: String = profile.bio,
        location: String = profile.location,
        avatarUrl: String? = profile.avatarUrl,
        preferences: ProfileNotificationPreferences = profile.notificationPreferences,
        name: String = profile.name
    ): UserProfile {
        val normalizedName = name.trim().take(MAX_NAME_LENGTH)
        return profile.copy(
            name = normalizedName,
            initials = initialsForName(normalizedName),
            username = normalizeUsername(username),
            email = email.trim(),
            bio = bio.take(MAX_BIO_LENGTH),
            location = location.trim(),
            avatarUrl = avatarUrl,
            notificationPreferences = preferences
        )
    }

    fun initialsForName(name: String): String = name.trim()
        .split(Regex("\\s+"))
        .filter(String::isNotBlank)
        .take(2)
        .mapNotNull { part -> part.firstOrNull()?.uppercaseChar() }
        .joinToString("")

    fun canSave(
        username: String,
        email: String,
        location: String,
        name: String = username
    ): Boolean = name.trim().isNotBlank() &&
        name.length <= MAX_NAME_LENGTH &&
        username.isNotBlank() &&
        username == normalizeUsername(username) &&
        emailPattern.matches(email.trim()) &&
        location.isNotBlank()
}
