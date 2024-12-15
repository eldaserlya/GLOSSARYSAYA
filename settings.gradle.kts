// Hapus blok pluginManagement lainnya yang mungkin ada

pluginManagement {
    repositories {
        google()          // Menambahkan repositori Google
        mavenCentral()    // Repositori Maven Central
        gradlePluginPortal() // Repositori plugin Gradle jika diperlukan
    }
}

dependencyResolutionManagement {
    repositories {
        google()          // Menambahkan repositori Google untuk dependencies
        mavenCentral()    // Repositori Maven Central untuk dependencies
    }
}


rootProject.name = "GLOSSARYSAYA"
include(":app")
 