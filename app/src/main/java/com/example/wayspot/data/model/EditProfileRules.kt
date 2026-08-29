package com.example.wayspot.data.model

object EditProfileRules {
    const val MAX_BIO_LENGTH = 160

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
        preferences: ProfileNotificationPreferences = profile.notificationPreferences
    ): UserProfile = profile.copy(
        username = normalizeUsername(username),
        email = email.trim(),
        bio = bio.take(MAX_BIO_LENGTH),
        location = location.trim(),
        avatarUrl = avatarUrl,
        notificationPreferences = preferences
    )

    fun canSave(
        username: String,
        email: String,
        location: String
    ): Boolean = username.isNotBlank() &&
        username == normalizeUsername(username) &&
        emailPattern.matches(email.trim()) &&
        location.isNotBlank()
}
