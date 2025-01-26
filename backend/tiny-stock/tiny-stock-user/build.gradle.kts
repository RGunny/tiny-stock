plugins {
    id("org.springframework.boot")      // spring boot plugin
    kotlin("plugin.spring")             // kotlin spring plugin
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
}