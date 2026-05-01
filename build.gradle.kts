import earth.terrarium.cloche.api.metadata.CommonMetadata
import earth.terrarium.cloche.api.target.FabricTarget
import earth.terrarium.cloche.api.target.NeoforgeTarget

plugins {
    id("earth.terrarium.cloche") version "0.18.11"

    kotlin("jvm") version "2.3.10"
}

group   = project.property("mod_group").toString()
version = project.property("mod_version").toString()

repositories {
    maven("https://maven.fabricmc.net/")

    maven("https://thedarkcolour.github.io/KotlinForForge/")

    cloche.librariesMinecraft()

    mavenCentral()
    mavenLocal()

    cloche {
        main()

        mavenNeoforged()
        mavenNeoforgedMeta()

        mavenParchment()
    }
}

cloche {
    // Common Metadata //
    metadata {
        modId       = project.property("mod_id").toString()
        name        = project.property("mod_name").toString()
        description = project.property("mod_description").toString()
        license     = project.property("mod_license").toString()

        val authors = project.property("mod_authors")
            .toString()
            .split(',')
        authors.forEach { author(it) }
    }

    // Mappings //
    mappings { official() }

    // Versions //

    fun FabricTarget.fabricMetadata(versionInt: Int) {
        metadata {
            metadata {
                dependency {
                    modId = "fabric-language-kotlin"
                    version {  }
                    type = CommonMetadata.Dependency.Type.Required
                }
            }

            entrypoint("main") {
                adapter = "kotlin"
                value   = "com.example.${project.property("mod_id").toString()}.ExampleModFabric"
            }
            entrypoint("client") {
                adapter = "kotlin"
                value   = "com.example.${project.property("mod_id").toString()}.client.ExampleModFabricClient"
            }
        }
    }

    fun NeoforgeTarget.neoforgeMetadata(versionInt: Int) {
        metadata {
            dependency {
                modId = "kotlinforforge"
                version {  }
                type = CommonMetadata.Dependency.Type.Required
            }
        }
    }

    // Read versions //
    val fabricVersions   = project.property("fabric_versions").toString().split(',')
    val neoforgeVersions = project.property("neoforge_versions").toString().split(',')

    fabricVersions.forEach { versionStr ->
        val versionInt = versionStr.replace(".", "").toInt() // ex: "1.21.1" -> 1211 (1_21_1)
        fabric("$versionStr:fabric") {
            minecraftVersion = versionStr
            loaderVersion = "0.19.2"

            fabricMetadata(versionInt)
            includedClient()

            dependencies {
                val fabricApiVersion =
                    when(versionInt) {
                        1_21_1  -> "0.116.11"
                        1_21_11 -> "0.141.3"
                        else -> ""
                    }
                val fabricKotlinLanguageVersion =
                    when(versionInt) {
                        1_21_1, 1_21_11 -> "1.13.9+kotlin.2.3.10"
                        else -> ""
                    }

                fabricApi(fabricApiVersion)
                implementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinLanguageVersion")
            }

            runs { client(); server() }
        }
    }

    neoforgeVersions.forEach { versionStr ->
        val versionInt = versionStr.replace(".", "").toInt() // ex: "1.21.1" -> 1211 (1_21_1)
        neoforge("$versionStr:neoforge") {
            minecraftVersion = versionStr
            loaderVersion =
                when(versionInt) {
                    1_21_1  -> "21.1.26"
                    1_21_11 -> "21.11.42"
                    else -> ""
                }

            neoforgeMetadata(versionInt)

            dependencies {
                val kotlinforforgeVersion =
                    when(versionInt) {
                        1_21_1  -> "5.11.0"
                        1_21_11 -> "6.2.0"
                        else -> ""
                    }

                implementation("thedarkcolour:kotlinforforge-neoforge:$kotlinforforgeVersion")
            }

            runs { client(); server() }
        }
    }
}

afterEvaluate {
    tasks.matching { it.name.startsWith("prepare") && it.name.endsWith("Run") }.configureEach {
        enabled = true
    }
}

kotlin {
    jvmToolchain(21)
}
