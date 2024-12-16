plugins {
    id("com.gradleup.shadow") version "8.3.2" // Import shadow API.
    java // Tell gradle this is a java project.
    eclipse // Import eclipse plugin for IDE integration.
    kotlin("jvm") version "2.0.21" // Import kotlin jvm plugin for kotlin/java integration.
}

java {
    // Declare java version.
    sourceCompatibility = JavaVersion.VERSION_17
}

group = "nl.skbotnl.rewindfixog" // Declare bundle identifier.
version = "1.2.1" // Declare plugin version (will be in .jar).
val apiVersion = "1.19" // Declare minecraft server target version.

val customMavenLocal = System.getProperty("SELF_MAVEN_LOCAL_REPO")
if (customMavenLocal != null) {
    val mavenLocalDir = file(customMavenLocal)
    if (mavenLocalDir.isDirectory) {
        println("Using SELF_MAVEN_LOCAL_REPO at: $customMavenLocal")
        repositories {
            maven {
                url = uri("file://${mavenLocalDir.absolutePath}")
            }
        }
    } else {
        logger.error("TrueOG Bootstrap not found, defaulting to ~/.m2 for mavenLocal()")
    }
} else {
    logger.error("TrueOG Bootstrap not found, defaulting to ~/.m2 to mavenLocal()")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://repo.viaversion.com")
    maven("https://repo.dmulloy2.net/repository/public")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("file://${System.getProperty("user.home")}/.m2/repository")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.19.4-R0.1-SNAPSHOT")
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:2.2.3") // Import MiniPlaceholders API.
    compileOnly("org.purpurmc.purpur:purpur-api:1.19.4-R0.1-SNAPSHOT") // Import Purpur API.
    compileOnly("com.viaversion:viaversion-api:5.0.5") // Import ViaVersion API.
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0") // Import ProtocolLib API.
}

tasks.withType<AbstractArchiveTask>().configureEach { // Ensure reproducible builds.
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.shadowJar {
    archiveClassifier.set("") // Use empty string instead of null
    from("LICENSE") {
        into("/")
    }
    exclude("io.github.miniplaceholders.*") // Exclude the MiniPlaceholders package from being shadowed.
    minimize()
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.jar {
    archiveClassifier.set("part")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
    options.compilerArgs.add("-Xlint:deprecation") // Triggers deprecation warning messages.
    options.encoding = "UTF-8"
    options.isFork = true
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.GRAAL_VM
    }
}

