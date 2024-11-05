plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.msadsapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.msadsapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug"){
            keyAlias = "key0"
            keyPassword = "123456"
            storeFile = file("//Users/anthonyrajan/Startups/MediaStreet/keystore_v1")
            storePassword = "123456"
            enableV1Signing = true
            enableV2Signing = true
        }
        create("release"){
            keyAlias = "key0"
            keyPassword = "123456"
            storeFile = file("//Users/anthonyrajan/Startups/MediaStreet/keystore_v1")
            storePassword = "123456"
            enableV1Signing = true
            enableV2Signing = true
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        /*debug {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }*/

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    // Clover SDK
    implementation("com.clover.sdk:clover-android-sdk:316")
    implementation("com.clover.sdk:clover-android-connector-sdk:316")
    // Glide dependency
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}