plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.flashcardmobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.flashcardmobile"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    val lifecycle_version = "2.6.2"
    val arch_version = "2.2.0"
    val room_version = "2.6.0"

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.5")
    implementation("androidx.navigation:navigation-ui:2.7.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata:$lifecycle_version")
    implementation("androidx.room:room-runtime:$room_version")
    implementation ("com.github.javafaker:javafaker:1.0.2")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.github.yukuku:ambilwarna:2.0.1")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    annotationProcessor("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}