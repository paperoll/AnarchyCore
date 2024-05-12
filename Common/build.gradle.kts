plugins {
    java
}

group = "org.wksh"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.txmc.me/releases") }
    maven { url = uri("https://repo.wksh.org/releases") }
}

dependencies {
    compileOnly("pl.moresteck:uberbukkit:1.0")
    compileOnly("github.scarsz:configuralize:1.4.0")
    compileOnly("com.diogonunes:JColor:5.5.1")
}
