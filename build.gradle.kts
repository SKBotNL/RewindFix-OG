import java.io.BufferedReader

plugins {
    kotlin("jvm") version "2.1.21"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17"
    id("com.gradleup.shadow") version "8.3.5"
    eclipse
}

val commitHash = Runtime
    .getRuntime()
    .exec(arrayOf("git", "rev-parse", "--short=10", "HEAD"))
    .let { process ->
        process.waitFor()
        val output = process.inputStream.use {
            it.bufferedReader().use(BufferedReader::readText)
        }
        process.destroy()
        output.trim()
    }

val apiVersion = "1.19"

group = "nl.skbotnl.rewindfixog"
version = "$apiVersion-$commitHash"

repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.viaversion.com")
    maven("https://repo.dmulloy2.net/repository/public")
}

dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")

    compileOnly("io.github.miniplaceholders:miniplaceholders-api:2.2.3")
    compileOnly("com.viaversion:viaversion-api:5.0.5")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
}

val targetJavaVersion = 17
kotlin {
    jvmToolchain(targetJavaVersion)
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

tasks.assemble {
    dependsOn(tasks.reobfJar)
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.jar {
    archiveClassifier.set("part")
}

tasks.shadowJar {
    archiveClassifier.set("")
    exclude("io.github.miniplaceholders.*")
    minimize()
}

tasks.named<ProcessResources>("processResources") {
    val props = mapOf(
        "version" to version,
        "apiVersion" to apiVersion,
    )
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }

    from("LICENSE") {
        into("/")
    }
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(targetJavaVersion)
        vendor = JvmVendorSpec.GRAAL_VM
    }
}

