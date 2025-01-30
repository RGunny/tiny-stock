plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    runtimeOnly("com.h2database:h2")
}
