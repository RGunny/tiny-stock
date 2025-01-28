plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25" apply false
    id("org.springframework.boot") version "3.4.2" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

// (1) 모든 프로젝트(루트+서브) 공통 설정
allprojects {
    repositories {
        mavenCentral()
    }
}

// (2) 하위(서브)프로젝트 공통 설정
subprojects {

    // Kotlin JVM & 의존성관리 플러그인 적용
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.spring.dependency-management")

    group = "me.rgunny"
    version = "0.0.1-SNAPSHOT"

    kotlin {
        jvmToolchain(21)
    }

    dependencies {
        // 공통으로 쓰고 싶은 의존성
        implementation(kotlin("stdlib"))
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
