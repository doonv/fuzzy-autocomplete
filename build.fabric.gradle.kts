import kotlin.text.compareTo

plugins {
	id("mod-platform")
	id("dev.kikugie.loom-back-compat")
}

stonecutter {
	val (version, loader) = current.project.split('-', limit = 2)
	properties.tags(version, loader)
}

platform {
	loader = "fabric"
	dependencies {
		required("minecraft") {
			fabricLikeVersionRange = prop("deps.mc_dep")
		}
		required("fabricloader") {
			fabricLikeVersionRange = ">=${prop("deps.fabric-loader")}"
		}
		required("yet_another_config_lib_v3") {
			slug("yacl")
			fabricLikeVersionRange = "^${prop("deps.yacl")}"
		}
		optional("modmenu") {}
	}
}

loom {
	runs.named("client") {
		client()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "client"
		programArgs("--username=Dev")
		configName = "Fabric Client"
	}
	runs.named("server") {
		server()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "server"
		configName = "Fabric Server"
	}
}

repositories {
	mavenCentral()

	// FIXME: Remove and uncomment terraformersmc maven when it's fixed.
	strictMaven("https://maven.gnomecraft.net/releases", "com.terraformersmc") { name = "TerraformersMC" }
//	strictMaven("https://maven.terraformersmc.com/", "com.terraformersmc") { name = "TerraformersMC" }
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
	strictMaven("https://maven.parchmentmc.org", "org.parchmentmc.data") { name = "ParchmentMC" }
	// Do not make this a `strictMaven`, as 3.8.2 specifically is only on mavenCentral for some reason
	maven(url = "https://maven.isxander.dev/releases") {
		name = "Xander Maven"
	}
}

configurations.all {
	resolutionStrategy {
		force("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
	if (sc.current.parsed < "26") {
		@Suppress("UnstableApiUsage")
		mappings(loom.layered {
			officialMojangMappings()
			if (hasProperty("deps.parchment"))
				parchment("org.parchmentmc.data:parchment-${prop("deps.parchment")}@zip")
		})
	}
	modImplementation("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
	val yaclVersion =
		if (hasProperty("deps.yacl_minecraft_version")) prop("deps.yacl_minecraft_version") else prop("deps.minecraft")
	modImplementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}+${yaclVersion}-fabric")
	modImplementation("com.terraformersmc:modmenu:${prop("deps.modmenu")}")
	modLocalRuntime("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
}
