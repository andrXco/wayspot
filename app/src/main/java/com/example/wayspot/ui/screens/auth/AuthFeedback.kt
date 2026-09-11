package com.example.wayspot.ui.screens.auth

import androidx.annotation.StringRes
import com.example.wayspot.R
import com.example.wayspot.data.model.AuthFailure

@StringRes
fun AuthFailure.messageRes(): Int = when (this) {
    AuthFailure.EmptyFields -> R.string.auth_error_empty_fields
    AuthFailure.InvalidEmail -> R.string.auth_error_invalid_email
    AuthFailure.InvalidPassword -> R.string.auth_error_invalid_password
    AuthFailure.PasswordMismatch -> R.string.auth_error_password_mismatch
    AuthFailure.TermsNotAccepted -> R.string.auth_error_terms
    AuthFailure.EmailAlreadyInUse -> R.string.auth_error_email_in_use
    AuthFailure.UsernameAlreadyInUse -> R.string.auth_error_username_in_use
    AuthFailure.InvalidCredentials -> R.string.auth_error_invalid_credentials
    AuthFailure.Network -> R.string.auth_error_network
    AuthFailure.RecentLoginRequired -> R.string.auth_error_recent_login
    AuthFailure.Unknown -> R.string.auth_error_unknown
}
