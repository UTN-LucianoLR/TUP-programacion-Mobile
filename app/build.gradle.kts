import java.util.Properties

plugins {
    id("com.android.application")
    // kotlin("android") es aplicado automáticamente por AGP 9.x — no declarar manualmente
    kotlin("plugin.compose")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.kotlin.serialization)
}

// Se usa rootProject.file() para que la ruta sea siempre relativa a la raíz
// del proyecto, independientemente del sistema operativo o entorno de CI.
val localProperties = Properties().also { props: Properties ->
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        localFile.inputStream().use { stream -> props.load(stream) }
    } else {
        logger.warn(
            "\nlocal.properties no encontrado en: ${rootProject.projectDir}/local.properties" +
            "\nCrealo copiando local.properties.example y completá los valores." +
            "\nLas funciones de red usarán la URL de placeholder hasta que lo configures.\n"
        )
    }
}

val apiBaseUrl: String = localProperties.getProperty("API_BASE_URL")
    ?: "https://placeholder.example.com/"

// ─────────────────────────────────────────────────────────────────────────────

android {
    namespace  = "com.app.partidos"
    compileSdk = 37

    defaultConfig {
        applicationId             = "com.app.partidos"
        minSdk                    = 29
        targetSdk                 = 36
        versionCode               = 1
        versionName               = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Expone la URL base de la API al código Kotlin mediante BuildConfig.
        // El valor viene de local.properties (nunca hardcodeado aquí).
        buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // kotlinOptions fue reemplazado por kotlin { jvmToolchain } compatible con AGP 9.x

    buildFeatures {
        compose     = true
        buildConfig = true  // Necesario para que BuildConfig.API_BASE_URL sea accesible
    }
}


kotlin {
    jvmToolchain(11)
}

// ── KSP: argumentos de Room ─────────────────────────────────────────────────
// IMPORTANTE: este bloque debe estar fuera de android {} (nivel raíz del módulo).
// projectDir resuelve la ruta de forma relativa al directorio del módulo :app.
ksp {
    arg("room.schemaLocation",    "${projectDir}/schemas")
    arg("room.incremental",       "true")
    arg("room.expandProjection",  "true")
}

dependencies {
    // ── AndroidX Core ─────────────────────────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)

    // ── Jetpack Compose (versiones gestionadas por el BOM) ────────────────────
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    // ── Navigation Compose ────────────────────────────────────────────────────
    implementation(libs.androidx.navigation.compose)

    // ── Lifecycle / ViewModel ─────────────────────────────────────────────────
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // ── Hilt ─────────────────────────────────────────────────────────────────
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    // ── Room ──────────────────────────────────────────────────────────────────
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)

    // ── Paging 3 ──────────────────────────────────────────────────────────────
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // ── Retrofit + OkHttp + Gson ──────────────────────────────────────────────
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.gson)

    // ── Coroutines ────────────────────────────────────────────────────────────
    implementation(libs.kotlinx.coroutines.android)

    // ── DataStore ─────────────────────────────────────────────────────────────
    implementation(libs.androidx.datastore.preferences)

    // ── Serialization ─────────────────────────────────────────────────────────
    implementation(libs.kotlinx.serialization.json)

    // ── Coil ──────────────────────────────────────────────────────────────────
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

    // ── Tests ─────────────────────────────────────────────────────────────────
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}