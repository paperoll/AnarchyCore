plugins {
    java
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
    compileOnly(files("../libs/uberbukkit.jar"))
    compileOnly("org.projectlombok:lombok:1.18.24")
    compileOnly("com.diogonunes:JColor:5.5.1")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}
