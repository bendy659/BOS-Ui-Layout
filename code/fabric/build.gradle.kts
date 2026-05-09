plugins {
    id("com.gradleup.shadow")
}

loom {
    silentMojangMappingsLicense()
}

architectury {
    platformSetupLoomIde()
    fabric()
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
    named("developmentFabric") {
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
    maven("https://maven.fabricmc.net/")
}

dependencies {
    val fabricKotlinLanguageVersion = project.property("fabric_kotlin_language_version")

    minecraft("net.minecraft:minecraft:${rootProject.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric_loader_version")}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${rootProject.property("fabric_api_version")}")

    // Architectury API. This is optional, and you can comment it out if you don't need it.
    modImplementation("dev.architectury:architectury-fabric:${rootProject.property("architectury_api_version")}")

    // Fabric Kotlin Language
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinLanguageVersion")

    "common"(project(":common"))
    "shadowBundle"(project(mapOf("path" to ":common", "configuration" to "transformProductionFabric")))
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
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

