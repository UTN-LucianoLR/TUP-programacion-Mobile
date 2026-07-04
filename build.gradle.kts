// Top-level build file: plugins de nivel raíz declarados como apply false
// para que cada módulo los aplique individualmente según su necesidad.
plugins {
    alias(libs.plugins.android.application)  apply false
    alias(libs.plugins.kotlin.android)       apply false
    alias(libs.plugins.kotlin.compose)       apply false
    alias(libs.plugins.ksp)                  apply false
    alias(libs.plugins.hilt.android)         apply false
    alias(libs.plugins.kotlin.serialization) apply false
}