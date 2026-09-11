package com.example.wayspot.data.model

data class AuthSession(
    val uid: String,
    val email: String,
    val username: String?,
    val isEmailVerified: Boolean
)

sealed interface AuthStatus {
    data object Loading : AuthStatus
    data object SignedOut : AuthStatus
    data class Authenticated(val session: AuthSession) : AuthStatus
}

sealed interface AuthFailure {
    data object EmptyFields : AuthFailure
    data object InvalidEmail : AuthFailure
    data object InvalidPassword : AuthFailure
    data object PasswordMismatch : AuthFailure
    data object TermsNotAccepted : AuthFailure
    data object EmailAlreadyInUse : AuthFailure
    data object UsernameAlreadyInUse : AuthFailure
    data object InvalidCredentials : AuthFailure
    data object Network : AuthFailure
    data object RecentLoginRequired : AuthFailure
    data object Unknown : AuthFailure
}

sealed interface AuthOutcome<out T> {
    data class Success<T>(val value: T) : AuthOutcome<T>
    data class Failure(val reason: AuthFailure) : AuthOutcome<Nothing>
}

object AuthRules {
    private val emailPattern = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private val usernamePattern = Regex("^[a-z0-9_]{3,20}$")

    fun normalizedUsername(value: String): String = value.trim().lowercase()

    fun isValidEmail(value: String): Boolean = emailPattern.matches(value.trim())

    fun isValidUsername(value: String): Boolean = usernamePattern.matches(normalizedUsername(value))

    fun isValidPassword(value: String): Boolean =
        value.length >= 8 && value.any(Char::isUpperCase) &&
            value.any(Char::isLowerCase) && value.any(Char::isDigit)
}
