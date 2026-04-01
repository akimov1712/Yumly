plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = libs.versions.namespace.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.android)
    implementation(project.dependencies.platform(libs.koin.bom))

    // Voyager
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.transition)
    implementation(libs.voyager.koin)

    // Default
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.datastore.preferences)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(project(":navigation"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core:android"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:auth_welcome"))
    implementation(project(":feature:auth_login"))
    implementation(project(":feature:auth_register"))
    implementation(project(":feature:auth_confirm"))
    implementation(project(":feature:auth_reset_request"))
    implementation(project(":feature:auth_reset"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:dashboard"))
    implementation(project(":feature:home"))
    implementation(project(":feature:upload"))
    implementation(project(":feature:assistant"))
    implementation(project(":feature:notification"))
    implementation(project(":feature:profile"))

}
