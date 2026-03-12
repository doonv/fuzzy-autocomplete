plugins {
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom-remap") version "1.14-SNAPSHOT" apply false
    // id("me.modmuss50.mod-publish-plugin") version "1.0.+" apply false
}

stonecutter active "1.21.11"

/*
// Make newer versions be published last
stonecutter tasks {
    order("publishModrinth")
    order("publishCurseforge")
}
 */

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
    }
}
