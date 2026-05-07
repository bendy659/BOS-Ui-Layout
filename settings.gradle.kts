pluginManagement {
    repositories {
        maven("https://maven.msrandom.net/repository/cloche/")

        mavenCentral()
        mavenLocal()
        gradlePluginPortal()

        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "BOS-Ui-Layout"