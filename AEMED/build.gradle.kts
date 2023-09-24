plugins {
    id("java")
    id("io.freefair.lombok") version "8.3"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.h2database:h2:2.1.210")
    implementation("org.mybatis:mybatis:3.5.13")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.5.0")
}

tasks.test {
    useJUnitPlatform()
}