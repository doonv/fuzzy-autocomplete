@file:OptIn(dev.kikugie.stonecutter.StonecutterExperimentalAPI::class)

import dev.kikugie.stonecutter.controller.StonecutterControllerExtension
import org.gradle.kotlin.dsl.replace

plugins {
	alias(libs.plugins.stonecutter)
	alias(libs.plugins.dotenv)
	alias(libs.plugins.loom.back.compat).apply(false)
	alias(libs.plugins.neoforged.moddev).apply(false)
	alias(libs.plugins.jsonlang.postprocess).apply(false)
	alias(libs.plugins.mod.publish.plugin)
	alias(libs.plugins.kotlin.jvm).apply(false)
	alias(libs.plugins.devtools.ksp).apply(false)
	alias(libs.plugins.fletching.table).apply(false)
}

stonecutter active file(".sc_active_version")

val modVersion: String get() = the<StonecutterControllerExtension>().properties.get<String>("mod.version")
val baseVersion: String get() = modVersion

tasks.register("runActiveClient") {
	group = "stonecutter"
	description = "Run client of the active Stonecutter version"
	dependsOn(stonecutter.current!!.project + ":runClient")
}

tasks.register("runActiveServer") {
	group = "stonecutter"
	description = "Run server of the active Stonecutter version"
	dependsOn(stonecutter.current!!.project + ":runServer")
}

stonecutter parameters {
	constants.match(current.project.substringAfterLast('-'), "fabric", "neoforge")
	swaps["mod_version"] = "\"${properties.get<String>("mod.version")}\";"
	swaps["mod_id"] = "\"${properties.get<String>("mod.id")}\";"
	swaps["mod_name"] = "\"${properties.get<String>("mod.name")}\";"
	swaps["mod_group"] = "\"${properties.get<String>("mod.group")}\";"
	swaps["minecraft"] = "\"${current.version}\";"

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
	type.set(me.modmuss50.mpp.ReleaseType.STABLE)
	version = baseVersion

	github {
		val token = providers.environmentVariable("GITHUB_TOKEN").orNull
		dryRun = token == null
		accessToken = token
		repository = "doonv/fuzzy-autocomplete"
		commitish = "main"
		tagName = baseVersion
		allowEmptyFiles = true
	}
}

for (version in stonecutter.versions.map { it.version }.distinct()) tasks.register("publish$version") {
	group = "publishing"
	dependsOn(stonecutter.tasks.named("publishMods") { metadata.version == version })
}

fun getLatestPatchNotes(file: java.io.File): String = file.readLines()
	.dropWhile { !it.startsWith("## ") || it.contains("Unreleased", ignoreCase = true) }
	.drop(1)
	.takeWhile { !it.startsWith("## ") && !it.startsWith("[") }
	.joinToString("\n")
	.trim()
