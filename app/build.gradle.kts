plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.lyy.myapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.lyy.myapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    // GitHub 自动打包
    signingConfigs {
        // 使用 GitHub Secrets 传递签名信息
        create("release") {
            storeFile = file(project.findProperty("KEYSTORE_FILE") ?: "/default/path/to/keystore.jks")
            storePassword = project.findProperty("KEYSTORE_PASSWORD") as String? ?: ""
            keyAlias = project.findProperty("KEY_ALIAS") as String? ?: ""
            keyPassword = project.findProperty("KEY_PASSWORD") as String? ?: ""
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release") // 启用签名配置
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
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.okhttp)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.ui.test.manifest)
    implementation(libs.accompanist.navigation.animation)

    // 单元测试 (JUnit 5)
    androidTestImplementation(libs.junit.jupiter) // JUnit 5 API
    testImplementation(libs.junit.jupiter) // 使用集合依赖


    // Android 测试
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // 调试工具
    debugImplementation(libs.androidx.ui.tooling)
}
