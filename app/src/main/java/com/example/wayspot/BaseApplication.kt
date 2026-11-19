package com.example.wayspot

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import dagger.hilt.android.HiltAndroidApp

internal const val FIREBASE_EMULATOR_HOST = "10.0.2.2"
internal const val STORAGE_EMULATOR_PORT = 9199

/** Inicializa el contenedor de dependencias de Hilt para todo el proceso de la aplicación. */
@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        // Cloud queda como destino predeterminado; el emulador se activa con
        // -Pwayspot.useFirebaseEmulator=true para pruebas locales.
        if (BuildConfig.DEBUG && BuildConfig.USE_FIREBASE_EMULATOR) {
            Firebase.firestore.useEmulator(FIREBASE_EMULATOR_HOST, 8083)
            Firebase.auth.useEmulator(FIREBASE_EMULATOR_HOST, 9099)
            Firebase.storage.useEmulator(FIREBASE_EMULATOR_HOST, STORAGE_EMULATOR_PORT)
        }
    }
}

