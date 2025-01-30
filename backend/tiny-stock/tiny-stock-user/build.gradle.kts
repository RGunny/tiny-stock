plugins {
    id("org.springframework.boot")      // spring boot plugin
    kotlin("plugin.spring")             // kotlin spring plugin
}

dependencies {
    implementation(project(":tiny-stock-common-aop"))
    implementation(project(":tiny-stock-common-exception"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    runtimeOnly("com.h2database:h2")
}