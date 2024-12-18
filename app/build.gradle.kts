plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp.android)
}

android {
    namespace = "paiva.thiago.news"
    compileSdk = 35

    defaultConfig {
        applicationId = "paiva.thiago.news"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_KEY", "\"8dbee7204c074ca3a11fd0c1da39168a\"")
    }

    flavorDimensions.add("source")

    productFlavors {
        create("bbc") {
            dimension = "source"
            applicationIdSuffix = ".bbc"
            resValue("string", "app_name", "BBC News")
            buildConfigField("String", "NEWS_SOURCE", "\"bbc-news\"")
        }

        create("cnn") {
            dimension = "source"
            applicationIdSuffix = ".cnn"
            resValue("string", "app_name", "CNN News")
            buildConfigField("String", "NEWS_SOURCE", "\"cnn\"")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Android libraries
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Retrofit
    implementation(libs.squareup.converter.moshi)
    implementation(libs.squareup.logging.interceptor)
    implementation(libs.squareup.retrofit)

    // Moshi
    implementation(libs.squareup.moshi)
    implementation(libs.squareup.moshi.kotlin)
    ksp(libs.squareup.moshi.kotlin.codegen)

    // Coil
    implementation(libs.kt.coil.compose)

    // Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)

    // Coroutines and Flow
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.coroutines.android)

    // Biometric
    implementation(libs.androidx.biometric.ktx)

    // Testing
    testImplementation (libs.androidx.paging.common)
    testImplementation (libs.junit)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.mockito.inline)
    testImplementation (libs.mockito.kotlin)
    testImplementation (libs.truth)
    androidTestImplementation (libs.androidx.core.testing)
    androidTestImplementation (platform(libs.androidx.compose.bom))
    androidTestImplementation (libs.androidx.junit)
    androidTestImplementation (libs.androidx.runner)
    androidTestImplementation (libs.androidx.ui.test.junit4)
}