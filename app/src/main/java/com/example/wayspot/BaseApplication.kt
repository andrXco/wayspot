package com.example.wayspot

import android.app.Application
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.HiltAndroidApp

/** Inicializa el contenedor de dependencias de Hilt para todo el proceso de la aplicación. */
@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        // La propiedad Gradle selecciona explícitamente Firebase local o Firebase Cloud.
        if (BuildConfig.USE_FIREBASE_EMULATORS) {
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
        }
    }
}
