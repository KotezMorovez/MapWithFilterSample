import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.jetbrainsKotlinPluginParcelize)
    alias(libs.plugins.kapt)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.compose.compiler)
}

val projectProperties = Properties().apply {
    file("../settings.properties").inputStream().use { fis ->
        load(fis)
    }
}

android {
    namespace = "com.example.filterpointsonthemap"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.filterpointsonthemap"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "MAP_API_KEY",
                projectProperties["MAPKIT_API_KEY"] as String
            )
        }
        release {
            buildConfigField(
                "String",
                "MAP_API_KEY",
                projectProperties["MAPKIT_API_KEY"] as String
            )

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
        buildConfig = true
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
//    Map
    implementation(libs.maps.mobile)
    implementation(libs.converter.gson)

//    Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.lifecycle.viewmodel.compose.viewModel)
    implementation(libs.glide)
    implementation(libs.glide.compose)

//    Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.accompanist.permissions)

//    DI
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

//    Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
kapt {
    correctErrorTypes = true
}