pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9"
}

stonecutter {
    create(rootProject) {
        fun vers(vararg versions: String) =
            versions
                .forEach {ver ->
                    val string = if (eval(ver, ">=26.1")) "unobf." else ""
                    version(ver).buildscript("build.${string}gradle.kts")
                }

        // See https://stonecutter.kikugie.dev/wiki/start/#choosing-minecraft-versions
        vers("1.20.1", "1.21.5", "1.21.11", "26.1")
        vcsVersion = "26.1"
    }
}

rootProject.name = "Fuzzy Autocomplete"