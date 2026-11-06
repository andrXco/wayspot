package com.example.wayspot

import android.app.Application
import android.os.Build
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        name: String?,
        context: Context?
    ): Application = super.newApplication(
            cl,
            HiltTestApplication::class.java.name,
            context
        )

    override fun onStart() {
        val appContext = targetContext
        if (FirebaseApp.getApps(appContext).isEmpty()) {
            FirebaseApp.initializeApp(appContext)
        }
        Firebase.firestore.useEmulator("10.0.2.2", 8080)
        Firebase.auth.useEmulator("10.0.2.2", 9099)
        Firebase.auth.signOut()

        if (Build.VERSION.SDK_INT >= 37) {
            InstrumentationRegistry.getInstrumentation().uiAutomation.grantRuntimePermission(
                appContext.packageName,
                "android.permission.ACCESS_LOCAL_NETWORK"
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            InstrumentationRegistry.getInstrumentation().uiAutomation.grantRuntimePermission(
                appContext.packageName,
                "android.permission.POST_NOTIFICATIONS"
            )
        }
        super.onStart()
    }
}
