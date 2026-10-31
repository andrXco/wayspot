package com.example.wayspot

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.HiltAndroidApp

/** Inicializa el contenedor de dependencias de Hilt para todo el proceso de la aplicación. */
@HiltAndroidApp
class BaseApplication : Application() {



    // Local

    /*

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        // En la variante debug, Auth y Firestore usan los emuladores locales.
        if (BuildConfig.DEBUG) {
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
        }
    }


    */
}
