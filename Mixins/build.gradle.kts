plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "org.iceanarchy"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.txmc.me/releases") }
}

dependencies {
    implementation("me.txmc:rtmixin:1.5.6-BETA")
    implementation(project(":Common"))
    compileOnly(files("../libs/uberbukkit.jar"))
    compileOnly("org.projectlombok:lombok:1.18.24")
    implementation("com.diogonunes:JColor:5.5.1")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}
tasks.shadowJar {
    manifest {
        attributes("Manifest-Version" to "1.0", "Premain-Class" to "me.txmc.rtmixin.jagent.AgentMain", "Agent-Class" to "me.txmc.rtmixin.jagent.AgentMain", "Can-Redefine-Classes" to "true", "Can-Retransform-Classes" to "true", "Can-Set-Native-Method-Prefix" to "true")
    }
    doLast {
        val pluginProj = project(":Plugin")
        val pluginResources = File(pluginProj.projectDir, "src/main/resources")
        val mixinLibs = File(project.buildDir, "libs")
        pluginResources.listFiles()?.filter { it.name.equals("mixins.dat") }?.forEach { it.delete() }
        mixinLibs.listFiles()?.filter { fi -> fi.name.endsWith("-all.jar") }?.forEach { //Copy the new file over
            val newFile = File(pluginResources, "mixins.dat")
            if (newFile.exists()) {
                newFile.delete()
                println("Deleted old mixin file ${newFile.name}")
            }
            it.copyTo(newFile, true)
            println("Copied a file ${newFile.absolutePath}")
        }
    }
}