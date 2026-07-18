// Top-level build file where you can add configuration options common to all sub-projects/modules.
// AGP 9.0+ includes built-in Kotlin support — kotlin.android plugin is no longer needed.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
}