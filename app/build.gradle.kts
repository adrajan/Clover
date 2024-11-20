plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.msadsapp123"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.msadsapp123"
        minSdk = 21
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 22
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

    implementation ("androidx.appcompat:appcompat:1.5.0")
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
    implementation ("com.squareup.picasso:picasso:2.71828")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:28.0.0"))
    implementation("com.google.firebase:firebase-core")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-config")

}
