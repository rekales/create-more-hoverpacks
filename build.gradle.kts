plugins {
    id("idea")
    id("java")
    id("java-library")
    id("net.neoforged.moddev") version("2.0.78")
}

version = project.properties["mod_version"]!!
group = project.properties["mod_group"]!!

repositories {
    mavenLocal()

    maven("https://maven.neoforged.net/#/releases/")
    maven("https://www.cursemaven.com")
    maven("https://api.modrinth.com/maven")
    maven("https://modmaven.dev")
    maven("https://maven.createmod.net") // Create, Ponder, Flywheel
    maven("https://maven.ithundxr.dev/snapshots") // Registrate
    maven("https://maven.blamejared.com") // JEI, Vazkii's Mods
}

dependencies {
    implementation("com.simibubi.create:create-${property("minecraft_version")}:${property("create_version")}:slim") { isTransitive = false }
    implementation("net.createmod.ponder:Ponder-NeoForge-${property("minecraft_version")}:${property("ponder_version")}")
    compileOnly("dev.engine-room.flywheel:flywheel-neoforge-api-${property("minecraft_version")}:${property("flywheel_version")}")
    runtimeOnly("dev.engine-room.flywheel:flywheel-neoforge-${property("minecraft_version")}:${property("flywheel_version")}")
    implementation("com.tterrag.registrate:Registrate:${property("registrate_version")}")

    runtimeOnly("curse.maven:kotlin-for-forge-351264:7471280")
    runtimeOnly("curse.maven:create-jetpack-655608:7285435")

    runtimeOnly("curse.maven:cucumber-272335:7180790")
    runtimeOnly("curse.maven:iron-jetpacks-284497:7105279")
    runtimeOnly("curse.maven:create-stuff-additions-466792:6448012")
    runtimeOnly("maven.modrinth:builders-jetpack-mod:2.0-1.21.1")
    runtimeOnly("maven.modrinth:createaddition:neoforge-1.21.1-1.5.10")

    // Dev QOL
    runtimeOnly("curse.maven:jei-238222:7270455")
    runtimeOnly("maven.modrinth:freecam:1.3.0+mc1.21.1-neoforge")
}

neoForge {
    version = property("neo_version").toString()

    accessTransformers.from("src/main/resources/META-INF/accesstransformer.cfg")

    parchment {
        mappingsVersion = property("parchment_mappings_version")!!.toString()
        minecraftVersion = property("parchment_minecraft_version")!!.toString()
    }

    runs {
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel.set(org.slf4j.event.Level.DEBUG)
        }

        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", property("mod_id")!!.toString())
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", property("mod_id")!!.toString())
        }

        create("data") {
            data()
            programArguments.addAll(
                "--mod", property("mod_id")!!.toString(),
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }
    }

    mods {
        create(property("mod_id")!!.toString()) {
            sourceSet(sourceSets["main"])
        }
    }
}

tasks.processResources {
    val props = project.providers.gradlePropertiesPrefixedBy("").get()
    inputs.properties(props)
    filesMatching("META-INF/neoforge.mods.toml") { expand(props) }
}

tasks {
    jar {
        archiveBaseName.set("${rootProject.property("mod_id")}-neoforge")
    }
}

sourceSets {
    main {
        java {
            srcDir("src")
        }
        resources {
            srcDir("src/generated/resources")
        }
    }
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}