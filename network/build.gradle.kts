import org.jlleitschuh.gradle.ktlint.KtlintExtension

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)

    // Is third party plugins
    alias(libs.plugins.kapt)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktlintPlugin)
    alias(libs.plugins.daggerPlugin)
}

android {
    namespace = "ru.lanik.network"
    compileSdk = 33

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Is third party dependencies
    implementation(libs.json.serialization)
    implementation(libs.rxjava3.rxjava)
    implementation(libs.rxjava3.rxandroid)
    implementation(libs.rxjava3.rxkotlin)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.adapter.rxjava3)
    implementation(libs.retrofit.converter.kotlin)
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    testImplementation(libs.dagger.hilt.android.testing)
    kaptTest(libs.dagger.hilt.compiler)
    androidTestImplementation(libs.dagger.hilt.android.testing)
    kaptAndroidTest(libs.dagger.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}

configure<KtlintExtension> {
    version.set("0.49.1")
}