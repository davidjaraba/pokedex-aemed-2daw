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
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
    implementation("com.h2database:h2:2.2.224")
    implementation("org.mybatis:mybatis:3.5.13")
}

tasks.test {
    useJUnitPlatform()
}


tasks.compileJava {
    options.release = 17
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "dev.Main"
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

}