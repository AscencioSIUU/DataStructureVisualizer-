plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"

    // Add the Google services Gradle plugin

    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.datastructurevisualizerapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.datastructurevisualizerapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //Navigation
    implementation(libs.androidx.navigation.compose)

    // Shapes
    implementation(libs.androidx.graphics.shapes)

    //Compose ViewMode
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    //Network calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //json to Kotlin object mapping
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //Image loading
    implementation("io.coil-kt:coil-compose:2.4.0")

    //HTTP networking
    implementation("com.squareup.okhttp3:okhttp:4.9.3")


    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.test.ext:junit-ktx:1.2.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
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

    // Import the Firebase BoM

    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))


    // When using the BoM, don't specify versions in Firebase dependencies

    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")


}