plugins {
    alias(libs.plugins.android.application) // 应用插件
    alias(libs.plugins.jetbrains.kotlin.android) // Kotlin 插件
}

android {
    namespace = "com.lyy.myapp" // 应用包名
    compileSdk = 35 // 编译 SDK 版本

    defaultConfig {
        applicationId = "com.lyy.myapp" // 应用 ID
        minSdk = 24 // 最低支持 SDK 版本
        targetSdk = 35 // 目标 SDK 版本
        versionCode = 1 // 版本号
        versionName = "1.0.0" // 版本名称

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // 测试运行器
        vectorDrawables {
            useSupportLibrary = true // 使用支持库处理矢量图标
        }
        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")) // 支持的架构列表
        }
    }

    // 签名配置（用于发布版本）
    signingConfigs {
        create("release") {
            storeFile = file(project.findProperty("KEYSTORE_FILE") ?: "/default/path/to/keystore.jks")
            storePassword = project.findProperty("KEYSTORE_PASSWORD") as String? ?: ""
            keyAlias = project.findProperty("KEY_ALIAS") as String? ?: ""
            keyPassword = project.findProperty("KEY_PASSWORD") as String? ?: ""
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true // 启用代码压缩
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), // 默认混淆规则
                "proguard-rules.pro" // 自定义混淆规则
            )
            signingConfig = signingConfigs.getByName("release") // 启用签名配置
        }
    }

    // ABI 分包配置
    splits {
        abi {
            isEnable = true // 启用 ABI 分包
            reset() // 重置默认设置
            include("arm64-v8a", "armeabi-v7a", "x86", "x86_64") // 指定支持的架构
            isUniversalApk = true // 生成通用 APK
        }
    }

    // 编译选项
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11 // 源代码兼容性
        targetCompatibility = JavaVersion.VERSION_11 // 目标代码兼容性
    }
    kotlinOptions {
        jvmTarget = "11" // Kotlin JVM 目标版本
    }

    // 启用 Jetpack Compose
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15" // Compose 编译器扩展版本
    }

    // 资源打包配置
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // 排除不必要的资源
        }
    }
}

dependencies {
    // 使用 Compose BOM 统一管理 Compose 相关依赖版本
    implementation(platform(libs.androidx.compose.bom))
    // Jetpack Compose 基础依赖
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.material3) // Material 3
    // Material Icons 扩展支持（额外图标）
    implementation(libs.androidx.material.icons.extended)
    // Navigation 和动画支持
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.navigation.animation)
    // Activity Compose
    implementation(libs.androidx.activity.compose)
    // 引入 OkHttp BOM
    implementation(platform(libs.okhttp.bom))
    // OkHttp 核心库
    implementation(libs.okhttp.core)
    // OkHttp 日志拦截器（可选）
    implementation(libs.okhttp.logging)
    // 调试工具
    debugImplementation(libs.androidx.ui.tooling)
    // 测试依赖
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    // 测试清单
    implementation(libs.androidx.ui.test.manifest)
}