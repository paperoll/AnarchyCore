group = "org.wksh"
version = "1.0-SNAPSHOT"
description = ""

java.sourceCompatibility = JavaVersion.VERSION_1_8

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.txmc.me/releases") }
    maven { url = uri("https://repo.wksh.org/releases") }
}

dependencies {
    implementation("com.diogonunes:JColor:5.5.1")
    implementation(project(":Common"))
    compileOnly("pl.moresteck:uberbukkit:1.0")
    implementation("github.scarsz:configuralize:1.4.0")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    val resources = File("src/main/resources")
    from("META-INF").exclude("maven/*")
    from(resources).include("*")
    from(resources).include("config/*")
}

tasks.shadowJar {
    includeEmptyDirs = false
    minimize()
}


tasks.register("debugcopyjar") {
    doFirst {
        val pluginfolder: String by project
        val plugins = File(pluginfolder)
        if (plugins.exists()) {
            val plugin = File(project.buildDir, "libs").listFiles()?.first { it.name.endsWith("-all.jar") }
            println("Found plugin at ${plugin?.absolutePath}")
            if (plugin != null) {
                if (plugin.exists()) {
                    val newFile = File(plugins, plugin.name)
                    if (newFile.exists()) {
                        newFile.delete()
                        println("Deleted old plugin ${newFile.absolutePath}")
                    }
                    plugin.copyTo(newFile, true)
                    println("Copied ${plugin.absolutePath} to ${newFile.absolutePath}")
                }
            } else println("Could not find built plugin jar")
        } else println("The plugins folder provided doesnt exits")
    }
}

tasks.register("normalcopy") {
    val plugin = File(project.buildDir, "libs").listFiles()?.first { it.name.endsWith("-all.jar") }
    println("Found build at ${plugin?.absolutePath}")
    if (plugin != null) {
        if (plugin.exists()) {
            val newFile = File(projectDir.parent, "${rootProject.name}-${project.version}.jar")
            if (newFile.exists()) {
                newFile.delete()
                println("Deleted old build ${newFile.absolutePath}")
            }
            plugin.copyTo(newFile, true)
            println("Copied ${plugin.absolutePath} to ${newFile.absolutePath}")
        }
    }
}

if (project.hasProperty("pluginfolder")) {
    tasks.shadowJar.get().finalizedBy(tasks.getByName("debugcopyjar"))
} else tasks.shadowJar.get().finalizedBy(tasks.getByName("normalcopy"))

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}