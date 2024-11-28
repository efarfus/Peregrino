plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlinx-serialization") // Plugin para kotlinx.serialization, caso esteja usando
    id("com.google.gms.google-services")
    id ("kotlin-kapt")

}

android {
    namespace = "com.angellira.peregrino"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.angellira.peregrino"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding {
        enable = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))

    implementation (libs.retrofit)
    implementation (libs.retrofit2.kotlinx.serialization.converter) // Conversor para kotlinx.serialization
    implementation (libs.kotlinx.serialization.json) // Biblioteca para kotlinx.serialization
    //noinspection GradleDependency
    implementation ("me.relex:circleindicator:2.1.6")
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)
    implementation(libs.okhttp)
    implementation (libs.me.circleindicator)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.ui.android)
    implementation ("androidx.room:room-ktx:2.5.2")
    kapt ("androidx.room:room-compiler:2.6.1") // Adicione se usar anotações do Room
    implementation ("com.google.firebase:firebase-common-ktx")
    implementation ("com.google.firebase:firebase-database-ktx")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}