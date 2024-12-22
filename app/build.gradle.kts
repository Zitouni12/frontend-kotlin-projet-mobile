plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.facedetectionapp"
    compileSdk = 35 // Android 15 (API Level 35)

    defaultConfig {
        applicationId = "com.example.facedetectionapp"
        minSdk = 24
        targetSdk = 34 // Android 15 (API Level 35)
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
}




dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation ("com.squareup.retrofit2:retrofit:2.9.0") // Pour les appels réseau
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0") // Pour la conversion JSON
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1") // Gestion du ViewModel
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1") // Gestion du LiveData
    implementation ("com.github.bumptech.glide:glide:4.15.1") // Pour afficher les images
    implementation ("androidx.camera:camera-core:1.3.0") // CameraX pour capturer des images
    implementation ("androidx.camera:camera-lifecycle:1.3.0")
    implementation ("androidx.camera:camera-view:1.3.0")

    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")


    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    // Retrofit pour faire des requêtes HTTP
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Convertisseur Gson pour la conversion JSON <-> Kotlin
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")


    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    // Core Navigation Component
    implementation("androidx.core:core:1.14.0")
    implementation("androidx.core:core-ktx:1.14.0")

    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.gridlayout:gridlayout:1.0.0")




}


