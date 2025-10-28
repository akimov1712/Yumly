plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

android {
    namespace = "ru.topbun.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")


        val baseUrl = property("BASE_URL")?.toString() ?: error("BASE_URL not found")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures{
        buildConfig = true
    }
    kotlin {
        jvmToolchain(17)
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.compiler)

    // SharedPreferences
    implementation(libs.sharedPreferences.crypto)

    // Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)
    implementation(libs.okhttp.interceptor)

    // Default
    implementation(libs.appcompat)
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.runner)
    androidTestImplementation(libs.espresso.core)

    implementation(project(":domain"))
    implementation(project(":core:android"))
}