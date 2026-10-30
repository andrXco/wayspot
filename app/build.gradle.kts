plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val useFirebaseEmulators = providers.gradleProperty("useFirebaseEmulators")
    .getOrElse("true")
    .toBoolean()

android {
    namespace = "com.example.wayspot"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.wayspot"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
        buildConfigField("boolean", "USE_FIREBASE_EMULATORS", useFirebaseEmulators.toString())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
        debug {
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.camera.camera2.pipe)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.navigation.compose)

    implementation(libs.coil.compose)
    implementation(libs.places)

    implementation(libs.dagger.hilt)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    ksp(libs.dagger.hilt.compiler)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.kotlinx.coroutines.play.services)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.retrofit.converter.gson)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(kotlin("test"))

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.truth.android.test)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.test.uiautomator)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    kspAndroidTest(libs.dagger.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
