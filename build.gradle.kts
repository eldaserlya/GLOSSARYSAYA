// Menggunakan blok `plugins` untuk mendeklarasikan plugin dan dependensinya
plugins {
    id("com.android.application") version "8.6.0" apply false // Pastikan versi plugin Android sesuai
    kotlin("android") version "1.9.0" apply false // Pastikan versi Kotlin sesuai
}

buildscript {
    repositories {
        google()  // Pastikan repositori Google untuk plugin Android dan Kotlin
        mavenCentral()  // Repositori Maven untuk dependensi lainnya
    }
    dependencies {
        // Ini untuk plugin yang digunakan di seluruh proyek
        classpath("com.android.tools.build:gradle:8.6.0") // Versi terbaru Android Gradle Plugin
        classpath("com.google.gms:google-services:4.3.15")  // Plugin Google services
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2") // Jika menggunakan Crashlytics
    }
}


allprojects {
    repositories {
        google()  // Repositori Google untuk semua subproyek
        mavenCentral()  // Repositori Maven untuk dependensi
    }
}
