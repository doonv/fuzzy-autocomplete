plugins {
    id("net.fabricmc.fabric-loom")

    // `maven-publish`
    id("me.modmuss50.mod-publish-plugin")
}

version = "${property("mod.version")}+${property("mod.mc_title")}"
base.archivesName = property("mod.id") as String

val requiredJava = JavaVersion.VERSION_25

repositories {
    /**
     * Restricts dependency search of the given [groups] to the [maven URL][url],
     * improving the setup speed.
     */
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }
    strictMaven("https://www.cursemaven.com", "CurseForge", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")

    maven("https://maven.terraformersmc.com/") { name = "Terraformers" }
    maven("https://maven.isxander.dev/releases") { name = "Xander Maven" }
    maven("https://maven.nucleoid.xyz/") { name = "Nucleoid" }
}

loom {
    splitEnvironmentSourceSets()

    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json") // Useful for interface injection

    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1") // Adds names to lambdas - useful for mixins
    }

    runConfigs.all {
        ideConfigGenerated(true)
        vmArgs("-Dmixin.debug.export=true") // Exports transformed classes for debugging
        runDir = "../../run" // Shares the run directory between versions
    }
}


dependencies {
//    /**
//     * Fetches only the required Fabric API modules to not waste time downloading all of them for each version.
//     * @see <a href="https://github.com/FabricMC/fabric">List of Fabric API modules</a>
//     */
//    fun fapi(vararg modules: String) {
//        for (it in modules) modImplementation(fabricApi.module(it, property("deps.fabric_api") as String))
//    }

    minecraft("com.mojang:minecraft:${sc.current.version}")
    implementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    implementation("io.github.llamalad7:mixinextras-fabric:${property("deps.mixinextras")}")

    "clientImplementation"("dev.isxander:yet-another-config-lib:${property("deps.yacl")}+${sc.current.version}-fabric")
    runtimeOnly("maven.modrinth:yacl:${property("deps.yacl")}+${sc.current.version}-fabric")

    "clientImplementation"("com.terraformersmc:modmenu:${property("deps.modmenu")}")
    runtimeOnly("maven.modrinth:modmenu:${property("deps.modmenu")}")


//    fapi("fabric-lifecycle-events-v1", "fabric-resource-loader-v0", "fabric-content-registries-v0")
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava
}

tasks {
    withType<ProcessResources> {
        inputs.property("id", project.property("mod.id"))
        inputs.property("name", project.property("mod.name"))
        inputs.property("version", version)
        inputs.property("minecraft", project.property("mod.mc_dep"))
        inputs.property("yacl", project.property("deps.yacl"))

        val props = mapOf(
            "id" to project.property("mod.id"),
            "name" to project.property("mod.name"),
            "version" to version,
            "minecraft" to project.property("mod.mc_dep"),
            "yacl" to project.property("deps.yacl")
        )

        filesMatching("fabric.mod.json") { expand(props) }

        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }
    }

    // Builds the version into a shared folder in `build/libs/${mod version}/`
    register<Copy>("buildAndCollect") {
        group = "build"
        from(jar.map { it.archiveFile }, named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

// Publishes builds to Modrinth and Curseforge with changelog from the CHANGELOG.md file
publishMods {
    file = tasks.jar.map { it.archiveFile.get() }
    additionalFiles.from(
        tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() }
    )
    displayName = "${property("mod.name")} ${property("mod.version")} for ${property("mod.mc_title")}"
    version = property("mod.version") as String
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE
    modLoaders.add("fabric")

    modrinth {
        dryRun = providers.environmentVariable("MODRINTH_TOKEN").getOrNull() == null
        projectId = property("publish.modrinth") as String
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.addAll(property("mod.mc_targets").toString().split(' '))
        projectDescription = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map { s ->
            s.replace(
                "./img/demo1.webp",
                "https://cdn.modrinth.com/data/OXXOaUrC/images/e72a57c2f85e3b5c9768346e07af0fa4d9c54c29.webp"
            )
        }


        requires {
            slug = "yacl"
        }
    }

    github {
        dryRun = providers.environmentVariable("GITHUB_TOKEN").getOrNull() == null
        accessToken = providers.environmentVariable("GITHUB_TOKEN")

        parent(project(":").tasks.named("publishGithub"))
    }

//    curseforge {
//        dryRun = providers.environmentVariable("CURSEFORGE_TOKEN").getOrNull() == null
//
//        projectId = property("publish.curseforge") as String
//        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
//        minecraftVersions.addAll(property("mod.mc_targets").toString().split(' '))
//        requires {
//            slug = "fabric-api"
//        }
//    }
}

/*
// Publishes builds to a maven repository under `com.example:template:0.1.0+mc`
publishing {
    repositories {
        maven("https://maven.example.com/releases") {
            name = "myMaven"
            // To authenticate, create `myMavenUsername` and `myMavenPassword` properties in your Gradle home properties.
            // See https://stonecutter.kikugie.dev/wiki/tips/properties#defining-properties
            credentials(PasswordCredentials::class.java)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "${property("mod.group")}.${property("mod.id")}"
            artifactId = property("mod.id") as String
            version = project.version

            from(components["java"])
        }
    }
}
 */
