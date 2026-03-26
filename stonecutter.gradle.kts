plugins {
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom-remap") version "1.15-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT" apply false
    id("me.modmuss50.mod-publish-plugin") version "1.0.+"
}

stonecutter active "26.1"

// Make newer versions be published last
stonecutter tasks {
    order("publishModrinth")
//    order("publishCurseforge")
}

// See https://stonecut/ter.kikugie.dev/wiki/config/params
stonecutter parameters {
    swaps["mod_id"] = "\"${property("mod.id")}\";"
    swaps["mod_version"] = "\"${property("mod.version")}\";"
    swaps["minecraft"] = "\"${node.metadata.version}\";"
    constants["release"] = property("mod.id") != "template"

    replacements {
        string(current.parsed < "1.21") {
            replace("ResourceLocation.fromNamespaceAndPath", "new ResourceLocation")
        }
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
            // This is needed because of https://stonecutter.kikugie.dev/wiki/config/params#replacement-overlapping
            replace("ResourceLocation.fromNamespaceAndPath", "Identifier.fromNamespaceAndPath")
        }
        string(current.parsed >= "26.1") {
            replace("GuiGraphics", "GuiGraphicsExtractor")
        }
    }
}

publishMods {
    changelog.set(getLatestPatchNotes(rootProject.file("CHANGELOG.md")))
    version = property("mod.version") as String
    type = STABLE

    github {
        dryRun = providers.environmentVariable("GITHUB_TOKEN").getOrNull() == null
        accessToken = providers.environmentVariable("GITHUB_TOKEN")
        repository = "doonv/fuzzy-autocomplete"
        commitish = "main" // This is the branch the release tag will be created from
        tagName = "${property("mod.version")}"

        // Allow the release to be initially created without any files.
        allowEmptyFiles = true
    }
}

fun getLatestPatchNotes(file: File): String = file.readLines()
    .dropWhile { !it.startsWith("## ") || it.contains("Unreleased", ignoreCase = true) }
    .drop(1)
    .takeWhile { !it.startsWith("## ") && !it.startsWith("[") }
    .joinToString("\n")
    .trim()

// Global publishMods properties
subprojects {
    plugins.withId("me.modmuss50.mod-publish-plugin") {
        configure<me.modmuss50.mpp.ModPublishExtension> {
            changelog.set(getLatestPatchNotes(rootProject.file("CHANGELOG.md")))
            type.set(STABLE)
            modrinth {
                dryRun.set(providers.environmentVariable("MODRINTH_TOKEN").getOrNull() == null)
                accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))
                projectDescription.set(rootProject.file("README.md").readText().replace(
                    "./img/demo1.webp",
                    "https://cdn.modrinth.com/data/OXXOaUrC/images/e72a57c2f85e3b5c9768346e07af0fa4d9c54c29.webp"
                ))
            }
            github {
                dryRun.set(providers.environmentVariable("GITHUB_TOKEN").getOrNull() == null)
                accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))
            }
        }
    }
}
