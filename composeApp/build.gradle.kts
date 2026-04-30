import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation("org.jetbrains.kotlin:kotlin-reflect:2.1.21")
            implementation("com.alibaba.fastjson2:fastjson2:2.0.52")
            implementation("org.apache.logging.log4j:log4j-api:2.25.3")
            implementation("org.apache.logging.log4j:log4j-core:2.25.3")
            implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.25.3")
            implementation("com.fazecast:jSerialComm:2.11.4")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "cius.mai_onsyn.dobot.gui.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "cius.mai_onsyn.dobot"
            packageVersion = "1.0.0"
        }

        jvmArgs += listOf(
            "-Duser.dir=${rootProject.projectDir.absolutePath}",
            "-Dfile.encoding=UTF-8",
            "-Dsun.stdout.encoding=UTF-8",
            "-Dsun.stderr.encoding=UTF-8",
            "-Dsun.java2d.uiScale=1.0"  // 防止系统缩放倍率不为整数产生的抖动
        )
    }
}
