import earth.terrarium.cloche.api.metadata.CommonMetadata
import earth.terrarium.cloche.api.target.FabricTarget
import earth.terrarium.cloche.api.target.NeoforgeTarget
import org.gradle.kotlin.dsl.support.kotlinCompilerOptions

plugins {
    id("earth.terrarium.cloche") version "0.18.11"

    kotlin("jvm") version "2.2.0"
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
        val propModGroup = project.property("mod_group").toString()
        val propModId    = project.property("mod_id").toString()

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
                value   = "$propModGroup.libs.$propModId.UiLayoutFabric"
            }
            entrypoint("client") {
                adapter = "kotlin"
                value   = "$propModGroup.libs.$propModId.client.UiLayoutFabricClient"
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

    fun getParchmentMcVersion(version: Int): String =
        when (version) {
            1_21_11 -> "2025.12.20"
            1_21_1  -> "2024.11.17"
            else -> ""
        }

    fabricVersions.forEach { versionStr ->
        val versionInt = versionStr.replace(".", "").toInt() // ex: "1.21.1" -> 1211 (1_21_1)
        fabric("$versionStr:fabric") {
            minecraftVersion = versionStr
            loaderVersion = "0.19.2"

            fabricMetadata(versionInt)
            includedClient()

            mappings { parchment(getParchmentMcVersion(versionInt)) }

            dependencies {
                val fabricApiVersion =
                    when(versionInt) {
                        1_21_1  -> "0.116.11"
                        1_21_11 -> "0.141.3"
                        else -> ""
                    }
                val fabricKotlinLanguageVersion =
                    when(versionInt) {
                        //1_21_1, 1_21_11 -> "1.13.9+kotlin.${project.property("kotlin_version").toString()}"
                        1_21_11 -> "1.13.7+kotlin.2.2.21"
                        1_21_1  -> "1.13.4+kotlin.${project.property("kotlin_version").toString()}"
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

            //mappings { parchment(getParchmentMcVersion(versionInt)) }

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

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
