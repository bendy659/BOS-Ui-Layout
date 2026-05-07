import earth.terrarium.cloche.api.metadata.CommonMetadata
import earth.terrarium.cloche.api.target.FabricTarget
import earth.terrarium.cloche.api.target.NeoforgeTarget

plugins {
    id("earth.terrarium.cloche") version "0.18.11"

    kotlin("jvm") version "2.2.21"
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

        val authors = project.property("mod_authors").toString()
            .split(',')
        authors.forEach { author(it) }
    }

    // Mappings //
    mappings { official() }

    // Versions //

    fun FabricTarget.fabricMetadata() {
        val propModId       = project.property("mod_id").toString()
        val prefixClassName = project.property("prefix_class_name").toString()

        metadata {
            entrypoint("main") {
                adapter = "kotlin"
                value   = "$group.$propModId.${prefixClassName}Fabric"
            }
            entrypoint("client") {
                adapter = "kotlin"
                value   = "$group.$propModId.client.${prefixClassName}FabricClient"
            }

            dependency {
                modId = "fabric-language-kotlin"
                version {  }
                type = CommonMetadata.Dependency.Type.Required
            }
        }
    }

    fun NeoforgeTarget.neoforgeMetadata() {
        metadata {
            dependency {
                modId = "kotlinforforge"
                version {  }
                type = CommonMetadata.Dependency.Type.Required
            }
        }
    }

    minecraftVersion = project.property("minecraft_version").toString()

    fabric {
        loaderVersion = project.property("fabric_loader_version").toString()

        fabricMetadata()

        runs { client(); server() }

        dependencies {
            fabricApi(project.property("fabric_api_version").toString())

            val fabricKotlinLanguageVersion = project.property("fabric_kotlin_language_version").toString()
            implementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinLanguageVersion")
        }

        client { }
    }

    neoforge {
        loaderVersion = project.property("neoforge_loader_version").toString()

        neoforgeMetadata()

        runs { client(); server() }

        dependencies {
            val kotlinForForgeVersion = project.property("kotlin_for_forge_version").toString()
            implementation("thedarkcolour:kotlinforforge-neoforge:$kotlinForForgeVersion")
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
