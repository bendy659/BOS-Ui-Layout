pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            url = uri("https://maven.architectury.dev/")
        }
        maven {
            url = uri("https://files.minecraftforge.net/maven/")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "bos_ui_layout"

include(":common", ":fabric", ":neoforge")

project(":common").projectDir   = file("code/common")
project(":fabric").projectDir   = file("code/fabric")
project(":neoforge").projectDir = file("code/neoforge")