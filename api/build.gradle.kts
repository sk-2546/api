plugins {
    kotlin("jvm") version "1.9.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.6")
    implementation("io.ktor:ktor-server-netty:2.3.6")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.6")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation(project(":compose"))
}

application {
    mainClass.set("in.shabinder.soundbound.api.ServerKt")
}

kotlin {
    jvmToolchain(17)
}
