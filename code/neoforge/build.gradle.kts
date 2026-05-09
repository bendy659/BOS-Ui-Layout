plugins {
    id("com.gradleup.shadow")
}

loom {
    silentMojangMappingsLicense()
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

configurations {
    val common by creating {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
    named("compileClasspath") {
        extendsFrom(common)
    }
    named("runtimeClasspath") {
        extendsFrom(common)
    }
    named("developmentNeoForge") {
        extendsFrom(common)
    }

    // Files in this configuration will be bundled into your mod using the Shadow plugin.
    // Don't use the `shadow` configuration from the plugin itself as it's meant for excluding files.
    val shadowBundle by creating {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

repositories {
    maven {
        name = "NeoForged"
        url = uri("https://maven.neoforged.net/releases")
    }

    maven("https://thedarkcolour.github.io/KotlinForForge/")
}

dependencies {
    val kotlinForForgeVersion = project.property("kotlin_for_forge_version")

    minecraft("net.minecraft:minecraft:${rootProject.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())

    neoForge("net.neoforged:neoforge:${rootProject.property("neoforge_version")}")

    // Architectury API. This is optional, and you can comment it out if you don't need it.
    modImplementation("dev.architectury:architectury-neoforge:${rootProject.property("architectury_api_version")}")

    // Kotlin for Forge
    modImplementation("thedarkcolour:kotlinforforge:$kotlinForForgeVersion")

    "common"(project(":common"))
    "shadowBundle"(project(mapOf("path" to ":common", "configuration" to "transformProductionNeoForge")))
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand("version" to inputs.properties["version"])
    }
}

tasks.shadowJar {
    configurations = listOf(project.configurations["shadowBundle"])
    archiveClassifier = "dev-shadow"
}

tasks.remapJar {
    inputFile.set(tasks.named<org.gradle.jvm.tasks.Jar>("shadowJar").flatMap { it.archiveFile })
}

