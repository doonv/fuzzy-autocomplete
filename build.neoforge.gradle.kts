plugins {
	id("mod-platform")
	id("net.neoforged.moddev")
}

stonecutter {
	val (version, loader) = current.project.split('-', limit = 2)
	properties.tags(version, loader)

}

platform {
	loader = "neoforge"
	dependencies {
		required("minecraft") {
			forgeLikeVersionRange = prop("deps.mc_dep")
		}
		required("neoforge") {
			forgeLikeVersionRange.set("[1,)")
		}

		/**
		 * Equivalent of `^1.2.3` in Fabric or like any other fucking dependency system
		 */
		fun carrotRangeButForge(version: String): String {
			val parts = version.split(".")
			val nextMajor = parts[0] + 1
			return "[$version,${nextMajor}.0.0)"
		}
		required("yet_another_config_lib_v3") {
			slug("yacl")
			environment = "client"
			forgeLikeVersionRange = carrotRangeButForge(prop("deps.yacl"))
		}
	}
}

neoForge {
	version = prop("deps.neoforge")

	if (hasProperty("deps.parchment")) parchment {
		val (mc, ver) = prop("deps.parchment").split(':')
		mappingsVersion = ver
		minecraftVersion = mc
	}

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "NeoForge Client (${stonecutter.current.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "NeoForge Server (${stonecutter.current.version})"
		}
	}

	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
}

repositories {
	mavenCentral()
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
	// Do not make this a `strictMaven`, as 3.8.2 specifically is only on mavenCentral for some reason
	maven(url = "https://maven.isxander.dev/releases") {
		name = "Xander Maven"
	}
}

dependencies {
	val yaclVersion =
		if (hasProperty("deps.yacl_minecraft_version")) prop("deps.yacl_minecraft_version") else prop("deps.minecraft")
	implementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}+${yaclVersion}-neoforge")
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}
